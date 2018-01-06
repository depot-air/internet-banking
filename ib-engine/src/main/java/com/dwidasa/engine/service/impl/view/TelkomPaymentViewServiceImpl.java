package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.TelkomPaymentViewService;
import com.dwidasa.engine.util.MoneyUtils;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:20
 */
@Service("telkomPaymentViewService")
public class TelkomPaymentViewServiceImpl extends PaymentViewServiceImpl implements TelkomPaymentViewService{
	private static Logger logger = Logger.getLogger(TelkomPaymentViewServiceImpl.class);
    @Autowired
    private LoggingService loggingService;
    
    private boolean isIndovisionOkeTopTV(String billerCode) {
    	if (billerCode.equals(Constants.TV_CODE.INDOVISION) || billerCode.equals(Constants.TV_CODE.OKEVISION) || billerCode.equals(Constants.TV_CODE.TOPTV)) {
    		return true;
    	}
    	return false;
    }
    public TelkomPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            TelkomPaymentView tv = (TelkomPaymentView) view;

            //-- customer id n(13) left justified padding with space
            String customData = String.format("%1$-13s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode("0111101001001");
            logger.info("transaction.getCardData2() inquiry=" + transaction.getCardData2());
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TelkomPaymentView tv = (TelkomPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            String bit48 = transaction.getFreeData1();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMM");

            int numOfBill = Integer.valueOf(bit48.substring(19, 20));
            if(numOfBill == 0){
            	throw new BusinessException("IB-1009","91");
            }
            int base = 20;
            int len = 23;

            tv.setNumOfBill(numOfBill);
            tv.setReferenceName(bit48.substring(base + len * numOfBill, base + len * numOfBill + 30).trim());
            tv.setAmount1(BigDecimal.ZERO);
            tv.setAmount2(BigDecimal.ZERO);
            tv.setAmount3(BigDecimal.ZERO);
            tv.setFee(BigDecimal.ZERO);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String y = String.valueOf(cal.get(Calendar.YEAR)).substring(2, 3);
//            BigDecimal fee = new BigDecimal(0);

            for (int i = 0; i < numOfBill; i++) {
                String yMM = bit48.substring(base + len * i, base + len * i + 3);
                String ref = bit48.substring(base + len * i + 3, base + len * i + 11).trim();

                switch (i) {
                    case 0 :
                        try {
                            tv.setBillPeriod1(sdf.parse(y + yMM));
                        } catch (ParseException e) {
                            tv.setBillPeriod1(null);
                        }
                        tv.setRef1(ref);
                        tv.setAmount1(new BigDecimal(bit48.substring(base + len * i + 11, base + len * i + 11 + 12)));
//                        fee=BigDecimal.valueOf(1);
                        break;
                    case 1 :
                        try {
                            tv.setBillPeriod2(sdf.parse(y + yMM));
                        } catch (ParseException e) {
                            tv.setBillPeriod2(null);
                        }
                        tv.setRef2(ref);
                        tv.setAmount2(new BigDecimal(bit48.substring(base + len * i + 11, base + len * i + 11 + 12)));
//                        fee=BigDecimal.valueOf(2);
                        break;
                    case 2 :
                        try {
                            tv.setBillPeriod3(sdf.parse(y + yMM));
                        } catch (ParseException e) {
                            tv.setBillPeriod3(null);
                        }
                        tv.setRef3(ref);
                        tv.setAmount3(new BigDecimal(bit48.substring(base + len * i + 11, base + len * i + 11 + 12)));
//                        fee=BigDecimal.valueOf(3);
                        break;
                }
            }
            
            tv.setAmount(transaction.getTransactionAmount());
            tv.setFeeIndicator(transaction.getFeeIndicator());
//            tv.setFee(transaction.getFee().multiply(fee));
            tv.setFee(transaction.getFee());
            tv.setBit48(bit48);
            tv.setTotal(tv.getAmount().add(tv.getFee()));

            tv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            TelkomPaymentView tv = (TelkomPaymentView) view;
            transaction.setFreeData1(tv.getBit48());
            
            //-- customer id n(9) left justified padding with space
            String customData = String.format("%1$-13s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode("0110201001001");
            
            logger.info("transaction.getCardData2() posting=" + transaction.getCardData2());
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TelkomPaymentView tv = (TelkomPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());

            tv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(tv.getCustomerId(), Constants.TELKOM_PAYMENT_CODE, "Pembayaran Telkom, ID Pelanggan = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for reprint phase
     */
    private class ReprintMessageCustomizer implements MessageCustomizer {
        private ReprintMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            TelkomPaymentView tv = (TelkomPaymentView) view;

            //-- customer id n(9) left justified padding with space
            String customData = String.format("%1$-13s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode(Constants.TRANSLATION_TELKOM_REPRINT);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TelkomPaymentView tv = (TelkomPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());

            tv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(tv.getCustomerId(), Constants.TELKOM_PAYMENT_REP_CODE, "Reprint Pembayaran Telkom, ID Pelanggan = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
            return Boolean.TRUE;
        }
    }
}
