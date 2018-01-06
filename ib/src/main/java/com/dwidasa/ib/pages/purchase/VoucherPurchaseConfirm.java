package com.dwidasa.ib.pages.purchase;

import java.text.NumberFormat;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.kaptcha.components.KaptchaField;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.services.AccountResource;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/27/11
 * Time: 10:24 AM
 */
public class VoucherPurchaseConfirm {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private int tokenType;

    @Property(write = false)
    @Persist
    private VoucherPurchaseView voucherPurchaseView;

    @Inject
    private PurchaseService purchaseService;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private SessionManager sessionManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Property
    private TokenView tokenView;

    @InjectPage
    private VoucherPurchaseReceipt voucherPurchaseReceipt;

    @Inject
	private ParameterDao parameterDao;

    @Inject
    private ComponentResources componentResources;
    
    @Inject
    private AccountResource accountResource;

    @Component(id="kaptchaField")
    KaptchaField kaptchaField;
    
    public Long getDenomination() {
        return Long.valueOf(voucherPurchaseView.getDenomination());
    }

    public void setVoucherPurchaseView(VoucherPurchaseView voucherPurchaseView) {
        this.voucherPurchaseView = voucherPurchaseView;
    }

    public void setupRender() {
    	System.out.println("Card Number Confirm "+voucherPurchaseView.getCardNumber());
    	System.out.println("Ip Server "+voucherPurchaseView.getIp());
    	setTokenType();
    }

    void onPrepare() {
    	sessionManager.setSessionLastPage(VoucherPurchaseConfirm.class.toString());
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (voucherPurchaseView == null) {
            return VoucherPurchaseInput.class;
        }

        return null;
    }
    
    public void onValidateFromForm() {

        //form.clearErrors();



        
    }
    
    public Object onFailure(){
    	String em = form.getDefaultTracker().getError(kaptchaField);
    	if (em == null) em = "";
    	
        if (!em.isEmpty()) { 
        	//System.out.println("Clearing errors... utk mengganti error msg");
        	
        	form.clearErrors();
        	form.recordError(messages.get("tapestry-incorrect-captcha"));
        }

    	//if (!form.getHasErrors()){
        //    form.recordError(messages.get("tapestry-incorrect-captcha"));
        //}

        return null;
    }

    //@DiscardAfter
    public Object onSuccess() {

        form.clearErrors();

        try {
            if (otpManager.validateToken(voucherPurchaseView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
                purchaseService.execute(voucherPurchaseView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }

        if (form.getHasErrors()){
            return null;
        }

        voucherPurchaseReceipt.setVoucherPurchaseView(voucherPurchaseView);
        componentResources.discardPersistentFieldChanges();
        return voucherPurchaseReceipt;
    }

    public boolean isMerchant() {    	
//    	String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
//    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
//    	String[] tokens = ip.getParameterValue().split(",");
//    	boolean isMerch = false;
//    	if (tokens.length > 0 ) {
//    		for(int i = 0; i < tokens.length; i++) {
//    			if (firstFour.equals(tokens[i]))
//    				isMerch = true;
//    		}
//    	}
    	return sessionManager.isMerchant();
    }
    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public boolean isProductUseCaptcha() {    	
    	boolean isUseCaptcha = false;

    	String pcode = voucherPurchaseView.getProductCode();
    	if (isMerchant() && pcode.equalsIgnoreCase(Constants.PREPAID_PRODCODE.FINNET_TELKOMSEL) )
    		isUseCaptcha = true;
    		
    	return isUseCaptcha;
    }
}
