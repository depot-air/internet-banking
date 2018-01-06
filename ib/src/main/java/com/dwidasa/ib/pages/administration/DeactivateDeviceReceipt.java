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
 * Date: 8/4/11
 * Time: 00:53 am
 */

public class DeactivateDeviceReceipt {

    @Property
    private String nowDate;
    @Persist
    private String deviceId;
    @Persist
    private ResultView resultView;
    @Property
    private String status;

    @Inject
    private Messages messages;


    private void setTransactionViewDate() {
        Date date = new Date();
        SimpleDateFormat sdate = new SimpleDateFormat("dd MMMM yyyy / hh:mm:ss");
        nowDate = sdate.format(date);
        nowDate = nowDate + " WIB";
    }

    void setupRender(){

          if (resultView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        setTransactionViewDate();
    }



    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ResultView getResultView() {
        return resultView;
    }

    public void setResultView(ResultView resultView) {
        this.resultView = resultView;
    }
}



