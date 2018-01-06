package com.dwidasa.ib.pages.transfer;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

public class TransferAtmbInput {
	private Logger logger = Logger.getLogger(TransferAtmbInput.class);
    @Property//(write = false)
    @Persist
    private TransferView transferView;

	@Property
    private TokenView tokenView;
	
    @Property
    private String transactionType;
    
    @Property
    private String chooseValue;

    @Property
    private boolean saveBoxValue;
         
    @Property
    private String customerReference1;

    @Property
    private String customerReference2;
    
    @Property
    @InjectPage
    private TransferAtmbConfirm transferAtmbConfirm;
    
    @Property
    private SelectModel fromListModel;
    
    @Inject
    private Messages message;

    @Inject
    private TransferService transferService;

    @Inject
    private CacheManager cacheManager;
    
    @Inject
    private SessionManager sessionManager;
   
    @InjectPage
    private TransferAtmbReceipt transferAtmbReceipt;
    
    @InjectComponent
    private Form form;
       
    public String getDateFieldFormat() {
    	return Constants.SHORT_FORMAT;
    }
    
    @Inject
    private RequestGlobals requestGlobals;
    
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    
    @Property
    private int tokenType;
    
    @Inject
    private OtpManager otpManager;
        
    private void buildFromListModel() {
    	List<CustomerRegister> customerRegisters = null; 
    	if (sessionManager.getSessionLastPage().equals(TransferAtmbOption.class.toString()) ) {
    		transferView.setTransactionType(com.dwidasa.engine.Constants.ATMB.TT_INQUIRY) ;
    		customerRegisters = transferService.getRegisters(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.ATMB.TT_POSTING, transferView.getBillerCode());
        } else if (sessionManager.getSessionLastPage().equals(TransferAltoOption.class.toString()) ) {
        	transferView.setTransactionType(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY) ;
//        	customerRegisters = transferService.getRegisters(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.ALTO.TT_POSTING, transferView.getBillerCode());
        	customerRegisters = transferService.getRegisters(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.ATMB.TT_POSTING, transferView.getBillerCode());
        }
        fromListModel = genericSelectModelFactory.create(customerRegisters);
    }
    
    public void onPrepare() {
    	tokenView = new TokenView();
//    	Random random = new Random();
//        String challenge = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
    	String accountNumber = sessionManager.getLoggedCustomerView().getAccountNumber();
    	String challenge = accountNumber.substring(accountNumber.length() - 8);
        tokenView.setChallenge(challenge);        
        sessionManager.setChallenge(challenge);
        logger.info("sessionManager.getChallenge()=" + sessionManager.getChallenge());
    }
     
    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    	//APPL2
    	if (tokenType == Constants.TOKEN_RESPONSE_TOKEN) {
    		tokenType = Constants.TOKEN_CHALLENGE_TOKEN;
    	}
    }

    void updateChallenge(String custRef) {
    	
    }
    
	public String generateChallenge(String custRef) {
		String lastEight = custRef.substring(custRef.length() - 8);
    	return lastEight;
	}

    void setupRender() {
    	if (!(sessionManager.getSessionLastPage().equals(TransferAtmbConfirm.class.toString()) || sessionManager.getSessionLastPage().equals(TransferAtmbInput.class.toString()) || sessionManager.getSessionLastPage().equals(TransferAtmbOption.class.toString())  || sessionManager.getSessionLastPage().equals(TransferAltoOption.class.toString()) )) {
    		transferView = null;
    	} else {
    		if (transferView.getCustomerReference() != null && transferView.getInputType().equals("M")) {
    			customerReference1 = transferView.getCustomerReference();
    		}
    	}
    	chooseValue = "fromId";
    	setTokenType();
    	buildFromListModel();
    	sessionManager.setSessionLastPage(TransferAtmbInput.class.toString());
    }
        
    private void setTransferAtmbInputData(){
    	transferView.setTransactionDate(new Date());
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setSenderName(sessionManager.getLoggedCustomerView().getName());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        
        
        //transferView.setBillerCode(cacheManager.getBillers(com.dwidasa.engine.Constants.ATMB.TT_POSTING).get(0).getBillerCode());
//        transferView.setProviderCode(com.dwidasa.engine.Constants.ATMB.PROVIDER_CODE);	sudah diset di Option / pilih bank
        
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setSave(saveBoxValue);
        transferView.setToAccountType("00");   
            	
	    if (chooseValue.equalsIgnoreCase("fromList")) {
	    	transferView.setInputType("L");
	    	if (transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY) ) {
	            String custRef = customerReference2.substring(0, 50);
	            transferView.setCustomerReference(custRef.trim());
	    	} else {
	    		String custRefAlto = customerReference2.substring(0, 50);
	    		transferView.setCustomerReference(custRefAlto.trim());
	    	}
	    } else if (chooseValue.equalsIgnoreCase("fromId")) {
	    	transferView.setInputType("M");
	    	transferView.setCustomerReference(customerReference1);
	    }
	    
	    if(transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY)){
	    
	    	//transferView.setTerminalAddress(StringUtils.rightPad("BPRKS", 32, " ") + " JBR IDN");
     
	    }else{
	    	
	    	String ipAdress = StringUtils.rightPad(requestGlobals.getHTTPServletRequest().getRemoteAddr(), 32, " ");
	    	transferView.setTerminalAddress(ipAdress);
		
	    }
	    
   	}

    void onValidateFromForm() {
    	if (transferView.getAmount().doubleValue() <= 0) {
            form.recordError(message.get("inputAmount-cannotZero-message"));
        } else if (transferView.getAmount().doubleValue() < com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MIN_PER_TRX.doubleValue()) {
            form.recordError(message.get("inputAmount-minimum-message"));
        } else if (transferView.getAmount().doubleValue() > com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MAX_PER_TRX.doubleValue()) {
            form.recordError(message.get("inputAmount-maximum-message"));
        }  
    	
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
                setTransferAtmbInputData();
                if (isAlto()) {
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
    
    @DiscardAfter
    public Object onSuccess() {
    	setTransferAtmbInputData();   	        
    	//transferView.setResponseCode(Constants.SUCCESS_CODE);
        //transferView.setReferenceNumber(ReferenceGenerator.generate());
        transferAtmbConfirm.setTransferConfirmView(transferView);
        return transferAtmbConfirm;

    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
    	if (transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ATMB.TT_INQUIRY) ) {
    		return TransferAtmbOption.class;
    	}
    	return TransferAltoOption.class;
    }

    public void setTransferInputView(TransferView transferView) {
        this.transferView = transferView;
        
    }
    
    public Object onActivate() {
        if (transferView == null) {
            return TransferAltoOption.class;
        }

        return null;
    }

    Object onParseClientFromCustomerReference1(String input)
    {
        if(chooseValue.equalsIgnoreCase("fromList"))
            return "";
        return input;
    }

    public boolean isMerchant() {    	
    	boolean isMerch = (sessionManager.getTokenType() == Constants.TOKEN_RESPONSE_TOKEN);
		return isMerch;
    }
    public boolean isAlto() {    	
    	if (transferView.getTransactionType() == null) {
    		if (sessionManager.getSessionLastPage().equals(TransferAltoOption.class.toString()) ) {
    			return true;
    		}
    	} else if (transferView.getTransactionType().equals(com.dwidasa.engine.Constants.ALTO.TT_INQUIRY) ) {
    		return true;
    	}
    	return false;
    }
    
}


