package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Fanny
 * Date: 15/09/11
 * Time: 10:21
 */
public class AccountSummary {
    @Property(write = false)
    @Persist
    private AccountView accountView;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private ThreadLocale threadLocale;

    @InjectPage
    private AccountStatementResult accountStatementResult;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, threadLocale.getLocale());

    @Property
    private Date startDate;

    @Property
    private Date endDate;

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    void setupRender() {
        startDate = DateUtils.before(new Date(), 30);
        endDate = new Date();
    }

    public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }

    Object onActivate() {
        if (accountView == null) {
            return AccountBalance.class;
        }

        return null;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return AccountBalance.class;
    }

    @DiscardAfter
    Object onSelectedFromNext() {
        AccountInfo ai = sessionManager.getAccountInfo(accountView.getAccountNumber());

        accountView.setCurrencyCode(ai.getCurrencyCode());
        accountView.setAccountType(ai.getAccountType());
        accountView.setMerchantType(sessionManager.getDefaultMerchantType());
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());

        accountStatementResult.setAccountView(accountView);
        accountStatementResult.setStartDate(startDate);
        accountStatementResult.setEndDate(endDate);
        accountStatementResult.setReferer(this.getClass().getSimpleName());

        return accountStatementResult;
    }
}
