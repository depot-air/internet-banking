package com.dwidasa.ib.pages.purchase;

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
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.view.MncLifePurchaseView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.AccountResource;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/27/11
 * Time: 10:24 AM
 */
public class MncLifePurchaseConfirm {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private int tokenType;

    @Property(write = false)
    @Persist
    private MncLifePurchaseView mncLifePurchaseView;

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
    private MncLifePurchaseReceipt mnclifepurchaseReceipt;

    @Inject
	private ParameterDao parameterDao;
    
    @Inject
    private AccountResource accountResource;
    
    public Long getDenomination() {
        return Long.valueOf(mncLifePurchaseView.getDenomination());
    }

    public void setVoucherPurchaseView(MncLifePurchaseView voucherPurchaseView) {
        this.mncLifePurchaseView = voucherPurchaseView;
    }

    public void setupRender() {
    	setTokenType();
    }

    void onPrepare() {
    	sessionManager.setSessionLastPage(MncLifePurchaseConfirm.class.toString());
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (mncLifePurchaseView == null) {
            return MncLifePurchaseView.class;
        }

        return null;
    }
    
    public String getFormatTgl(){
    	SimpleDateFormat format = new SimpleDateFormat(Constants.SHORT_FORMAT);
    	return format.format(mncLifePurchaseView.getTanggalLahir()).toString();
    }
    
    public String getJenisKelamin(){
    	String jenis = "";
    	if(mncLifePurchaseView.getJenisKelamin().equals("L")){
    		jenis = "Laki-laki";
    	}else{
    		jenis = "Perempuan";
    	}
    	return jenis;
    }
    
    public String getTipeDokumen(){
    	String tipe = "";
    	if(mncLifePurchaseView.getTipeDokumen().equals("P")){
    		tipe = "Polis";
    	}else{
    		tipe = "Ktp";
    	}
    	return tipe;
    }
    
    public void onValidateFromForm() {
        try {
            if (otpManager.validateToken(mncLifePurchaseView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
                purchaseService.execute(mncLifePurchaseView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }
    
    

    @DiscardAfter
    public Object onSuccess() {
        mnclifepurchaseReceipt.setMncLifePurchaseView(mncLifePurchaseView);
        mnclifepurchaseReceipt.setFromStatus(false);
        return mnclifepurchaseReceipt;
    }

    public boolean isMerchant() {    	
    	return sessionManager.isMerchant();
    }
    void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

}
