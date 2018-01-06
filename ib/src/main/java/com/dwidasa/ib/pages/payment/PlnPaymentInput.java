package com.dwidasa.ib.pages.payment;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 12:09
 */
@Import(library = {"context:bprks/js/payment/PlnPaymentInput.js"})
public class PlnPaymentInput {

    @Persist
    @Property
    private PlnPaymentView plnPaymentView;

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
    private PlnPaymentConfirm plnPaymentConfirm;

    @InjectPage
    private PlnPaymentReceipt plnPaymentReceipt;

    @Inject
    private OtpManager otpManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel providerModel;

    @Property
    private SelectModel customerReferenceModel;

    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create(
                paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                        plnPaymentView.getTransactionType(), plnPaymentView.getBillerCode()));
    }

    private void buildProviderModel() {
        providerModel = genericSelectModelFactory.create(
                cacheManager.getProviders(plnPaymentView.getTransactionType(),
                        plnPaymentView.getBillerCode(), plnPaymentView.getProductCode()));
        if (providerModel.getOptions().size() > 0) {
            plnPaymentView.setProviderCode(providerModel.getOptions().get(0).getValue().toString());
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
    	if (!sessionManager.getSessionLastPage().equals(PlnPaymentConfirm.class.toString()) ) {
    		plnPaymentView = null;
    	} else {
    		if (plnPaymentView.getCustomerReference() != null && plnPaymentView.getInputType().equals("M")) {
    			customerReference1 = plnPaymentView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(PlnPaymentInput.class.toString());
        chooseValue = "fromId";
        setTokenType();

        if (plnPaymentView == null){
            plnPaymentView = new PlnPaymentView();

            String billerCode = cacheManager.getBillers(Constants.PLN_PAYMENT_CODE).get(0).getBillerCode();
            String productCode = cacheManager.getBillerProducts(Constants.PLN_PAYMENT_CODE, billerCode)
                    .get(0).getProductCode();

            plnPaymentView.setBillerCode(billerCode);
            plnPaymentView.setProductCode(productCode);
            plnPaymentView.setTransactionType(Constants.PLN_PAYMENT_CODE);
        }

        plnPaymentView.setAmount(BigDecimal.ZERO);
        plnPaymentView.setFee(BigDecimal.ZERO);

        buildProviderModel();
        buildCustomerReferenceModel();
    }

    private void setPaymentView() {
        plnPaymentView.setTransactionDate(new Date());
        plnPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        plnPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        plnPaymentView.setAmount(BigDecimal.ZERO);
        plnPaymentView.setFee(BigDecimal.ZERO);

        String billerName = cacheManager.getBiller(plnPaymentView.getTransactionType(),
                plnPaymentView.getBillerCode()).getBillerName();
        plnPaymentView.setBillerName(billerName);

        String providerName = cacheManager.getProviderProduct(
                plnPaymentView.getTransactionType(), plnPaymentView.getBillerCode(),
                plnPaymentView.getProductCode(), plnPaymentView.getProviderCode()).getProvider().getProviderName();
        plnPaymentView.setProviderName(providerName);

        plnPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        plnPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        plnPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        plnPaymentView.setSave(saveBoxValue);
        plnPaymentView.setToAccountType("00");

        if (chooseValue.equalsIgnoreCase("fromList")) {
            plnPaymentView.setInputType("L");
            plnPaymentView.setCustomerReference(customerReference2);
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            plnPaymentView.setInputType("M");
            plnPaymentView.setCustomerReference(customerReference1);
            plnPaymentView.setTransactionType(Constants.PLN_PAYMENT_INQ_CODE);
            plnPaymentView.setTransactionType(Constants.PLN_PAYMENT_CODE);
        }
    }

    public void onValidateFromForm() {
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
            if (!form.getHasErrors()) {
                setPaymentView();
                
                plnPaymentView.setTransactionType(Constants.PLN_PAYMENT_INQ_CODE);
                paymentService.inquiry(plnPaymentView);
                plnPaymentView.setTransactionType(Constants.PLN_PAYMENT_CODE);
                paymentService.confirm(plnPaymentView);
            }
        } catch (BusinessException e) {
        	form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
        plnPaymentConfirm.setPlnPaymentView(plnPaymentView);
        return plnPaymentConfirm;
    }

    void pageReset() {
//        plnPaymentView = null;
//        plnPaymentConfirm.setPlnPaymentView(null);
//        plnPaymentReceipt.setPlnPaymentView(null);
    }
}
