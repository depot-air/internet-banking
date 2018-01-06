package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.model.view.PlanePaymentView;
import com.dwidasa.ib.Constants;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 11/08/11
 * Time: 13:19
 */
public class PlanePaymentReceipt {

    @Persist
    private PlanePaymentView planePaymentView;
    @Property
    private BigDecimal total;
    @Property
    private String dateTime;
    @Property
    private String status;
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(Locale.GERMAN);
    @Inject
    private Messages messages;

    public void setupRender() {
        setPaymentViewDate();
        getPlanePaymentView().setResponseCode("00");
        if (getPlanePaymentView().getResponseCode().equals(Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }

    public Object onActivate() {
        if (planePaymentView == null) {
            return PlanePaymentInput.class;
        }
        return null;
    }

    public PlanePaymentView getPlanePaymentView() {
        return planePaymentView;
    }

    public void setPlanePaymentView(PlanePaymentView planePaymentView) {
        this.planePaymentView = planePaymentView;
    }

    private void setPaymentViewDate() {
        SimpleDateFormat sdate = new SimpleDateFormat("dd MMMM yyyy / hh:mm:ss");
        dateTime = sdate.format(planePaymentView.getTransactionDate());
        dateTime = dateTime + " WIB";
    }

    @DiscardAfter
    public Object onSelectedFromBack(){
        return PlanePaymentInput.class;
    }
}
