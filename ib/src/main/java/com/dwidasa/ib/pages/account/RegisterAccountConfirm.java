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
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: wks-001
 * Date: 8/12/11
 * Time: 11:13 AM
 */
public class RegisterAccountConfirm {

    @Property
    private int tokenType;

    @Persist
    private AccountView accountView;

    @Inject
    private AccountService accountService;

    @Inject
    private CacheManager cacheManager;

    @InjectPage
    private RegisterAccountReceipt registerAccountReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Inject
    private SessionManager sessionManager;
    
    @Persist
    @Property
    private TokenView tokenView;

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setupRender() {
        tokenView = new TokenView();
        setTokenType();
    }

    public Object onActivate() {
        if (getAccountView() == null) {
            return registerAccountReceipt;
        }
        return null;
    }

    public Object onSelectedFromCancel() {
        return RegisterAccountInput.class;
    }

    void onValidateFromForm() {
        if (tokenView.getToken() == null) {
            form.recordError(messages.get("nullTokenError"));
        } else if (!tokenView.getToken().matches("[0-9]+")) {
            form.recordError(messages.get("formatTokenError"));
//        } else if (tokenView.getToken().length() != 8) {
//            form.recordError(messages.get("lengthTokenError"));
        }
    if (!form.getHasErrors()) {
            if (otpManager.validateToken(getAccountView().getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
                try {
                   setAccountView(accountService.registerAccount(getAccountView()));
                } catch (BusinessException e) {
                    form.recordError(e.getFullMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        registerAccountReceipt.setAccountView(accountView);
        return registerAccountReceipt;
    }

    public AccountView getAccountView() {
        return accountView;
    }

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }
}
