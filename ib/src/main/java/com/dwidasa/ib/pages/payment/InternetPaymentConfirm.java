package com.dwidasa.ib.pages.payment;

import java.text.NumberFormat;
import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:27
 */
public class InternetPaymentConfirm {
    @Property(write = false)
    @Persist
    private InternetPaymentView internetPaymentView;

    @Property
    private int tokenType;
    
    @Property
    private String paidPeriods;

    @InjectPage
    private InternetPaymentReceipt internetPaymentReceipt;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CacheManager cacheManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Property
    private TokenView tokenView;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (internetPaymentView == null) {
            return InternetPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(InternetPaymentConfirm.class.toString());
    	paidPeriods = internetPaymentView.getPaidPeriods(threadLocale.getLocale());
        setTokenType();
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(internetPaymentView.getCustomerId(), this.getClass().getSimpleName(),
                    tokenView)) {
                setInternetPaymentView((InternetPaymentView) paymentService.execute(internetPaymentView));
            }
            if (internetPaymentView.getSave() && internetPaymentView.getInputType().equalsIgnoreCase("M")) {
                paymentService.register(internetPaymentView.transform());
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        internetPaymentView.setTransactionDate(new Date());
        internetPaymentReceipt.setInternetPaymentView(internetPaymentView);
        return internetPaymentReceipt;
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setInternetPaymentView(InternetPaymentView internetPaymentView) {
        this.internetPaymentView = internetPaymentView;
    }
}
