package com.dwidasa.ib.pages.administration;

import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.ib.Constants;
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
 * Time: 00:22 am
 */
public class ActivateSoftTokenReceipt {
    @Property
    private String nowDate;
    @Property
    private String status;
    @Persist
    private ResultView resultView;
    @Inject
    private Messages messages;

    void setupRender() {
            if (resultView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        setTransactionViewDate();
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
