package com.dwidasa.ib.pages.payment;

import java.util.Date;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.KartuKreditBNIPaymentView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 12:29
 */
@Import(library = {"context:bprks/js/payment/TelkomPaymentInput.js"})
public class KartuKreditBNIPaymentInput {
    @Persist
    @Property
    private KartuKreditBNIPaymentView kartuKreditBNIPaymentView;

    @Property
    private int tokenType;

    @InjectPage
    private KartuKreditBNIPaymentConfirm kartuKreditBNIPaymentConfirm;
    
    @Inject
	private ParameterDao parameterDao;
    

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Inject
    private OtpManager otpManager;

    @Property
    private String chooseValue;

    @Property
    private boolean saveBoxValue;

    @Property
    private String customerReference1;

    @Property
    private String customerReference2;

    @Property
    private SelectModel providerModel;

    @Property
    private SelectModel customerReferenceModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private PaymentService paymentService;

    @Inject
    private SessionManager sessionManager;

    private void buildCustomerReferenceModel() {
        customerReferenceModel = genericSelectModelFactory.create(
                paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                		kartuKreditBNIPaymentView.getTransactionType(), null));
    }


    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void onPrepare() {
       
    }

    public void setupRender() {
    	
    	if (!sessionManager.getSessionLastPage().equals(KartuKreditBNIPaymentInput.class.toString()) ) {
    		kartuKreditBNIPaymentView = null;
    	} else {
    		if (kartuKreditBNIPaymentView.getCustomerReference() != null && kartuKreditBNIPaymentView.getInputType().equals("M")) {
    			customerReference1 = kartuKreditBNIPaymentView.getCustomerReference();
    		}
    	}
    	sessionManager.setSessionLastPage(KartuKreditBNIPaymentInput.class.toString());
        chooseValue = "fromId";
        if (kartuKreditBNIPaymentView == null) {
        	kartuKreditBNIPaymentView = new KartuKreditBNIPaymentView();

        	kartuKreditBNIPaymentView.setTransactionType(Constants.PAYMENT_KARTU_KREDIT_BNI.PAYMENT_BNI);
        }
        buildCustomerReferenceModel();
    }

    private void setPaymentView() {
    	kartuKreditBNIPaymentView.setTransactionDate(new Date());
    	kartuKreditBNIPaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	kartuKreditBNIPaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
    	kartuKreditBNIPaymentView.setCardNumber(sessionManager.getCardNumber(kartuKreditBNIPaymentView.getAccountNumber()));

        kartuKreditBNIPaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
        kartuKreditBNIPaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        kartuKreditBNIPaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
        kartuKreditBNIPaymentView.setSave(saveBoxValue);
        kartuKreditBNIPaymentView.setToAccountType("00");

        if (chooseValue.equalsIgnoreCase("fromList")) {
        	kartuKreditBNIPaymentView.setInputType("L");
        	kartuKreditBNIPaymentView.setCustomerReference(customerReference2);
        	kartuKreditBNIPaymentView.setNomorKartuKredit(customerReference2);
        } else if (chooseValue.equalsIgnoreCase("fromId")) {
        	kartuKreditBNIPaymentView.setInputType("M");
            kartuKreditBNIPaymentView.setCustomerReference(customerReference1);
            kartuKreditBNIPaymentView.setNomorKartuKredit(customerReference1);
        }
        kartuKreditBNIPaymentView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
    }

    void onValidateFromForm() {
        if (chooseValue.equalsIgnoreCase("fromId")) {
            if (customerReference1 == null) {
                form.recordError(message.get("customerReference1-requiredIf-message"));
            }
        } else if (chooseValue.equalsIgnoreCase("fromList")) {
            if (customerReference2 == null) {
                form.recordError(message.get("customerReference2-requiredIf-message"));
            }
        }
        
        try {
        	
        	if (!isBinKartu(customerReference1, getBINKartuKredit())){
            	form.recordError("Nomor Kartu Tidak Valid");
            }
        	
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        	
        try {
            if (!form.getHasErrors() ) {
                setPaymentView();
            }
        } catch (BusinessException e) {
            //telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_CODE);
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }

    }

    public Object onSuccess() {
    	kartuKreditBNIPaymentConfirm.setKartuKreditBNIPaymentView(kartuKreditBNIPaymentView);
    	return kartuKreditBNIPaymentConfirm;
    }

    void pageReset() {
    }
    
    
    public String getBINKartuKredit() {    	
    	com.dwidasa.engine.model.Parameter parameter = parameterDao.get("BIN_KARTU_KREDIT");
    	return parameter.getParameterValue();
    }
    
    public boolean isBinKartu(String accountNumber, String accountNumberPac) {    	
    	String firstFive = accountNumber.substring(0, 6);
    	String[] PAC = accountNumberPac.split(",");
    	boolean isPAC = false;
    	if (PAC.length > 0 ) {
    		for(int i = 0; i < PAC.length; i++) {
    			if (firstFive.equals(PAC[i]))
    				isPAC = true;
    		}
    	}
    	return isPAC;
    }
    
    
}
