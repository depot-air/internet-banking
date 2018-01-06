package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
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
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TvPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 13:39
 */
public class TvPaymentConfirm {
	private static Logger logger = Logger.getLogger( TvPaymentConfirm.class );
    @Property(write = false)
    @Persist
    private TvPaymentView tvPaymentView;

    @Property(write = false)    
    @Persist
    private TelkomPaymentView telkomPaymentView;
    
    @Property
    private int tokenType;

    @InjectPage
    private TvPaymentReceipt tvPaymentReceipt;

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
    private DateFormat dateList = new SimpleDateFormat("MMMyy", threadLocale.getLocale());
    
    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (tvPaymentView == null) {
            return TvPaymentInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(TvPaymentConfirm.class.toString());
        setTokenType();
    }

    void onValidateFromForm() {
        try {        	
            if (!form.getHasErrors() && otpManager.validateToken(tvPaymentView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {                
	        	if (isFinnet()){	
	        		logger.info("telkomPaymentView.getAccountNumber()=" + telkomPaymentView.getAccountNumber());
	        		telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
	        		paymentService.execute(telkomPaymentView);
	        		
	        		tvPaymentView.setReferenceNumber(telkomPaymentView.getReferenceNumber());
	        		            		
	        	} else {
	        		setTvPaymentView((TvPaymentView) paymentService.execute(tvPaymentView));
	        	}
	
	            if (tvPaymentView.getSave() && tvPaymentView.getInputType().equalsIgnoreCase("M")) {
	                paymentService.register(tvPaymentView.transform());
	            }
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        tvPaymentView.setTransactionDate(new Date());
        tvPaymentReceipt.setTvPaymentView(tvPaymentView);
        return tvPaymentReceipt;
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setTvPaymentView(TvPaymentView tvPaymentView) {
        this.tvPaymentView = tvPaymentView;
    }

    public void setTelkomPaymentView(TelkomPaymentView telkomPaymentView) {
        this.telkomPaymentView = telkomPaymentView;
        logger.info("telkomPaymentView.getAccountNumber()=" + telkomPaymentView.getAccountNumber());
    }

    public boolean isFinnet() {
    	if (tvPaymentView.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE) && tvPaymentView.getBillerCode().equals(Constants.TELKOM_VISION)) {
    		return true;
    	}
    	return false;
    }
}
