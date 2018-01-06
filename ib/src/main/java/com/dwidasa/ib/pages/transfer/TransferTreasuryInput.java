package com.dwidasa.ib.pages.transfer;

import java.util.Date;
import java.util.List;

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
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.dwidasa.engine.BusinessException;
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
public class TransferTreasuryInput {
	private Logger logger = Logger.getLogger(TransferTreasuryInput.class);
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
    private Zone bankZone, branchZone, cityZone, receiverNameZone;
    
    @InjectPage
    private TransferOtherConfirm transferOtherConfirm;

    @InjectPage
    private TransferOtherReceipt transferOtherReceipt;

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
                com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE, "");
        transferListModel = genericSelectModelFactory.create(customerRegisters);
        if (customerRegisters.size() > 0) {
        	if (tokenView == null ) tokenView = new TokenView();
        	tokenView.setChallenge(customerRegisters.get(0).getCustomerReference().trim());        	
        }
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
    	sessionManager.setSessionLastPage(TransferTreasuryInput.class.toString());
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
            CustomerRegister custRegister = custRegisterDao.get(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE, custRef);
            Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE, custRegister.getData1());
            transferView.setBillerName(biller.getBillerName());
            transferView.setBillerCode(biller.getBillerCode());
            transferView.setBillerName(biller.getBillerName());
            transferView.setReceiverName(custRegister.getData3());
            transferView.setBranchName(custRegister.getData4());
            transferView.setBranchCity(custRegister.getData5());
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
            form.recordError(messages.get("inputAmount-cannotZero-message"));
        } else if (transferView.getAmount().doubleValue() < 50000D) {
            form.recordError(messages.get("inputAmount-minimum-message"));
        }  

        if (!form.getHasErrors()) {
            setTransferViewData();
            try {
            	//cek informasi saldo
            	AccountView accountView = new AccountView();
                accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
                accountView.setCurrencyCode(Constants.CURRENCY_CODE);
                accountView.setMerchantType(sessionManager.getDefaultMerchantType());
                accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
                accountView.setAccountNumber(transferView.getAccountNumber());
                accountView = accountService.getAccountBalance(accountView);

                if (transferView.getAmount().compareTo(accountView.getAvailableBalance()) > 0) {
                    form.recordError(messages.get("inputAmount-notEnough-message"));
                    return;
                }
                tokenView.setChallenge(sessionManager.getChallenge());
                logger.info("tokenView.getToken()=" + tokenView.getToken());
            	logger.info("tokenView.getChallenge()=" + tokenView.getChallenge());
            	
//            	Date date = new Date();   // given date
//            	Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
//            	calendar.setTime(date);   // assigns calendar to given date 
//            	int hour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
//            	if (hour >= 11) {
//            		transferView.setTransferType(2);
//            		Calendar cal = Calendar.getInstance();
//            		cal.add(Calendar.DAY_OF_YEAR, 1);
//            		Date tomorrow = cal.getTime();
//            		transferView.setValueDate(tomorrow);
//            	}
                if (!isMerchant() || (isMerchant() && otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) ) {
                	transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_TREASURY_INQ_CODE);
                    transferView = (TransferView) transferService.inquiry(transferView);
                    transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE);
                    transferService.confirm(transferView);	
                }
            } catch (BusinessException e) {
                transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE);
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    private void setTransferViewData() {
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setTransactionDate(new Date());
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
//        transferView.setBillerCode(cacheManager.getBillers(
//                com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerCode());
        Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE, transferView.getBillerCode());
        transferView.setBillerName(biller.getBillerName());
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setToAccountType("00");
        transferView.setInputType("M");

        transferView.setTransferType(Constants.TRANSFER_NOW);

		String custRef = customerReference1.substring(0, 50);
        custRef = custRef.trim();
//        transferView.setCustomerReference(custRef);
        Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TREASURY_TEMP_ACCOUNT);
        transferView.setCustomerReference(ip.getParameterValue());
        transferView.setCustRefAtmb(custRef);
        transferView.setReceiverName(receiverName);
            
    }        

    Object onSuccess() {
        transferOtherConfirm.setTransferView(transferView);
        return transferOtherConfirm;
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
        CustomerRegister custRegister = custRegisterDao.get(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE, custRef);
        Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE, custRegister.getData1());
        transferView.setBillerCode(biller.getBillerCode());
        transferView.setBillerName(biller.getBillerName());
        transferView.setReceiverName(custRegister.getData3());
        transferView.setBranchName(custRegister.getData4());
        transferView.setBranchCity(custRegister.getData5());
        ajaxResponseRenderer.addRender(bankZone).addRender(branchZone).addRender(cityZone).addRender(receiverNameZone);
//        bank = biller.getBillerName();
//        branch =custRegister.getData4();
//        city =custRegister.getData5();
//        receiverName = custRegister.getData3();
//        ajaxResponseRenderer.addRender(transferListZone);
        
//        System.out.println("namaBank=" + biller.getBillerName());
//        System.out.println("custRegister.getData3()=" + custRegister.getData3());
//        System.out.println("custRegister.getData4()=" + custRegister.getData4());
//        System.out.println("custRegister.getData5()=" + custRegister.getData5());
        
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
