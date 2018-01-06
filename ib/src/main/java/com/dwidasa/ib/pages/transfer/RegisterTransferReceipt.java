package com.dwidasa.ib.pages.transfer;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.service.facade.TransferService;
import com.dwidasa.ib.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/07/11
 * Time: 11:06
 */
public class RegisterTransferReceipt {
    @Persist
    private TransferView transferView;
    @Property
    private String status;
    @Property
    private String nowDate;
    @Inject
    private TransferService transferService;
    @Inject
    private Messages messages;
    @Inject
    private ThreadLocale threadLocale;
    @Inject
    private SessionManager sessionManager;

    public Object onActivate(){
        if(transferView == null){
            return RegisterTransferInput.class;
        }
        return null;
    }

    void setupRender() {
        sessionManager.setSessionLastPage(RegisterTransferReceipt.class.toString());
        sessionManager.setSmsTokenSent(false);

        if (transferView.getResponseCode() != null && transferView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
        setTransactionViewDate();
    }

    private void setTransactionViewDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
        nowDate = sdf.format(new Date());
        transferView.setTransactionDate(new Date());
        transferView.setValueDate(new Date());
    }

    public TransferView getTransferView() {
        return transferView;
    }

    public void setTransferView(TransferView transferView) {
        this.transferView = transferView;
    }
}
