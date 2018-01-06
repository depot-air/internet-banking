package com.dwidasa.ib.pages.administration;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.ib.pages.payment.TelkomPaymentConfirm;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

public class PhoneChangeConfirm {
	@Property(write = false)
    @Persist
    private CustomerView customerView;
	
	@Property
    private ResultView resultView;

    @Property
    private int tokenType;

    @InjectPage
    private PhoneChangeReceipt phoneChangeReceipt;

    @Inject
    private WebAdministrationService administrationService;

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
    private SessionManager sessionManager;

    
    public void setCustomerView(CustomerView customerView) {
        this.customerView = customerView;
    }

    public void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public Object onActivate() {
        if (customerView == null) {
            return PhoneChangeInput.class;
        }
        return null;
    }

    public void setupRender() {
    	sessionManager.setSessionLastPage(TelkomPaymentConfirm.class.toString());
    	setTokenType();
    }

    @DiscardAfter
    public Object onSuccess() {
        phoneChangeReceipt.setResultView(resultView);
        return phoneChangeReceipt;
    }

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(customerView.getId(), this.getClass().getSimpleName(),
                    tokenView)) {
            	resultView = administrationService.changeCustomerInfo(customerView, null, 2);
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
