package com.dwidasa.ib.pages.eula;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.ib.pages.indexHiperwallet;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
@Import(library = {"context:bprks/js/jquery-1.6.2.min.js", "context:bprks/js/jquery.js",
        "context:bprks/js/script.js", "context:bprks/js/layout.js", "context:bprks/js/validation.js", "context:bprks/js/timeDate.js",
        "context:bprks/js/zone-overlay.js"}, stylesheet={"context:bprks/css/zone-overlay.css", "context:bprks/css/reset.css", "context:bprks/css/text.css", "context:bprks/css/960.css",
		"context:bprks/css/style.css"})
public class EulaAcceptance {

	@InjectPage
	private EulaWelcome eulaWelcome;
	
	@Inject
	private LoggingService loggingService;

    @Inject
    private Request request;
    
    private boolean isCancelButton;

    void onSelectedFromCancel() {
    	isCancelButton = true;
    }
    
    void onSelectedFromAgree() {
    	isCancelButton = false;
    }

    void onPrepare() {
    	System.out.println("MenuPage : EulaAcceptance");
    }

    void onValidateFromForm() {
    	if (isCancelButton)  {    		
    		return;
    	}

    }

    public Object onSuccess() {
    	if (isCancelButton) {
    		request.getSession(Boolean.FALSE).invalidate();
            return null;
    	}
    	if(isMerchant()){
    		return indexHiperwallet.class;
    	}else{
    		return eulaWelcome;
    	} 	
    }
    @Property
    private CustomerView customerView;

    @Inject
    private SessionManager sessionManager;
    
    public boolean isMerchant() {    
    	boolean isMerch = false;
    	String accountNumber = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
    	if(accountNumber.startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC1) || accountNumber.startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC2) || accountNumber.startsWith(com.dwidasa.ib.Constants.HIPERWALLET)){
    		isMerch = true;
    	}
    	return isMerch;
    }
    
    void onActionFromLogout() {
        loggingService.logActivity(sessionManager.getLoggedCustomerView().getId(), com.dwidasa.engine.Constants.LOGOUT_TYPE, "Logout", "", sessionManager.getDefaultMerchantType(), sessionManager.getLoggedCustomerView().getTerminalId());
        request.getSession(Boolean.FALSE).invalidate();
    }
    
    
    public CustomerView getUser() {
    	return sessionManager.getLoggedCustomerView();
    }
    
    public String getCurrentTime() {
    	return String.valueOf(System.currentTimeMillis());
    }
}
