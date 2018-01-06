package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.aspectj.apache.bcel.classfile.Constant;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.dao.ProductDenominationDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CellularPrefixService;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.pages.payment.PlnPaymentConfirm;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Page class for handling input of voucher puchase template.
 *
 * @author rk
 */
public class MncLifePurchaseInput {
    @Property
    @Persist
    private MncLifePurchaseView mncLifePurchaseView;

    @Property
    private String chooseValue;
    
    @Property
    private String chooseTipeDokumen;

    @Property
    private int tokenType;

    @Inject
	private Messages messages;
    
    @Inject
    private PurchaseService purchaseService;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private CacheManager cacheManager;

   
    @InjectComponent
    private Form form;

    @Property
    private String customerReference1;
    
    @Property
    private String customerReference2;

    @Inject
    private Messages message;

    @Inject
    private OtpManager otpManager;

    @Property
    private SelectModel providerDenominationModel;

    @InjectPage
    private MncLifePurchaseConfirm mncLivePurchaseConfirm;

    @InjectPage
    private VoucherPurchaseReceipt voucherPurchaseReceipt;

    @Inject
    private CellularPrefixService cellularPrefixService;

    @Inject
    private AccountService accountService;

    @Inject
	private ParameterDao parameterDao;

    @Inject
	private ProductDenominationDao productDenominationDao;
    
    @Inject
    private ExtendedProperty extendedProperty;
    
    @Property
    private SelectModel customerReferenceModel;
    
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}    	
    	return null;
    }

    
    public String getDateFieldFormat() {
		return Constants.SHORT_FORMAT;
	}
    
    public void setupRender() {
 
    	if (!sessionManager.getSessionLastPage().equals(MncLifePurchaseConfirm.class.toString()) ) {
    		mncLifePurchaseView = null;
    	} else {
    		if (mncLifePurchaseView.getCustomerReference() != null) {
   				customerReference1 = mncLifePurchaseView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(MncLifePurchaseInput.class.toString());
        chooseValue = "fromLaki";
        //chooseTipeDokumen = "fromPolis";
        //setTokenType();
        if (mncLifePurchaseView == null) {
        	mncLifePurchaseView = new MncLifePurchaseView();
        }
        
        buildCustomerReferenceModel();
        
    }
    
    
    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create(purchaseService.getRegisters(
                sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian,
                cacheManager.getBillers(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian).get(0).getBillerCode()
        ));
        if (customerReferenceModel.getOptions().size() > 0) {
            customerReference1 = customerReferenceModel.getOptions().get(0).getValue().toString();
        }
    }
    
  
    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }
    

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;



    public void onValidateFromForm() {
        try {
//        	 if (chooseValue.equalsIgnoreCase("fromLaki")) {
//                 if (customerReference1 == null) {
//                     form.recordError(message.get("customerReference1-requiredIf-message"));
//                 }
//             } else if (chooseValue.equalsIgnoreCase("fromList")) {
//                 if (customerReference2 == null) {
//                     form.recordError(message.get("customerReference2-requiredIf-message"));
//                 }
//             }
        	if(mncLifePurchaseView.getTanggalLahir() == null){
        		form.recordError("Format Tanggal Salah");
        	}
        	
        	if(!form.getHasErrors()){
        		
        		setMncLifePurchaseViewData();
        	}
        	  
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
       }
    }

  
    private void setMncLifePurchaseViewData() {
        //-- populate placeholder fields
    	mncLifePurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	mncLifePurchaseView.setAccountType("10"); //10 jadi Tabungan //
        String billerCode = cacheManager.getBillers(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian).get(0).getBillerCode();
         //
        mncLifePurchaseView.setToAccountType("00");
        mncLifePurchaseView.setBillerCode(billerCode);
        mncLifePurchaseView.setIssuercode(com.dwidasa.engine.Constants.MNCLIFE.PROVIDER_CODE);
        mncLifePurchaseView.setKodeProduk("06D");
        mncLifePurchaseView.setProviderCode(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Provider);
        
        
        //Sementara
//        mncLifePurchaseView.setNomoPolis1("ML.POL.POS");
//        mncLifePurchaseView.setTglAwalPolis1(new Date());
//        mncLifePurchaseView.setTglAkhirPolis1(new Date());
        //lokal
        
        mncLifePurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        mncLifePurchaseView.setTransactionType(com.dwidasa.engine.Constants.MNCLIFE.Mnc_Live_Posting_Pembelian);
        mncLifePurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        mncLifePurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
       
        mncLifePurchaseView.setTransactionDate(new Date());
        mncLifePurchaseView.setAmount(new BigDecimal(45500));
        mncLifePurchaseView.setFee(BigDecimal.ZERO);
        mncLifePurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        mncLifePurchaseView.setTanggalLahir(mncLifePurchaseView.getTanggalLahir());
        if (chooseValue.equalsIgnoreCase("fromLaki")) {
        	mncLifePurchaseView.setJenisKelamin("L");
        	
        } else if (chooseValue.equalsIgnoreCase("fromPerempuan")) {
        	mncLifePurchaseView.setJenisKelamin("P");
        }
        
//        if(chooseTipeDokumen.equalsIgnoreCase("fromPolis")){
//        	mncLifePurchaseView.setTipeDokumen("P");
//        }else if(chooseTipeDokumen.equalsIgnoreCase("fromKtp")){
//        	mncLifePurchaseView.setTipeDokumen("K");
//        }
        //mncLifePurchaseView.setCustomerReference(mncLifePurchaseView.getNomorKtp());
        
      
    }
    
    

    public Object onSuccess() {
    	mncLivePurchaseConfirm.setVoucherPurchaseView(mncLifePurchaseView);
        return mncLivePurchaseConfirm;
    }
    
    
    

    public void pageReset() {
//        mncLifePurchaseView = null;
//        voucherPurchaseConfirm.setmncLifePurchaseView(null);
//        voucherPurchaseReceipt.setmncLifePurchaseView(null);
    }

    public boolean isMerchant() {    	
    	/*String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
    	String accountNumber = sessionManager.getLoggedCustomerView().getAccountNumber();
    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
    	String[] tokens = ip.getParameterValue().split(",");
    	boolean isMerch = false;
    	if (tokens.length > 0 ) {
    		for(int i = 0; i < tokens.length; i++) {
    			if (firstFour.equals(tokens[i]) || accountNumber.equals(tokens[i]))
    				isMerch = true;
    		}
    	}
    	return isMerch;*/
    	
    	// Pengecekan pulsa untuk mengeluarkan denom sesuai user merchant atau individual
    	// Menya
    	return sessionManager.isMerchant();
    }
}
