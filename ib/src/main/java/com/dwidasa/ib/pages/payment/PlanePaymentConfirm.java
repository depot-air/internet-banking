package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.PlanePaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 11/08/11
 * Time: 13:19
 */
public class PlanePaymentConfirm {
    @Persist
    private PlanePaymentView planePaymentView;
    @Property
    private TokenView tokenView;
    @Inject
    private Messages messages;
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
    private PlanePaymentReceipt planePaymentReceipt;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);

    public Object onActivate(){
        if(planePaymentView == null){
            return PlanePaymentInput.class;
        }
        return null;
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public void setupRender() {
        setTokenType();
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public PlanePaymentView getPlanePaymentView() {
        return planePaymentView;
    }

    public void setPlanePaymentView(PlanePaymentView planePaymentView) {
        this.planePaymentView = planePaymentView;
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
                otpManager.validateToken(planePaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            try {
                setPlanePaymentView((PlanePaymentView) paymentService.execute(getPlanePaymentView()));
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        planePaymentView.setTransactionDate(new Date());
        planePaymentReceipt.setPlanePaymentView(planePaymentView);
        return planePaymentReceipt;
    }

    @DiscardAfter
    public Object onSelectedFromCancel(){
        return PlanePaymentInput.class;
    }
}
