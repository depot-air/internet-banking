package com.dwidasa.ib.pages.payment;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:43
 */
public class WaterPaymentConfirm {
    @Persist
    private WaterPaymentView waterPaymentView;
    @Property
    private int tokenType;
    @InjectPage
    private WaterPaymentReceipt waterPaymentReceipt;
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
    
    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (waterPaymentView == null) {
            return WaterPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(WaterPaymentConfirm.class.toString());
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
                    otpManager.validateToken(waterPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            setWaterPaymentView((WaterPaymentView) paymentService.execute(getWaterPaymentView()));
        	}
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
        if (getWaterPaymentView().getSave() && waterPaymentView.getInputType().equalsIgnoreCase("M")) {
            try {
                paymentService.register(getWaterPaymentView().transform());
            } catch (BusinessException e) {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }
    
    @DiscardAfter
    public Object onSuccess() {
        getWaterPaymentView().setTransactionDate(new Date());
        waterPaymentReceipt.setWaterPaymentView(waterPaymentView);
        return waterPaymentReceipt;
    }

    public WaterPaymentView getWaterPaymentView() {
        return waterPaymentView;
    }

    public void setWaterPaymentView(WaterPaymentView waterPaymentView) {
        this.waterPaymentView = waterPaymentView;
    }
    
    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }
    
    public String getBillerName(){
    	if(waterPaymentView.getBillerName().equals(Constants.WATER.BILLER_NAME.PAMSurabaya)){
    		return "PAM Surabaya";
    	}else
    		return "";
    }
    
    
    public double getRetribusi(){
    	return Double.parseDouble(waterPaymentView.getReserved2());
    }
    
    public BigDecimal getJmlTagihan(){
//    	return waterPaymentView.getJmlTagihan();
        return null;
    }
    
}
