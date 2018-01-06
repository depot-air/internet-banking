package com.dwidasa.ib.pages.payment;

import java.util.Date;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 12:46
 */
@Import(library = {"context:bprks/js/payment/InternetPaymentInput.js"})
public class InternetPaymentInput {
    @Persist
    @Property
    private InternetPaymentView internetPaymentView;

    @Property
    private int tokenType;

    @InjectPage
    private InternetPaymentConfirm internetPaymentConfirm;

    @InjectPage
    private InternetPaymentReceipt internetPaymentReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Inject
    private OtpManager otpManager;

    @Property
    private String chooseValue;

    @Property
    private boolean saveBoxValue;

    @Property
    private String customerReference1;

    @Property
    private String customerReference2;

    @Property
    private SelectModel billerModel;

    @Property
    private SelectModel customerReferenceModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private PaymentService paymentService;

    @Inject
    private SessionManager sessionManager;

    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create
                (paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                        internetPaymentView.getTransactionType(), internetPaymentView.getBillerCode()));
    }

    private void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(
                cacheManager.getBillers(internetPaymentView.getTransactionType()));
        if (billerModel.getOptions().size() > 0) {
            internetPaymentView.setBillerCode(billerModel.getOptions().get(0).getValue().toString());
        }
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void onPrepare() {
        
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(InternetPaymentConfirm.class.toString()) ) {
    		internetPaymentView = null;
    	} else {
    		if (internetPaymentView.getCustomerReference() != null && internetPaymentView.getInputType().equals("M")) {
    			customerReference1 = internetPaymentView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(InternetPaymentInput.class.toString());
        chooseValue = "fromId";
        setTokenType();
        if (internetPaymentView == null) {
            internetPaymentView = new InternetPaymentView();
            List<Biller> billers = cacheManager.getBillers(Constants.INTERNET_PAYMENT_CODE);
            String billerCode = billers.get(0).getBillerCode();
            String productCode = cacheManager.getBillerProducts(Constants.INTERNET_PAYMENT_CODE, billerCode)
                    .get(0).getProductCode();

            internetPaymentView.setBillerCode(billerCode);
            internetPaymentView.setProductCode(productCode);
            internetPaymentView.setTransactionType(Constants.INTERNET_PAYMENT_CODE);
        }
        buildBillerModel();
        buildCustomerReferenceModel();
    }

    private void setPaymentView() {
        internetPaymentView.setTransactionDate(new Date());
        internetPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        internetPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());

        String billerName = cacheManager.getBiller(internetPaymentView.getTransactionType(),
                internetPaymentView.getBillerCode()).getBillerName();
        internetPaymentView.setBillerName(billerName);

        internetPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        internetPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        internetPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        internetPaymentView.setSave(saveBoxValue);
        internetPaymentView.setToAccountType("00");

        if (chooseValue.equalsIgnoreCase("fromList")) {
            internetPaymentView.setInputType("L");
            internetPaymentView.setCustomerReference(customerReference2);
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            internetPaymentView.setInputType("M");
            internetPaymentView.setCustomerReference(customerReference1);
        }
        internetPaymentView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
    }

    void onValidateFromForm() {
        if (chooseValue.equalsIgnoreCase("fromId")) {
            if (customerReference1 == null) {
                form.recordError(message.get("customerReference1-requiredIf-message"));
            }
        } else if (chooseValue.equalsIgnoreCase("fromList")) {
            if (customerReference2 == null) {
                form.recordError(message.get("customerReference2-required-message"));
            }
        }
        try {
            if (!form.getHasErrors() ) {
                setPaymentView();
                internetPaymentView.setTransactionType(Constants.INTERNET_PAYMENT_INQ_CODE);
                paymentService.inquiry(internetPaymentView);
                internetPaymentView.setTransactionType(Constants.INTERNET_PAYMENT_CODE);
                paymentService.confirm(internetPaymentView);
            }
        } catch (BusinessException e) {
            internetPaymentView.setTransactionType(Constants.INTERNET_PAYMENT_CODE);

            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
        internetPaymentConfirm.setInternetPaymentView(internetPaymentView);
        return internetPaymentConfirm;
    }

    void pageReset() {
//        internetPaymentView = null;
//        internetPaymentConfirm.setInternetPaymentView(null);
//        internetPaymentReceipt.setInternetPaymentView(null);
    }
}
