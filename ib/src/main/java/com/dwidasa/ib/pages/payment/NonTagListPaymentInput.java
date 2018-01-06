package com.dwidasa.ib.pages.payment;

import java.util.Date;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/08/11
 * Time: 22:43
 */
public class NonTagListPaymentInput {
    @Persist
    @Property
    private NonTagListPaymentView nonTagListPaymentView;

    @Property
    private int tokenType;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel billerModel;

    @Property
    private String registrationNumber;

    @Inject
    private PaymentService paymentService;

    @InjectPage
    private NonTagListPaymentConfirm nonTagListPaymentConfirm;

    @InjectPage
    private NonTagListPaymentReceipt nonTagListPaymentReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private OtpManager otpManager;

    @Inject
    private Messages message;
    
    public void onPrepare() {
        
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(NonTagListPaymentConfirm.class.toString()) ) {
    		nonTagListPaymentView = null;
    	} else {
    		if (nonTagListPaymentView.getCustomerReference() != null && nonTagListPaymentView.getInputType().equals("M")) {
    			registrationNumber = nonTagListPaymentView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(NonTagListPaymentInput.class.toString());
        if (nonTagListPaymentView == null) {
            nonTagListPaymentView = new NonTagListPaymentView();
        }
        setTokenType();
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    private void setPaymentView() {
        nonTagListPaymentView.setTransactionDate(new Date());
        nonTagListPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        nonTagListPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        nonTagListPaymentView.setBillerCode(cacheManager.getBillers(Constants.NONTAGLIST_PAYMENT_CODE).
                get(0).getBillerCode());
        nonTagListPaymentView.setBillerName(cacheManager.getBiller(Constants.NONTAGLIST_PAYMENT_CODE,
                nonTagListPaymentView.getBillerCode()).getBillerName());
        nonTagListPaymentView.setProductCode(cacheManager.getBillerProducts(Constants.NONTAGLIST_PAYMENT_CODE,
                nonTagListPaymentView.getBillerCode()).get(0).getProductCode());
        nonTagListPaymentView.setProductName(cacheManager.getBillerProducts(Constants.NONTAGLIST_PAYMENT_CODE,
                nonTagListPaymentView.getBillerCode()).get(0).getProductName());
        nonTagListPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        nonTagListPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        nonTagListPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        //nonTagListPaymentView.setCustomerReference(registrationNumber);
        nonTagListPaymentView.setInputType("M");
        nonTagListPaymentView.setToAccountType("00");
        nonTagListPaymentView.setProviderCode(Constants.GSP.PROVIDER_CODE);
    }

    void onValidateFromForm() {
    	if (nonTagListPaymentView.getCustomerReference().length() != 13) {
            form.recordError(message.get("registrationNumber-wrong-message"));
        }
        try {
            if (!form.getHasErrors() ) {
                setPaymentView();
                nonTagListPaymentView.setTransactionType(Constants.NONTAGLIST_PAYMENT_INQ_CODE);
                paymentService.inquiry(nonTagListPaymentView);
            }
        } catch (BusinessException e) {
            nonTagListPaymentView.setTransactionType(Constants.NONTAGLIST_PAYMENT_INQ_CODE);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
        nonTagListPaymentConfirm.setNonTagListPaymentView(nonTagListPaymentView);
        return nonTagListPaymentConfirm;
    }

    void pageReset() {
//        nonTagListPaymentView = null;
//        nonTagListPaymentConfirm.setNonTagListPaymentView(null);
//        nonTagListPaymentReceipt.setNonTagListPaymentView(null);
    }
}
