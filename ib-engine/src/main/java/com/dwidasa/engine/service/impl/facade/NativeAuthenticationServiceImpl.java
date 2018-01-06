package com.dwidasa.engine.service.impl.facade;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.dao.CustomerSessionDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.CustomerSession;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.facade.NativeAuthenticationService;
import com.dwidasa.engine.service.otp.NativeOtpClient;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Implementation class of <code>NativeAuthenticationService</code> service.
 *
 * @author rk
 */
@Service("nativeAuthenticationService")
public class NativeAuthenticationServiceImpl implements NativeAuthenticationService {
    private static Logger logger = Logger.getLogger(  NativeAuthenticationServiceImpl.class);
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerDeviceDao customerDeviceDao;

    @Autowired
    private CustomerSessionDao customerSessionDao;

    @Autowired
    private NativeOtpClient nativeOtpClient;

    @Autowired
    private LoggingService loggingService;

    public NativeAuthenticationServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public CustomerView authenticate(Long customerId, String deviceId, String token) {

        switch(customerDao.checkCustomerStatus(customerId)) {
            case -1 : throw new BusinessException("IB-1002");
            case 0  : throw new BusinessException("IB-1019");
        }

        CustomerDevice cd = customerDeviceDao.get(deviceId);
        if (cd == null){
        	loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile gagal, perangkat belum teregistrasi" , "", Constants.MERCHANT_TYPE.MOBILE, Constants.TERMINALID_DEFAULT.MOBILE);
            throw new BusinessException("IB-1003");
        }

        if (!customerDeviceDao.isActive(customerId, deviceId)) {
        	loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile gagal, perangkat belum teregistrasi" , "", Constants.MERCHANT_TYPE.MOBILE, Constants.TERMINALID_DEFAULT.MOBILE);
            throw new BusinessException("IB-1003");
        }

        CustomerSession customerSession = customerSessionDao.get(customerId, deviceId);
        if (customerSession == null) {
            throw new BusinessException("IB-1001");
        }

        Customer customer = customerDao.getWithDefaultAccount(customerId);

        if (!nativeOtpClient.validateToken(customerId, deviceId, token, customerSession.getChallenge())) {
            loggingService.incFailedAuthAttempts(customer);
            loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile gagal, data otentifikasi tidak valid" , "", Constants.MERCHANT_TYPE.MOBILE, Constants.TERMINALID_DEFAULT.MOBILE);
            throw new BusinessException("IB-1000");
        }

        customerSession = updateSession(customerSession, customerId);
        CustomerView customerView = generateView(customer, customerSession);
        loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile sukses" , "", Constants.MERCHANT_TYPE.MOBILE, customerView.getTerminalId());
        return customerView;
    }


    /**
     * {@inheritDoc}
     */
    public CustomerView authenticate2(Long customerId, String deviceId, String token, String challenge) {
        switch(customerDao.checkCustomerStatus(customerId)) {
            case -1 : throw new BusinessException("IB-1002");
            case 0  : throw new BusinessException("IB-1019");
        }

        CustomerDevice cd = customerDeviceDao.get(deviceId);
        if (cd == null){
        	loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile gagal, perangkat belum teregistrasi" , "", Constants.MERCHANT_TYPE.MOBILE, Constants.TERMINALID_DEFAULT.MOBILE);
            throw new BusinessException("IB-1003");
        }

        if (!customerDeviceDao.isActive(customerId, deviceId)) {
        	loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile gagal, perangkat belum teregistrasi" , "", Constants.MERCHANT_TYPE.MOBILE, Constants.TERMINALID_DEFAULT.MOBILE);
            throw new BusinessException("IB-1003");
        }

        CustomerSession customerSession = customerSessionDao.get(customerId, deviceId);
        if (customerSession == null) {
            throw new BusinessException("IB-1001");
        }

        Customer customer = customerDao.getWithDefaultAccount(customerId);

        String challengeCode = EngineUtils.generateChallengeCode(EngineUtils.generateCrc32(challenge), customer.getCustomerPin());

        if (!nativeOtpClient.validateToken2(customerId, deviceId, token, challengeCode)) {
            loggingService.incFailedAuthAttempts(customer);
            loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile gagal, data otentifikasi tidak valid" , "", Constants.MERCHANT_TYPE.MOBILE, Constants.TERMINALID_DEFAULT.MOBILE);
            throw new BusinessException("IB-1000");
        }

        customerSession = updateSession(customerSession, customerId);
        CustomerView customerView = generateView(customer, customerSession);
        loggingService.logActivity(customerId, Constants.LOGIN_TYPE, "Login Mobile sukses" , "", Constants.MERCHANT_TYPE.MOBILE, customerView.getTerminalId());
        return customerView;
    }

    /**
     * Update session related info. This method will also invalidate other user's session
     * that currently logged in.
     * @param customerSession customer session object to be updated
     * @param customerId customer id
     * @return customerSession after update
     */
    private CustomerSession updateSession(CustomerSession customerSession, Long customerId) {
        customerSession.setSessionId(UUID.randomUUID().toString());
        customerSession.setChallenge(null);
        customerSession.setUpdated(new Date());
        customerSession.setUpdatedby(customerId);
        customerSessionDao.save(customerSession);

        customerSessionDao.invalidate(customerId, customerSession.getDeviceId());
        return customerSession;
    }

    /**
     * Generate view object for presentation/client layer
     * @param customer customer object
     * @param customerSession session object
     * @return customerView object
     */
    private CustomerView generateView(Customer customer, CustomerSession customerSession) {
        CustomerView customerView =  new CustomerView();
        customerView.setId(customer.getId());
        customerView.setUsername(customer.getCustomerUsername());
        customerView.setName(customer.getCustomerName());
        customerView.setEmail(customer.getCustomerEmail());
        customerView.setPhone(customer.getCustomerPhone());

        CustomerAccount customerAccount = customer.getCustomerAccounts().iterator().next();
        customerView.setAccountNumber(customerAccount.getAccountNumber());
        customerView.setAccountType(customerAccount.getAccountType().getAccountType());
        customerView.setCardNumber(customerAccount.getCardNumber());

        customerView.setFirstLogin(customer.getFirstLogin());
        customerView.setLastLogin(customer.getLastLogin());
        customerView.setLastDeviceId(customer.getLastTokenId());
        customerView.setSessionId(customerSession.getSessionId());

        if (customer.getFailedAuthAttempts() != 0) {
            customer.setFailedAuthAttempts(0);
        }
        customer.setLastLogin(new Date());
        customer.setLastTokenId(customerSession.getDeviceId());
        customerDao.save(customer);

        if (EngineUtils.isMerchant(customerView.getAccountNumber())) {
            CustomerDevice cd = customerDeviceDao.get(customer.getId(), customerSession.getDeviceId());
            customerView.setTerminalId(cd.getTerminalId());
        }
        else {
            customerView.setTerminalId("MBS");
        }

        return customerView;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean authenticateToken(Long customerId, String deviceId, String sessionId, String token) {
        if (validateCustomerSession(customerId, sessionId)) {
        	
        	//Jika Untuk Kiost Aktifkan Coding di bawah
        	//Terus Enable yang coding di bawahnya
        	//Gunanya untuk mencari device Id yang aktif buat soft token yang berdasarkan Customer Id
        	if (validateCustomerSession(customerId, sessionId)) {
            	CustomerDevice customerDevice = customerDeviceDao.getDeviceSoftToken(customerId);
            	System.out.println("Device Soft Token "+customerDevice.getDeviceId());
            	logger.info("customerId=" + customerId +  " deviceId=" + deviceId + " sessionId=" + sessionId + " token=" + token);
                System.out.println("customerId=" + customerId +  " deviceId=" + deviceId + " sessionId=" + sessionId + " token=" + token);
            	//CustomerSession customerSession = customerSessionDao.get(customerId, deviceId);
                Customer customer = customerDao.get(customerId);
                logger.info("customer=" + customer);
                System.out.println("customer=" + customer);
                CustomerDevice cd = customerDeviceDao.getDevice(customerDevice.getDeviceId());
                logger.info("cd=" + cd);
                System.out.println("cd="+cd);
                if (cd == null){
                    throw new BusinessException("IB-1003");
                }
                //logger.info("customerSession=" + customerSession);
                //logger.info("customerSession.getChallenge()=" + customerSession.getChallenge());
                if (!nativeOtpClient.validateToken(customerId, customerDevice.getDeviceId(), token, "")) {
                    loggingService.incFailedAuthAttempts(customer);
                    throw new BusinessException("IB-1000");
                }

                if (customer.getFailedAuthAttempts() != 0) {
                    customer.setFailedAuthAttempts(0);
                    customerDao.save(customer);
                }
            }
        	//Untuk IB
        	logger.info("customerId=" + customerId +  " deviceId=" + deviceId + " sessionId=" + sessionId + " token=" + token);
            System.out.println("customerId=" + customerId +  " deviceId=" + deviceId + " sessionId=" + sessionId + " token=" + token);
        	//CustomerSession customerSession = customerSessionDao.get(customerId, deviceId);
            Customer customer = customerDao.get(customerId);
            logger.info("customer=" + customer);
            System.out.println("customer=" + customer);
            CustomerDevice cd = customerDeviceDao.getDevice(deviceId);
            logger.info("cd=" + cd);
            System.out.println("cd="+cd);
            if (cd == null){
                throw new BusinessException("IB-1003");
            }
            //logger.info("customerSession=" + customerSession);
            //logger.info("customerSession.getChallenge()=" + customerSession.getChallenge());
            if (!nativeOtpClient.validateToken(customerId, deviceId, token, "")) {
                loggingService.incFailedAuthAttempts(customer);
                throw new BusinessException("IB-1000");
            }

            if (customer.getFailedAuthAttempts() != 0) {
                customer.setFailedAuthAttempts(0);
                customerDao.save(customer);
            }
        }

        return Boolean.TRUE;
    }

    public Boolean authenticateToken2(Long customerId, String deviceId, String sessionId, String token, String json){

        if (validateCustomerSession(customerId, sessionId)) {
            CustomerSession customerSession = customerSessionDao.get(customerId, deviceId);
            Customer customer = customerDao.get(customerId);

            CustomerDevice cd = customerDeviceDao.get(deviceId);
            if (cd == null){
                throw new BusinessException("IB-1003");
            }

            String challengeCode = EngineUtils.generateChallengeCode(EngineUtils.generateCrc32(json), customer.getCustomerPin());

            if (!nativeOtpClient.validateToken2(customerId, deviceId, token, challengeCode)) {
                loggingService.incFailedAuthAttempts(customer);
                throw new BusinessException("IB-1000");
            }

            if (customer.getFailedAuthAttempts() != 0) {
                customer.setFailedAuthAttempts(0);
                customerDao.save(customer);
            }
        }
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validateCustomerSession(Long customerId, String sessionId) {
        switch (customerSessionDao.validate(customerId, sessionId)) {
            case -1 : throw new BusinessException("IB-1001");
            case 0  : throw new BusinessException("IB-1010");
        }

        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    public String requestChallenge(Long customerId, String deviceId) {
        CustomerSession customerSession = customerSessionDao.force(customerId, deviceId);
        customerSession.setChallenge(generateChallenge());
        //-- don't update updated field here OR SESSION_TIMEOUT won't work correctly
        customerSessionDao.save(customerSession);

        return customerSession.getChallenge();
    }

    /**
     * Generate random number for a challenge.
     * @return challenge number
     */
    private String generateChallenge() {
        Random rnd = new Random();
        Boolean[] flag = new Boolean[6];

        for (int i = 0; i < 6; i++) {
            flag[i] = Boolean.FALSE;
        }

        String challenge = "";
        for (int i = 0; i < 30; i++) {
            int r = rnd.nextInt(6);
            if (!flag[r]) {
                challenge = challenge + (r + 1);
                flag[r] = Boolean.TRUE;
            }
        }

        for (int i = 0; i < 6; i++) {
            if (!flag[i]) {
                challenge = challenge + (i + 1);
            }
        }

        return challenge;
    }
}
