package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.KotaAsalListSelect;
import com.dwidasa.ib.services.KotaTujuanListSelect;
import com.dwidasa.ib.services.LocationListSelect;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/08/11
 * Time: 11:13
 */

@Import(stylesheet = {"context:bprks/css/newKramatDjati/select2.css","context:bprks/css/newKramatDjati/style_train.css",
"context:bprks/css/newKramatDjati/ui-lightness/jquery-ui-1.10.4.custom.min.css"}, 
library={
	"context:bprks/js/newKramatDjati/aero/jquery.js", "context:bprks/js/newKramatDjati/aero/jquery-ui.js",
	"context:bprks/js/newKramatDjati/aero/select2.min.js", 			
	"context:bprks/js/purchase/PlnPurchaseInput.js"})

public class TiketKeretaDjatiPurchaseInput {
	private Logger logger = Logger.getLogger(TiketKeretaDjatiPurchaseInput.class);
    @Property
    private String chooseValue;

    @Property
    @Persist
    private TiketKeretaDjatiPurchaseView djatiPurchaseView;
    
    @Persist
    @Property
    private List<TiketKeretaDjatiPurchaseView> djatiPurchaseViews;

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
    private TiketKeretaDjatiPurchaseJurusan tiketKeretaDjatiPurchaseJurusan;

    @InjectPage
    private PlnPurchaseReceipt plnPurchaseReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    
    @Property
    private SelectModel selectKotaAsal;
    
    
    @Property
    private SelectModel selectKotaTujuan;
    
    
    @InjectComponent
    private Zone billerProviderZone;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    
    @Inject
    private CustomerAccountDao customerAccountDao;
   
    
    public void onPrepare() {
       
    	System.out.println("Account Number "+djatiPurchaseView.getAccountNumber());
    	try {
        	djatiPurchaseView.getListKotaAsal().clear();
        	setKotaAsalKramatDjati(djatiPurchaseView.getAccountNumber());
        	
        	djatiPurchaseView.getListKotaTujuan().clear();
    		setKotaTujuanKramatDjati(selectKotaAsal.getOptions().get(0).getValue().toString(), djatiPurchaseView.getAccountNumber());
		} catch (Exception e) {
			
			//form.recordError(e.getMessage());
		}
        
    }
    
    
    public void buildKotaAsalModel(List<TiketKeretaDjatiPurchaseView> dataKotaAsal) {
    	
    	try {
    		selectKotaAsal = new KotaAsalListSelect(dataKotaAsal);
		} catch (Exception e) {
			djatiPurchaseViews.clear();
			selectKotaAsal = new KotaAsalListSelect(djatiPurchaseViews);
		}
    	
    	
    }
    
    
    public void buildKotaTujuanModel(List<TiketKeretaDjatiPurchaseView> dataKotaTujuan) {
    	try {
    		selectKotaTujuan = new KotaTujuanListSelect(dataKotaTujuan);
		} catch (Exception e) {
			djatiPurchaseViews.clear();
			selectKotaTujuan = new KotaTujuanListSelect(djatiPurchaseViews);
		}
    	
    }
    
    void onValueChangedFromdari(String kotaAsal) {
    	
    		System.out.println("Kota Asal "+kotaAsal);
    		djatiPurchaseView.getListKotaTujuan().clear();
    		setKotaTujuanKramatDjati(kotaAsal, djatiPurchaseView.getAccountNumber());
    		ajaxResponseRenderer.addRender(billerProviderZone);
    }
    
    
    
    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}    	
    	return null;
    }

    public void setupRender() {
    	
    	sessionManager.setSessionLastPage(TiketKeretaDjatiPurchaseInput.class.toString());
        chooseValue = "fromId";
        tiketKeretaDjatiPurchaseJurusan.setKeretaDjatiPurchaseViews(null);
        setTokenType();
        if (djatiPurchaseView == null) {
        	djatiPurchaseView = new TiketKeretaDjatiPurchaseView();
        	djatiPurchaseView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
        }
        
        
             
    }

    void onValidateFromForm() {
     
    	if (djatiPurchaseView.getTglKeberangkatan() == null) {
            form.recordError(messages.get("tglKeberangkatan-requiredIf-message"));
        }else 
            if (djatiPurchaseView.getDari() == null) {
                form.recordError(messages.get("dari-requiredIf-message"));
            }else if (djatiPurchaseView.getTujuan() == null) {
                form.recordError(messages.get("tujuan-requiredIf-message"));
            }
       
        try {
            if (!form.getHasErrors()) {
            	setTiketKeretaPurchaseViewData();
            	djatiPurchaseView.getDjatiPurchaseViews().clear();
            	djatiPurchaseView.getListLocation().clear();
            	djatiPurchaseView.getListKursi().clear();
            	djatiPurchaseView.setTransactionType(Constants.TIKET_KERETA_DJATI.INQ_KERETA_DJATI);
                purchaseService.inquiry(djatiPurchaseView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("e.getMessage()=" + e.getMessage());
        }
    }

    public Object onSuccess() {
    	tiketKeretaDjatiPurchaseJurusan.setAccountNumber(djatiPurchaseView.getAccountNumber());
    	tiketKeretaDjatiPurchaseJurusan.setCardNumber(sessionManager.getCardNumber(djatiPurchaseView.getAccountNumber()));
    	tiketKeretaDjatiPurchaseJurusan.setTglKebarangkatan(djatiPurchaseView.getTglKeberangkatan());
    	tiketKeretaDjatiPurchaseJurusan.setDari(djatiPurchaseView.getDari());
    	tiketKeretaDjatiPurchaseJurusan.setTujuan(djatiPurchaseView.getTujuan());
    	tiketKeretaDjatiPurchaseJurusan.setTiketKeretaDjatiPurchaseView(djatiPurchaseView);
    	tiketKeretaDjatiPurchaseJurusan.setKeretaDjatiPurchaseViews(djatiPurchaseView.getDjatiPurchaseViews());
    	tiketKeretaDjatiPurchaseJurusan.setNoKursi(djatiPurchaseView.getListKursi());
    	tiketKeretaDjatiPurchaseJurusan.setLocation(djatiPurchaseView.getListLocation());
    	tiketKeretaDjatiPurchaseJurusan.setScheduleId(djatiPurchaseView.getScheduleCodeId());
    	tiketKeretaDjatiPurchaseJurusan.setDepartBranch(djatiPurchaseView.getDepartBranch());
    	tiketKeretaDjatiPurchaseJurusan.setDestinationBranch(djatiPurchaseView.getDestinationBranch());
    	tiketKeretaDjatiPurchaseJurusan.setTransactionId(djatiPurchaseView.getTerminalId());
    	tiketKeretaDjatiPurchaseJurusan.setVehicleCode(djatiPurchaseView.getVechileTypeCode());
        return tiketKeretaDjatiPurchaseJurusan;
        
    }


    public void setTiketKeretaPurchaseViewData() {
    	
    	djatiPurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	djatiPurchaseView.setAccountType(sessionManager.getAccountType(djatiPurchaseView.getAccountNumber()));
    	djatiPurchaseView.setCardNumber(sessionManager.getCardNumber(djatiPurchaseView.getAccountNumber()));
        String billerCode = cacheManager.getBillers(Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI).get(0).getBillerCode();
        String productCode = cacheManager.getBillerProducts(Constants.TIKET_KERETA_DJATI.POS_KERETA_DJATI, billerCode).get(0).getProductCode();
        djatiPurchaseView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
        djatiPurchaseView.setBillerCode(billerCode);
        djatiPurchaseView.setProductCode(productCode);
        djatiPurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        djatiPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        djatiPurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        djatiPurchaseView.setProviderName("");
        djatiPurchaseView.setTransactionDate(new Date());
        djatiPurchaseView.setJenisLayanan("KramatDjati");
        
    }
    
    
    public void setKotaAsalKramatDjati(String AccountNumber){
    	
    	System.out.println("Data Account "+djatiPurchaseView.getAccountNumber());
    	
    	djatiPurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	djatiPurchaseView.setAccountType(sessionManager.getAccountType(AccountNumber));
    	djatiPurchaseView.setCardNumber(sessionManager.getCardNumber(AccountNumber));
        djatiPurchaseView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
        djatiPurchaseView.setBillerCode("003");
        djatiPurchaseView.setProductCode("0001");
        djatiPurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        djatiPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        djatiPurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        djatiPurchaseView.setProviderName("");
        djatiPurchaseView.setTransactionDate(new Date());
        try {
        	 djatiPurchaseView.setTransactionType(Constants.TIKET_KERETA_DJATI.INQ_KOTA_ASAL_KRAMAT_DJATI);
             purchaseService.inquiry(djatiPurchaseView);
             
             List<TiketKeretaDjatiPurchaseView> liDjatiPurchaseViews = new ArrayList<TiketKeretaDjatiPurchaseView>();
             liDjatiPurchaseViews.clear();
             for(int i=0; i<djatiPurchaseView.getListKotaAsal().size(); i++){
            	 String kota = djatiPurchaseView.getListKotaAsal().get(i);
            	 
            	 TiketKeretaDjatiPurchaseView temp = new TiketKeretaDjatiPurchaseView();
                 temp.setDataKotaAsal(kota);
                 liDjatiPurchaseViews.add(temp);
             }
             
             if(liDjatiPurchaseViews.size() > 0){
            	 buildKotaAsalModel(liDjatiPurchaseViews);
             }else{
            	 selectKotaAsal = null;
             }
             
             
             
		} catch (Exception e) {
			System.out.println("Kurang Apa "+e.getMessage());
			form.recordError(e.getMessage());
		}
       
        
    }
    
    
    public void setKotaTujuanKramatDjati(String kotaAsal, String AccountNumber){
    	
    	//djatiPurchaseView = new TiketKeretaDjatiPurchaseView();
    	djatiPurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	djatiPurchaseView.setAccountType(sessionManager.getAccountType(AccountNumber));
    	djatiPurchaseView.setCardNumber(sessionManager.getCardNumber(AccountNumber));
        djatiPurchaseView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
        djatiPurchaseView.setBillerCode("003");
        djatiPurchaseView.setProductCode("0001");
        djatiPurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        djatiPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        djatiPurchaseView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        djatiPurchaseView.setProviderName("");
        djatiPurchaseView.setDataKotaAsal(kotaAsal);
        djatiPurchaseView.setTransactionDate(new Date());
        try {
        	 djatiPurchaseView.setTransactionType(Constants.TIKET_KERETA_DJATI.INQ_KOTA_TUJUAN_KRAMAT_DJATI);
             purchaseService.inquiry(djatiPurchaseView);
             
             List<TiketKeretaDjatiPurchaseView> liDjatiPurchaseViews = new ArrayList<TiketKeretaDjatiPurchaseView>();
             liDjatiPurchaseViews.clear();
             for(int i=0; i<djatiPurchaseView.getListKotaTujuan().size(); i++){
            	 String kota = djatiPurchaseView.getListKotaTujuan().get(i);
            	 TiketKeretaDjatiPurchaseView temp = new TiketKeretaDjatiPurchaseView();
                 temp.setDestinationKotaTujuan(kota);
                 liDjatiPurchaseViews.add(temp);
             }
             
             
             if(liDjatiPurchaseViews.size() > 0){
            	 buildKotaTujuanModel(liDjatiPurchaseViews);
             }else{
            	 selectKotaTujuan = null;
             }
            	 
		
             
		} catch (Exception e) {
			System.out.println("Kurang Apa "+e.getMessage());
			form.recordError(e.getMessage());
		}
       
        
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
    
    
    public String getDateFieldFormat() {
		return com.dwidasa.ib.Constants.SHORT_FORMAT;
	}

    
}
