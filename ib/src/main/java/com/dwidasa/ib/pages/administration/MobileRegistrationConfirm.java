package com.dwidasa.ib.pages.administration;

import java.text.NumberFormat;
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
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.MobileRegistrationView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.service.facade.RegistrationService;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 29/10/12
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */


public class MobileRegistrationConfirm {
    private static Logger logger = Logger.getLogger( MobileRegistrationConfirm.class );
    @Property(write = false)
    @Persist
    private MobileRegistrationView mobileRegistrationView;

    @Property
    private int tokenType;
    
    @InjectPage
    private MobileRegistrationReceipt mobileRegistrationReceipt;
    
    @Inject
    private PaymentService paymentService;

    @Inject
    private CacheManager cacheManager;

    @InjectComponent
    private Form form;

    @Inject
    private Messages messages;

    @Inject
    private OtpManager otpManager;

    @Property
    private TokenView tokenView;

    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private RegistrationService registrationService;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (mobileRegistrationView == null) {
            return MobileRegistrationInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(MobileRegistrationConfirm.class.toString());
        setTokenType();
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(mobileRegistrationView.getCustomerId(), this.getClass().getSimpleName(), tokenView)) {
            	Transaction t = TransformerFactory.getTransformer(mobileRegistrationView).transformTo(mobileRegistrationView, new Transaction());
            	logger.info("mobileRegistrationView.getEncryptedTinMobile()=" + mobileRegistrationView.getEncryptedTinMobile());            	
            	try {
	            	registrationService.registerEBanking(t);
	            	t.setResponseCode(Constants.SUCCESS_CODE);
	            } catch (Exception e) {
	                t.setResponseCode("11");
	            }
	            String bit48 = t.getFreeData1();
	            logger.info("bit48 after=" + bit48);
	            mobileRegistrationView.setReferenceName(bit48.substring(65, 85).trim());
	            mobileRegistrationView.setActivationCode(bit48.substring(85, 105).trim());
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    @DiscardAfter
    public Object onSuccess() {
        mobileRegistrationView.setTransactionDate(new Date());
        mobileRegistrationReceipt.setMobileRegistrationView(mobileRegistrationView);
        return mobileRegistrationReceipt;
    }

    private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setMobileRegistrationView(MobileRegistrationView mobileRegistrationView) {
        this.mobileRegistrationView = mobileRegistrationView;
    }
}
