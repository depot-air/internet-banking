package com.dwidasa.ib.pages.administration;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;


/**
 * Created by IntelliJ IDEA.
 * User: wks-001
 * Date: 8/4/11
 * Time: 2:04 PM
 */
public class PhoneChangeInput {
    @Property
	private CustomerView customerView;

	@Inject
    private SessionManager sessionManager;

    @Property
    private String confirmPhoneValue;

    @InjectPage
    private PhoneChangeConfirm phoneChangeConfirm;

    @InjectComponent
	private Form form;

    @Inject
	private Messages message;
    
    @Inject
    private OtpManager otpManager;

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    void onSelectedFromNext(){
        if (customerView.getPhone() == null) {
            form.recordError(message.get("errorNewPhoneNull"));
        } else if (!customerView.getPhone().matches("[0-9]+")) {
            form.recordError(message.get("formatNewPhoneError"));
        } else if (customerView.getPhone().length() < 8) {
            form.recordError(message.get("lengthNewPhoneError"));
        } else if (!customerView.getPhone().equalsIgnoreCase(confirmPhoneValue)){
			form.recordError(message.get("errorPhoneChange"));
        }
        if (form.getHasErrors()) return;

        if (confirmPhoneValue == null) {
            form.recordError(message.get("errorConfPhoneNull"));
        } else if (!confirmPhoneValue.matches("[0-9]+")) {
            form.recordError(message.get("formatConfPhoneError"));
        } else if (confirmPhoneValue.length() < 8) {
            form.recordError(message.get("lengthConfPhoneError"));
        }
        if (form.getHasErrors()) return;
        
       

        if(!form.getHasErrors()){
        	try {
        		customerView.setId(sessionManager.getLoggedCustomerView().getId());
            } catch (BusinessException e) {
            	form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    void onPrepare() {
        if(customerView == null){
            customerView = new CustomerView();
        }

    }
    
    @Inject
    private CacheManager cacheManager;
    
    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void setupRender(){
    	setTokenType();
		if(customerView == null){
			customerView = new CustomerView();
		}
	}

	@DiscardAfter
	Object onSuccess(){
		phoneChangeConfirm.setCustomerView(customerView);
		return PhoneChangeConfirm.class;
	}
	
	
	@Property
	private int tokenType;

}
