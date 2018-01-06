package com.dwidasa.ib.pages.eula;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.dwidasa.engine.dao.IbMerchantDao;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.IbMerchant;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.administration.ActivateSoftTokenInput;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: ib
 * Date: 10/13/11
 * Time: 8:49 PM
 */
public class EulaActivateTransaction {
    @InjectComponent
    private Form form;

	@InjectPage
	private ActivateSMSToken activateSMSToken;

	@InjectPage
	private ActivateSoftTokenInput activateSoftTokenInput;

	@InjectPage
	private ActivateHardToken activateHardToken;
	
	@Property
    @Persist
    private IndividualType individualType;
	
	@Property
    @Persist
    private SoftTokenType SoftTokenType;
	
	@Property
    @Persist
    private MerchantType merchantType;
	
	public enum IndividualType
	{
//	    SOFT_TOKEN, SMS_TOKEN
		SMS_TOKEN
	}
	
	public enum MerchantType
	{
//	    SOFT_TOKEN, HARD_TOKEN
		HARD_TOKEN
	}
	
	public enum SoftTokenType
	{
		SOFT_TOKEN
	}
	
    void beginRender() {
    		System.out.println("MenuPage : EulaActivateTransaction");
    }

    void onValidateFromForm() {

    }


    @DiscardAfter
    Object onAction() {
//    	if (individualType.equals(IndividualType.SMS_TOKEN)) {
//    		return activateSMSToken;
//    	}
//    	return activateSoftTokenInput;
    	if(isMerchantSoft()){
    		return activateSoftTokenInput;
    	}
    	else if(isMerchant()){
    		return activateHardToken;
    	}
    	else{
    		return activateSMSToken;
    	}
    }
    
    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private IbMerchantDao ibMerchantDao;
    
    @Inject
   	private ParameterDao parameterDao;
    
    @Inject
    private Request request;
    
    public boolean isMerchant() {   
    	boolean isMerch = false;
    	int isM = sessionManager.getTokenType();
    	String akunNumber = sessionManager.getLoggedCustomerView().getAccountNumber();
    	if (isM == Constants.TOKEN_RESPONSE_TOKEN || akunNumber.equals("1527290968")){
    		System.out.println("merchant");
    		isMerch = true;
    	}
    	return isMerch;
    }
    
    public boolean isMerchantSoft()
    {
    	IbMerchant ibMerchant = ibMerchantDao.getByCustomerId(sessionManager.getLoggedCustomerView().getId());
		if(ibMerchant != null){
			
			if(ibMerchant.getSoftToken().equals(Boolean.TRUE)){
				return true;
			}else{
				return false;
			}
			
		}else{
			
			return false;
			
		}
    }
    
}
