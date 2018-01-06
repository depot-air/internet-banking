package com.dwidasa.ib.pages.administration;

import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.SmsRegistrationView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.SMSService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created with IntelliJ IDEA.
 * User: fatakhurah-NB
 * Date: 29/10/12
 * Time: 17:23
 * To change this template use File | Settings | File Templates.
 */


public class SMSRegistrationInput
{
    @Property
    @Persist
    private SmsRegistrationView smsRegistrationView;

    @Property
    private List<String> accountModel;

    @Inject
    private SessionManager sessionManager;

    @Property
    private String senderName;

    @Property
    private String tinNoConfirm;

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Inject
    private CacheManager cacheManager;

    @Property
    private int tokenType;

    @Inject
    private SMSService smsService;

    @InjectPage
    private SMSRegistrationReceipt smsRegistrationReceipt;

    @Inject
    private OtpManager otpManager;

    public void onPrepare() {
       
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    void setTokenType()
    {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public void setupRender()
    {
        setTokenType();
        smsRegistrationView = new SmsRegistrationView();
        senderName =  sessionManager.getLoggedCustomerView().getName();
        accountModel = sessionManager.getAccountNumber();


    }

    public void onValidateFromForm()
    {
        if(!smsRegistrationView.getTinUssd().equals(tinNoConfirm))
            form.recordError(message.get("noTinNotSame"));
        if(!form.getHasErrors() )
        {
            setData();
            try
            {
                smsRegistrationView = smsService.executeSMSRegistration(smsRegistrationView) ;
            }
            catch(BusinessException e)
            {
                form.recordError(e.getFullMessage());
                e.printStackTrace();
            }
        }
    }

    private void setData()
    {
        smsRegistrationView.setTransactionType(Constants.SMS_REGISTRATION.TRANS_TYPE);
        smsRegistrationView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        smsRegistrationView.setMerchantType(sessionManager.getDefaultMerchantType());
        smsRegistrationView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        smsRegistrationView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        smsRegistrationView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        smsRegistrationView.setReferenceName(sessionManager.getLoggedCustomerView().getName());
        smsRegistrationView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
    }


    public Object onSuccess()
    {
        smsRegistrationReceipt.setSMSRegistrationView(smsRegistrationView);
        return smsRegistrationReceipt;
    }

}
