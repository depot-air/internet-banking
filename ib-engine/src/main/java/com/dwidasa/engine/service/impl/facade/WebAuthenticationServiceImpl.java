package com.dwidasa.engine.service.impl.facade;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.dwidasa.engine.dao.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.CustomerSession;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TokenAgentService;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.facade.WebAuthenticationService;
import com.dwidasa.engine.service.otp.WebOtpClient;

/**
 * Implementation class of <code>WebAuthenticationService</code> service.
 *
 * @author rk
 */
@Service("webAuthenticationService")
public class WebAuthenticationServiceImpl implements WebAuthenticationService {
	private static Logger logger = Logger.getLogger( WebAuthenticationServiceImpl.class );
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAccountDao customerAccountDao;


    @Autowired
    private CustomerSessionDao customerSessionDao;

    @Autowired
    private CustomerDeviceDao customerDeviceDao;

    @Autowired
    private ParameterDao parameterDao;
    
    @Autowired
    private LoggingService loggingService;

    @Autowired
    private WebOtpClient webOtpClient;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ExtendedProperty extendedProperty;

    @Autowired
    private TokenAgentService tokenAgentService;
    
    public WebAuthenticationServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public CustomerView authenticate(String customerUsername, String customerPin) {
   		Customer customer = customerDao.getWithDefaultAccount(customerUsername.toUpperCase());	

   		//-- check existence of a customerUsername
        if (customer == null) {
            throw new BusinessException("IB-1002");
        }

        String ip = "";
		try {
			InetAddress thisIp = InetAddress.getLocalHost();
			ip = thisIp.getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();		
		}
        
        //-- check status of customer whether active or not
        Long customerId = customer.getId();
		if (!customerDao.isActive(customerId)) {
			String maxFailedAuthAttempts = cacheManager.getParameter("FAILED_AUTHENTICATION_ATTEMPTS").getParameterValue();
            throw new BusinessException("IB-1015", maxFailedAuthAttempts);	//IB-1002
        }
        
        if (customer.getEncryptedCustomerPin().length() == 16) {
	        if (!customerDao.authenticate(customerUsername.toUpperCase(), customerPin)) {
	            loggingService.incFailedAuthAttempts(customer);
	            throw new BusinessException("IB-1000");
	        }
        } else  {
        	if (!customerDao.authenticateSHA(customerUsername.toUpperCase(), customerPin)) {
	            loggingService.incFailedAuthAttempts(customer);
	            throw new BusinessException("IB-1000");
	        }
        }

        //-- TODO separating counter for authentication using token with authentication using tin only
        if (customer.getFailedAuthAttempts() != 0) {
            customer.setFailedAuthAttempts(0);
            customerDao.save(customer);            
        }

        CustomerSession customerSession = obtainSession(customer);
        CustomerView customerView = generateView(customer, customerSession);
        loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login IB sukses, IP = " + ip , "", extendedProperty.getDefaultMerchantType(), customerView.getTerminalId());

//        // check SMS Token phone number for IB or Kiosk non merchant
//        if (extendedProperty.getDefaultTerminalId().equals(customerView.getTerminalId())){
//            String phoneNumber = cacheManager.validateSmsTokenPhoneNumber(customer.getCustomerPhone());
//            if (phoneNumber == null){
//                throw new BusinessException("IB-STXX");
//            }
//            else {
//                if (!phoneNumber.equals(customer.getCustomerPhone())){
//                    customer.setCustomerPhone(phoneNumber);
//                    customerDao.save(customer);
//                }
//            }
//        }


        return customerView;
    }

    @Override
    public CustomerView authenticateCardPin(Customer customer, String cardNo) {
        //customer = customerDao.getWithDefaultAccount(customer.getCustomerUsername().toUpperCase());
        customer = customerDao.getByUsernameCardNo(customer.getCustomerUsername().toUpperCase(), cardNo);
        //-- check existence of a customerUsername
        if (customer == null) {
            throw new BusinessException("IB-1002");
        }

        CustomerSession customerSession = obtainSession(customer);
        return generateView(customer, customerSession);
    }

    /**
     * Create a session corresponding to customer information provided. This method will also
     * invalidate other user's session that currently logged in.
     * @param customer customer object
     * @return session object
     */
    private CustomerSession obtainSession(Customer customer) {
        //-- TODO change with deviceId
        String deviceId = customer.getMobileWebTokenId();
        if (deviceId == null) {
            deviceId = "-";
        }
        CustomerSession mcs = customerSessionDao.force(customer.getId(), deviceId);
        mcs.setSessionId(UUID.randomUUID().toString());
        mcs.setChallenge(null);
        mcs.setUpdatedby(customer.getId());
        mcs.setUpdated(new Date());
        customerSessionDao.save(mcs);

        customerSessionDao.invalidate(customer.getId(), deviceId);
        return mcs;
    }

    /**
     * Generate view object for presentation/client layer
     * @param customer customer object
     * @param customerSession session object
     * @return customerView object
     */
    private CustomerView generateView(Customer customer, CustomerSession customerSession) {
        CustomerView customerView = new CustomerView();
        customerView.setId(customer.getId());
        customerView.setUsername(customer.getCustomerUsername());
        customerView.setName(customer.getCustomerName());
        customerView.setEmail(customer.getCustomerEmail());
        customerView.setPhone(customer.getCustomerPhone());

        CustomerAccount customerAccount = customerAccountDao.getDefaultWithType(customer.getId());
        customerView.setAccountNumber(customerAccount.getAccountNumber());
        customerView.setAccountType(customerAccount.getAccountType().getAccountType());
        customerView.setCardNumber(customerAccount.getCardNumber());
        customerView.setAccountType(customerAccount.getAccountType().getAccountType());

        customerView.setFirstLogin(customer.getFirstLogin());
        customerView.setLastLogin(customer.getLastLogin());
        customerView.setLastDeviceId(customer.getLastTokenId());
        customerView.setSessionId(customerSession.getSessionId());

        if (customer.getFailedAuthAttempts() != 0) {
            customer.setFailedAuthAttempts(0);
        }
        customer.setLastLogin(new Date());
        //-- TODO set with device id, the problem what is the device id for web access ?
        customer.setLastTokenId(null);
        customerDao.save(customer);

        logger.info("customer.getId()=" + customer.getId() + " customerView.getCardNumber()=" + customerView.getCardNumber());
        List<CustomerDevice> cds = customerDeviceDao.getAll(customer.getId());

        // for MB
        if ("1".equals(extendedProperty.getServerType())) {
            CustomerDevice customerDevice = cds.get(0);

            if (customerDevice != null && customerDevice.getTerminalId() != null && !customerDevice.getTerminalId().equals("") && customerDevice.getTerminalId().length() == 8) {
                // merchant
                customerView.setTerminalId(customerDevice.getTerminalId());
                customerView.setCustomerType(Constants.CUSTOMER_TYPE.MERCHANT);
            }
            else {
                // non merchant
            	customerView.setTerminalId(extendedProperty.getDefaultTerminalId());
                customerView.setCustomerType(Constants.CUSTOMER_TYPE.INDIVIDUAL);
            }
        }
        // for IB and Kiosk
        else{

            CustomerDevice cd = customerDeviceDao.getHardTokenDevice(customer.getId());

            if (cd == null){
                // individual / non merchant
                customerView.setTerminalId(extendedProperty.getDefaultTerminalId());
                customerView.setCustomerType(Constants.CUSTOMER_TYPE.INDIVIDUAL);
            }
            else {
                // merchant
                customerView.setTerminalId(cd.getTerminalId());
                customerView.setCustomerType(Constants.CUSTOMER_TYPE.MERCHANT);
            }

        }

        //jika dia IBS, set ulang device ID
        //customerView.setLastDeviceId((customerView.getTerminalId().equals(extendedProperty.getDefaultTerminalId())) ? customer.getMobileWebTokenId() : customerView.getLastDeviceId());
        customerView.setLastDeviceId(customer.getMobileWebTokenId() != null ? customer.getMobileWebTokenId() : customerView.getLastDeviceId());
        return customerView;
    }

    public boolean isIBMerchant(CustomerView customerView) {    	
    	String firstFour = customerView.getAccountNumber().substring(0, 4);
    	String accountNumber = customerView.getAccountNumber();
    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
    	String[] tokens = ip.getParameterValue().split(",");
    	boolean isMerch = false;
    	if (tokens.length > 0 ) {
    		for(int i = 0; i < tokens.length; i++) {
    			if (firstFour.equals(tokens[i]) || accountNumber.equals(tokens[i]))
    				isMerch = true;
    		}
    	}
    	return isMerch;
    }
    /**
     * {@inheritDoc}
     */
//    public Boolean authenticateToken(Long customerId, String token) {
//        Customer customer = customerDao.get(customerId);
//
//        if (webOtpClient.validateToken(customerId, token)) {
//            loggingService.incFailedAuthAttempts(customer);
//            throw new BusinessException("IB-1000");
//        }
//
//        if (customer.getFailedAuthAttempts() != 0) {
//            customer.setFailedAuthAttempts(0);
//            customerDao.save(customer);
//        }
//
//        return Boolean.TRUE;
//    }
    public Boolean authenticateToken(Long customerId, String token) {
        Customer customer = customerDao.get(customerId);
        Boolean result = false;
        if (webOtpClient.validateToken(customerId, token)) {
//            loggingService.incFailedAuthAttempts(customer);
//            throw new BusinessException("IB-1000");
        	result = true;
        }

        if (customer.getFailedAuthAttempts() != 0) {
            customer.setFailedAuthAttempts(0);
            customerDao.save(customer);
        }

        return result;
    }
    /**
     * {@inheritDoc}
     */
//    public Boolean validateCustomerSession(Long customerId, String sessionId) {
//        switch (customerSessionDao.validate(customerId, sessionId)) {
//            case -1 : throw new BusinessException("IB-1001");
//            case 0  : throw new BusinessException("IB-1010");
//        }
//
//        return Boolean.TRUE;
//    }
    public Boolean validateCustomerSession(Long customerId, String sessionId) {
        switch (customerSessionDao.validate(customerId, sessionId)) {
            case -1 : throw new BusinessException("IB-1001");
            case 0  : throw new BusinessException("IB-1010");
            case 1	: return true;
        }

        return false;
    }
    /**
     * {@inheritDoc}
     */
//    public Boolean authenticateToken(Long customerId, String deviceId, String sessionId, String token) {
//        if (validateCustomerSession(customerId, sessionId)) {
//            authenticateToken(customerId, token);
//        }
//
//        return Boolean.TRUE;
//    }
    public Boolean authenticateToken(Long customerId, String deviceId, String sessionId, String token) {
        if (validateCustomerSession(customerId, sessionId)) {
           return authenticateToken(customerId, token);
        }

        return false;
    }
//	@Override
//	public Boolean authenticateTokenRO(String username, String transactionId, String token, Customer customer, String sessionId) {
//		if (validateCustomerSession(customer.getId(), sessionId)) {
////			if ("DEMO".equals(Constants.TOKEN_MODE) && !Constants.userUseTokenMap.contains(username)) return Boolean.TRUE;
//			Boolean oke = tokenAgentService.verifyTokenNoChallenge(username, token);
//			if (oke) {
//				return Boolean.TRUE;
//			} else {
//				loggingService.incFailedAuthAttempts(customer);
////				loggingService.logActivity(customerId, transactionId, "Input Hard Token Salah"  , "", extendedProperty.getDefaultMerchantType(), "");
////				throw new BusinessException("IB-1007", "IB-1007");
//			}
//		}
//		return Boolean.TRUE;
//	}
//    @Override
	public Boolean authenticateTokenRO(String username, String transactionId, String token, Customer customer, String sessionId) {
		Boolean oke = false;
		if (validateCustomerSession(customer.getId(), sessionId)) {
//			if ("DEMO".equals(Constants.TOKEN_MODE) && !Constants.userUseTokenMap.contains(username)) return Boolean.TRUE;
			oke = tokenAgentService.verifyTokenNoChallenge(username, token);
			System.out.println("authRO: "+oke);
			if(oke){
				return true;
			}else{
				loggingService.incFailedAuthAttempts(customer);
		        throw new BusinessException("IB-1000");
			}
		}
		
		return oke;
	}
	@Override
	public Boolean authenticateTokenWithChallenge(String username, String transactionId, String token, Customer customer, String sessionId, String challenge) {
		Boolean oke = false;
		if (validateCustomerSession(customer.getId(), sessionId)) {
			oke =  tokenAgentService.verifyTokenWithChallenge(username, challenge, token);
			System.out.println("authROWC: "+oke);
			if (oke) {
				return true;
			} else {
				loggingService.incFailedAuthAttempts(customer);
		        throw new BusinessException("IB-1000");
			}
		}
		return false;
	}
	
//	@Override
//	public Boolean authenticateTokenWithChallenge(String username, String transactionId, String token, Customer customer, String sessionId, String challenge) {
//		if (validateCustomerSession(customer.getId(), sessionId)) {
//			Boolean oke = tokenAgentService.verifyTokenWithChallenge(username, challenge, token);
//			if (oke) {
//				return Boolean.TRUE;
//			} else {
//				loggingService.incFailedAuthAttempts(customer);
//			}
//		}
//		return Boolean.TRUE;
//	}

}
