package com.dwidasa.ib.pages.eula;

import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.SmsTokenModel;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.ib.pages.administration.ActivateSoftTokenInput;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class ActivateSMSTokenConfirm {
    @Property
    private String inputPassword = "";

	@InjectPage
	private ActivateSMSTokenReceipt activateSMSTokenReceipt;

	@InjectComponent
	private Form form;
	
	@Inject
	private Messages message;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private CustomerDao customerDao;

    @Inject
    private CustomerDeviceDao customerDeviceDao;

    private boolean isCancelButton;
    private SmsTokenModel smsTokenModel;
    
    void onSelectedFromCancel() {
    	isCancelButton = true;
    }

	void onPrepare() {
		
	}

	void onSelectedFromNext() {
		isCancelButton = false;
		if (inputPassword == null) {
			form.recordError(message.get("idNullError"));
		}
        smsTokenModel = sessionManager.getSmsTokenModel();      
    	if (!inputPassword.equals(smsTokenModel.getToken())) {
    		form.recordError(message.get("tokenWrong"));
    	}
	}

    void onValidateFromForm() {
    	if (isCancelButton)  {
    		form.clearErrors();
    		return;
    	}
    	if (!form.getHasErrors()) {
    		Customer customer = sessionManager.getLoggedCustomerPojo();
    		CustomerView cv = sessionManager.getLoggedCustomerView();
    		List<CustomerDevice> cds = customerDeviceDao.getAllNotActiveYet(customer.getId());
    		for (CustomerDevice customerDevice : cds) {
				customerDevice.setTerminalId(smsTokenModel.getPhoneNumber());
				customerDevice.setStatus(1);
	    		customerDeviceDao.save(customerDevice);
			}
    		customer.setStatus(com.dwidasa.engine.Constants.STATUS.ONE);
    		customer.setCustomerPhone(smsTokenModel.getPhoneNumber());
    		customer.setFirstLogin(com.dwidasa.engine.Constants.STATUS.NO);
    		customer.setTokenActivated(com.dwidasa.engine.Constants.STATUS.YES);
    		customerDao.save(customer);
    		sessionManager.setSessionLastPage(ActivateSMSTokenConfirm.class.toString());
    	}
    }

    @DiscardAfter
    public Object onSuccess() {
    	if (isCancelButton) {
            return ActivateSMSToken.class;
    	}
        return activateSMSTokenReceipt;
    }
    
}
