package com.dwidasa.ib.pages.payment;

import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CcPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 22/07/11
 * Time: 15:59
 */
public class CcPaymentConfirm {
    @Persist
    private CcPaymentView ccPaymentView;
    @Property
    private int tokenType;
    @InjectPage
    private CcPaymentReceipt ccPaymentReceipt;
    @Inject
    private PaymentService paymentService;
    @Inject
    private CacheManager cacheManager;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;
    @Property
    private TokenView tokenView;
    @Inject
    private OtpManager otpManager;

    public void onPrepare(){
        if(tokenView == null){
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (ccPaymentView == null) {
            return CcPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
        setTokenType();
    }

    void onValidateFromForm() {
        if (tokenView.getToken() == null) {
            form.recordError(messages.get("nullTokenError"));
        } else if (!tokenView.getToken().matches("[0-9]+")) {
            form.recordError(messages.get("formatTokenError"));
//        } else if (tokenView.getToken().length() != 8) {
//            form.recordError(messages.get("lengthTokenError"));
        }
        if (!form.getHasErrors() &&
                otpManager.validateToken(ccPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            try {
                setCcPaymentView((CcPaymentView) paymentService.execute(getCcPaymentView()));
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
            if (getCcPaymentView().getSave() && ccPaymentView.getInputType().equalsIgnoreCase("M")) {
                try {
                    paymentService.register(getCcPaymentView().transform());
                } catch (BusinessException e) {
                    form.recordError(e.getFullMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        getCcPaymentView().setTransactionDate(new Date());
        ccPaymentReceipt.setCcPaymentView(ccPaymentView);
        return ccPaymentReceipt;
    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
        return CcPaymentInput.class;
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public CcPaymentView getCcPaymentView() {
        return ccPaymentView;
    }

    public void setCcPaymentView(CcPaymentView ccPaymentView) {
        this.ccPaymentView = ccPaymentView;
    }
}
