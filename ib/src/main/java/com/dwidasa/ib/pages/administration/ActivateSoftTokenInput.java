package com.dwidasa.ib.pages.administration;

import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.facade.NativeAuthenticationService;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.pages.payment.MultiFinancePaymentInput;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:21 am
 */
public class ActivateSoftTokenInput {
    @Property
    private String tokenCode;
    
    @Inject
    private WebAdministrationService webAdministrationService;
    
    @Inject
    private NativeAuthenticationService authenticationService;
    
    @Inject
    private SessionManager sessionManager;
    @InjectPage
    private ActivateSoftTokenReceipt activateSoftTokenReceipt;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;
    private ResultView resultView;

    @Inject
    private CustomerDeviceService customerDeviceService;

    @Inject
    private CustomerService customerService;

    void onValidateFromForm() {
        if (tokenCode == null) {
            form.recordError(messages.get("tokenError"));
        }
//        else if (tokenCode.length() != 8) {
//            form.recordError(messages.get("lengthTokenError"));
//        }
        else if (!tokenCode.matches("[0-9]+")) {
            form.recordError(messages.get("formatTokenError"));
        }
        if (!form.getHasErrors()) {
        	
        	boolean result = false;
        	
            try {
            	sessionManager.setAlreadyActivation();
                //resultView = webAdministrationService.validateFirstToken(sessionManager.getLoggedCustomerView().getId(), tokenCode);
                
            	CustomerDevice customerDevices = customerDeviceService.getDeviceSoftToken(sessionManager.getLoggedCustomerView().getId());
            	
            	try {
            	result = authenticationService.authenticateToken(sessionManager.getLoggedCustomerView().getId(), 
            			customerDevices.getDeviceId(), sessionManager.getLoggedCustomerView().getSessionId(), tokenCode);
            	
            	if(result){
            		
            		resultView = new ResultView();
            		resultView.setResponseCode("00");
            		Customer customer = sessionManager.getLoggedCustomerPojo();
            		customer.setStatus(com.dwidasa.engine.Constants.STATUS.ONE);
            		customer.setFirstLogin(com.dwidasa.engine.Constants.STATUS.NO);
            		customerService.save(customer);
            		sessionManager.setSessionLastPage(ActivateSoftTokenInput.class.toString());
            		
            	}else{
            		
            		 form.recordError("Data Otentikasi Tidak Valid");
            		
            	}
            	
            	}catch (BusinessException e) {
            		 form.recordError(e.getFullMessage());
				}
            	
                //set active, already login
//                List<CustomerDevice> customerDevices = customerDeviceService.getNotActiveByCustomerId(sessionManager.getLoggedCustomerView().getId());
//                for(int j=0; j < customerDevices.size(); j++) {
//        			CustomerDevice customerDevice = customerDevices.get(j);
//        			customerDevice.setStatus(Constants.ACTIVE_STATUS);
//        			customerDeviceService.save(customerDevice);    	
//            	}     
//                Customer customer = sessionManager.getLoggedCustomerPojo();
//                customer.setFirstLogin(Constants.STATUS.YES);
//                customerService.save(customer);
                
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    @DiscardAfter
    Object onSuccess() {
        activateSoftTokenReceipt.setResultView(resultView);
        return ActivateSoftTokenReceipt.class;
    }

}
