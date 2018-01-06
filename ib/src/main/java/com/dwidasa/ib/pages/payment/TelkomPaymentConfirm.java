package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:20
 */
public class TelkomPaymentConfirm {
    @Property(write = false)
    @Persist
    private TelkomPaymentView telkomPaymentView;

    @Property
    private int tokenType;

    @InjectPage
    private TelkomPaymentReceipt telkomPaymentReceipt;

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

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat dateList = new SimpleDateFormat("MMMyy", threadLocale.getLocale());
    
    public void setTelkomPaymentView(TelkomPaymentView telkomPaymentView) {
        this.telkomPaymentView = telkomPaymentView;
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (telkomPaymentView == null) {
            return TelkomPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(TelkomPaymentConfirm.class.toString());
    	setTokenType();
    }

    @DiscardAfter
    public Object onSuccess() {
        telkomPaymentReceipt.setTelkomPaymentView(telkomPaymentView);
        return telkomPaymentReceipt;
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(telkomPaymentView.getCustomerId(), this.getClass().getSimpleName(),
                    tokenView)) {
                paymentService.execute(telkomPaymentView);
            }
            if (telkomPaymentView.getSave() && telkomPaymentView.getInputType().equalsIgnoreCase("M")) {
                paymentService.register(telkomPaymentView.transform());
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
}
