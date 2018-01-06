package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:13
 */
@Import(library = {"context:bprks/js/purchase/PlnPurchaseInput.js"})
public class PlnPurchaseInput {
	private Logger logger = Logger.getLogger(PlnPurchaseInput.class);
    @Property
    private String chooseValue;

    @Property
    private String customerReference1;

    @Property
    private String customerReference2;

    @Property
    private boolean saveBoxValue;

    @InjectComponent
    private Zone providerZone;

    @Persist
    @Property
    private PlnPurchaseView plnPurchaseView;

    @Property
    private int tokenType;

    @Property
    private SelectModel customerReferenceModel;

    @Property
    private SelectModel amountModel;

    @Property
    private SelectModel providerModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private PurchaseService purchaseService;

    @Inject
    private SessionManager sessionManager;

    @InjectPage
    private PlnPurchaseConfirm plnPurchaseConfirm;

    @InjectPage
    private PlnPurchaseReceipt plnPurchaseReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    public void onPrepare() {
       
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}    	
    	return null;
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(PlnPurchaseConfirm.class.toString()) ) {
    		plnPurchaseView = null;
    	} else {
    		if (plnPurchaseView.getCustomerReference() != null && plnPurchaseView.getInputType().equals("M")) {
   				customerReference1 = plnPurchaseView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(PlnPurchaseInput.class.toString());
        chooseValue = "fromId";
        setTokenType();
        if (plnPurchaseView == null) {
            plnPurchaseView = new PlnPurchaseView();
        }
        buildAmountModel();
        buildProviderModel(plnPurchaseView.getDenomination());
        buildCustomerReferenceModel();        
    }

    void onValidateFromForm() {
        if (chooseValue.equalsIgnoreCase("fromList")) {
            if (customerReference2 == null) {
                form.recordError(messages.get("customerReference2-requiredIf-message"));
            }
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            if (customerReference1 == null) {
                form.recordError(messages.get("customerReference1-requiredIf-message"));
            }
        }
        try {
            if (!form.getHasErrors()) {
                setPlnPurchaseViewData();
                
                plnPurchaseView.setTransactionType(Constants.PLN_PURCHASE_INQ_CODE);
                purchaseService.inquiry(plnPurchaseView);
                plnPurchaseView.setTransactionType(Constants.PLN_PURCHASE_CODE);
                purchaseService.confirm(plnPurchaseView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        } catch (Exception e) {
//            form.recordError(com.dwidasa.ib.Constants.EXCEPTION_OVERRIDE_MESSAGE);
            logger.info("e.getMessage()=" + e.getMessage());
        }
    }

    public Object onSuccess() {
        plnPurchaseConfirm.setPlnPurchaseView(plnPurchaseView);
        return plnPurchaseConfirm;
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create(purchaseService.getRegisters(
                sessionManager.getLoggedCustomerView().getId(), Constants.PLN_PURCHASE_CODE,
                cacheManager.getBillers(Constants.PLN_PURCHASE_CODE).get(0).getBillerCode()
        ));
        if (customerReferenceModel.getOptions().size() > 0) {
            customerReference2 = customerReferenceModel.getOptions().get(0).getValue().toString();
        }
    }

    private void buildAmountModel() {
        String billerCode = cacheManager.getBillers(Constants.PLN_PURCHASE_CODE).get(0).getBillerCode();
        amountModel = genericSelectModelFactory.create(purchaseService.getProductDenominations(
                cacheManager.getBillerProducts(Constants.PLN_PURCHASE_CODE, billerCode).get(0).getProductCode()
        ));
        if (amountModel.getOptions().size() > 0) {
            plnPurchaseView.setDenomination(amountModel.getOptions().get(0).getValue().toString());
            plnPurchaseView.setAmount(new BigDecimal(plnPurchaseView.getDenomination()));
        }
    }

    private void buildProviderModel(String denomination) {
        if (denomination == null) {
            providerModel = genericSelectModelFactory.create(new ArrayList<Provider>());
            return;
        } else {
            String billerCode = cacheManager.getBillers(Constants.PLN_PURCHASE_CODE).get(0).getBillerCode();
            String productCode = cacheManager.getBillerProducts(Constants.PLN_PURCHASE_CODE, billerCode).get(0).getProductCode();
            providerModel = genericSelectModelFactory.create(cacheManager.getProviders(
                    Constants.PLN_PURCHASE_CODE, billerCode, productCode, denomination));
            if (providerModel.getOptions().size() > 0) {
                plnPurchaseView.setProviderCode(providerModel.getOptions().get(0).getValue().toString());
            }
        }
    }

    public void setPlnPurchaseViewData() {
        plnPurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        plnPurchaseView.setAccountType(sessionManager.getAccountType(plnPurchaseView.getAccountNumber()));
        String billerCode = cacheManager.getBillers(Constants.PLN_PURCHASE_CODE).get(0).getBillerCode();
        String productCode = cacheManager.getBillerProducts(Constants.PLN_PURCHASE_CODE, billerCode).get(0).getProductCode();
        ProviderDenomination providerDenomination = cacheManager.getProviderDenomination(
                Constants.PLN_PURCHASE_CODE, billerCode, productCode, plnPurchaseView.getDenomination(),
                plnPurchaseView.getProviderCode());
        plnPurchaseView.setBillerCode(billerCode);
        plnPurchaseView.setProductCode(productCode);
        plnPurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        plnPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        plnPurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        plnPurchaseView.setProviderName(providerDenomination.getProvider().getProviderName());
        plnPurchaseView.setTransactionDate(new Date());
        plnPurchaseView.setSave(saveBoxValue);
        if (chooseValue.equalsIgnoreCase("fromList")) {
            plnPurchaseView.setInputType("L");
            plnPurchaseView.setCustomerReference(customerReference2);
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            plnPurchaseView.setInputType("M");
            plnPurchaseView.setCustomerReference(customerReference1);
        }
        plnPurchaseView.setAmount(new BigDecimal(plnPurchaseView.getDenomination()));
    }

    public Object onValueChangedFromAmount(String productDenomination) {
        buildProviderModel(productDenomination);
        return providerZone.getBody();
    }

    public void pageReset() {    	
//        plnPurchaseView = null;
//        plnPurchaseConfirm.setPlnPurchaseView(null);
//        plnPurchaseReceipt.setPlnPurchaseView(null);
    }
}
