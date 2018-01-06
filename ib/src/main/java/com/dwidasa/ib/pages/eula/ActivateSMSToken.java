package com.dwidasa.ib.pages.eula;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.SmsTokenModel;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.facade.IGatePurposeService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class ActivateSMSToken {
	private static Logger logger = Logger.getLogger( ActivateSMSToken.class );
    @Property
    private String inputPassword = "";

    @Property
    private String inputHp = "";

    @Property
    private String reInputHp = "";

	@InjectPage
	private ActivateSMSTokenConfirm activateSMSTokenConfirm;

	@InjectComponent
	private Form form;
	
	@Inject
	private Messages message;

	@Inject
	private ParameterDao parameterDao;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private IGatePurposeService iGatePurposeService;

    private boolean isCancelButton;

    void onSelectedFromCancel() {
    	isCancelButton = true;
    }

	void onPrepare() {
		inputPassword = null;
	}

	void setupRender() {
		inputPassword = null;
	}
	
	void onSelectedFromNext() {
		isCancelButton = false;
		String prefixes = getSMSTokenPrefixes();
		Customer customer = sessionManager.getLoggedCustomerPojo();
		if (inputPassword == null) {
			form.recordError(message.get("passwordNullError"));
		} else if (!EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, inputPassword).equals(customer.getEncryptedCustomerPin())) {
			form.recordError(message.get("passwordWrong"));
		}
		
		if (inputHp == null) {
			form.recordError(message.get("phoneNullError"));
		} else if (!inputHp.matches("[0-9]+")) {
			form.recordError(message.get("phoneFormatError"));
		} else if (inputHp.length() < 8) {
			form.recordError(message.get("lengthPhoneError"));
		} else if (!isPhonePrefix(inputHp, prefixes)) {
			form.recordError(message.get("phonePrefixError") + " " + getFormattedSMSTokenPrefixes(prefixes));
		}
		
		if (reInputHp == null) {
			form.recordError(message.get("rephoneNullError"));
		} else if (!reInputHp.matches("[0-9]+")) {
			form.recordError(message.get("rephoneFormatError"));
		} else if (reInputHp.length() < 8) {
			form.recordError(message.get("lengthRephoneError"));	
		} else if (!reInputHp.equals(inputHp)) {
			form.recordError(message.get("phoneMustBeSameError"));
		} 
	}

    void onValidateFromForm() {
    	if (isCancelButton)  {
    		form.clearErrors();
    		return;
    	}
    	if (!form.getHasErrors()) {
    		CustomerView customerView = sessionManager.getLoggedCustomerView();
    		AccountView av = new AccountView();
    		av.setTransactionType(Constants.SMS_TOKEN.TRANS_TYPE);
    		av.setAccountType(customerView.getAccountType());
    		av.setTransactionDate(new Date());
    		av.setCustomerId(sessionManager.getLoggedCustomerView().getId());
            av.setCurrencyCode(Constants.CURRENCY_CODE);
            av.setMerchantType(sessionManager.getDefaultMerchantType());
            av.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
            av.setAccountNumber(customerView.getAccountNumber());
            
            av.setCardNumber(inputHp);
            Random random = new Random();
            String token = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
            //String token = "87654321";
            av.setCustomerName(token);
            
            SmsTokenModel smsTokenModel = new SmsTokenModel();
            smsTokenModel.setCreated(new Date());
            smsTokenModel.setCustomerId(customerView.getId());
            smsTokenModel.setPhoneNumber(inputHp);
            smsTokenModel.setToken(token);
            sessionManager.setSmsTokenModel(smsTokenModel);
            
            try {
            	av = (AccountView) iGatePurposeService.execute(av);
            } catch (BusinessException e) {
                // just log exception and move on
                e.printStackTrace();
            }
        }
    }

	public String getSMSTokenPrefixes() {    	
    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.SMS_TOKEN_PREFIX);
    	return ip.getParameterValue();
    }
	public String getFormattedSMSTokenPrefixes(String prefixes) {
		String prefs = "";
		String[] tokens = prefixes.split(",");
    	if (tokens.length > 0 ) {
    		for(int i = 0; i < tokens.length; i++) {
    			if (i != tokens.length - 1 ) {
    				prefs += tokens[i] + ", ";
    			} else {
    				prefs += " atau " + tokens[i];
    			}
    		}
    	}
    	return prefs;
    }
	
    public boolean isPhonePrefix(String phoneInput, String prefixes) {    	
    	String firstFive = phoneInput.substring(0, 5);
    	String[] tokens = prefixes.split(",");
    	boolean isMerch = false;
    	if (tokens.length > 0 ) {
    		for(int i = 0; i < tokens.length; i++) {
    			if (firstFive.equals(tokens[i]))
    				isMerch = true;
    		}
    	}
    	return isMerch;
    }
/*
	@DiscardAfter
	Object onSuccessFromForm() {
		return eulaActivateTransaction;
	}
*/
    @DiscardAfter
    public Object onSuccess() {
    	if (isCancelButton) {
            return EulaActivateTransaction.class;
    	}
        return activateSMSTokenConfirm;
    }
    
}
