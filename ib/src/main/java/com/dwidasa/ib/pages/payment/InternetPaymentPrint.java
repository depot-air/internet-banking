package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 08/10/11
 * Time: 10:37
 */
public class InternetPaymentPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;
    
    @Property
    private String paidPeriods;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMyy", threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate2 = new SimpleDateFormat(Constants.SHORT_DATE, threadLocale.getLocale());

    @InjectPage
    private InternetPaymentReceipt internetPaymentReceipt;

    @Property
    private InternetPaymentView internetPaymentView;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @ActivationRequestParameter
    private String reprint;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private PaymentService paymentService;

    void setupRender() {
    	
        if (reprint != null) {
            internetPaymentView = (InternetPaymentView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
            internetPaymentView.setTransactionType(com.dwidasa.engine.Constants.INTERNET_PAYMENT_REP_CODE);
            paymentService.reprint(internetPaymentView, transactionId);
            return;
        }
       
        internetPaymentView = internetPaymentReceipt.getInternetPaymentView();
        paidPeriods = internetPaymentView.getPaidPeriods(threadLocale.getLocale());
    }
}
