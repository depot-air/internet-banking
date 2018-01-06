package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.model.view.LoanPaymentView;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 14:14
 */
public class LoanPaymentReceipt {
    @Persist
    private LoanPaymentView loanPaymentView;
    @Property
    private String status;
    @Inject
    private Messages messages;
    @Inject
    private ComponentResources componentResources;

    public Object onActivate() {
        if (loanPaymentView == null) {
            return LoanPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
        if (getLoanPaymentView().getResponseCode() != null && getLoanPaymentView().getResponseCode().equals(com.dwidasa.ib.Constants.SUCCESS_CODE)) {
            status = messages.get("success");
        } else {
            status = messages.get("failed");
        }
    }

    public LoanPaymentView getLoanPaymentView() {
        return loanPaymentView;
    }

    public void setLoanPaymentView(LoanPaymentView loanPaymentView) {
        this.loanPaymentView = loanPaymentView;
    }

    @DiscardAfter
    public Object onSelectedFromBack() {
        return LoanPaymentInput.class;
    }
}
