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
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/1/11
 * Time: 00:52 am
 */
public class DeactivateCardConfirm {
    @Property(write = false)
    @Persist
    private AccountView accountView;

    @Property
    private int tokenType;

    @Inject
    private CacheManager cacheManager;

    @InjectPage
    private DeactivateCardReceipt deactivateCardReceipt;

    @Inject
    private AccountService accountService;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private OtpManager otpManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Property
    private TokenView tokenView;

    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    void setupRender() {
        setTokenType();
    }

    @DiscardAfter
    Object onSelectedFromCancel() {
        return DeactivateCardInput.class;
    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    void onValidateFromForm() {
        AccountInfo ai = sessionManager.getDefaultAccountInfo();
        accountView.setAccountNumber(ai.getAccountNumber());
        accountView.setAccountType(ai.getAccountType());
        accountView.setCurrencyCode(ai.getCurrencyCode());
        accountView.setMerchantType(sessionManager.getDefaultMerchantType());
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());

        try {
            if (otpManager.validateToken(accountView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
                accountService.deactivateCard(accountView);
                sessionManager.invalidateAccount();
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    Object onSuccess() {
        deactivateCardReceipt.setAccountView(accountView);
        return deactivateCardReceipt;
    }

    Object onActivate() {
        if (accountView == null) {
            return DeactivateCardInput.class;
        }

        return null;
    }
}
