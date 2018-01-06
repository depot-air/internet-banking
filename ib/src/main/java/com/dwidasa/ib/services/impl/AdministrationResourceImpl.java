package com.dwidasa.ib.services.impl;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.ProductDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.Product;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.*;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.KioskAdministrationService;
import com.dwidasa.engine.service.facade.NativeAdministrationService;
import com.dwidasa.engine.service.facade.RegistrationService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.AdministrationResource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 11:25 AM
 */
@PublicPage
public class AdministrationResourceImpl implements AdministrationResource {
    private static Logger logger = Logger.getLogger(AdministrationResourceImpl.class);
    @Inject
    private NativeAdministrationService administrationService;

    @Inject
    private KioskAdministrationService kioskAdministrationService;

    @Inject
    private AccountService accountService;

    @Inject
    private CustomerDao customerDao;

    @Inject
    private CustomerAccountDao customerAccountDao;
    
    @Inject
    private ProductDao productDao;
    
    @Inject
    private RegistrationService registrationService;
    
    public AdministrationResourceImpl() {
    }

    public String changeCustomer(Long customerId, String deviceId, String sessionId, String token,
            Integer operation, String oldValue, String json) {
        return changeCustomerExtract(operation, oldValue, json);
    }

    public String changeCustomer2(Long customerId, String deviceId, String sessionId, String token,
            Integer operation, String oldValue, String json) {
        return changeCustomerExtract(operation, oldValue, json);
    }
    
    public String changeCustomerPost(Long customerId, String deviceId, String sessionId, String token, Integer operation, String oldValue, String json) {
        return changeCustomerExtract(operation, oldValue, json);
    }

    
    public String changeCustomerPostPin(Long customerId, String deviceId, String sessionId, String trackNo2, String oldValue, String newValue, String json) {
        AccountView av = PojoJsonMapper.fromJson(json, AccountView.class);
        //taruh dl trackNo2, pin, old pin temporary
        av.setCustomerName(trackNo2);
        av.setProductName(EngineUtils.getEncryptedPin(oldValue, av.getCardNumber()));    //PIN lama, utk bit 52
        av.setAccountName(EngineUtils.getEncryptedPin(newValue, av.getCardNumber()));    //PIN baru, utk bit 53

        av.setTransactionType("12");
        av.setAccountType("10");
        av = (AccountView) changePin(av);
        av.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(av);
    }

    private String changeCustomerExtract(Integer operation, String oldValue, String json) {
        CustomerView view = PojoJsonMapper.fromJson(json, CustomerView.class);
        ResultView resultView = administrationService.changeCustomerInfo(view, oldValue, operation);
        return PojoJsonMapper.toJson(resultView);
    }

    public String registerEmail(Long customerId, String deviceId, String activationCode, String email) {

        administrationService.registerEmail(customerId, deviceId, activationCode, email);
        return Constants.OK;
    }

    public String activateDevice(String username, String deviceId, String activationCode) {
    	logger.info("activate device: username=" + username + ", deviceId=" + deviceId + ", activationCode=" + activationCode);
        CustomerView cv = administrationService.activateDevice(username, deviceId, activationCode);
        return PojoJsonMapper.toJson(cv);
    }

    public String deactivateDevice(Long customerId, String deviceId, String sessionId, String token) {
    	logger.info("deactivate device: customer=" + customerId + ", deviceId=" + deviceId);
        administrationService.deactivateDevice(customerId, deviceId);
        return Constants.OK;
    }

    public String deactivateDevice2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return deactivateDevice(customerId, deviceId, sessionId, token);
    }


    public String switchDevice(Long customerId, String deviceId, String sessionId, String token) {

        CustomerDevice cd = administrationService.switchDevice(customerId, deviceId);
        return PojoJsonMapper.toJson(cd);
    }

    public String switchDevice2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return switchDevice(customerId, deviceId, sessionId, token);
    }

    public String deactivateService(Long customerId, String deviceId, String sessionId, String token) {

        administrationService.deactivateService(customerId);
        return Constants.OK;
    }

    public String deactivateService2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return deactivateService(customerId, deviceId, sessionId, token);
    }

    public String getDevices(Long customerId, String sessionId) {

        List<CustomerDevice> devices = administrationService.getDevices(customerId);
        return PojoJsonMapper.toJson(devices);
    }

    public BaseView changePin(BaseView view) {
        Transaction transaction = TransformerFactory.getTransformer(view)
                .transformTo(view, new Transaction());
        MessageCustomizer msgCustomizer = new CardMessageCustomizer();

        transaction.setReferenceNumber(view.getReferenceNumber());

        msgCustomizer.compose(view, transaction);

        CommLink link = new MxCommLink(transaction);
        link.sendMessage();

        if (transaction.getResponseCode().equals(com.dwidasa.engine.Constants.SUCCESS_CODE)) {
            msgCustomizer.decompose(view, transaction);
        }
        else {
            throw new BusinessException("IB-1009", transaction.getResponseCode());
        }

        EngineUtils.setTransactionStatus(transaction);

        return view;
    }

    private static class CardMessageCustomizer implements MessageCustomizer{
		private CardMessageCustomizer() {}

		public void compose(BaseView view, Transaction transaction) {
	        AccountView av = (AccountView) view;

	        transaction.setCardData1(av.getCustomerName());   //temporary utk bit 35
            transaction.setCardData2(av.getProductName());    //temporary utk bit 52
            transaction.setCardData3(av.getAccountName());    //temporary utk bit 53
            transaction.setTranslationCode("02605");
	    }

	    public Boolean decompose(Object view, Transaction transaction) {
            AccountView av = (AccountView) view;
            String bit48 = transaction.getFreeData1();

            //Encrypted Working Key an32 Blank on request
            //Key Check Value an6 Blank on request
            av.setProductName(bit48);    //temporary working key

	        return Boolean.TRUE;
	    }
	}

    public String smsRegistration(Long customerId, String deviceId, String sessionId, String token, String json) {
        return smsRegistrationExtract(customerId, json);
    }

    public String smsRegistration2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return smsRegistrationExtract(customerId, json);
    }

    public String smsRegistrationPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return smsRegistrationExtract(customerId, json);
    }

    public String smsRegistrationPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return smsRegistrationExtract(customerId, json);
    }

    private String smsRegistrationExtract(Long customerId, String json) {
        SmsRegistrationView srv = PojoJsonMapper.fromJson(json, SmsRegistrationView.class);
        if (srv.getCardData2() != null) srv.setCardData2(EngineUtils.getEncryptedPin(srv.getCardData2(), srv.getCardNumber()));
        srv.setTransactionType(com.dwidasa.engine.Constants.SMS_REGISTRATION.TRANS_TYPE);
        srv.setEncryptedTinUssd(EngineUtils.getEncryptedPin(srv.getTinUssd(), srv.getCardNumber()));

        /*
        CustomerAccount ca = customerAccountDao.getDefaultWithType(customerId);
        CustomerAccount customerAccount = customerAccountDao.getWithTypeAndProduct(customerId, ca.getAccountNumber());
        Product product = customerAccount.getProduct();
        logger.info("customerAccount=" + customerAccount);
        logger.info("product=" + product);

        srv.setProductCode(product.getProductCode());
        srv.setProductName(product.getProductName());
		*/
        Customer cust = customerDao.get(customerId);
        srv.setCifNumber(cust.getCifNumber());
        srv.setReferenceName(cust.getCustomerName());
		List<AccountView> accountViews = accountService.getAccounts(customerId);
		logger.info("accountViews.size()=" + accountViews.size());
        for (int i = 0; i < accountViews.size(); i++) {
			AccountView accountView = accountViews.get(i);			
			if (accountView.getProductName() != null && !accountView.getProductName().equals("")) {
				CustomerAccount customerAccount = customerAccountDao.getWithTypeAndProduct(customerId, accountView.getAccountNumber());
		        Product product = customerAccount.getProduct();
		        
		        logger.info("product.getProductCode()=" + product.getProductCode());
		        logger.info("product.getProductName()=" + product.getProductName());
		        accountView.setAccountType((accountView.getAccountType().equals("10")) ? "1" : (accountView.getAccountType().equals("20")) ? "2" : (accountView.getAccountType().equals("30")) ? "3" : "4");
                accountView.setProductCode(product.getProductCode());
                accountView.setProductName(product.getProductName());

			}
		}
        srv.setAccountViews(accountViews);

        srv = (SmsRegistrationView) kioskAdministrationService.posting(srv);
        if (srv.getReferenceNumber() == null)
            srv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(srv);
    }

	@Override
	public String cekSoftToken(Long customerId, String deviceId) {
		// TODO Auto-generated method stub
		CustomerDevice devices = administrationService.getByDeviceSoftToken(customerId, deviceId);
		return PojoJsonMapper.toJson(devices);
	}

	@Override
	public String registrationSoftToken(Long customerId, String deviceId) {
		
		CustomerDevice device = administrationService.registrationSoftToken(customerId, deviceId);
		return PojoJsonMapper.toJson(device);
	}

	@Override
	public String mobilRegistration(Long customerId, String sessionId, String json) {
		
		MobileRegistrationView view = PojoJsonMapper.fromJson(json, MobileRegistrationView.class);
		Customer customer = customerDao.get(customerId);
		System.out.println("NO Telepon 1 "+customer.getCustomerPhone());
		System.out.println("NO Telepon 2 "+view.getCustomerReference());
		
//		if(view.getCustomerReference().equals(customer.getCustomerPhone())){
		
	    String encryptedCustomerPin = EngineUtils.encrypt(com.dwidasa.engine.Constants.SERVER_SECRET_KEY, customer.getCustomerPin());
	    //String tinInput = EngineUtils.decrypt(com.dwidasa.engine.Constants.SERVER_SECRET_KEY, view.getTinMobile());
	    String customerPin = EngineUtils.decrypt(com.dwidasa.engine.Constants.SERVER_SECRET_KEY, encryptedCustomerPin);
        
        if(!view.getTinMobile().equals(customerPin)) {
        	
        	throw new BusinessException("IB-1009", "DJ");
        	
        }else{
		
			String encryptedTin = EngineUtils.getEncryptedPin(customerPin, view.getCardNumber());
	        String customData = StringUtils.rightPad(customer.getCifNumber(), 8, " ");
	        customData += StringUtils.rightPad(encryptedTin, 16, " ");
	        customData += StringUtils.rightPad(view.getCardNumber(), 16, " ");
	        customData += StringUtils.rightPad(view.getAccountNumber(), 10, " ");
	        customData += StringUtils.rightPad(view.getCustomerReference(), 15, " ");
	        customData += StringUtils.rightPad(view.getCustomerReference(), 20, " ");
	        customData += StringUtils.rightPad(view.getCustomerReference(), 20, " ");
	        customData += StringUtils.rightPad("01", 2, " ");
	        customData += StringUtils.rightPad(view.getAccountNumber(), 10, " ");
	        customData += StringUtils.rightPad(customer.getCustomerName(), 30, " ");

	        CustomerAccount custAccount = customerAccountDao.getDefaultWithType(customerId);
	        Product product = productDao.get(custAccount.getProductId()); 
	        customData += StringUtils.rightPad("" + custAccount.getAccountTypeId(), 1, " ");
	        customData += StringUtils.rightPad(product.getProductCode(), 30, " ");

	        System.out.println("customData=" + customData);
	        logger.info("customData=" + customData);
	        view.setBit48(customData);
	        
	        Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
	        logger.info("mobileRegistrationView.getEncryptedTinMobile()=" + view.getEncryptedTinMobile());            	
	    	try {
	        	registrationService.registerEBanking(t);
	        	t.setResponseCode(Constants.SUCCESS_CODE);
	        } catch (Exception e) {
	            t.setResponseCode("11");
	        }
	        String bit48 = t.getFreeData1();
	        logger.info("bit48 after=" + bit48);
	        
	        
	        view.setReferenceName(bit48.substring(65, 85).trim());
	        view.setActivationCode(bit48.substring(85, 105).trim());
	        
	        return PojoJsonMapper.toJson(view);
			
        }	
//		}else{
//		
//			throw new BusinessException("IB-1009", "DJ");
//        
//		}
		
		
	}

}
