package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.model.view.RefundDelimaView;
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

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 7/26/11
 * Time: 00:09 am
 */
public class RefundDelimaReceipt {
    @Property
    private String status;
    @Inject
    private ThreadLocale threadLocale;
    @Property
    private String typeTransfer;
    @InjectPage
    private RefundDelimaInput refundDelimaInput;
    @Persist
    private RefundDelimaView refundDelimaView;
    @Inject
    private Messages messages;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());


    public void setupRender() {

        if (getRefundDelimaView().getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }

    }

    public Object onActivate() {
        if (getRefundDelimaView() == null) {
            return refundDelimaInput;
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
        return refundDelimaInput;
    }


    public RefundDelimaView getRefundDelimaView() {
        return refundDelimaView;
    }

    public void setRefundDelimaView(RefundDelimaView refundDelimaView) {
        this.refundDelimaView = refundDelimaView;
    }
}
