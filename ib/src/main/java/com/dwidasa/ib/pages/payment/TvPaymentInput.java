package com.dwidasa.ib.pages.payment;

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
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TvPaymentView;
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
 * Time: 12:56
 */
@Import(library = {"context:bprks/js/payment/TvPaymentInput.js"})
public class TvPaymentInput {
	private static Logger logger = Logger.getLogger( TvPaymentInput.class );
    @Persist
    @Property
    private TvPaymentView tvPaymentView;
    
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
    private TvPaymentConfirm tvPaymentConfirm;

    @InjectPage
    private TvPaymentReceipt tvPaymentReceipt;

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
    private Zone customerReferenceZone;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create
                (paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                        tvPaymentView.getTransactionType(), tvPaymentView.getBillerCode()));
    }

    private void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(
                cacheManager.getBillers(tvPaymentView.getTransactionType()));
        if(tvPaymentView.getBillerCode() == null)
        {
            if (billerModel.getOptions().size() > 0) {
                tvPaymentView.setBillerCode(billerModel.getOptions().get(0).getValue().toString());
            }
        }
    }

    private void buildProviderModel(String billerCode) {
        String productCode = cacheManager.getBillerProducts(Constants.ENTERTAINMENT_PAYMENT_CODE, billerCode)
                .get(0).getProductCode();
        tvPaymentView.setProductCode(productCode);

        providerModel = genericSelectModelFactory.create(
                cacheManager.getProviders(tvPaymentView.getTransactionType(),
                        billerCode, tvPaymentView.getProductCode()));
        if (providerModel.getOptions().size() > 0) {
            tvPaymentView.setProviderCode(providerModel.getOptions().get(0).getValue().toString());
        }
    }

    public void onValueChangedFromBillerCode(String billerCode) {
        tvPaymentView.setBillerCode(billerCode);
        buildProviderModel(billerCode);
        buildCustomerReferenceModel();
        ajaxResponseRenderer.addRender(providerZone).addRender(customerReferenceZone);
        //return providerZone.getBody();
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
    	if (!sessionManager.getSessionLastPage().equals(TvPaymentConfirm.class.toString()) ) {
    		tvPaymentView = null;
    	} else {
    		if (tvPaymentView.getCustomerReference() != null && tvPaymentView.getInputType().equals("M")) {
    			customerReference1 = tvPaymentView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(TvPaymentInput.class.toString());
        chooseValue = "fromId";
        setTokenType();
        if (tvPaymentView == null) {
            tvPaymentView = new TvPaymentView();
            List<Biller> billers = cacheManager.getBillers(Constants.ENTERTAINMENT_PAYMENT_CODE);
            String billerCode = billers.get(0).getBillerCode();
            String productCode = cacheManager.getBillerProducts(Constants.ENTERTAINMENT_PAYMENT_CODE, billerCode)
                    .get(0).getProductCode();

            tvPaymentView.setBillerCode(billerCode);
            tvPaymentView.setProductCode(productCode);
            tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_CODE);
        }
        buildBillerModel();
        buildCustomerReferenceModel();
        buildProviderModel(tvPaymentView.getBillerCode());
    }

    private void setPaymentView() {
        tvPaymentView.setTransactionDate(new Date());
        tvPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        tvPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());

        String billerName = cacheManager.getBiller(tvPaymentView.getTransactionType(),
                tvPaymentView.getBillerCode()).getBillerName();
        tvPaymentView.setBillerName(billerName);

        tvPaymentView.setProviderName(cacheManager.getProviderProduct(tvPaymentView.getTransactionType(),
                tvPaymentView.getBillerCode(), tvPaymentView.getProductCode(), tvPaymentView.getProviderCode()).
                getProvider().getProviderName());
        
        tvPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        tvPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        tvPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        tvPaymentView.setSave(saveBoxValue);
        tvPaymentView.setToAccountType("00");

        if (chooseValue.equalsIgnoreCase("fromList")) {
            tvPaymentView.setInputType("L");
            tvPaymentView.setCustomerReference(customerReference2);
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
            tvPaymentView.setInputType("M");
            tvPaymentView.setCustomerReference(customerReference1);
        }
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
                /*
                tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_INQ_CODE);
                paymentService.inquiry(tvPaymentView);
                System.out.println("Amount  : "+tvPaymentView.getAmount());
                tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_CODE);
                paymentService.confirm(tvPaymentView);
                */
            	if (tvPaymentView.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE)){
            		String json = PojoJsonMapper.toJson(tvPaymentView);
            		telkomPaymentView = PojoJsonMapper.fromJson(json, TelkomPaymentView.class);
            		
            		telkomPaymentView.setTransactionDate(new Date());
            		if (tvPaymentView.getBillerCode().equals(Constants.TELKOM_VISION) ) {
	            		telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_INQ_CODE);
	            		paymentService.inquiry(telkomPaymentView);
	                    
	            		telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
	            		paymentService.confirm(telkomPaymentView);
            		} else {
            			tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_INQ_CODE);
            			tvPaymentView = (TvPaymentView)paymentService.inquiry(tvPaymentView);
            			
            			json = PojoJsonMapper.toJson(tvPaymentView);
                		telkomPaymentView = PojoJsonMapper.fromJson(json, TelkomPaymentView.class);
                        tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_CODE);
                        paymentService.confirm(tvPaymentView);
            		}
            		            		
            	} else {
            		tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_INQ_CODE);
                    paymentService.inquiry(tvPaymentView);
                    tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_CODE);
                    paymentService.confirm(tvPaymentView);
            	}
            }
        } catch (BusinessException e) {
            tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_CODE);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
    	if (telkomPaymentView != null && telkomPaymentView.getResponseCode() != null){ 
    		tvPaymentView.setReferenceName(telkomPaymentView.getReferenceName());
    		tvPaymentView.setAmount(telkomPaymentView.getAmount());
    		tvPaymentView.setAmount1(telkomPaymentView.getAmount1());
    		tvPaymentView.setAmount2(telkomPaymentView.getAmount2());
    		tvPaymentView.setAmount3(telkomPaymentView.getAmount3());
    		tvPaymentView.setBillPeriod1(telkomPaymentView.getBillPeriod1());
    		tvPaymentView.setBillPeriod2(telkomPaymentView.getBillPeriod2());
    		tvPaymentView.setBillPeriod3(telkomPaymentView.getBillPeriod3());

    		tvPaymentView.setFee(telkomPaymentView.getFee());
    		tvPaymentView.setTotal(telkomPaymentView.getTotal());
    		tvPaymentView.setResponseCode(telkomPaymentView.getResponseCode());
    		logger.info("telkomPaymentView.getAccountNumber()=" + telkomPaymentView.getAccountNumber());
    		tvPaymentConfirm.setTelkomPaymentView(telkomPaymentView);
    	}
        //setPaymentView();
        tvPaymentConfirm.setTvPaymentView(tvPaymentView);
        return tvPaymentConfirm;
    }

    void pageReset() {
//        tvPaymentView = null;
//        tvPaymentConfirm.setTvPaymentView(null);
//        tvPaymentReceipt.setTvPaymentView(null);
    }
}
