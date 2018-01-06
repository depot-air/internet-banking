package com.dwidasa.ib.pages.payment;

import java.text.NumberFormat;

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
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:33
 */
public class HpPaymentConfirm {
    @Property(write = false)
    @Persist
    private HpPaymentView hpPaymentView;

    @Property(write = false)    
    @Persist
    private TelkomPaymentView telkomPaymentView;

    @Property
    private int tokenType;

    @InjectPage
    private HpPaymentReceipt hpPaymentReceipt;

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
    
    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (hpPaymentView == null) {
            return HpPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(HpPaymentConfirm.class.toString());
        setTokenType();
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(hpPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {            	
            	if (hpPaymentView.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            	
            		telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
            	
            		telkomPaymentView.setBillerName(telkomPaymentView.getBillerName());
            		paymentService.execute(telkomPaymentView);
            		
            		hpPaymentView.setReferenceNumber(telkomPaymentView.getReferenceNumber());
            		
            	} else {
            		hpPaymentView.setTransactionType(Constants.TELCO_PAYMENT_CODE);
            		
            		paymentService.execute(hpPaymentView);
            	}
                
                if (hpPaymentView.getSave() && hpPaymentView.getInputType().equalsIgnoreCase("M")) {
                    paymentService.register(hpPaymentView.transform());
                }
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        hpPaymentReceipt.setHpPaymentView(hpPaymentView);
        return hpPaymentReceipt;
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setHpPaymentView(HpPaymentView hpPaymentView) {
        this.hpPaymentView = hpPaymentView;
    }

    public void setTelkomPaymentView(TelkomPaymentView telkomPaymentView) {
        this.telkomPaymentView = telkomPaymentView;
    }
}
