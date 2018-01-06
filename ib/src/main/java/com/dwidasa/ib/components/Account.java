package com.dwidasa.ib.components;

import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 08/07/11
 * Time: 14:03
 */
public class Account {
    @Parameter
    @Property
    private String accountNumber;

    @Parameter
    private String cardNumber;
    
    @Property
    private List<String> accountModel;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private FormSupport formSupport;
    
    @InjectComponent
    private Zone billerAccount;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;
    
    
    private final static ComponentAction<Account> PROCESS_SUBMISSION = new ComponentAction<Account>() {
        public void execute(Account component) {
            component.processSubmission();
        }
    };

    private void processSubmission() {
        cardNumber = sessionManager.getCardNumber(accountNumber);
    }

    void onValueChangedFromaccountNumber(String accountNumberValue) {
    	
        accountNumber = accountNumberValue;
    }
    
    void setupRender() {
        accountModel = sessionManager.getAccountNumber();
        accountNumber = accountModel.get(0);

        //-- todo replace with defer instead of storeandexecute !
        formSupport.storeAndExecute(this, PROCESS_SUBMISSION);
    }
}
