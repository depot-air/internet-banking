package com.dwidasa.ib.pages.payment;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.LoanPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:47
 */
public class LoanPaymentConfirm {
    @Persist
    private LoanPaymentView loanPaymentView;
    @Property
    private int tokenType;
    @InjectPage
    private LoanPaymentReceipt loanPaymentReceipt;
    @Inject
    private PaymentService paymentService;
    @Inject
    private CacheManager cacheManager;
    @InjectComponent
    private Form form;
    @Inject
    private Messages messages;
    @Property
    private TokenView tokenView;
    @Inject
    private OtpManager otpManager;

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (loanPaymentView == null) {
            return LoanPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
        setTokenType();
    }

    void onValidateFromForm() {
        if (tokenView.getToken() == null) {
            form.recordError(messages.get("nullTokenError"));
        } else if (!tokenView.getToken().matches("[0-9]+")) {
            form.recordError(messages.get("formatTokenError"));
//        } else if (tokenView.getToken().length() != 8) {
//            form.recordError(messages.get("lengthTokenError"));
        }        
        try {
        	if (!form.getHasErrors() &&
                    otpManager.validateToken(loanPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            setLoanPaymentView((LoanPaymentView) paymentService.execute(getLoanPaymentView()));
        	}
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
        if (getLoanPaymentView().getSave() && loanPaymentView.getInputType().equalsIgnoreCase("M")) {
            try {
                paymentService.register(getLoanPaymentView().transform());
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    @DiscardAfter
    public Object onSuccess() {
            getLoanPaymentView().setTransactionDate(new Date());
            loanPaymentReceipt.setLoanPaymentView(loanPaymentView);
            return loanPaymentReceipt;
    }

    @DiscardAfter
    public Object onSelectedFromCancel() {
        return LoanPaymentInput.class;
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }


    public LoanPaymentView getLoanPaymentView() {
        return loanPaymentView;
    }

    public void setLoanPaymentView(LoanPaymentView loanPaymentView) {
        this.loanPaymentView = loanPaymentView;
    }
}
