/**
 * 
 */
package com.dwidasa.ib.pages.payment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.ColumbiaPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.SessionManager;

/**
 * @author dsi-bandung
 *
 */
public class ColumbiaPaymentInput {
	private static Logger logger = Logger.getLogger( ColumbiaPaymentInput.class );
	
	@Inject
	private SessionManager sessionManager;

	@Property
	private String tinNoConfirm;

	@Property
	private boolean saveBoxValue;
	
	@Property
    private String customerReference1;

    @Property
    private String customerReference2;
	
	@Property
	private List<String> accountModel;

	@Inject
	private CacheManager cacheManager;

	@Property
	private String chooseValue;

	@InjectComponent
	private Form form;
	
	@Property
	private List<String> dateList;
	
	@Property
	@Persist
	private ColumbiaPaymentView columbiaPaymentView;
	
	@InjectPage
	private ColumbiaPaymentConfirm columbiaPaymentConfirm;

	@Inject
	private Messages message;

	@Property
    private int tokenType;
	
	@Inject
    private CustomerRegisterService customerRegisterService;
	
	@Inject
    private PaymentService paymentService;
	
	@Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel providerModel;

    @Property
    private SelectModel customerReferenceModel;
	
	
	
	

    public Object onActivate() {
    	columbiaPaymentView = new ColumbiaPaymentView();
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }


    void setTokenType() {
    	try{
    		FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
    		tokenType = formConfig.getTokenType();
    	}catch(Exception e){
    		System.out.println(e.getMessage());
    	}
    }

    public void setupRender() {
    	setTokenType();
    	accountModel = sessionManager.getAccountNumber();
    }

    private void setPaymentView() {
        columbiaPaymentView.setTransactionDate(new Date());
        columbiaPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        columbiaPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
        columbiaPaymentView.setAmount(BigDecimal.ZERO);
        columbiaPaymentView.setFee(BigDecimal.ZERO);
        columbiaPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        columbiaPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        columbiaPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        columbiaPaymentView.setToAccountType("00");
        columbiaPaymentView.setCardNumber(sessionManager.getCardNumber(columbiaPaymentView.getAccountNumber()));
        columbiaPaymentView.setProviderCode("A004");


        
    }

    public void onValidateFromForm() {
    	if (columbiaPaymentView.getCustomerReference() == null) {
    		form.recordError(message.get("customerReference1-requiredIf-message"));
    	}

        try {
            if (!form.getHasErrors()) {
                setPaymentView();
                
                columbiaPaymentView.setTransactionType(Constants.COLUMBIA.INQ_COLUMBIA);
                paymentService.inquiry(columbiaPaymentView);
                columbiaPaymentView.setTransactionType(Constants.COLUMBIA.PAYMENT_COLUMBIA);
                paymentService.confirm(columbiaPaymentView);
            }
        } catch (BusinessException e) {
        	form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

    public Object onSuccess() {
        columbiaPaymentConfirm.setColumbiaPaymentView(columbiaPaymentView);
        return columbiaPaymentConfirm;
    }
	
	
	
	
	
}