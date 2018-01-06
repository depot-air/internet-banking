package com.dwidasa.ib.pages.account;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/2/11
 * Time: 00:19 am
 */
public class RegisterCardConfirm {
    @Persist
    private AccountView accountView;
    @Property
    private int tokenType = 1;
    @Inject
    private CacheManager cacheManager;
    @InjectPage
    private RegisterCardReceipt registerCardReceipt;
    @Inject
    private AccountService accountService;
    @Inject
    private SessionManager sessionManager;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;
    @Property
    private TokenView tokenView;


    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    void setupRender() {
        // setTokenType();
    }

    void onValidateFromForm() {
        if (tokenView.getToken() == null) {
            form.recordError(messages.get("tokenError"));
        }
//        if (tokenView.getToken().length() != 8) {
//            form.recordError(messages.get("lengthTokenError"));
//        }
        if (!tokenView.getToken().matches("[0-9]+")) {
            form.recordError(messages.get("formatTokenError"));
        }
    }

    @DiscardAfter
    Object onSuccess() {
        try {
            accountService.registerCard(accountView);
        } catch (BusinessException e) {
            //-- TODO where to go depend on error code
            e.printStackTrace();
        }
        registerCardReceipt.setAccountView(accountView);
        return registerCardReceipt;
    }

    Object onSelectedFromCancel() {
        return DeactivateCardInput.class;
    }

    public AccountView getAccountView() {
        return accountView;
    }

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }
}
