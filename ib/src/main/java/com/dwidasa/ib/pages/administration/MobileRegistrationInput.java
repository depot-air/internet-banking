package com.dwidasa.ib.pages.administration;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.ProductDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Product;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.MobileRegistrationView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.RegistrationService;
import com.dwidasa.engine.util.EngineUtils;
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


public class MobileRegistrationInput {
    private static Logger logger = Logger.getLogger( MobileRegistrationInput.class );
    @Property
    @Persist
    private MobileRegistrationView mobileRegistrationView;

    @Inject
    private SessionManager sessionManager;

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
    private RegistrationService registrationService;

    @Inject
    private CustomerAccountDao customerAccountDao;

    @Inject
    private ProductDao productDao;
    
    @InjectPage
    private MobileRegistrationConfirm mobileRegistrationConfirm;

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
        mobileRegistrationView = new MobileRegistrationView();
        setData();
    }

    public void onValidateFromForm()
    {
        CustomerView customerView = sessionManager.getLoggedCustomerView();
        Customer customer = sessionManager.getLoggedCustomerPojo();
        String customerPin = EngineUtils.decrypt(Constants.SERVER_SECRET_KEY, customer.getEncryptedCustomerPin());
        
        if(!mobileRegistrationView.getTinMobile().equals(customerPin)) {
            form.recordError(message.get("tinNo-regexp-message"));
        } else if(!mobileRegistrationView.getTinMobile().equals(tinNoConfirm)) {
            form.recordError(message.get("noTinNotSame"));
        }
        
        if(!form.getHasErrors() )
        {
            try
            {
                //007171782400F5C359BB2FE862787907270166870727169598087822908909   087822908909        087822908909        010727169598SHELVAN NOVIANA               16002                          
                //00717178
                //2400F5C359BB2FE8
                //6278790727016687
                //0727169598
                //087822908909   
                //087822908909  X
                //087822908909  X
                //010727169598SHELVAN NOVIANA               16002
                
                
            	String encryptedTin = EngineUtils.getEncryptedPin(tinNoConfirm, customerView.getCardNumber());
                String customData = StringUtils.rightPad(customer.getCifNumber(), 8, " ");
                customData += StringUtils.rightPad(encryptedTin, 16, " ");
                customData += StringUtils.rightPad(customerView.getCardNumber(), 16, " ");
                customData += StringUtils.rightPad(customerView.getAccountNumber(), 10, " ");
                customData += StringUtils.rightPad(mobileRegistrationView.getCustomerReference(), 15, " ");
                customData += StringUtils.rightPad(mobileRegistrationView.getCustomerReference(), 20, " ");
                customData += StringUtils.rightPad(mobileRegistrationView.getCustomerReference(), 20, " ");
                customData += StringUtils.rightPad("01", 2, " ");
                customData += StringUtils.rightPad(customerView.getAccountNumber(), 10, " ");
                customData += StringUtils.rightPad(customer.getCustomerName(), 30, " ");

                CustomerAccount custAccount = customerAccountDao.getDefaultWithType(customer.getId());
                Product product = productDao.get(custAccount.getProductId()); 
                customData += StringUtils.rightPad("" + custAccount.getAccountTypeId(), 1, " ");
                customData += StringUtils.rightPad(product.getProductCode(), 30, " ");

                System.out.println("customData=" + customData);
                logger.info("customData=" + customData);
                mobileRegistrationView.setBit48(customData);
                
//            	Transaction transaction = TransformerFactory.getTransformer(mobileRegistrationView).transformTo(mobileRegistrationView, new Transaction());
//            	registrationService.registerEBanking(transaction);
                //mobileRegistrationView = smsService.executeSMSRegistration(mobileRegistrationView) ;
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
        mobileRegistrationView.setTransactionType(Constants.REGISTRATION_CODE);
        mobileRegistrationView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        mobileRegistrationView.setMerchantType(sessionManager.getDefaultMerchantType());
        mobileRegistrationView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        mobileRegistrationView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        mobileRegistrationView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        mobileRegistrationView.setReferenceName(sessionManager.getLoggedCustomerView().getName());        
        mobileRegistrationView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
//        mobileRegistrationView.setCustomerReference(sessionManager.getLoggedCustomerView().getPhone());
    }


    public Object onSuccess()
    {
        mobileRegistrationConfirm.setMobileRegistrationView(mobileRegistrationView);
        return mobileRegistrationConfirm;
    }

}
