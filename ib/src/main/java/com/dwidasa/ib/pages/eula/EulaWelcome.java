package com.dwidasa.ib.pages.eula;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class EulaWelcome {
    @Property
    private CustomerView customerView;

    @Inject
    private SessionManager sessionManager;
    
    @InjectComponent
    private Form form;

	@InjectPage
	private EulaActivateTransaction eulaActivateTransaction;
	
    void beginRender() {
    	System.out.println("MenuPage : EulaActivateTransaction");
        customerView = sessionManager.getLoggedCustomerView();
    }

    void onValidateFromForm() {

    }

    @DiscardAfter
    Object onAction() {
    		return eulaActivateTransaction;
    }
   
}
