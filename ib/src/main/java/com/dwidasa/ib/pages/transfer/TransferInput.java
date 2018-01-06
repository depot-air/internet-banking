package com.dwidasa.ib.pages.transfer;

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
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.DayOfWeek;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

@Import(library = "context:bprks/js/transfer/TransferInput.js")
public class TransferInput {
	private static Logger logger = Logger.getLogger( TransferInput.class );
    @Property
    @Persist
    private TransferView transferView;

	@Property
    private TokenView tokenView;
	
    @Inject
    private OtpManager otpManager;

    @Property
    private DayOfWeek day;

    @Property
    private List<String> ownAccountModel;

    @Property
    private String customerReference1, customerReference2, customerReference3;

    @Property
    private int transferType1;

    @Property
    private int transferType3;

    @Property
    private Date valueDate;

    @Property
    private Date endDate;

    @Property
    private String transferValue;

    @Property
    private String transferKindValue;

    @Property
    private boolean saveAccount;

    @Property
    private String periodicTransfer;

    @Property
    private String periodicTransferGroup;

    @Property
    private SelectModel transferListModel;

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
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private int tokenType;

    @Inject
    private Request request;

    @Inject
    private Messages messages;
    
    @InjectPage
    private TransferConfirm transferConfirm;

    @InjectPage
    private TransferReceipt transferReceipt;

	public String generateChallenge(String custRef) {
		String lastEight = custRef.substring(custRef.length() - 8);
    	return lastEight;
	}
    private void builtTransferListModel() {
        List<CustomerRegister> customerRegisters = transferService.getRegisters(
                sessionManager.getLoggedCustomerView().getId(),
                com.dwidasa.engine.Constants.TRANSFER_CODE,
                cacheManager.getBillers(com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerCode());
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

    void onPrepare() {
    	tokenView = new TokenView();
//    	Random random = new Random();
//        String challenge = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
    	String accountNumber = sessionManager.getLoggedCustomerView().getAccountNumber();
    	String challenge = accountNumber.substring(accountNumber.length() - 8);
        tokenView.setChallenge(challenge);        
        sessionManager.setChallenge(challenge);
        logger.info("sessionManager.getChallenge()="+ sessionManager.getChallenge());
    }
    
    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(TransferConfirm.class.toString()) ) {
    		transferView = null;
    	} else {
    		if (transferView.getCustomerReference() != null && transferView.getInputType().equals("M")) {
    			customerReference1 = transferView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(TransferInput.class.toString());
        transferValue = "ownAccount";
        transferKindValue = "transferNow";
        periodicTransferGroup = "every";
        setTokenType();
        if (transferView == null) {
            transferView = new TransferView();
            transferView.setBillerCode(com.dwidasa.engine.Constants.BPRKS.CODE);
            transferView.setBillerName(com.dwidasa.engine.Constants.BPRKS.NAME);
        }
        builtTransferListModel();
        ownAccountModel = getOwnerAccount();
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
        } else if (transferView.getAmount().doubleValue() < com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MIN_PER_TRX.doubleValue()) {
            form.recordError(messages.get("inputAmount-minimum-message"));
        } else if (transferView.getAmount().doubleValue() > com.dwidasa.engine.Constants.TRANSFER_AMOUNT.MAX_PER_TRX.doubleValue()) {
            form.recordError(messages.get("inputAmount-maximum-message"));
        }  
        if (transferValue.equalsIgnoreCase("inputManual")) {
            if (customerReference3 == null) {
                form.recordError(messages.get("inputAccountField-requiredIf-message"));
            }
        }

        if (transferValue.equalsIgnoreCase("fromAccountList") && customerReference2 == null) {
            form.recordError(messages.get("transferListSelect-requiredIf-message"));
        }

        if (transferValue.equalsIgnoreCase("ownAccount")) {
            if (customerReference1.equalsIgnoreCase(transferView.getAccountNumber())) {
                form.recordError(messages.get("ownAccountSelect-acrossField-message"));
            }
        }

        if (transferKindValue.equalsIgnoreCase("transferAt")) {
            if (transferView.getValueDate() == null) {
                form.recordError(messages.get("transferDate-requiredIf-message"));
            }
        }
        else if (transferKindValue.equalsIgnoreCase("periodicTransfer")) {
            if (periodicTransferGroup.equals("every")) {
                if (transferType1 == 0) {
                    form.recordError(messages.get("everyField-regexp-message"));
                }
            }
            else if (periodicTransferGroup.equals("everyDate")) {
                if (transferType3 < 1 || transferType3 > 31) {
                    form.recordError(messages.get("everyDateField-regexp-message"));
                }
            }

            if (transferView.getEndDate() == null) {
                form.recordError(messages.get("endDate-requiredIf-message"));
            }
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

//                if (transferView.getAmount().compareTo(accountView.getAvailableBalance()) > 0) {
//                    form.recordError(messages.get("inputAmount-notEnough-message"));
//                    return;
//                }

            	logger.info("this.getClass().getSimpleName()=" + this.getClass().getSimpleName());
            	tokenView.setChallenge(sessionManager.getChallenge());
            	logger.info("tokenView.getToken()=" + tokenView.getToken());
            	logger.info("2 tokenView.getChallenge()=" + tokenView.getChallenge());
                if (!isMerchant() || (isMerchant() && otpManager.validateToken(transferView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) ) {
                	transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_INQ_CODE);
	                transferView = (TransferView) transferService.inquiry(transferView);
	                transferView.setTransactionType(com.dwidasa.engine.Constants.TRANSFER_CODE);
	                transferService.confirm(transferView);
            	}
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                //e.printStackTrace();
            }
        }
    }

    private void setTransferViewData() {
        transferView.setCurrencyCode(Constants.CURRENCY_CODE);
        transferView.setTransactionDate(new Date());
        transferView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        transferView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        transferView.setBillerCode(cacheManager.getBillers(
                com.dwidasa.engine.Constants.TRANSFER_CODE).get(0).getBillerCode());
        transferView.setMerchantType(sessionManager.getDefaultMerchantType());
        transferView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        transferView.setToAccountType("00");
        transferView.setInputType("M");

        if (transferKindValue.equals("transferNow")) {
            transferView.setTransferType(Constants.TRANSFER_NOW);
        } else if (transferKindValue.equals("transferAt")) {
            transferView.setTransferType(Constants.TRANSFER_POSTDATE);
        } else if (transferKindValue.equals("periodicTransfer")) {
            transferView.setTransferType(Constants.TRANSFER_PERIODIC);
            if (periodicTransferGroup.equals("every")) {
                transferView.setPeriodType(1);
                transferView.setPeriodValue(transferType1);
            } else if (periodicTransferGroup.equals("everyDay")) {
                transferView.setPeriodType(2);
                transferView.setPeriodValue(Integer.valueOf(day.toString()));
            } else if (periodicTransferGroup.equals("everyDate")) {
                transferView.setPeriodType(3);
                transferView.setPeriodValue(transferType3);
            }
        }

        if (transferValue.equals("ownAccount")) {
            transferView.setCustomerReference(customerReference1);
        } else if (transferValue.equals("fromAccountList")) {
            String custRef = customerReference2.substring(0, 50);
            custRef = custRef.trim();
            String custName = customerReference2.substring(50);
            transferView.setCustomerReference(custRef);
            transferView.setReceiverName(custName);
        } else if (transferValue.equals("inputManual")) {
            transferView.setCustomerReference(customerReference3);
        }
        if (transferView.getDescription() == null) {
        	transferView.setDescription("");
        }
        transferView.setNews(transferView.getDescription());
    }

    Object onSuccess() {
        transferConfirm.setTransferView(transferView);
        return transferConfirm;
    }

    void pageReset() {
//        transferView = null;
//        transferConfirm.setTransferView(null);
//        transferReceipt.setTransferView(null);
    }

    public boolean isMerchant() {    	
    	boolean isMerch = (sessionManager.getTokenType() == Constants.TOKEN_RESPONSE_TOKEN);
		return isMerch;
    }    
}
