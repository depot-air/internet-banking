package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.PlanePaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 11/08/11
 * Time: 13:19
 */
public class PlanePaymentInput {
    @Persist
    @Property
    private PlanePaymentView planePaymentView;
    @Property
    private TokenView tokenView;
    @Property
    private int tokenType;
    @Inject
    private CacheManager cacheManager;
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    @Property
    private SelectModel billerModel;
    @Inject
    private PaymentService paymentService;
    @InjectPage
    private PlanePaymentConfirm planePaymentConfirm;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private OtpManager otpManager;

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public void setupRender() {
        if (planePaymentView == null) {
            planePaymentView = new PlanePaymentView();
        }
        setTokenType();
        buildBillerModel();
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    private void buildBillerModel() {
        if (billerModel == null) {
            billerModel = genericSelectModelFactory.create(cacheManager.getBillers(Constants.PLANE_PAYMENT_CODE));
        }
    }

    private void setPaymentView() {
        planePaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        planePaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        planePaymentView.setBillerName(cacheManager.getBiller(Constants.PLANE_PAYMENT_CODE,
                planePaymentView.getBillerCode()).getBillerName());
        planePaymentView.setProductCode(cacheManager.getBillerProducts(Constants.PLANE_PAYMENT_CODE,
                planePaymentView.getBillerCode()).get(0).getProductCode());
        planePaymentView.setProductName(cacheManager.getBillerProducts(Constants.PLANE_PAYMENT_CODE,
                planePaymentView.getBillerCode()).get(0).getProductName());
        planePaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        planePaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        planePaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        planePaymentView.setTransactionType(Constants.PLANE_PAYMENT_INQ_CODE);
        planePaymentView.setInputType("M");
        try {
            planePaymentView = (PlanePaymentView) paymentService.inquiry(planePaymentView);
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
        planePaymentView.setTransactionType(Constants.PLANE_PAYMENT_CODE);
        planePaymentView.setToAccountType("00");
    }

    void onValidateFromForm() {
        if (planePaymentView.getCustomerReference() == null) {
            form.recordError(messages.get("nullCustomerReferenceError"));
        }
        if(planePaymentView.getBillerCode() == null){
            form.recordError(messages.get("nullBillerError"));
        }

        if (!form.getHasErrors() &&
                otpManager.validateToken(planePaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            setPaymentView();
            try {
                paymentService.confirm(planePaymentView);
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        planePaymentConfirm.setPlanePaymentView(planePaymentView);
        return planePaymentConfirm;
    }
}
