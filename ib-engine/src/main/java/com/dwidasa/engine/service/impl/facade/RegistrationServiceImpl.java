package com.dwidasa.engine.service.impl.facade;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.AccountTypeDao;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.dao.ProductDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.NativeAdministrationService;
import com.dwidasa.engine.service.facade.RegistrationService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 6/27/13
 * Time: 4:03 PM
 */
@Service("registrationService")
public class RegistrationServiceImpl implements RegistrationService {

    private static Logger logger = Logger.getLogger( RegistrationServiceImpl.class );
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerAccountDao customerAccountDao;

    @Autowired
    private CustomerDeviceDao customerDeviceDao;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private AccountTypeDao accountTypeDao;

    @Autowired
    private NativeAdministrationService nativeAdministrationService;
    
    public RegistrationServiceImpl(){

    }

    @Override
    public void registerEBanking(Transaction t) {

        String bit48 = t.getFreeData1();

        String cardNumber = bit48.substring(24, 40).trim();
        String accountNumber = bit48.substring(40, 50).trim();
        String phoneNumber = bit48.substring(50, 65).trim();

        logger.info("cardNumber=" + cardNumber + " accountNumber=" + accountNumber + " phoneNumber=" + phoneNumber);

        //validate phone number for SMS Token, dihapus karena untuk mobile
        phoneNumber = cacheManager.validateMobileRegistrationPhoneNumber(phoneNumber);
        if (phoneNumber == null){
            t.setResponseCode("56");
            return;
        }

        //check customer account
        List<Customer> customers = customerDao.getByDefaultAccountNumber(accountNumber);
        if (customers == null){
            t.setResponseCode("23");
            return;
        }

        Customer customer = new Customer();
        CustomerDevice customerDevice = new CustomerDevice();
        if (customers.size() == 0){
            // new customer
            createNewCustomer(customer, customerDevice, t);

        }
        else {
            // existing customer
            for (Customer c : customers){
//tidak perlu DELETE jika sudah ada di m_customer
//                // check customer
//                boolean deleteAccount = false;
//                if (c.getCreatedby() == 0){
//                    // delete MB existing account
//                    customerDao.remove(c.getId());
//                }
//                else {
                    // update existing account
                    customer = c;
                    updateExistingCustomer(customer, customerDevice, t);
//                }
            }
//CIF tidak boleh null
//            if (customer.getCifNumber() == null){
//                createNewCustomer(customer, customerDevice, t);
//            }

        }


        String tail = bit48.substring(105);
        bit48 = bit48.substring(0, 65);
        String feedBack = String.format("%1$-20s", customer.getCustomerUsername());
        feedBack += String.format("%1$-20s", customerDevice.getActivatePin());

        t.setFreeData1(bit48 + feedBack + tail);
    }

    private void createNewCustomer(Customer customer, CustomerDevice customerDevice, Transaction t){

        String bit48 = t.getFreeData1();

        String cif = bit48.substring(0, 8).trim();
        String encryptedPin = bit48.substring(8, 24);
        String tin = EngineUtils.getEdcPin(encryptedPin, t.getCardNumber());
        String cardNumber = bit48.substring(24, 40).trim();
        String accountNumber = bit48.substring(40, 50).trim();
        String phoneNumber = bit48.substring(50, 65).trim();
        String customerName = bit48.substring(107 + 10, 107 + 40).trim();

        //set username, bukan name
        customer.setCustomerUsername(generateUsername(customerName, accountNumber));
        customer.setCustomerName(customerName);
        customer.setCustomerPin(tin);
        customer.setCustomerPhone(phoneNumber);
        customer.setCifNumber(cif);
        customer.setFailedAuthAttempts(0);
        customer.setFirstLogin("Y");
        customer.setTokenActivated("N");
        customer.setMobileWebTokenId(cardNumber);
        customer.setStatus(Constants.ACTIVE_STATUS);
        customer.setCreated(new Date());
        customer.setCreatedby(0L);
        customer.setUpdated(new Date());
        customer.setUpdatedby(0L);
        
        customerDao.save(customer);

        // create customer accounts
        createCustomerAccounts(customer.getId(), cardNumber, accountNumber, t);

        // register customer devices
        registerCustomerDevice(customer.getId(), cardNumber, accountNumber, customerDevice);
    }
    private void registerSoftToken(String cardNumber, String accountNumber, CustomerDevice device){

        CustomerDevice tokenDevice = customerDeviceDao.get(cardNumber);
        boolean needActivation = false;
        if (tokenDevice == null) {
            tokenDevice = new CustomerDevice();
            tokenDevice.setCustomerId(device.getCustomerId());
            tokenDevice.setDeviceId(cardNumber);
            tokenDevice.setIme(cardNumber);

            if (EngineUtils.isMerchant(accountNumber)) {

                String terminalId = EngineUtils.generateTerminalIdByAccountNumber(accountNumber);
                tokenDevice.setTerminalId(terminalId);
            }

            tokenDevice.setCreated(new Date());
            tokenDevice.setCreatedby(0L);

            needActivation = true;
        }

        tokenDevice.setActivatePin(device.getActivatePin());
        tokenDevice.setExpiredDate(new Date());
        tokenDevice.setStatus(Constants.ACTIVE_STATUS);
        tokenDevice.setUpdated(new Date());
        tokenDevice.setUpdatedby(0L);
        tokenDevice = customerDeviceDao.save(tokenDevice);
        logger.info("tokenDevice=" + tokenDevice);
        logger.info("needActivation=" + needActivation);
        if (needActivation) {
            nativeAdministrationService.activateSoftToken(device.getCustomerId(),cardNumber,device.getActivatePin());
        }
    }

    private void updateExistingCustomer(Customer customer, CustomerDevice customerDevice, Transaction t){

        String bit48 = t.getFreeData1();

        String cif = bit48.substring(0, 8).trim();
        String encryptedPin = bit48.substring(8, 24);
        String tin = EngineUtils.getEdcPin(encryptedPin, t.getCardNumber());
        String cardNumber = bit48.substring(24, 40).trim();
        String accountNumber = bit48.substring(40, 50).trim();
        String phoneNumber = bit48.substring(50, 65).trim();
        String customerName = bit48.substring(107 + 10, 107 + 40).trim();

        customer.setCustomerName(customerName);
        customer.setCustomerPin(tin);
        customer.setCustomerPhone(phoneNumber);
        customer.setCifNumber(cif);
        customer.setFailedAuthAttempts(0);
        customer.setFirstLogin("N");
        customer.setStatus(Constants.ACTIVE_STATUS);
        customer.setUpdated(new Date());
        customer.setUpdatedby(customer.getId());

        customerDao.save(customer);

        // delete customer accounts
        customerAccountDao.deleteCustomerAccounts(customer.getId());

        // re-create customer accounts
        createCustomerAccounts(customer.getId(), cardNumber, accountNumber, t);

        // remove inactive device
        customerDeviceDao.removeInactiveDevices(customer.getId());

        // register customer devices
        registerCustomerDevice(customer.getId(), cardNumber, accountNumber, customerDevice);
    }

    private String generateUsername(String customerName, String accountNumber){

        String nameNoSpace = customerName.replaceAll(" ", "").toUpperCase();
        int leftCount = 4;
        int count = 8;
        if (nameNoSpace.length() < 8) {
            count = nameNoSpace.length();
            leftCount = 12 - count;
        }

        String usernameAlpha = nameNoSpace.substring(0, count);
        String usernameNumeric = accountNumber.substring(accountNumber.length() - leftCount);

        String username = usernameAlpha + usernameNumeric;
        boolean isNotUnique = true;

        while (isNotUnique){
            // check username availability
            Customer c = customerDao.get(username);

            //jika customer ketemu, artinya harus generate username baru
            if (c != null){
                // generate random username numeric part
                if (count == 8){
                    usernameAlpha = usernameAlpha.substring(0,7);
                    count = 7;
                    leftCount = 5;
                }

                usernameNumeric = RandomStringUtils.randomNumeric(leftCount);
                username = usernameAlpha + usernameNumeric;
            }
            else {
                isNotUnique = false;
            }
        }

        return username;
    }

    private void createCustomerAccounts(Long customerId, String defaultCardNumber, String defaultAccountNumber, Transaction t){

        String bit48 = t.getFreeData1();
        int numOfAccounts = Integer.valueOf(bit48.substring(105, 107));
        int base = 107;
        int len = 71;

        for (int i = 0; i < numOfAccounts; i++) {
            String accountNumber = bit48.substring(base + i * len, base + i * len + 10).trim();
            String accountType = bit48.substring(base + i * len + 40, base + i * len + 41);
            String productType = bit48.substring(base + i * len + 41, base + i * len + 71).trim();


            CustomerAccount ca = new CustomerAccount();
            if (accountType.equals("1")) {
                ca.setAccountTypeId(accountTypeDao.get(Constants.TABUNGAN_CODE).getId());
            }
            else {
                ca.setAccountTypeId(accountTypeDao.get(Constants.GIRO_CODE).getId());
            }
            ca.setCurrencyId(cacheManager.getCurrency("360").getId());
            ca.setCustomerId(customerId);
            ca.setProductId(productDao.get(productType).getId());
            ca.setAccountNumber(accountNumber);
            ca.setCardNumber(defaultCardNumber);
            if (accountNumber.equals(defaultAccountNumber)) {
                ca.setIsDefault("Y");
            }
            else {
                ca.setIsDefault("N");
            }
            ca.setStatus(Constants.ACTIVE_STATUS);
            ca.setCreated(new Date());
            ca.setCreatedby(customerId);
            ca.setUpdated(new Date());
            ca.setUpdatedby(customerId);
            ca = customerAccountDao.save(ca);
        }
    }

    private CustomerDevice registerCustomerDevice(Long customerId, String cardNumber, String accountNumber, CustomerDevice cd) {

        int expiredDays = Integer.parseInt(cacheManager.getParameter("REGISTRATION_EXPIRED_DAYS").getParameterValue());

        cd.setCustomerId(customerId);

        if (EngineUtils.isMerchant(accountNumber)) {

           String terminalId = EngineUtils.generateTerminalIdByAccountNumber(accountNumber);
           cd.setTerminalId(terminalId);
        }

        cd.setActivatePin(RandomStringUtils.randomNumeric(6));
        cd.setStatus(Constants.INACTIVE_STATUS);
        cd.setExpiredDate(DateUtils.after(new Date(), expiredDays));
        cd.setSoftToken(Boolean.FALSE);
        
        cd.setCreated(new Date());
        cd.setCreatedby(customerId);
        cd.setUpdated(new Date());
        cd.setUpdatedby(customerId);

        customerDeviceDao.save(cd);

        //registerSoftToken
        registerSoftToken(cardNumber, accountNumber, cd);
        return cd;
    }
}
