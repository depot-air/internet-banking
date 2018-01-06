package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: wks-001
 * Date: 8/15/11
 * Time: 10:30 AM
 */
public class RegisterAccountReceipt {
    @Persist
    private AccountView accountView;
    @Property
    private String status;
    @Property
    private String nowDate;
    @Inject
    private AccountService accountService;
    @Inject
    private Messages messages;

    public Object onActivate(){
        if(getAccountView() == null){
            return RegisterAccountInput.class;
        }
        return null;
    }

    void setupRender() {
        if (getAccountView().getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        setAccountViewDate();
    }

    private void setAccountViewDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd MMMM yyyy / hh:mm:ss");
        nowDate = sdate.format(date);
        nowDate = nowDate + " WIB";
        getAccountView().setTransactionDate(new Date());
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return RegisterAccountInput.class;
    }

    public AccountView getAccountView() {
        return accountView;
    }

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
