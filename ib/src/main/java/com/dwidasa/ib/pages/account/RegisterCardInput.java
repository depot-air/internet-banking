package com.dwidasa.ib.pages.account;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/2/11
 * Time: 00:18 am
 */
public class RegisterCardInput {
    @Property
    @Persist
    private AccountView accountView;
    @Property
    private int tokenType = 0;
    @Inject
    private CacheManager cacheManager;
    @Inject
    private SessionManager sessionManager;
    @InjectPage
    private RegisterCardConfirm registerCardConfirm;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;

    void setupRender() {
        if (accountView == null) {
            accountView = new AccountView();
        }
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void onValidateFromForm() {
        if (accountView.getCardNumber() == null) {
            form.recordError(messages.get("cardNumberError"));
        } else if (!accountView.getCardNumber().matches("[0-9]+")) {
            form.recordError(messages.get("formatCardNumberError"));
        } else if (accountView.getCardNumber().length() != 10) {
            form.recordError(messages.get("lengthCardNumberError"));
        }
    }

    @DiscardAfter
    Object onSuccess() {
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        accountView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
        accountView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        accountView.setCurrencyCode(Constants.CURRENCY_CODE);
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setMerchantType(sessionManager.getDefaultMerchantType());

        registerCardConfirm.setAccountView(accountView);
        return registerCardConfirm;

    }


}
