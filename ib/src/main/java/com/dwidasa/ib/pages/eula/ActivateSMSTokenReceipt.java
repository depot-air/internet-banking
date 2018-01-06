package com.dwidasa.ib.pages.eula;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.SmsTokenModel;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class ActivateSMSTokenReceipt {
	@InjectComponent
	private Form form;

    @Inject
    private SessionManager sessionManager;
    
	@Property
	private String reffNoValue = "";

    private SmsTokenModel smsTokenModel;
    
	void onPrepare() {
		smsTokenModel = sessionManager.getSmsTokenModel();    
		reffNoValue = smsTokenModel.getPhoneNumber().substring(0, 2) + " " + smsTokenModel.getPhoneNumber().substring(2, 6) + " " + smsTokenModel.getPhoneNumber().substring(6);
		
		sessionManager.setAlreadyActivation();		//isNotActivatedYet()
	}
}
