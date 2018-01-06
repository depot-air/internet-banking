package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.TrainPaymentViewService;
import com.dwidasa.engine.util.MoneyUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 2:43 PM
 */
@Service("trainPaymentViewService")
public class TrainPaymentViewServiceImpl extends PaymentViewServiceImpl implements TrainPaymentViewService {
    @Autowired
    private LoggingService loggingService;
    
    public TrainPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            TrainPaymentView tv = (TrainPaymentView) view;

            //-- customer id an(20) left justified padding with space
            String customData = String.format("%1$-20s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode("01113000801030002");
            
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TrainPaymentView tv = (TrainPaymentView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("yyddMMHHmm");

            String bit48 = transaction.getFreeData1();
            tv.setReferenceName(bit48.substring(43, 73).trim());
            tv.setNumOfPassenger(Integer.valueOf(bit48.substring(20, 22)));
            tv.setTrainName(bit48.substring(78, 110).trim());
            tv.setTripInfo(bit48.substring(25, 28) + " - " + bit48.substring(28, 31));

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String yy = String.valueOf(cal.get(Calendar.YEAR)).substring(2);

            try {
                tv.setDepartureDate(sdf.parse(yy + bit48.substring(35, 43)));
            } catch (ParseException e) {
                tv.setDepartureDate(null);
            }

            tv.setAmount(transaction.getTransactionAmount());
            tv.setFeeIndicator(transaction.getFeeIndicator());
            tv.setFee(transaction.getFee());
            tv.setTotal(tv.getAmount().add(tv.getFee()));
            tv.setBit48(transaction.getFreeData1());

            tv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            TrainPaymentView tv = (TrainPaymentView) view;
            transaction.setFreeData1(tv.getBit48());

            //-- customer id an(20) left justified padding with space
            String customData = String.format("%1$-20s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode("01106000801030002");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TrainPaymentView tv = (TrainPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());

            tv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(tv.getCustomerId(), Constants.TRANSPORTATION_PAYMENT_CODE, "Pembayaran Kereta Api, Kode Booking = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
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
            TrainPaymentView tv = (TrainPaymentView) view;
            transaction.setFreeData1(tv.getBit48());

            //-- customer id an(20) left justified padding with space
            String customData = String.format("%1$-20s", tv.getCustomerReference());
            transaction.setFreeData2(customData);
            transaction.setTranslationCode("01107000801030002");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            TrainPaymentView tv = (TrainPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());

            tv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(tv.getCustomerId(), Constants.TRANSPORTATION_PAYMENT_REP_CODE, "Pembayaran Kereta Api, Kode Booking = " + tv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(tv.getAmount()), tv.getReferenceNumber(), tv.getMerchantType(), tv.getTerminalId());
            return Boolean.TRUE;
        }
    }
}
