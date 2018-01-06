package com.dwidasa.ib.pages.payment;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
//import com.dwidasa.ib.services.WaterListSelect;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:01
 */
public class WaterPaymentInput {
	private static Logger logger = Logger.getLogger( WaterPaymentInput.class );
    @Persist
    @Property
    private WaterPaymentView waterPaymentView;
    
    @InjectPage
    private WaterPaymentConfirm waterPaymentConfirm;
    
    @Inject
    private BillerDao billerDao;

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

    @Inject
    private OtpManager otpManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel billerModel;
    
    @Property
    private SelectModel providerModel;
    
    @Property
    private SelectModel customerReferenceModel;

    @Autowired
    private ExtendedProperty extendedProperty;

    @InjectComponent
    private Zone billerProviderZone;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    void onValueChangedFromBiller(String billerCode) {
    	waterPaymentView.setBillerCode(billerCode);
    	//System.out.println("HOHOHOHO Biller "+billerCode);
    	String productCode = cacheManager.getBillerProducts(Constants.WATER.TRANSACTION_TYPE.POSTING, billerCode)
                .get(0).getProductCode();
    	logger.info("billerCode=" + billerCode + " productCode=" + productCode);
    	waterPaymentView.setProductCode(productCode);
    	buildProviderModel();
    	ajaxResponseRenderer.addRender(billerProviderZone);        
    }

    private void buildCustomerReferenceModel() {
    	
        customerReferenceModel = genericSelectModelFactory.create(
                paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                		waterPaymentView.getTransactionType(), null));
    }

    private void buildBillerModel() {
    	billerModel = genericSelectModelFactory.create(cacheManager.getBillers(
                com.dwidasa.engine.Constants.WATER.TRANSACTION_TYPE.POSTING));
    	
//    	billerModel = new WaterListSelect(cacheManager.getBillers(
//                com.dwidasa.engine.Constants.WATER.TRANSACTION_TYPE.POSTING));
//    	
//    	Biller b = billerDao.getBillerName(billerModel.getOptions().get(0).getValue().toString());
//    	System.out.println("Biller Code "+b.getBillerCode());
    	
        if(waterPaymentView.getBillerCode() == null)
        if (billerModel.getOptions().size() > 0) {
        	waterPaymentView.setBillerCode(billerModel.getOptions().get(0).getValue().toString());
        }
    }

    private void buildProviderModel() {
        providerModel = genericSelectModelFactory.create(
                cacheManager.getProviders(waterPaymentView.getTransactionType(),
                		waterPaymentView.getBillerCode(), waterPaymentView.getProductCode()));

        if (providerModel.getOptions().size() > 0) {
        	waterPaymentView.setProviderCode(providerModel.getOptions().get(0).getValue().toString());
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
    	if (!sessionManager.getSessionLastPage().equals(WaterPaymentConfirm.class.toString()) ) {
    		waterPaymentView = null;
    	} else {
    		System.out.println("input waterPaymentView=" + waterPaymentView);
//    		if (waterPaymentView.getCustomerReference() != null && waterPaymentView.getInputType().equals("M")) {
//    			customerReference1 = waterPaymentView.getCustomerReference();
//    		}
    	}
    	sessionManager.setSessionLastPage(WaterPaymentInput.class.toString());
        chooseValue = "fromId";
        setTokenType();

        if (waterPaymentView == null) {
        	waterPaymentView = new WaterPaymentView();

        	String billerCode = cacheManager.getBillers(Constants.WATER.TRANSACTION_TYPE.POSTING).get(0).getBillerCode();
        	
            String productCode = cacheManager.getBillerProducts(Constants.WATER.TRANSACTION_TYPE.POSTING, billerCode)
                    .get(0).getProductCode();
            
           
            logger.info("productCode initial=" + productCode);
            waterPaymentView.setBillerCode(billerCode);
            waterPaymentView.setProductCode(productCode);
            waterPaymentView.setTransactionType(Constants.WATER.TRANSACTION_TYPE.POSTING);
            logger.info("waterPaymentView.getProductCode() initial=" + waterPaymentView.getProductCode());
        }
        buildBillerModel();
        buildProviderModel();
        buildCustomerReferenceModel();
    }

    private void setPaymentView() {
    	waterPaymentView.setTransactionDate(new Date());
    	waterPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	waterPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());

//        String billerName = cacheManager.getBiller(waterPaymentView.getTransactionType(),
//        		waterPaymentView.getBillerCode()).getBillerName();
//        waterPaymentView.setBillerName(billerName);

        if (waterPaymentView.getBillerCode().equals(Constants.WATER.BILLER_CODE.PALYJA)) {
        	waterPaymentView.setBillerName(Constants.WATER.BILLER_NAME.PALYJA);
        } else if (waterPaymentView.getBillerCode().equals(Constants.WATER.BILLER_CODE.AETRA)) {
        	waterPaymentView.setBillerName(Constants.WATER.BILLER_NAME.AETRA);
        } 
        
        String providerName = cacheManager.getProviderProduct(
        waterPaymentView.getTransactionType(), waterPaymentView.getBillerCode(),
        waterPaymentView.getProductCode(), waterPaymentView.getProviderCode()).getProvider().getProviderName();
        
        waterPaymentView.setProviderName(providerName);
        
        if (waterPaymentView.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAMSurabaya)){
        	waterPaymentView.setBillerCode(Constants.WATER.BILLER_CODE.PAM_BILLER);
        	waterPaymentView.setBillerName(Constants.WATER.BILLER_NAME.PAMSurabaya);
        } else if (waterPaymentView.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAMSemarang)){
        	waterPaymentView.setBillerCode(Constants.WATER.BILLER_CODE.PAM_BILLER);
        	waterPaymentView.setBillerName(Constants.WATER.BILLER_NAME.PAMSemarang);
        }
        
        System.out.println("HOHOHO Biller "+waterPaymentView.getBillerCode());

        waterPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        waterPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        waterPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        waterPaymentView.setSave(saveBoxValue);
        waterPaymentView.setToAccountType("00");

        if (chooseValue.equalsIgnoreCase("fromList")) {
        	waterPaymentView.setInputType("L");
        	waterPaymentView.setCustomerReference(customerReference2);
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
        	waterPaymentView.setInputType("M");
        	waterPaymentView.setCustomerReference(customerReference1);
        	waterPaymentView.setTransactionType(Constants.WATER.TRANSACTION_TYPE.POSTING);
        }
        waterPaymentView.setToAccountType("00");
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
                waterPaymentView.setTransactionType(Constants.WATER.TRANSACTION_TYPE.INQUIRY);
                paymentService.inquiry(waterPaymentView);
                waterPaymentView.setTransactionType(Constants.WATER.TRANSACTION_TYPE.POSTING);
                paymentService.confirm(waterPaymentView);
            }
        } catch (BusinessException e) {
        	waterPaymentView.setTransactionType(Constants.WATER.TRANSACTION_TYPE.POSTING);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }

    }

	@DiscardAfter
    public Object onSuccess() {
		//setPaymentView();
        waterPaymentConfirm.setWaterPaymentView(waterPaymentView);
        return waterPaymentConfirm;
    }


    void pageReset() {
//        waterPaymentView = null;
    }
}
