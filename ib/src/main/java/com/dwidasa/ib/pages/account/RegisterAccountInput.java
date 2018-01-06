package com.dwidasa.ib.pages.account;


import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: wks-001
 * Date: 8/11/11
 * Time: 1:03 PM
 */
public class RegisterAccountInput {
    @Persist
    @Property
    private AccountView accountView;
    @Property
    private int tokenType;
    @InjectPage
    private RegisterAccountConfirm registerAccountConfirm;
    @InjectComponent
    private Form form;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private CacheManager cacheManager;
    @Inject
    private AccountService accountService;
    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;
    @Property
    private String cardNumber;
    @Property
    private List<String> cardModel;
    @Parameter
    @Property
    private String accountNumber;

    public void onPrepare() {

    }

    public void setupRender() {
        cardModel = sessionManager.getCardNumber();
        cardNumber = cardModel.get(0);
        setTokenType();
        setAccountViewData();

    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    void onValidateFromForm() {
        if (accountView.getAccountNumber() == null) {
            form.recordError(messages.get("nullAccountError"));
        } else if (!accountView.getAccountNumber().matches("[0-9]+")) {
            form.recordError(messages.get("formatAccountError"));
        } else if (accountView.getAccountNumber().length() != 10) {
            form.recordError(messages.get("lengthAccountError"));
        }
    }

    private void setAccountViewData() {
        if (accountView == null) {
            accountView = new AccountView();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        accountView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
        accountView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setMerchantType(sessionManager.getDefaultMerchantType());
        accountView.setCurrencyCode(Constants.CURRENCY_CODE);

        registerAccountConfirm.setAccountView(accountView);
        return registerAccountConfirm;
    }

}
