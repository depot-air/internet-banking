package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.view.TransferView;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 24/10/12
 * Time: 4:09
 * To change this template use File | Settings | File Templates.
 */
public class TransferATMBStatusReceipt
{
    @Property
    @Persist
    private TransferView transferView;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    @Inject
    private Messages messages;

    @Property
    private String note1;

    @Property
    private String note2;

    void setupRender()
    {
        if(transferView.getTransactionStatus().toUpperCase().equals(com.dwidasa.engine.Constants.SUCCEED_STATUS))
        {
            note1 = messages.get("transferTransSuccess1");
            note2 = messages.get("transferTransSuccess2");
        }
        else
        if(transferView.getTransactionStatus().toUpperCase().equals(com.dwidasa.engine.Constants.PENDING_STATUS))
        {
            note1 = messages.get("transferTransPending1");
            note2 = messages.get("transferTransPending2");
        }
        //setTransactionViewDate();
    }

    public void setTransferInputsView(TransferView transferView) {
        this.transferView = transferView;

    }
    
    public TransferView getTransferInputsView() {
        return transferView;
    }
}
