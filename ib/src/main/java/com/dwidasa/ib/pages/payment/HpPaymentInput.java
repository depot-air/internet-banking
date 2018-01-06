package com.dwidasa.ib.pages.payment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.view.HpPaymentView;
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
 * Time: 12:52
 */
@Import(library = {"context:bprks/js/payment/HpPaymentInput.js"})
public class HpPaymentInput {
	private static Logger logger = Logger.getLogger( HpPaymentInput.class );
    @Persist
    @Property
    private HpPaymentView hpPaymentView;

    @Property
    private TelkomPaymentView telkomPaymentView;

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
    private HpPaymentConfirm hpPaymentConfirm;

    @InjectPage
    private HpPaymentReceipt hpPaymentReceipt;

    @Inject
    private OtpManager otpManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Property
    private SelectModel billerModel;

    @Property
    private SelectModel providerModel;

    @Property
    private SelectModel customerReferenceModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @InjectComponent
    private Zone providerZone;

    @InjectComponent
    private Zone hpNoZone;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    public void onPrepare() {

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

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(HpPaymentConfirm.class.toString()) ) {
    		hpPaymentView = null;
    	} else {
    		if (hpPaymentView.getCustomerReference() != null && hpPaymentView.getInputType().equals("M")) {
    			customerReference1 = hpPaymentView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(HpPaymentInput.class.toString());
        chooseValue = "fromId";
        setTokenType();
        if (hpPaymentView == null) {
            hpPaymentView = new HpPaymentView();
            hpPaymentView.setTransactionType(Constants.TELCO_PAYMENT_CODE);
        }

        buildBillerModel();
        buildProviderModel(hpPaymentView.getBillerCode());
        buildCustomerReferenceModel();
    }

    private void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(
                cacheManager.getBillers(hpPaymentView.getTransactionType()));

        if(hpPaymentView.getBillerCode() == null)
        {
            if (billerModel.getOptions().size() > 0) {
                String billerCode = billerModel.getOptions().get(0).getValue().toString();
                hpPaymentView.setBillerCode(billerCode);
            }
        }
    }

    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create(
                paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                        hpPaymentView.getTransactionType(), hpPaymentView.getBillerCode()));
    }

    private void buildProviderModel(String billerCode) {
        if (billerCode != null) {
            String productCode = cacheManager.getBillerProducts(Constants.TELCO_PAYMENT_CODE, billerCode)
                    .get(0).getProductCode();
            hpPaymentView.setProductCode(productCode);

            providerModel = genericSelectModelFactory.create(
                    cacheManager.getProviders(hpPaymentView.getTransactionType(), hpPaymentView.getBillerCode(),
                            hpPaymentView.getProductCode()));
            if (providerModel.getOptions().size() > 0) {
                hpPaymentView.setProviderCode(providerModel.getOptions().get(0).getValue().toString());
            }

            return;
        }

        providerModel = genericSelectModelFactory.create(new ArrayList<Provider>());
    }

    private void setPaymentView() {
        hpPaymentView.setTransactionDate(new Date());
        hpPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        hpPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        hpPaymentView.setBillerName(cacheManager.getBiller(Constants.TELCO_PAYMENT_CODE,
                hpPaymentView.getBillerCode()).getBillerName());
        hpPaymentView.setProviderName(cacheManager.getProviderProduct(hpPaymentView.getTransactionType(),
                hpPaymentView.getBillerCode(), hpPaymentView.getProductCode(), hpPaymentView.getProviderCode()).
                getProvider().getProviderName());
        hpPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        String tid = sessionManager.getLoggedCustomerView().getTerminalId();
        logger.info("tid=" + tid);
        hpPaymentView.setTerminalId(tid);
        hpPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        hpPaymentView.setSave(saveBoxValue);
        hpPaymentView.setToAccountType("00");
        hpPaymentView.setAmount(BigDecimal.ZERO);
        hpPaymentView.setFee(BigDecimal.ZERO);
    }

    //public Object onValueChangedFromBiller(String billerCode) {
    public void onValueChangedFromBiller(String billerCode) {
        hpPaymentView.setBillerCode(billerCode);
        buildProviderModel(billerCode);
        buildCustomerReferenceModel();
        ajaxResponseRenderer.addRender(providerZone).addRender(hpNoZone);
        //return providerZone.getBody();
    }

    private boolean validatePrefix()
    {
        boolean valid = false;
        List<BillerProduct> billerProducts =
                cacheManager.getBillerProducts
                        (Constants.TELCO_PAYMENT_CODE, hpPaymentView.getBillerCode());
        String billerProductId = "";
        for(BillerProduct billerProduct : billerProducts)
        {
            if(billerProduct.getProductCode().equals(hpPaymentView.getProductCode()))
            {
                billerProductId = billerProduct.getId() + "";
                break;
            }
        }
        List<CellularPrefix> cellularPrefixes =
                cacheManager.getCellularPrefixListByBillerProductId(billerProductId);
        if(cellularPrefixes == null)
            form.recordError(message.get("phoneNumberPrefixError"));
        else
        {
            for(CellularPrefix cellularPrefix : cellularPrefixes)
            {
                if(hpPaymentView.getCustomerReference().indexOf(cellularPrefix.getPrefix())==0)
                {
                    valid = true;
                    break;
                }
            }
            if(!valid)
                form.recordError(message.get("phoneNumberPrefixError"));
        }
        return valid;
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
            if (chooseValue.equalsIgnoreCase("fromList")) {
                hpPaymentView.setInputType("L");
                hpPaymentView.setCustomerReference(customerReference2);
            } else if (chooseValue.equalsIgnoreCase("fromId")) {
                hpPaymentView.setInputType("M");
                hpPaymentView.setCustomerReference(customerReference1);
            }
        	if(!validatePrefix())
                return;
            if (!form.getHasErrors()) {
            	setPaymentView();
            	if (hpPaymentView.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            		String json = PojoJsonMapper.toJson(hpPaymentView);
            		telkomPaymentView = PojoJsonMapper.fromJson(json, TelkomPaymentView.class);
            		
            		telkomPaymentView.setTransactionDate(new Date());
            		telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_INQ_CODE);            		
            		paymentService.inquiry(telkomPaymentView);
                    
            		telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
            		paymentService.confirm(telkomPaymentView);
            		            		
            	} else {
            		hpPaymentView.setTransactionType(Constants.TELCO_PAYMENT_INQ_CODE);
                    paymentService.inquiry(hpPaymentView);
                    hpPaymentView.setTransactionType(Constants.TELCO_PAYMENT_CODE);
                    paymentService.confirm(hpPaymentView);
            	}
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            hpPaymentView.setTransactionType(Constants.TELCO_PAYMENT_CODE);
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
    	if (hpPaymentView.getProductCode().equals(Constants.FLEXI_POSTPAID)){ 
    		
    		hpPaymentView.setReferenceName(telkomPaymentView.getReferenceName());
    		hpPaymentView.setAmount(telkomPaymentView.getAmount());
    		hpPaymentView.setAmount1(telkomPaymentView.getAmount1());
    		hpPaymentView.setAmount2(telkomPaymentView.getAmount2());
    		hpPaymentView.setAmount3(telkomPaymentView.getAmount3());
    		hpPaymentView.setFee(telkomPaymentView.getFee());
    		hpPaymentView.setTotal(telkomPaymentView.getTotal());
    		hpPaymentView.setResponseCode(telkomPaymentView.getResponseCode());
    		
    		logger.info("telkomPaymentView.getAccountNumber()=" + telkomPaymentView.getAccountNumber());
    		hpPaymentConfirm.setTelkomPaymentView(telkomPaymentView);
    	}
        hpPaymentConfirm.setHpPaymentView(hpPaymentView);
        
        return hpPaymentConfirm;
    }

    void pageReset() {
//        hpPaymentView = null;
//        hpPaymentConfirm.setHpPaymentView(null);
//        hpPaymentReceipt.setHpPaymentView(null);
    }
}
