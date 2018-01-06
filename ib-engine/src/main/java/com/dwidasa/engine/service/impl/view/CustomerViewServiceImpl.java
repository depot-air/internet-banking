package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CustomerAccountView;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.CustomerViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.PlnPaymentViewService;
import com.dwidasa.engine.util.MoneyUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Time: 11:03
 */
@Service("customerAccountViewService")
public class CustomerViewServiceImpl extends PaymentViewServiceImpl implements CustomerViewService {
    private static Logger logger = Logger.getLogger(  CustomerViewServiceImpl.class);
    @Autowired
    private LoggingService loggingService;
    
    public CustomerViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CustomerAccountView pv = (CustomerAccountView) view;
        	//-- customer id an(12) left justified padding with space
            String customData = pv.getDigitNoKartu();
            
            transaction.setFreeData1(customData);
            //transaction.setTranslationCode(Constants.TRANSLATION_LOTTERY);
            //set bit 4 = 0, karena kadang2 masih menyimpan nilai dr transaksi sebelumnya
            transaction.setTransactionAmount(BigDecimal.ZERO);
            System.out.println("Bit 48 "+transaction.getFreeData1());
           
        }

        public Boolean decompose(Object view, Transaction transaction) {
            
        	CustomerAccountView pv = (CustomerAccountView) view;
        	pv.setTransactionType(transaction.getTransactionType());
        	String bit48 = transaction.getFreeData1();
        	int total = 26;
        	
        	String totalData[] = bit48.split(";");
        	String dataAccount = bit48.replace("-", "").replace(";", "");
        	System.out.println("Data Bit "+dataAccount);
        	for(int i=0; i<totalData.length; i++){
        		int length = i * total;
        		CustomerAccountView accountView = new CustomerAccountView();
        		accountView.setDigitRekening(dataAccount.substring(0 + length, 10 + length));
        		accountView.setDigitNoKartu(dataAccount.substring(10 + length, 26 + length));
        		pv.getAccountViews().add(accountView);
        		
        	}
        	return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            
        }

        public Boolean decompose(Object view, Transaction transaction) {
            
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
