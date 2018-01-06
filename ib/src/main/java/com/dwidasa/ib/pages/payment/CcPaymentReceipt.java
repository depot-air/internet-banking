package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.model.view.CcPaymentView;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 22/07/11
 * Time: 15:58
 */
public class CcPaymentReceipt {
    @Persist
    private CcPaymentView ccPaymentView;
    @Property
    private String status;
    @Inject
    private Messages messages;
    @Inject
    private ComponentResources componentResources;

    public Object onActivate() {
        if (ccPaymentView == null) {
            return CcPaymentInput.class;
        }
        return null;
    }

    public void setupRender(){
        ccPaymentView.setResponseCode("00");
        if (ccPaymentView.getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        }
        else {
            status = messages.get("failed");
        }
    }

    public CcPaymentView getCcPaymentView() {
        return ccPaymentView;
    }

    public void setCcPaymentView(CcPaymentView ccPaymentView) {
        this.ccPaymentView = ccPaymentView;
    }

    @DiscardAfter
    public Object onSelectedFromBack(){
         return CcPaymentInput.class;
    }
}
