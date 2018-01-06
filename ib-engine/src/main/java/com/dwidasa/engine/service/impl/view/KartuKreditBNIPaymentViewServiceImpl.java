package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.DetilMultiFinance;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.KartuKreditBNIPaymentView;
import com.dwidasa.engine.model.view.MultiFinancePaymentView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.KartuKreditBNIPaymentViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.MultiFinancePaymentViewService;
import com.dwidasa.engine.service.view.TrainPaymentViewService;
import com.dwidasa.engine.util.MoneyUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 2:43 PM
 */
@Service("kartuKreditBNIPaymentViewService")
public class KartuKreditBNIPaymentViewServiceImpl extends PaymentViewServiceImpl implements KartuKreditBNIPaymentViewService {
    @Autowired
    private LoggingService loggingService;
    
    public KartuKreditBNIPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            
        }

        public Boolean decompose(Object view, Transaction transaction) {

            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            KartuKreditBNIPaymentView tv = (KartuKreditBNIPaymentView) view;
            String custom = StringUtils.rightPad(tv.getNomorKartuKredit(), 19, "");
            transaction.setFreeData1(custom);
            transaction.setTransactionAmount(tv.getAmount());
        }

        public Boolean decompose(Object view, Transaction transaction) {
            
        	KartuKreditBNIPaymentView tv = (KartuKreditBNIPaymentView) view;
            tv.setResponseCode(transaction.getResponseCode());
            tv.setReferenceNumber(transaction.getReferenceNumber());
            tv.setFee(transaction.getFee());
            
        	
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
          
        }

        public Boolean decompose(Object view, Transaction transaction) {
           return Boolean.TRUE;
        }
    }
}
