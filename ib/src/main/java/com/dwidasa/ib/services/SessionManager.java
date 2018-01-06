package com.dwidasa.ib.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dwidasa.engine.model.view.AeroTicketingView;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.services.Request;

import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.SmsTokenModel;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.pages.Login;

/**
 * Class to cache & retrieve general information frequently used by application.
 * Instead querying to database again and again, for information that tend unchanged
 * we cached it in session object for letter use.
 *
 * @author rk
 */
public class SessionManager {
	private static Logger logger = Logger.getLogger( SessionManager.class );
    @Inject
    private Request request;

    @Inject
    private CustomerAccountDao customerAccountDao;

    @Inject
    private ExtendedProperty extendedProperty;
    
    @Inject
    private CustomerDeviceService customerDeviceService;
    
    /**
     * Get logged customer. This variable is initialize during login page.
     * @return customerView object
     */
    public CustomerView getLoggedCustomerView() {
        return (CustomerView) request.getSession(false).getAttribute(Login.LOGGED_CUSTOMER_VIEW);
    }
    public Customer getLoggedCustomerPojo() {
        return (Customer) request.getSession(false).getAttribute(Login.LOGGED_CUSTOMER);
    }
    public Integer getLoggedFailedAuth() {
        return (Integer) request.getSession(false).getAttribute(Login.LOGGED_FAILED_AUTH);
    }
    public void setFailedAuth(Integer failedAuth) {
        request.getSession(false).setAttribute(Login.LOGGED_FAILED_AUTH, failedAuth);
    }
    public Boolean isNotActivatedYet() {
        return (Boolean) request.getSession(false).getAttribute(Login.LOGGED_NOT_ACTIVATED_YET);
    }
    public void setAlreadyActivation() {
    	request.getSession(false).setAttribute(Login.LOGGED_NOT_ACTIVATED_YET, false);
    }
    public String getDefaultMerchantType() {
    	return extendedProperty.getDefaultMerchantType();
    }
    public void setSessionLastPage(String className) {
    	request.getSession(false).setAttribute(Constants.LASTPAGE_SESSION, className);
    }
    public String getSessionLastPage() {
    	return (String) request.getSession(false).getAttribute(Constants.LASTPAGE_SESSION);
    }
    public void setAeroticketViewSession(AeroTicketingView aeroTicketingView) {
    	request.getSession(false).setAttribute(Constants.AEROTICKET_VIEW_SESSION, aeroTicketingView);
    }
    public AeroTicketingView getAeroticketViewSession() {
        return (AeroTicketingView) request.getSession(false).getAttribute(Constants.AEROTICKET_VIEW_SESSION);
    }

    public void setSmsTokenModel(SmsTokenModel smsTokenModel) {
    	request.getSession(false).setAttribute(Constants.SESSION.SMS_TOKEN, smsTokenModel);
    }
    public SmsTokenModel getSmsTokenModel() {
    	return (SmsTokenModel) request.getSession(false).getAttribute(Constants.SESSION.SMS_TOKEN);
    }

    public void setChallenge(String challenge) {
    	request.getSession(false).setAttribute(Constants.SESSION.CHALLENGE, challenge);
    }
    public String getChallenge() {
    	return (String) request.getSession(false).getAttribute(Constants.SESSION.CHALLENGE);
    }

    public void setSmsTokenSent(Boolean sent) {
    	request.getSession(false).setAttribute(Constants.SESSION.SMS_TOKEN_SENT, sent);
    }
    public Boolean getSmsTokenSent() {
    	return (Boolean) request.getSession(false).getAttribute(Constants.SESSION.SMS_TOKEN_SENT);
    }
  
    
//    public int getTokenType() {
//    	if (getLoggedCustomerView().getTerminalId().equals(extendedProperty.getDefaultTerminalId())) {
//    		return Constants.TOKEN_RESPONSE_TOKEN;
//    	}
//    	return Constants.TOKEN_SOFTTOKEN;
//    }
    
    public void setTokenType(int tokenType) {
    	request.getSession(false).setAttribute(Constants.SESSION.TOKEN_TYPE, tokenType);
    }
    public int getTokenType() {
    	Integer tokenType = (Integer) request.getSession(false).getAttribute(Constants.SESSION.TOKEN_TYPE);
    	if (tokenType == null) {//
    		CustomerDevice customerDevice2 = customerDeviceService.getDeviceSoftToken(getLoggedCustomerView().getId());
    		if(customerDevice2 != null){
        		return Constants.TOKEN_SOFTTOKEN;
        	}else{
    		if (getLoggedCustomerView().getTerminalId().equals(extendedProperty.getDefaultTerminalId())) {
        		return Constants.TOKEN_SMS_TOKEN;
        	}
        	return Constants.TOKEN_RESPONSE_TOKEN;
        	}
    	}     		
    	return tokenType; 
    }
    
    /**
     * Get account map, if it doesn't exist previously, then create it.
     * @return map of account
     */
    @SuppressWarnings("unchecked")
    private Map<String, AccountInfo> forceAccountMap() {
        Map<String, AccountInfo> accountMap = (HashMap<String, AccountInfo>)
                request.getSession(false).getAttribute(Login.LOGGED_CUSTOMER_ACCOUNT);

        if (accountMap == null) {
            accountMap = new HashMap<String, AccountInfo>();
            List<CustomerAccount> cas = customerAccountDao.getAllWithTypeAndCurrency(getLoggedCustomerView().getId());
            for (CustomerAccount ca : cas) {
                AccountInfo ai = new AccountInfo();
                ai.setAccountNumber(ca.getAccountNumber());
                ai.setCardNumber(ca.getCardNumber());
                ai.setAccountType(ca.getAccountType().getAccountType());
                ai.setCurrencyCode(ca.getCurrency().getSwiftCode());
                ai.setIsDefault(ca.getIsDefault());

                accountMap.put(ca.getAccountNumber(), ai);
            }

            request.getSession(false).setAttribute(Login.LOGGED_CUSTOMER_ACCOUNT, accountMap);
        }

        return accountMap;
    }

    /**
     * Get all customer account number
     * @return list of account number
     */
    public List<String> getAccountNumber() {
        return CollectionFactory.newList(forceAccountMap().keySet());
    }

    /**
     * Get card number by account number.
     * @param accountNumber account number
     * @return card number
     */
    public String getCardNumber(String accountNumber) {
        return forceAccountMap().get(accountNumber).getCardNumber();
    }

    /**
     * Gett all customer card number.
     * @return list of card number.
     */
    public List<String> getCardNumber() {
        Set<String> result = new HashSet<String>();
        for (Map.Entry<String, AccountInfo> entry : forceAccountMap().entrySet()) {
            result.add(entry.getValue().getCardNumber());
        }

        return CollectionFactory.newList(result);
    }

    /**
     * Get account type from account number.
     * @param accountNumber account number
     * @return account type
     */
    public String getAccountType(String accountNumber) {
        return forceAccountMap().get(accountNumber).getAccountType();
    }

    /**
     * Get account info from account number.
     * @param accountNumber account number
     * @return account info object
     */
    public AccountInfo getAccountInfo(String accountNumber) {
        return forceAccountMap().get(accountNumber);
    }

    /**
     * Get account info of default account
     * @return account info object
     */
    public AccountInfo getDefaultAccountInfo() {
        for (Map.Entry<String, AccountInfo> entry : forceAccountMap().entrySet()) {
            if (entry.getValue().getIsDefault().equals("Y")) {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * Invalidate cached customer account.
     */
    public void invalidateAccount() {
        request.getSession(false).setAttribute(Login.LOGGED_CUSTOMER_ACCOUNT, null);
    }
    
    public boolean isMerchant() {
		 if (getLoggedCustomerView().getTerminalId().equals(extendedProperty.getDefaultTerminalId())) {
		     	return false;
		 } else {
	         if (getLoggedCustomerView().getTerminalId() != null && getLoggedCustomerView().getTerminalId().length() == 8) {
	         	return true;
	         }else{
	        	 return false;
	         }
	     }
	}
    
    
}
