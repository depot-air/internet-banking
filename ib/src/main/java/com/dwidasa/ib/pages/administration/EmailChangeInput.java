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
 * Date: 8/3/11
 * Time: 11:20 PM
 */
public class EmailChangeInput {
	@Property
	private CustomerView customerView;

    @Property
    private ResultView resultView;

    @Inject
    private SessionManager sessionManager;

	@Inject
	private WebAdministrationService administrationService;


	@Property
	private String confirmEmailValue;

	@InjectPage
	private EmailChangeReceipt emailChangeReceipt;

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
        if (customerView.getEmail() == null) {
            form.recordError(message.get("errorNewEmailNull"));
        } else if (!customerView.getEmail().equalsIgnoreCase(confirmEmailValue)){
			form.recordError(message.get("errorEmailChange"));
        } 
        if (form.getHasErrors()) return;

        if (confirmEmailValue == null) {
            form.recordError(message.get("errorConfEmailNull"));
        }  else if (!confirmEmailValue.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")) {
            form.recordError(message.get("formatConfEmailError"));
        }
        if (form.getHasErrors()) return;
        
        try {
            customerView.setId(sessionManager.getLoggedCustomerView().getId());
            resultView = administrationService.changeCustomerInfo(customerView, null, 1);
            sessionManager.getLoggedCustomerView().setEmail(customerView.getEmail());
        }
        catch (BusinessException e) {
              form.recordError(e.getFullMessage());
              e.printStackTrace();
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
	Object onSuccessFromForm(){
		emailChangeReceipt.setResultView(resultView);
		return emailChangeReceipt;
	}
	
	@Property
	private int tokenType;
	
}
