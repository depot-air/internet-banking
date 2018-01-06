package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CcPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 22/07/11
 * Time: 11:49
 */
public class CcPaymentInput {
    @Property
    private CcPaymentView ccPaymentView;
    @Property
    private String transactionType;
    @Property
    private String chooseValue;
    @Property
    private boolean saveBoxValue;
    @Property
    private int tokenType;
    @Property
    private String customerReference1;
    @Property
    private String customerReference2;
    @Inject
    private CacheManager cacheManager;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private PaymentService paymentService;
    @InjectPage
    private CcPaymentConfirm ccPaymentConfirm;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;
    @Property
    private TokenView tokenView;
    @Inject
    private OtpManager otpManager;

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
        if (ccPaymentView == null) {
             ccPaymentView = new CcPaymentView();
        }
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public void setupRender() {
        setTokenType();
        if (ccPaymentView == null) {
            ccPaymentView = new CcPaymentView();
        }
        ccPaymentView.setTransactionType(Constants.CC_PAYMENT_CODE);

    }

    private void setPaymentView() {
        ccPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        ccPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        ccPaymentView.setBillerName(cacheManager.getBiller(Constants.CC_PAYMENT_CODE,
                ccPaymentView.getBillerCode()).getBillerName());
        ccPaymentView.setProductCode(cacheManager.getBillerProducts(Constants.CC_PAYMENT_CODE,
                ccPaymentView.getBillerCode()).get(0).getProductCode());
        ccPaymentView.setProductName(cacheManager.getBillerProducts(Constants.CC_PAYMENT_CODE,
                ccPaymentView.getBillerCode()).get(0).getProductName());
        ccPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        ccPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        ccPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        ccPaymentView.setSave(saveBoxValue);
        if (chooseValue.equalsIgnoreCase("fromList")) {
            ccPaymentView.setInputType("L");
            ccPaymentView.setCustomerReference(customerReference2);

        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            ccPaymentView.setInputType("M");
            ccPaymentView.setCustomerReference(customerReference1);
            ccPaymentView.setTransactionType(Constants.CC_PAYMENT_INQ_CODE);
            try {
                ccPaymentView = (CcPaymentView) paymentService.inquiry(ccPaymentView);
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
            ccPaymentView.setTransactionType(Constants.CC_PAYMENT_CODE);
        }
        ccPaymentView.setToAccountType("00");
    }

    void onValidateFromForm() {
        form.clearErrors();
        if (chooseValue.equalsIgnoreCase("fromId")) {
            if (customerReference1 == null) {
                form.recordError(messages.get("nullCustomerReferenceError"));
            } else if (!customerReference1.matches("[0-9]+")) {
                form.recordError(messages.get("formatCustomerReferenceError"));
            } else if (customerReference1.length() != 16) {
                form.recordError(messages.get("lengthCustomerReferenceError"));
            }
        } else if (chooseValue.equalsIgnoreCase("fromList")) {
            if (customerReference2 == null) {
                form.recordError(messages.get("nullCustomerReferenceError"));
            } else if (!customerReference2.matches("[0-9]+")) {
                form.recordError(messages.get("formatCustomerReferenceError"));
            }
        }
        if (ccPaymentView.getAmount() == null) {
            form.recordError(messages.get("nullAmountError"));
        }
        if (!form.getHasErrors() &&
                otpManager.validateToken(ccPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            setPaymentView();
            try {
                paymentService.confirm(ccPaymentView);
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        ccPaymentConfirm.setCcPaymentView(ccPaymentView);
        return ccPaymentConfirm;
    }

}
