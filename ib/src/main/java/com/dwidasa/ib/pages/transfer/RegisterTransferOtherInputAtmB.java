package com.dwidasa.ib.pages.transfer;
// untuk Transfer via ATMB ya Om..

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import com.dwidasa.ib.services.TransferListSelect;

/**
 * Created by IntelliJ IDEA.
 * User: HMS
 * Date: 7/12/11
 * Time: 00:27 am
 */
public class RegisterTransferOtherInputAtmB {
    @InjectPage
    private RegisterTransferOtherConfirmAtmB registerTransferOtherConfirmAtmB;

    @InjectPage
    private RegisterTransferOtherReceiptAtmB registerTransferOtherReceiptAtmB;

    @Property
    private SelectModel billerModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @Property
    private int tokenType;

    @Property
    @Persist
    private TransferView transferView;
    
    @Property
    @Persist
    private Biller biller;

    @Inject
    private TransferService transferService;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Persist
    @Property
    private TokenView tokenView;

    @Inject
    private OtpManager otpManager;
    
    @Inject
    private BillerDao billerDao;
    
    private String billerCode; //
    private long idBiler;

    public void buildBillerModel() {
    	
    	List<Biller> billers = billerDao.getAllWithTransactionTypeTransfer
        		(com.dwidasa.engine.Constants.ATMB.TT_POSTING);
    	
    	//List<Biller> billers = genericSelectModelFactory.create(billerDao.getAllWithTransactionTypeTransfer
        		//(com.dwidasa.engine.Constants.ATMB.TT_POSTING));
    	
    	billerModel = new TransferListSelect(billers);
                      
        System.out.println("ini test debug Other Input Biller Model Query");     
    }

    public void setupRender() {
        if (transferView == null) {
            transferView = new TransferView();
        }
        System.out.println("ini test debug Other Input token nulll");      
        if (tokenView == null) {
            tokenView = new TokenView();
        }
              
       // setTokenType();
        buildBillerModel();
        System.out.println("ini test debug Other Input set token");        
           
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void onValidateFromForm() {
    	try {
    		
    		long idB = biller == null ? null : biller.getId();
        	biller = billerDao.get(idB);
        	
    		long idBiller = biller.getId();
    		setIdBiler(idBiller);
    		Biller bil = billerDao.getTransactionType(idBiller);
    		//List<Biller>billers = billerDao.getAllTransactionTypeTransfer(idBiller, bil.getTransactionTypeId(), "4b");
    		Biller billerCekAlto = cacheManager.getBiller("4b", bil.getBillerCode()); 
    		System.out.println("Biller "+billerCekAlto);
    		//    		
    		if(billerCekAlto == null){
    		
    		System.out.println("ATMB");    
        	Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.ATMB.TT_POSTING, bil.getBillerCode());
            
            transferView.setBillerCode(biller.getBillerCode());
            transferView.setBillerName(biller.getBillerName());
            setBillerCode(biller.getBillerCode());
            transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_INQUIRY);
            transferView.setProviderCode(com.dwidasa.engine.Constants.ATMB.PROVIDER_CODE);
            
                
            
    		}else{
    			
    			System.out.println("ALTO");
    			Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.ALTO.TT_POSTING, bil.getBillerCode());
            	transferView.setBillerCode(biller.getBillerCode());
                transferView.setBillerName(biller.getBillerName());
                setBillerCode(biller.getBillerCode());
                transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY);
                transferView.setProviderCode(com.dwidasa.engine.Constants.ALTO.PROVIDER_CODE);
                
    			
    		}
//    		
    		transferView.setCurrencyCode(Constants.CURRENCY_CODE);
   		 	transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
            transferView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
            transferView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
            transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
            transferView.setToAccountType("00");
            transferView.setMerchantType(sessionManager.getDefaultMerchantType());
            transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
            transferView.setTransactionDate(new Date());
            transferView.setValueDate(new Date());
            transferView.setInputType("M");
            transferView.setSenderName(sessionManager.getLoggedCustomerView().getName());
            transferView.setAmount(new BigDecimal("15000"));	//inquiry butuh amount, tdk kedebet
            transferView.setFee(BigDecimal.ZERO);
            transferView.setFeeIndicator("C");
            
             
	    	//transferView.setCustomerReference(customerReference1);
       	
            transferView = transferService.inquiryATMB(transferView);
    		     	
//        	logger.info("transferView=" + transferView.getCustomerReference());

        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    public Object onSuccess() {
    	
    	System.out.println("HOHOHOHOHOHO 2 "+getIdBiler());
    	Biller bil = billerDao.getTransactionType(getIdBiler()); 
		List<Biller>billers = billerDao.getAllTransactionTypeTransfer(getIdBiler(), bil.getTransactionTypeId(), "4b");
    	
    	if(billers.size() == 0){
    		transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_POSTING);
    	}else{
    		transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_POSTING);
    	}
    	
    	registerTransferOtherConfirmAtmB.setIdBiller(getIdBiler());
    	registerTransferOtherConfirmAtmB.setBillerCode(getBillerCode());
        registerTransferOtherConfirmAtmB.setTransferView(transferView);
        return registerTransferOtherConfirmAtmB;
    }
    
    public void setBillerCode(String billerCode) {
		this.billerCode = billerCode;
	}
    
    public String getBillerCode() {
		return billerCode;
	}
    
    
    public void setIdBiler(long idBiler) {
		this.idBiler = idBiler;
	}
    
    public long getIdBiler() {
		return idBiler;
	}

    public void pageReset(){
        transferView = null;
        registerTransferOtherConfirmAtmB.setTransferView(null);
        registerTransferOtherReceiptAtmB.setTransferView(null);
    }
    
    
    @Property
    private final ValueEncoder<Biller> encoder = new ValueEncoder<Biller> () {

		public String toClient(Biller value) {
			return String.valueOf(value.getId());
		}

		public Biller toValue(String clientValue) {
			return billerDao.get(Long.parseLong(clientValue));
		}
    	
    };
    
}


