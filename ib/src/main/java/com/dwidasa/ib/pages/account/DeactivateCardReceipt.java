package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.ib.Constants;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/1/11
 * Time: 00:52 am
 */
public class DeactivateCardReceipt {
    @Property
    private String status;

    @Property
    private String nowDate;

    @Inject
    private ThreadLocale threadLocale;

    @Property(write = false)
    @Persist
    private AccountView accountView;

    @Inject
    private Messages messages;

    public void setAccountView(AccountView accountView) {
        this.accountView = accountView;
    }

    public void setupRender() {
        setTransactionViewDate();
        if (accountView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }

    private void setTransactionViewDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
        nowDate = sdf.format(new Date());
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return DeactivateCardInput.class;
    }
}
