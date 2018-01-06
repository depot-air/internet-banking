package com.dwidasa.ib.pages.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.OptionModel;
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
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.dao.CustomerRegisterDao;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

@Import(library = "context:bprks/js/transfer/TransferInput.js")
public class TransferAntarBankInput {
	private Logger logger = Logger.getLogger(TransferAntarBankInput.class);
    @Property
    @Persist
    private TransferView transferView;

    @Inject
    private OtpManager otpManager;

    @Property
    private List<String> ownAccountModel;

    @Property
    private String customerReference1;

    @Property
    @Persist
    private String bank, branch, city, receiverName;
    
    @Inject
    private BillerDao billerDao;
    
    @Property
    private SelectModel transferListModel;

	@Property
    private TokenView tokenView;
	
    @InjectComponent
    private Form form;

    @Inject
    private TransferService transferService;

    @Inject
    private AccountService accountService;
    
    @Inject
    private SessionManager sessionManager;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private CustomerRegisterDao custRegisterDao;

    @Inject
    private BillerService billerService;

    @Inject
    private ParameterDao parameterDao;
    
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private int tokenType;

    @Property
    private SelectModel billerModel;
    
    @Inject
    private Request request;

    @Inject
    private Messages messages;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @InjectComponent
    private Zone bankZone, customReference;
    
    @Inject
    private Messages message;
    
    @Property
    @InjectPage
    private TransferAtmbConfirm transferAtmbConfirm;

    @Inject
    private RequestGlobals requestGlobals;
    
    @InjectPage
    private TransferOtherReceipt transferOtherReceipt;
    
    @Property
    private String jaringan;
    
    private boolean alto;

    void updateChallenge(String custRef) {
        tokenView.setChallenge(generateChallenge(custRef));
    }

    void onPrepare() {
    	if (tokenView == null ) tokenView = new TokenView();
//    	Random random = new Random();
//        String challenge = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
    	String accountNumber = sessionManager.getLoggedCustomerView().getAccountNumber();
    	String challenge = accountNumber.substring(accountNumber.length() - 8);
        tokenView.setChallenge(challenge);        
        sessionManager.setChallenge(challenge);
        logger.info("sessionManager.getChallenge()=" + sessionManager.getChallenge());
    }

    private void builtTransferListModel() {
        List<CustomerRegister> customerRegisters = transferService.getRegisters(
                sessionManager.getLoggedCustomerView().getId(),
                com.dwidasa.engine.Constants.ATMB.TT_POSTING, "");
        transferListModel = genericSelectModelFactory.create(customerRegisters);
        
    }
    
   
    private List<String> getOwnerAccount() {
        return sessionManager.getAccountNumber();
    }

    public String getDateFieldFormat() {
        return Constants.SHORT_FORMAT;
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(TransferOtherConfirm.class.toString()) ) {
    		transferView = null;
    	} else {
//    		if (transferView.getCustomerReference() != null && transferView.getInputType().equals("M")) {
//    			customerReference1 = transferView.getCustomerReference();
//    		}
    	}
    	sessionManager.setSessionLastPage(TransferAntarBankInput.class.toString());
        setTokenType();
        ownAccountModel = getOwnerAccount();
        builtTransferListModel();
        if (transferView == null) {
            transferView = new TransferView();
            
            if (transferListModel.getOptions().size() == 0) {
                form.recordError(messages.get("register-account-first"));
                return;
            } 
            OptionModel selected = transferListModel.getOptions().get(0);
            customerReference1 = (String)selected.getValue();
            String custRef = customerReference1.substring(0, 50);
            custRef = custRef.trim();
            String custName = customerReference1.substring(50);
            
        	receiverName = custName;
            CustomerRegister custRegister = custRegisterDao.get(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.ATMB.TT_POSTING, custRef);
            List<Biller> biller = billerDao.getAllData1WithTransactionTypeTransfer(com.dwidasa.engine.Constants.ATMB.TT_POSTING, com.dwidasa.engine.Constants.ALTO.TT_POSTING, custRegister.getData1());
            		//cacheManager.getBiller(com.dwidasa.engine.Constants.ATMB.TT_POSTING, custRegister.getData1());
            for(Biller b : biller){
            	transferView.setBillerName(b.getBillerName());
                transferView.setBillerCode(b.getBillerCode());
                transferView.setReceiverName(custRegister.getData3());
                transferView.setBranchName(custRegister.getData4());
                transferView.setBranchCity(custRegister.getData5());
            }
            
        }
        
    }

    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    	//APPL2
    	if (tokenType == Constants.TOKEN_RESPONSE_TOKEN) {
    		tokenType = Constants.TOKEN_CHALLENGE_TOKEN;    		
    	}
    }

    void onValidateFromForm() {
    	if (transferView.getAmount().doubleValue() <= 0) {
            form.recordError(message.get("inputAmount-cannotZero-message"));
        } else if (transferView.getAmount().doubleValue() < com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MIN_PER_TRX.doubleValue()) {
            form.recordError(message.get("inputAmount-minimum-message"));
        } else if (transferView.getAmount().doubleValue() > com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MAX_IN_ONE_DAY.doubleValue()) {
            form.recordError(message.get("inputAmount-maximum-message"));
        }  

    	try {
            if (!form.getHasErrors() ) {
                setTransferAtmbInputData();
                if (jaringan.equals("ALTO")) {
                	transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY);
                } else {
                	transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_INQUIRY);
                }
                tokenView.setChallenge(sessionManager.getChallenge());
                logger.info("tokenView.getChallenge()=" + tokenView.getChallenge());
                if (!isMerchant() || (isMerchant() && otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) ) {
                    transferService.inquiryATMB(transferView);
                    if (transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ATMB.TT_INQUIRY)) {
                    	transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_POSTING);
                    } else {
                    	transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_POSTING);
                    }
                    
                    transferService.confirm(transferView);
            	}
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

   
    
    private void setTransferAtmbInputData(){
    	
       
    	
    	transferView.setTransactionDate(new Date());
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setSenderName(sessionManager.getLoggedCustomerView().getName());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
      
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setToAccountType("00");   
      
      
	    transferView.setInputType("L");
	    	//if (transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY) ) {
	    String custRef = customerReference1.substring(0, 50);
	    transferView.setCustomerReference(custRef.trim());
	    	//} else {
	    		//String custRefAlto = customerReference1.substring(0, 50);
	    		//transferView.setCustomerReference(custRefAlto.trim());
	    	//}
	    
	    
	    if(jaringan.equals("ALTO")){
	    
	    	transferView.setProviderCode(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY);
	        transferView.setProviderCode(com.dwidasa.engine.Constants.ALTO.PROVIDER_CODE);
	    	//transferView.setTerminalAddress(StringUtils.rightPad("BPRKS", 32, " ") + " JBR IDN");
     
	    }else{
	    	
	    	transferView.setProviderCode(com.dwidasa.engine.Constants.ATMB.TT_INQUIRY);
	        transferView.setProviderCode(com.dwidasa.engine.Constants.ATMB.PROVIDER_CODE);
	    	
	    	String ipAdress = StringUtils.rightPad(requestGlobals.getHTTPServletRequest().getRemoteAddr(), 32, " ");
	    	transferView.setTerminalAddress(ipAdress);
		
	    }
	    
   	}
    

    Object onSuccess() {
    	setTransferAtmbInputData();
    	
    	transferAtmbConfirm.setTransferConfirmView(transferView);
        return transferAtmbConfirm;
    }

    void pageReset() {
//        transferView = null;
//        transferConfirm.setTransferView(null);
//        transferReceipt.setTransferView(null);
    }
    

    void onValueChangedFromTransferListSelect(String customerReference1) {
    	System.out.println("customerReference1=" + customerReference1);

		String custRef = customerReference1.substring(0, 50);
        custRef = custRef.trim();
        String custName = customerReference1.substring(50);
        
    	receiverName = custName;
    	System.out.println("receiverName=" + receiverName);
        CustomerRegister custRegister = custRegisterDao.get(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.ATMB.TT_POSTING, custRef);
        List<Biller> biller = billerDao.getAllData1WithTransactionTypeTransfer(com.dwidasa.engine.Constants.ATMB.TT_POSTING, com.dwidasa.engine.Constants.ALTO.TT_POSTING, custRegister.getData1());
		//cacheManager.getBiller(com.dwidasa.engine.Constants.ATMB.TT_POSTING, custRegister.getData1());
        for(Biller b : biller){
        	transferView.setBillerName(b.getBillerName());
        	transferView.setBillerCode(b.getBillerCode());
        	transferView.setReceiverName(custRegister.getData3());
        	transferView.setBranchName(custRegister.getData4());
        	transferView.setBranchCity(custRegister.getData5());
        }
        ajaxResponseRenderer.addRender(bankZone);
        
    }
    
    
    void onValueChangedFromselect(String jaringan) {
    	System.out.println("Jaringan =" + jaringan);

    	if(jaringan.equals("ALTO")){
    		
    		setAlto(true);
    		
    	}else{
    		
    		setAlto(false);
    		
    	}
    	ajaxResponseRenderer.addRender(customReference);//
        
    }

    public void setAlto(boolean alto) {
		this.alto = alto;
	}
    
    public boolean isAlto() {
		return alto;
	}
    
	public String generateChallenge(String custRef) {
		String lastEight = custRef.substring(custRef.length() - 8);
    	return lastEight;
	}

    public boolean isMerchant() {    	
    	boolean isMerch = (sessionManager.getTokenType() == Constants.TOKEN_RESPONSE_TOKEN);
		return isMerch;
    }
    
    
}
