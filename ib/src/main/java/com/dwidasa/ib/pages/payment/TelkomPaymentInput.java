package com.dwidasa.ib.pages.payment;

import java.util.Date;

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
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.TelkomPaymentView;
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
 * Time: 12:29
 */
@Import(library = {"context:bprks/js/payment/TelkomPaymentInput.js"})
public class TelkomPaymentInput {
    @Persist
    @Property
    private TelkomPaymentView telkomPaymentView;

    @Property
    private int tokenType;

    @InjectPage
    private TelkomPaymentConfirm telkomPaymentConfirm;

    @InjectPage
    private TelkomPaymentReceipt telkomPaymentReceipt;

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
    private SelectModel providerModel;

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
        customerReferenceModel = genericSelectModelFactory.create(
                paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                        telkomPaymentView.getTransactionType(), telkomPaymentView.getBillerCode()));
    }

    private void buildProviderModel() {
        providerModel = genericSelectModelFactory.create(
                cacheManager.getProviders(telkomPaymentView.getTransactionType(),
                        telkomPaymentView.getBillerCode(), telkomPaymentView.getProductCode()));

        if (providerModel.getOptions().size() > 0) {
            telkomPaymentView.setProviderCode(providerModel.getOptions().get(0).getValue().toString());
        }
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void onPrepare() {
       
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(TelkomPaymentConfirm.class.toString()) ) {
    		telkomPaymentView = null;
    	} else {
    		if (telkomPaymentView.getCustomerReference() != null && telkomPaymentView.getInputType().equals("M")) {
    			customerReference1 = telkomPaymentView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(TelkomPaymentInput.class.toString());
        chooseValue = "fromId";
        setTokenType();
        if (telkomPaymentView == null) {
            telkomPaymentView = new TelkomPaymentView();

            String billerCode = cacheManager.getBillers(Constants.TELKOM_PAYMENT_CODE).get(0).getBillerCode();
            String productCode = cacheManager.getBillerProducts(Constants.TELKOM_PAYMENT_CODE, billerCode)
                    .get(0).getProductCode();

            telkomPaymentView.setBillerCode(billerCode);
            telkomPaymentView.setProductCode(productCode);
            telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
        }
        buildProviderModel();
        buildCustomerReferenceModel();
    }

    private void setPaymentView() {
        telkomPaymentView.setTransactionDate(new Date());
        telkomPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        telkomPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());

        String billerName = cacheManager.getBiller(telkomPaymentView.getTransactionType(),
                telkomPaymentView.getBillerCode()).getBillerName();
        telkomPaymentView.setBillerName(billerName);

        String providerName = cacheManager.getProviderProduct(
                telkomPaymentView.getTransactionType(), telkomPaymentView.getBillerCode(),
                telkomPaymentView.getProductCode(), telkomPaymentView.getProviderCode()).getProvider().getProviderName();
        telkomPaymentView.setProviderName(providerName);

        telkomPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        telkomPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        telkomPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        telkomPaymentView.setSave(saveBoxValue);
        telkomPaymentView.setToAccountType("00");

        if (chooseValue.equalsIgnoreCase("fromList")) {
            telkomPaymentView.setInputType("L");
            telkomPaymentView.setCustomerReference(customerReference2);
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            telkomPaymentView.setInputType("M");
            telkomPaymentView.setCustomerReference(customerReference1);
        }
        telkomPaymentView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
    }

    void onValidateFromForm() {
        if (chooseValue.equalsIgnoreCase("fromId")) {
            if (customerReference1 == null) {
                form.recordError(message.get("customerReference1-requiredIf-message"));
            }
        } else if (chooseValue.equalsIgnoreCase("fromList")) {
            if (customerReference2 == null) {
                form.recordError(message.get("customerReference2-requiredIf-message"));
            }
        }
        try {
            if (!form.getHasErrors() ) {
                setPaymentView();
                telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_INQ_CODE);
                paymentService.inquiry(telkomPaymentView);
                telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
                paymentService.confirm(telkomPaymentView);
            }
        } catch (BusinessException e) {
            telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }

    }

    public Object onSuccess() {
        telkomPaymentConfirm.setTelkomPaymentView(telkomPaymentView);
        return telkomPaymentConfirm;
    }

    void pageReset() {
//        telkomPaymentView = null;
//        telkomPaymentConfirm.setTelkomPaymentView(null);
//        telkomPaymentReceipt.setTelkomPaymentView(null);
    }
}
