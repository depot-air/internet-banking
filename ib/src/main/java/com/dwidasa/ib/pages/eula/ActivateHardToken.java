package com.dwidasa.ib.pages.eula;

import java.util.List;

import com.dwidasa.ib.pages.Login;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.IbMerchant;
import com.dwidasa.engine.model.IbToken;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.IbMerchantService;
import com.dwidasa.engine.service.IbTokenService;
import com.dwidasa.engine.service.TokenAgentService;
import com.dwidasa.engine.service.facade.IGatePurposeService;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.facade.NativeAdministrationService;
import com.dwidasa.engine.service.facade.WebAuthenticationService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class ActivateHardToken {
    @Inject
    private Request request;

    @Property
    private String inputPassword = "";

    @Property
    private String inputSerial = "";

    @Property
    private String inputApply = "";
    
    @Inject
    private SessionManager sessionManager;

	@InjectPage
	private ActivateHardTokenReceipt activateHardTokenReceipt;

	@InjectComponent
	private Form form;
	
	@Inject
	private Messages message;

    private boolean isCancelButton;

    void onSelectedFromCancel() {
    	isCancelButton = true;
    }

	void onPrepare() {
		inputPassword = null;
		inputApply = null;
	}

	void setupRender() {
		inputPassword = null;
		inputApply = null;
	}

	@Persist("flash")
	@Property
	private String error;
	 
	@Inject
    private Messages messages;
	 
	@Inject
    private OtpManager otpManager;
	
	 @Inject
	 private IbTokenService ibTokenService;

	 @Property
	 @Persist
	 private IbToken ibToken;
	 @Property
	 @Persist
	 private List<IbToken> ibTokens;
	 
	 @Inject
	 private IbMerchantService ibMerchantService;

	 @Property
	 @Persist
	 private IbMerchant ibMerchant;
	 @Property
	 @Persist
	 private List<IbMerchant> ibMerchants;

     @Inject
	 private CustomerDeviceService customerDeviceService;
	 
	 private Customer customer;

	 private CustomerDevice ibsCustomerDevice;



	 @Inject
	 private CustomerService customerService;
	 private CustomerView customerView;
	   @Inject
	    private TokenAgentService tokenAgentService;

	 void onSelectedFromNext() {
		isCancelButton = false;
       
        //cek password
        customerView = sessionManager.getLoggedCustomerView();
    	customer = customerService.get(customerView.getId());
		if (inputPassword == null) {
			form.recordError(message.get("passwordNullError"));
		} else if (!EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, inputPassword).equals(customer.getEncryptedCustomerPin())) {
			form.recordError(message.get("passwordWrong"));
		}
        
		if (inputSerial == null) {
        	form.recordError(message.get("serialNumberNullError"));
        } else if (!inputSerial.matches("^[a-zA-Z0-9]*$")) {
			form.recordError(message.get("serialFormatError"));
		} else if (inputSerial.length() < 10) {
			form.recordError(message.get("lengthSerialError"));
		}
		if (inputApply == null) {
			form.recordError(message.get("applyNullError"));
		} else if (!inputApply.matches("[0-9]+")) {
			form.recordError(message.get("tokenFormatError"));
		} else if (inputApply.length() < 6) {
			form.recordError(message.get("lengthTokenError"));
		}
		
      //cek serial number ke m_ib_token, jika tidak ada maka error
        ibTokens = ibTokenService.getLinkedTokensBySerialNumber(inputSerial.toUpperCase());
      //cek serial number ke m_ib_merchant, jika tidak ada maka error
        ibMerchants = ibMerchantService.getMerchantBySerialNumber(inputSerial.toUpperCase());
    	if (ibTokens.size() == 0 || ibMerchants.size() == 0) {
    		form.recordError(message.get("serialNumberEmpty"));
    	}else{
    		System.out.println("validSerialNumber");
    	}
        ibsCustomerDevice = customerDeviceService.getHardTokenDevice(customerView.getId());
        if (ibsCustomerDevice == null || ibsCustomerDevice.getStatus() == 1) {
			System.out.println("customer device is null or active");
    		form.recordError(message.get("customerDevice-null-message"));
		}

        if(!form.getHasErrors()){
//        	Boolean result = otpManager.validateHardToken(customerView.getId(), this.getClass().getSimpleName(), inputApply);
        	Boolean result = tokenAgentService.verifyTokenNoChallenge(customerView.getUsername(), inputApply);
        	if (result == false) {
        		form.recordError(message.get("tokenWrong"));
       	 	}
        	System.out.println("Resultnya : " +result);
        }
        
	 }
	 
    void onValidateFromForm() {
    	if (isCancelButton)  {
    		form.clearErrors();
    		return;
    	}
    	System.out.println("not error");
    	if (!form.getHasErrors()) {
    		//update m_ib_token status = act where sn
        	ibToken = ibTokens.get(0);
        	System.out.println("updating m_ib_token where : "+ibToken.getId());
        	ibToken.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_ACTIVE);
        	ibToken.setCustomerId(customerView.getId());
        	ibTokenService.save(ibToken);

//        	//update m_ib_merchant status = act where sn
        	ibMerchant = ibMerchants.get(0);
        	System.out.println("updating m_ib_merchant where :" +ibMerchant.getId());
        	ibMerchant.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_ACTIVE);
        	ibMerchant.setCustomerId(customerView.getId());
        	ibMerchantService.save(ibMerchant);
        	
//        	customer2 = customerService.getByUsername(userIdValue.toUpperCase());
        	System.out.println("updating m_customer where id :" + customerView.getId());
//        	update m_customer status = 1
        	customer.setStatus(com.dwidasa.engine.Constants.STATUS.ONE);
			customer.setFirstLogin(com.dwidasa.engine.Constants.STATUS.NO);
			customer.setTokenActivated(com.dwidasa.engine.Constants.STATUS.YES);
			customerService.save(customer);
//			update m_customer_device
			System.out.println("updating m_customer_device where id :" +ibsCustomerDevice.getId());
			ibsCustomerDevice.setStatus(com.dwidasa.engine.Constants.STATUS.ONE);
			ibsCustomerDevice.setActivatePin("123456");
			customerDeviceService.save(ibsCustomerDevice);

            // update session
            Session session = request.getSession(false);
            session.setAttribute(Login.LOGGED_NOT_ACTIVATED_YET, false);
            session.setAttribute(com.dwidasa.ib.Constants.SESSION.TOKEN_TYPE, com.dwidasa.ib.Constants.TOKEN_RESPONSE_TOKEN);
            session.setAttribute(com.dwidasa.ib.Constants.LASTPAGE_SESSION, Login.class.toString());
            session.setAttribute("customerId", customerView.getId());
        }
    }

    @DiscardAfter
    public Object onSuccess() {
    	System.out.println("onSuccess");
    	if (isCancelButton) {
    		
            return EulaActivateTransaction.class;
    	}


    	return activateHardTokenReceipt;
    }

}
