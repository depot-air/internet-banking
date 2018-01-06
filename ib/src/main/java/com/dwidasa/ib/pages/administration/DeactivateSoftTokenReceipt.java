package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/8/11
 * Time: 00:26 am
 */

public class DeactivateSoftTokenReceipt {
     @Property
    private String nowDate;
    @Property
    private String status;
    @Persist
    private ResultView resultView;
    @Inject
    private Messages messages;
    @Inject
    private SessionManager sessionManager;


    void setupRender() {
            if (resultView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        setTransactionViewDate();
    }

    public String getUserId(){
        return sessionManager.getLoggedCustomerView().getUsername();
    }

    private void setTransactionViewDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd MMMM yyyy / hh:mm:ss");
        nowDate = sdate.format(date);
        nowDate = nowDate + " WIB";
    }

    public ResultView getResultView() {
        return resultView;
    }

    public void setResultView(ResultView resultView) {
        this.resultView = resultView;
    }
}
