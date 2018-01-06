package com.dwidasa.ib.components;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.SmsTokenModel;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.facade.IGatePurposeService;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Date;
import java.util.Random;

@Import(library = {"TokenAero.js"})
public class TokenAero {
    @Parameter
    @Property
    private String token;

    @Parameter
    @Property
    private String token6Digit;
    
    @Parameter
    private String challenge;
    
    @Parameter
    @Property
    private int mode;
    
    @Inject
    private SessionManager sessionManager;

    @Inject
    private IGatePurposeService iGatePurposeService;
    
    public boolean isAppl1() {
    	return mode == 1;
    }

    public boolean isAppl2() {
    	return mode == 2;
    }

    public boolean isSoftToken() {
    	return mode == 3;
    }

    public boolean isSmsToken() {
    	return mode == 4;
    }


    Object beginRender(MarkupWriter writer) {
        if (mode == 0) {
            return false;
        }
        if (mode == 4 || (sessionManager.getSmsTokenSent() == null || sessionManager.getSmsTokenSent() == false) && !sessionManager.isMerchant()) {
	    	CustomerView customerView = sessionManager.getLoggedCustomerView();
	    	Customer customer= sessionManager.getLoggedCustomerPojo();
			AccountView av = new AccountView();
			av.setTransactionType(Constants.SMS_TOKEN.TRANS_TYPE);
			av.setAccountType(customerView.getAccountType());
			av.setTransactionDate(new Date());
			av.setCustomerId(sessionManager.getLoggedCustomerView().getId());
	        av.setCurrencyCode(Constants.CURRENCY_CODE);
	        av.setMerchantType(sessionManager.getDefaultMerchantType());
	        av.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
	        av.setAccountNumber(customerView.getAccountNumber());
	        
	        av.setCardNumber(customer.getCustomerPhone());
	        Random random = new Random();
	        String token = "" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
	        //String token = "87654321";
	        av.setCustomerName(token);
	        
	        SmsTokenModel smsTokenModel = new SmsTokenModel();
	        smsTokenModel.setCreated(new Date());
	        smsTokenModel.setCustomerId(customerView.getId());
	        smsTokenModel.setPhoneNumber(customer.getCustomerPhone());
	        smsTokenModel.setToken(token);
	        sessionManager.setSmsTokenModel(smsTokenModel);
	
	        try {
	        	av = (AccountView) iGatePurposeService.execute(av);
	        } catch (BusinessException e) {
	            e.printStackTrace();
	        }
	        sessionManager.setSmsTokenSent(true);
	        return true;
    	}
        return null;
    }
    
    public void setChallenge(String challenge) {
    	if (challenge == null) {
    		this.challenge = "00000000";
    	}
    	while (challenge.length() < 8) {
    		challenge = "0" + challenge;
    		this.challenge = challenge;
    	}
    	if (challenge.length() > 8) {
    		this.challenge = challenge.substring(challenge.length()-8);
    	}
    }
    
    public String getChallenge() {
    	if (challenge == null) {
    		challenge = "00000000";
    	}
    	while (challenge.length() < 8) {
    		challenge = "0" + challenge;
    	}
    	if (challenge.length() > 8) {
    		challenge = challenge.substring(challenge.length()-8);
    	}
    	return challenge;
    }
    
	public String getDisplayChallenge() {
    	String result = getChallenge();
    	result = result.substring(0, 3) + " " + result.substring(3, 6) + " " + result.substring(6);
    	return result;
    }

    public boolean isSixDigit() {
		return (mode == 1 || mode == 2) ;
    }
    
}
