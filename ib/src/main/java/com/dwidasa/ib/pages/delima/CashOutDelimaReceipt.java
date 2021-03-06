package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.ib.Constants;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/25/11
 * Time: 00:03 am
 */
public class CashOutDelimaReceipt {
    @Property
    private String status;
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private String typeTransfer;
    @Property
    private String nowDate;
    @InjectPage
    private CashOutDelimaInput cashOutDelimaInput;
    @Persist
    private CashOutDelimaView cashOutDelimaView;
    @Inject
    private Messages messages;
     @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());


    public void setupRender() {

        if (cashOutDelimaView.getResponseCode() != null && cashOutDelimaView.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

        SimpleDateFormat sdf = new SimpleDateFormat();
        nowDate = sdf.format(new Date());
        nowDate = nowDate + " WIB";

    }

    public Object onActivate() {
        if (cashOutDelimaView == null) {
            return cashOutDelimaInput;
        }
        return null;
    }
    
    @Persist
    private boolean fromHistory;
    
    public void setFromHistory(boolean fromHistory) {
    	this.fromHistory = true;
    }
    
    public boolean isFromHistory() {
    	return fromHistory;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return cashOutDelimaInput;
    }

    public CashOutDelimaView getCashOutDelimaView() {
        return cashOutDelimaView;
    }

    public void setCashOutDelimaView(CashOutDelimaView cashOutDelimaView) {
        this.cashOutDelimaView = cashOutDelimaView;
    }

}
