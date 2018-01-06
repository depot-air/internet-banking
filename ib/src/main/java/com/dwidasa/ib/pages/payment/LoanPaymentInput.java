package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.LoanPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:05
 */
public class LoanPaymentInput {
    @Persist
    @Property
    private LoanPaymentView loanPaymentView;
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
    private LoanPaymentConfirm loanPaymentConfirm;
    @Property
    private TokenView tokenView;
    @Inject
    private OtpManager otpManager;
    @InjectComponent
    private Form form;
    @Inject
    private Messages message;

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public void setupRender() {
        setTokenType();
        if (loanPaymentView == null) {
            loanPaymentView = new LoanPaymentView();
        }
        loanPaymentView.setTransactionType(Constants.MULTIFINANCE_PAYMENT_CODE);

    }

    private void setPaymentView() {
        loanPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        loanPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        loanPaymentView.setBillerName(cacheManager.getBiller(Constants.MULTIFINANCE_PAYMENT_CODE,
                loanPaymentView.getBillerCode()).getBillerName());
        loanPaymentView.setProductCode(cacheManager.getBillerProducts(Constants.MULTIFINANCE_PAYMENT_CODE,
                loanPaymentView.getBillerCode()).get(0).getProductCode());
        loanPaymentView.setProductName(cacheManager.getBillerProducts(Constants.MULTIFINANCE_PAYMENT_CODE,
                loanPaymentView.getBillerCode()).get(0).getProductName());
        loanPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        loanPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        loanPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        loanPaymentView.setSave(saveBoxValue);
        if (chooseValue.equalsIgnoreCase("fromList")) {
            loanPaymentView.setInputType("L");
            loanPaymentView.setCustomerReference(customerReference2);

        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            loanPaymentView.setInputType("M");
            loanPaymentView.setCustomerReference(customerReference1);
            loanPaymentView.setTransactionType(Constants.MULTIFINANCE_PAYMENT_INQ_CODE);
            loanPaymentView.setTransactionType(Constants.MULTIFINANCE_PAYMENT_CODE);
        }
        loanPaymentView.setToAccountType("00");
    }

    void onValidateFromForm() {
        if (chooseValue.equalsIgnoreCase("fromId")) {
            if (customerReference1 == null) {
                form.recordError(message.get("nullReceiverNumberError"));
            }
        } else if (chooseValue.equalsIgnoreCase("fromList")) {
            if (customerReference2 == null) {
                form.recordError(message.get("nullFromListError"));
            }
        }
        if (loanPaymentView.getAmount() == null) {
            form.recordError(message.get("nullAmountError"));
        }
        if (!form.getHasErrors() &&
                otpManager.validateToken(loanPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            setPaymentView();
            try {
                paymentService.confirm(loanPaymentView);
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        setPaymentView();
        loanPaymentConfirm.setLoanPaymentView(loanPaymentView);
        return loanPaymentConfirm;
    }

}
