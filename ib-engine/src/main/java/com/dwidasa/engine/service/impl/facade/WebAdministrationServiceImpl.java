package com.dwidasa.engine.service.impl.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.facade.WebAdministrationService;
import com.dwidasa.engine.service.otp.WebOtpClient;
import com.dwidasa.engine.util.ReferenceGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/14/11
 * Time: 7:09 PM
 */
@Service("webAdministrationService")
public class WebAdministrationServiceImpl extends AdministrationServiceImpl
        implements WebAdministrationService {
    @Autowired
    private WebOtpClient webOtpClient;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerDeviceDao customerDeviceDao;

    @Autowired
    private LoggingService loggingService;

    public WebAdministrationServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerDevice> getDevices(Long customerId) {
        return customerDeviceDao.getAll(customerId);
    }

    /**
     * {@inheritDoc}
     */
    public String requestDeviceActivationCode(Long customerId) {
        String activationCode = RandomStringUtils.randomNumeric(6);

        CustomerDevice cd = new CustomerDevice();
        cd.setCustomerId(customerId);
        cd.setActivatePin(activationCode);
        cd.setStatus(Constants.INACTIVE_STATUS);
        cd.setCreated(new Date());
        cd.setCreatedby(customerId);
        cd.setUpdated(new Date());
        cd.setUpdatedby(customerId);
        customerDeviceDao.save(cd);

        return activationCode;
    }

    /**
     * {@inheritDoc}
     */
    public void providePersonalInfo(Long customerId, String email, String phone) {
        Customer customer = customerDao.get(customerId);
        if (customer == null) {
            throw new BusinessException("IB-1004");
        }

        if (email != null) {
            customer.setCustomerEmail(email);
        }

        if (phone != null) {
            customer.setCustomerPhone(phone);
        }

        customer.setFirstLogin("N");
        customerDao.save(customer);
    }

    /**
     * {@inheritDoc}
     */
    public String requestSoftTokenActivationCode(Long customerId) {
        Customer customer = customerDao.getWithDefaultAccount(customerId);
        return customerDeviceDao.get(customerId, customer.getCustomerAccounts().iterator().next().getAccountNumber())
                .getActivatePin();
    }

    /**
     * {@inheritDoc}
     */
    public ResultView deactivateSoftToken(Long customerId) {
        Customer customer = customerDao.get(customerId);
        customer.setTokenActivated("N");

        webOtpClient.unregisterSoftToken(customerId);
        customerDao.save(customer);

        ResultView view = new ResultView();
        view.setReferenceNumber(ReferenceGenerator.generate());
        view.setResponseCode(Constants.SUCCESS_CODE);
        return view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterUser(Long customerId) {
        webOtpClient.unregisterUser(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterDevice(Long customerId, String deviceId) {
        webOtpClient.unregisterDevice(customerId, deviceId);
    }

    /**
     * {@inheritDoc}
     */
    public ResultView validateFirstToken(Long customerId, String token) {
        Customer customer = customerDao.get(customerId);

        if (webOtpClient.validateToken(customerId, token)) {
            loggingService.incFailedAuthAttempts(customer);
            throw new BusinessException("IB-1000");
        }

        if (customer.getFailedAuthAttempts() != 0) {
            customer.setFailedAuthAttempts(0);
            customerDao.save(customer);
        }

        ResultView view = new ResultView();
        view.setReferenceNumber(ReferenceGenerator.generate());
        view.setResponseCode(Constants.SUCCESS_CODE);

        return view;
    }
}
