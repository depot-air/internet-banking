package com.dwidasa.ib.services;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.SmsTokenModel;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.facade.NativeAdministrationService;
import com.dwidasa.engine.service.facade.NativeAuthenticationService;
import com.dwidasa.engine.service.facade.WebAuthenticationService;
import com.dwidasa.ib.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/4/11
 * Time: 8:13 PM
 */
public class OtpManager {
	private static Logger logger = Logger.getLogger( OtpManager.class );
    @Inject
    private CacheManager cacheManager;

    @Inject
    private WebAuthenticationService webAuthenticationService;

    @Inject
    private LoggingService loggingService;
   
    @Inject
    private CustomerService customerService;

    @Inject
    private NativeAdministrationService administrationService;

    @Inject
    private NativeAuthenticationService authenticationService;
    
    @Inject
    private CustomerDeviceService customerDeviceService;

    @Inject
    SessionManager sessionManager;
    
    /**
     * First, decide whether a form should provide token through form config. And if
     * it is, then validate token appropriate with form's token type.
     * @param customerId customer id
     * @param formName form name
     * @param view view that hold credential required
     * @return true if pass this validation, else exception will be thrown
     */
    public Boolean validateToken(Long customerId, String formName, TokenView view) {
//    	return true;
    	CustomerView customerView = sessionManager.getLoggedCustomerView();
    	Customer customer = customerService.get(customerId);
    	CustomerDevice customerDevice2 = customerDeviceService.getDeviceSoftToken(customerId);
        	if(customerDevice2 != null){
        		sessionManager.setTokenType(Constants.TOKEN_SOFTTOKEN);
        	}
        	
    	boolean result = false;    	
//    	
        if (sessionManager.getTokenType() == Constants.TOKEN_RESPONSE_TOKEN){
        	logger.info("formName=" + formName + " sessionManager.getTokenType()=" + sessionManager.getTokenType());
            if (formName.equals("TransferInput") || formName.equals("TransferAtmbInput") || formName.equals("TransferAntarBankInput") ) {
                result = webAuthenticationService.authenticateTokenWithChallenge(customer.getCustomerUsername(), "XX", view.getToken(), customer, customerView.getSessionId(), view.getChallenge());
                return result;
            } else {
            	result = webAuthenticationService.authenticateTokenRO(customer.getCustomerUsername(), "XX", view.getToken(), customer, customerView.getSessionId());
                return result;
            }
        }
//
        else if (sessionManager.getTokenType() == Constants.TOKEN_SMS_TOKEN){
            result = authenticateSmsToken(customer, customerView.getSessionId(), view.getToken());
            return result;
        }
        else if(sessionManager.getTokenType() == Constants.TOKEN_SOFTTOKEN){
        	logger.info("formName=" + formName + "Soft Token sessionManager.getTokenType()=" + sessionManager.getTokenType());
        	result = authenticationService.authenticateToken(customerId, customerDevice2.getDeviceId(), customerView.getSessionId(), view.getToken());
        	return result;
        }
        if (!result) {
        	customer.setFailedAuthAttempts(customer.getFailedAuthAttempts() + 1);
        	customerService.save(customer);

            String maxFailedAuthAttempts = cacheManager.getParameter("FAILED_AUTHENTICATION_ATTEMPTS").
                    getParameterValue();

            int slot = Integer.parseInt(maxFailedAuthAttempts) - customer.getFailedAuthAttempts().intValue();

            if (slot <= 0) {
                try {
                    administrationService.deactivateService(customer.getId());
                } catch (BusinessException e) {
                    //-- do nothing
                    e.printStackTrace();
                }
                throw new BusinessException("IB-1015", maxFailedAuthAttempts);
            }
            else if (slot < 3) {
                throw new BusinessException("IB-1020", Integer.toString(slot));
            }	
        } else {
        	customer.setFailedAuthAttempts(0);
        	customerService.save(customer);
        }    
        return result;
    }
    
    
    public Boolean validateSmsToken(Long customerId, String formName, TokenView view, int mode) {
//    	return true;
    	CustomerView customerView = sessionManager.getLoggedCustomerView();
    	Customer customer = customerService.get(customerId);
    	
    	boolean result = false;    	
//    	
        if (sessionManager.getTokenType() == Constants.TOKEN_RESPONSE_TOKEN){
        	logger.info("formName=" + formName + " sessionManager.getTokenType()=" + sessionManager.getTokenType());
            if (formName.equals("TransferInput") || formName.equals("TransferAtmbInput") || formName.equals("TransferAntarBankInput") ) {
                result = webAuthenticationService.authenticateTokenWithChallenge(customer.getCustomerUsername(), "XX", view.getToken(), customer, customerView.getSessionId(), view.getChallenge());
                return result;
            } else {
            	result = webAuthenticationService.authenticateTokenRO(customer.getCustomerUsername(), "XX", view.getToken(), customer, customerView.getSessionId());
                return result;
            }
        }
//
        else if (mode == 4){
            result = authenticateSmsToken(customer, customerView.getSessionId(), view.getToken());
            return result;
        }
       
        if (!result) {
        	customer.setFailedAuthAttempts(customer.getFailedAuthAttempts() + 1);
        	customerService.save(customer);

            String maxFailedAuthAttempts = cacheManager.getParameter("FAILED_AUTHENTICATION_ATTEMPTS").
                    getParameterValue();

            int slot = Integer.parseInt(maxFailedAuthAttempts) - customer.getFailedAuthAttempts().intValue();

            if (slot <= 0) {
                try {
                    administrationService.deactivateService(customer.getId());
                } catch (BusinessException e) {
                    //-- do nothing
                    e.printStackTrace();
                }
                throw new BusinessException("IB-1015", maxFailedAuthAttempts);
            }
            else if (slot < 3) {
                throw new BusinessException("IB-1020", Integer.toString(slot));
            }	
        } else {
        	customer.setFailedAuthAttempts(0);
        	customerService.save(customer);
        }    
        return result;
    }
    
    public Boolean validateHardToken(Long customerId, String formName, String token) {
    	System.out.println("start validateHardToken");
    	CustomerView customerView = sessionManager.getLoggedCustomerView();
    	Customer customer = customerService.get(customerId);
    	boolean result = false;    	
        result = webAuthenticationService.authenticateTokenRO(customer.getCustomerUsername(), "XX", token, customer, customerView.getSessionId()); 
        System.out.println("valid Hard Token result : " +result);
        if (!result) {
        	
        	customer.setFailedAuthAttempts(customer.getFailedAuthAttempts() + 1);
        	customerService.save(customer);

            String maxFailedAuthAttempts = cacheManager.getParameter("FAILED_AUTHENTICATION_ATTEMPTS").
                    getParameterValue();

            int slot = Integer.parseInt(maxFailedAuthAttempts) - customer.getFailedAuthAttempts().intValue();

            if (slot <= 0) {
                try {
                    administrationService.deactivateService(customer.getId());
                } catch (BusinessException e) {
                    //-- do nothing
                    e.printStackTrace();
                }
                throw new BusinessException("IB-1015", maxFailedAuthAttempts);
            }
            else if (slot < 3) {
                throw new BusinessException("IB-1020", Integer.toString(slot));
            }	
        } else {
        	customer.setFailedAuthAttempts(0);
        	customerService.save(customer);
        }    
        return result;
    }
    
    public Boolean authenticateSmsToken(Customer customer, String sessionId, String token) {
        SmsTokenModel smsTokenModel = sessionManager.getSmsTokenModel();

		Date date1 = smsTokenModel.getCreated();
		Date date2 = new Date();
//		long now = System.currentTimeMillis();
		long difference = date2.getTime() - date1.getTime();
		//One hour means a difference of 1000 * 60 * 60 milliseconds
		//One minute means a difference of 1000 * 60 milliseconds
		//One secondmeans a difference of 1000 milliseconds
		//3 minutes means a difference of 1000 * 60 * 3 milliseconds		
        if (!smsTokenModel.getToken().equals(token) || difference > (1000 * 60 * 3) ) {
            loggingService.incFailedAuthAttempts(customer);
            throw new BusinessException("IB-1000");
        }
        if (customer.getFailedAuthAttempts() != 0) {
            customer.setFailedAuthAttempts(0);
            customerService.save(customer);
        }

        return Boolean.TRUE;
	}
}
