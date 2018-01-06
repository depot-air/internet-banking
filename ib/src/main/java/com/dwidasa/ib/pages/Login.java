package com.dwidasa.ib.pages;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.CustomerAccountView;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.service.facade.WebAuthenticationService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.pages.eula.EulaAcceptance;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/13/11
 * Time: 12:30 PM
 */
@Import(library = {"context:bprks/js/jquery-1.6.2.min.js", "context:bprks/js/jquery.js",
        "context:bprks/js/script.js", "context:bprks/js/layout.js", "context:bprks/js/validation.js", "context:bprks/js/timeDate.js",
        "context:bprks/js/zone-overlay.js"}, stylesheet={"context:bprks/css/zone-overlay.css", "context:bprks/css/reset.css", "context:bprks/css/text.css", "context:bprks/css/960.css",
		"context:bprks/css/style.css"})
@PublicPage
public class Login {
    public static final String LOGGED_CUSTOMER_VIEW = "com.dwidasa.ib.pages.Login.CustomerView";
    public static final String LOGGED_CUSTOMER = "com.dwidasa.ib.pages.Login.Customer";
    public static final String LOGGED_FAILED_AUTH = "com.dwidasa.ib.pages.Login.FailedAuth";
    public static final String LOGGED_NOT_ACTIVATED_YET = "com.dwidasa.ib.pages.Login.NotActivatedYet";
    
    public static final String LOGGED_CUSTOMER_ACCOUNT = "com.dwidasa.ib.pages.Login.CustomerAccount";
//    public static final String MERCHANT_EDC = "1571";
//    public static final String MERCHANT_EDC2 = "1572";
//    public static final String HIPERWALLET = "1575";
    
    @Property
    private String username = "";

    @Property
    private String password = "";

    @Persist("flash")
    @Property
    private String error;

    @SessionAttribute(Constants.SELECTED_LANGUAGE)
    @Property
    private String selectedLanguage;

    @Component
    private Form loginForm;

    @Inject
    private Messages messages;

    @Inject
    private Request request;

    @Inject
    private WebAuthenticationService authenticator;

    @Inject
    private CustomerService customerService;

    @Inject
    private CustomerDeviceService customerDeviceService;

    @Inject
    private CustomerDeviceDao customerDeviceDao;

    @Inject
    private LoggingService loggingService;

    @Inject
    private CustomerDao customerDao;
    
    @Inject
    private CustomerAccountDao customerAccountDao;

    @Inject
    private ExtendedProperty extendedProperty;
    
    @Inject
    private PaymentService paymentService;
    
    @Inject
    private SessionManager sessionManager;

    public Object onSuccess() {

        String errorCode = null;
        String errorMessage = null;
        CustomerView customerView = null;
        if (username == null || username.equals("")) {
        	error = messages.get("username-required-message");
        	return this;
        } else if (password == null || password.equals("")) {
        	error = messages.get("password-required-message");
        	return this;
        } else {
            try {
            	
            	
            	customerView = authenticator.authenticate(username, password);
//            	setDataCustomerAccount(customerView);
            	
            } catch (BusinessException e) {
                errorCode = e.getErrorCode();
                errorMessage = e.getFullMessage();
                Customer customer = customerDao.getWithDefaultAccount(username.toUpperCase());
                if (e.getErrorCode().equals("IB-1000")) {
                	loggingService.logActivity(customer.getId(), com.dwidasa.engine.Constants.LOGIN_TYPE, "Login IB gagal, salah password, IP = " + EngineUtils.getIpPublic() , "", com.dwidasa.engine.Constants.MERCHANT_TYPE.IB, com.dwidasa.engine.Constants.TERMINALID_DEFAULT.IB);
                } else if(e.getErrorCode().equals("IB-1015")) {
                	loggingService.logActivity(customer.getId(), com.dwidasa.engine.Constants.LOGIN_TYPE, "Login IB gagal, User tidak aktif, IP = " + EngineUtils.getIpPublic() , "", com.dwidasa.engine.Constants.MERCHANT_TYPE.IB, com.dwidasa.engine.Constants.TERMINALID_DEFAULT.IB);
                }
            }

            if (errorCode != null) {
                error = errorMessage != null ? errorMessage : messages.get("error.login");
            }
            else {
                request.getSession(true).setAttribute(LOGGED_CUSTOMER_VIEW, customerView);
                Customer customer = customerService.get(customerView.getId());
                request.getSession(true).setAttribute(LOGGED_CUSTOMER, customer);
                request.getSession(true).setAttribute(LOGGED_FAILED_AUTH, 0);

                //Eula for the first time login after IB registration
                boolean isNotActivatedYet = customerDeviceService.isIbFirstLogin(customerView.getId());
                if(customerView.getAccountNumber().startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC1) || customerView.getAccountNumber().startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC2) || customerView.getAccountNumber().startsWith(com.dwidasa.ib.Constants.HIPERWALLET)){
                	isNotActivatedYet = false;
                	request.getSession(true).setAttribute(LOGGED_NOT_ACTIVATED_YET, isNotActivatedYet);
               	}else{
//               		isNotActivatedYet = true;
                    request.getSession(true).setAttribute(LOGGED_NOT_ACTIVATED_YET, isNotActivatedYet);

                    if (isNotActivatedYet) {
                    	return EulaAcceptance.class;
                    }
               	}

//                System.out.println("isNotActive : " +isNotActivatedYet);
                request.getSession(true).setAttribute(Constants.LASTPAGE_SESSION, Login.class.toString());
                request.getSession(true).setAttribute("customerId", customerView.getId());
                //lihat di applicationContext-resources.xml, serverType = 3, defaultTerminalId=KSK, defaultMerchantType=6015, migration=false
//                CustomerDevice customerDevice2 = customerDeviceService.getDeviceSoftToken(customerView.getId());
//                if(customerDevice2 != null){
//                	request.getSession(false).setAttribute(Constants.SESSION.TOKEN_TYPE, Constants.TOKEN_SOFTTOKEN);
//            	}else{
//                if (customerView.getTerminalId().equals(extendedProperty.getDefaultTerminalId())) {
//                	request.getSession(false).setAttribute(Constants.SESSION.TOKEN_TYPE, Constants.TOKEN_SMS_TOKEN);
//
//                } else {
//
//                    if (customerView.getTerminalId() != null && customerView.getTerminalId().length() == 8) {
//	                	request.getSession(false).setAttribute(Constants.SESSION.TOKEN_TYPE, Constants.TOKEN_RESPONSE_TOKEN);
//	                }
//                }
//            	}
            }
        }
        //user merchant EDC, hyperwalet dan CVPATRIO (3000113122)
        if(customerView != null && customerView.getAccountNumber() != null &&
                (customerView.getAccountNumber().startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC1) ||
                customerView.getAccountNumber().startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC2) ||
                customerView.getAccountNumber().startsWith(com.dwidasa.ib.Constants.HIPERWALLET) ||
                customerView.getAccountNumber().equals("3000113122")
                )
        ){
        	return indexHiperwallet.class;
        }
        else{
        	 return Index.class;
        }
    }

    

	private void setDataCustomerAccount(CustomerView customerView) {
		
		CustomerAccountView accountView = new CustomerAccountView();
    	CustomerAccount customerAccount = customerAccountDao.get(customerView.getId(), customerView.getAccountNumber());
    	//System.out.println("HOHOHOHO "+customerView.getAccountNumber());
    	accountView.setTransactionType(com.dwidasa.engine.Constants.MULTI_REKENING);
    	accountView.setAccountType("1");
    	accountView.setCardNumber(customerAccount.getCardNumber());
    	accountView.setToAccountType("00");
    	accountView.setDigitNoKartu(customerAccount.getAccountNumber());
    	accountView.setCurrencyCode(Constants.CURRENCY_CODE);
    	accountView.setProviderCode("A004");
    	accountView.setTerminalId(customerView.getTerminalId());
    	accountView.setMerchantType("6015");
    	accountView.setCustomerId(customerView.getId()); 
    	paymentService.inquiry(accountView);
    	
    	//customerAccountDao.deleteCustomerAccountsCartNumber(customerView.getId(), customerView.getAccountNumber());
    	List<CustomerAccount> accounts = customerAccountDao.getAllNoDefault(customerView.getId(), customerView.getAccountNumber());
    	for(CustomerAccount account : accounts){
    		System.out.println("ID "+account.getId());
    		customerAccountDao.deleteCustomerAccountsPerId(account.getId());
    	}
    	
    	for(int i=0; i<accountView.getAccountViews().size(); i++){
    		
    		if(!accountView.getAccountViews().get(i).getDigitRekening().equals(customerView.getAccountNumber()) && 
    		   !accountView.getAccountViews().get(i).getDigitNoKartu().equals("0000000000000000")){
    			
    			CustomerAccount account = new CustomerAccount();
        		account.setAccountTypeId((long)1);
        		account.setCurrencyId((long)1);
        		account.setCustomerId(customerView.getId());
        		account.setProductId((long)28);
        		account.setAccountNumber(accountView.getAccountViews().get(i).getDigitRekening());
        		//account.setCardNumber(EngineUtils.encrypt(com.dwidasa.engine.Constants.SERVER_SECRET_KEY, 
        		//									accountView.getAccountViews().get(i).getDigitNoKartu()));
        		account.setCardNumber(accountView.getAccountViews().get(i).getDigitNoKartu());
        		account.setIsDefault("N");
        		account.setStatus(1);
        		account.setCreated(new Date());
        		account.setCreatedby(customerView.getId());
        		account.setUpdated(new Date());
        		account.setUpdatedby(customerView.getId());
        		customerAccountDao.save(account);
    		}
    	}
    	
    	
	}



	public Object onFailure(){

        error = messages.get("error-kaptcha");

        return this;
    }

    public List<String> getAvailableLanguages() {
        return Arrays.asList(messages.get("indonesian"), messages.get("english"));
    }
    
    Object onActivate() {
    	//this.updateCardNumber();
    	String err = request.getParameter("err");
    	if("screenTimeout".equals(err)) {
    		error = messages.get("timeoutMessage");
    		if (request.getSession(false) != null) {
    			request.getSession(false).setAttribute("currentPage", "LOGIN");
    		}
    		return Login.class;
    	}
    	return null;
    }
    
//	private void updateCardNumber() {
//
//		List<CustomerAccount> customerAccounts = customerAccountDao
//				.getAllCardNumberCustomer();
//
//		for (CustomerAccount account : customerAccounts) {
//			try {
//				if (account.getCardNumber().length() == 16) {
//					System.out.println("Card Number " + account.getCardNumber());
//					customerAccountDao
//							.updateEncriptCardNumber(
//									account.getId(),
//									EngineUtils
//											.encrypt(
//													com.dwidasa.engine.Constants.SERVER_SECRET_KEY,
//													account.getCardNumber()));
//				}
//			} catch (Exception e) {
//				System.out.println("Errr " + e.toString());// TODO: handle
//															// exception
//			}
//
//		}
//
//	}

    
    public String getCurrentTime() {
    	return String.valueOf(System.currentTimeMillis());
    }
}
