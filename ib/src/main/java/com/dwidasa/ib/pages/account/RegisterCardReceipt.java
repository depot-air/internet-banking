package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.ib.pages.Index;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/2/11
 * Time: 00:19 am
 */
public class RegisterCardReceipt {
     @Property
    private String status;
    @Property
    private String typeTransfer;
    @Property
    private String nowDate;
    @InjectPage
    private RegisterCardInput registerCardInput;
    @Persist
    private AccountView accountView;
    @Inject
    private Messages messages;


    public void setupRender() {
        setTransactionViewDate();
//        if (accountView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
//            status = messages.get("success");
//        } else {
//            status = messages.get("failed");
//        }
    }

    private void setTransactionViewDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd MMMM yyyy / hh:mm:ss");
        nowDate = sdate.format(date);
        nowDate = nowDate + " WIB";
    }

    Object onSelectedFromSave() {
        return Index.class;
    }

    Object onSelectedFromBack() {
        return registerCardInput;
    }


    public AccountView getAccountView() {
        return accountView;
    }

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }
}
