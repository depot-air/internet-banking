package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.service.view.InternetPaymentViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:35
 */
@Service("internetPaymentViewService")
public class InternetPaymentViewServiceImpl extends PaymentViewServiceImpl implements InternetPaymentViewService {
    public InternetPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            InternetPaymentView iv = (InternetPaymentView) view;

            //-- speedy flag
            String customData = "0";
            //-- customer id an(20) left justified padding with space
            customData += String.format("%1$-20s", iv.getCustomerReference());

            transaction.setFreeData2(customData);
            transaction.setTranslationCode("0111101001001");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            InternetPaymentView iv = (InternetPaymentView) view;
            String bit48 = transaction.getFreeData1();

            int base = 20;
            int len = 23;
            int numOfBill = Integer.valueOf(bit48.substring(19, 20));
            
            if(numOfBill == 0){
            	throw new BusinessException("IB-1009","91");
            }
			
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            String year = sdf.format(new Date()).substring(0, 3);
            iv.setBillPeriods(new ArrayList<Date>());

            for (int i = 0; i < numOfBill; i++) {
                try {
                    iv.getBillPeriods().add(sdf.parse(year + bit48.substring(base + i * len, base + i * len + 3)));
                } catch (ParseException e) {
                    iv.getBillPeriods().add(null);
                }

                switch (i) {
                    case 0 :
                        iv.setAmount1(new BigDecimal(bit48.substring(base + i * len + 11, base + i * len + 23)));
                        break;
                    case 1 :
                        iv.setAmount2(new BigDecimal(bit48.substring(base + i * len + 11, base + i * len + 23)));
                        break;
                    case 2 :
                        iv.setAmount3(new BigDecimal(bit48.substring(base + i * len + 11, base + i * len + 23)));
                }
            }

            iv.setAmount(transaction.getTransactionAmount());
            iv.setFeeIndicator(transaction.getFeeIndicator());
            iv.setFee(transaction.getFee());
            iv.setReferenceName(bit48.substring(base + len * numOfBill, base + len * numOfBill + 30).trim());
            iv.setTotal(iv.getAmount().add(iv.getFee()));
            iv.setBit48(transaction.getFreeData1());

            iv.setSpeedyFlag(transaction.getFreeData2().substring(0, 1));

            iv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            InternetPaymentView iv = (InternetPaymentView) view;
            transaction.setFreeData1(iv.getBit48());

            //-- speedy flag
            String customData = iv.getSpeedyFlag();
            //-- customer id an(20) left justified padding with space
            customData += String.format("%1$-20s", iv.getCustomerReference());

            transaction.setFreeData2(customData);
            transaction.setTranslationCode("0110201001001");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            InternetPaymentView iv = (InternetPaymentView) view;
            iv.setResponseCode(transaction.getResponseCode());
            iv.setReferenceNumber(transaction.getReferenceNumber());

            iv.setTraceNumber(transaction.getStanSixDigit());
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
            InternetPaymentView iv = (InternetPaymentView) view;

            //-- speedy flag
            String customData = "0";
            //-- customer id an(20) left justified padding with space
            customData += String.format("%1$-20s", iv.getCustomerReference());

            transaction.setFreeData2(customData);
            transaction.setTranslationCode(Constants.TRANSLATION_TELKOM_REPRINT);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            InternetPaymentView iv = (InternetPaymentView) view;
            iv.setResponseCode(transaction.getResponseCode());
            iv.setReferenceNumber(transaction.getReferenceNumber());

            iv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }
}
