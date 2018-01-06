package com.dwidasa.ib.pages.payment;

import java.text.NumberFormat;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/08/11
 * Time: 22:43
 */
public class NonTagListPaymentConfirm {
    @Property(write = false)
    @Persist
    private NonTagListPaymentView nonTagListPaymentView;

    @Property
    private TokenView tokenView;

    @InjectComponent
    private Form form;

    @Inject
    private OtpManager otpManager;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CacheManager cacheManager;

    @Property
    private int tokenType;

    @InjectPage
    private NonTagListPaymentReceipt nonTagListPaymentReceipt;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    public Object onActivate(){
        if(nonTagListPaymentView == null){
            return TrainPaymentInput.class;
        }
        return null;
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(NonTagListPaymentConfirm.class.toString());
        setTokenType();
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setNonTagListPaymentView(NonTagListPaymentView nonTagListPaymentView) {
        this.nonTagListPaymentView = nonTagListPaymentView;
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(nonTagListPaymentView.getCustomerId(), this.getClass().getSimpleName(),
                    tokenView)) {
            	nonTagListPaymentView.setTransactionType(Constants.NONTAGLIST_PAYMENT_CODE);
                paymentService.execute(nonTagListPaymentView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        nonTagListPaymentReceipt.setNonTagListPaymentView(nonTagListPaymentView);
        return nonTagListPaymentReceipt;
    }

}
