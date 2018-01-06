package com.dwidasa.engine.service.impl.facade;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.service.facade.NativeAdministrationService;
import com.dwidasa.engine.service.otp.NativeOtpClient;
import com.dwidasa.engine.util.DateUtils;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/14/11
 * Time: 7:09 PM
 */
@Service("nativeAdministrationService")
public class NativeAdministrationServiceImpl extends AdministrationServiceImpl
        implements NativeAdministrationService {
//    private static Logger logger = Logger.getLogger( NativeAdministrationServiceImpl.class );

    @Autowired
    private CustomerDao customerDao;

    @SuppressWarnings("unused")
	@Autowired
    private CustomerAccountDao customerAccountDao;

    @Autowired
    private CustomerDeviceDao customerDeviceDao;

    @Autowired
    private NativeOtpClient nativeOtpClient;

    public NativeAdministrationServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public CustomerView activateDevice(String username, String deviceId, String activationCode) {
        Customer customer = customerDao.get(username.toUpperCase());
        if (customer == null){
            throw new BusinessException("IB-6001");
        }
        CustomerDevice customerDevice = customerDeviceDao.validateActivationCode(customer.getId(),
                activationCode, Constants.INACTIVE_STATUS);
        if (customerDevice == null) {
            throw new BusinessException("IB-6001");
        }
        if (DateUtils.truncate(customerDevice.getExpiredDate()).compareTo(DateUtils.truncate(new Date())) < 0){
            customerDeviceDao.remove(customerDevice.getId(), customer.getId());
            throw new BusinessException("IB-6002");
        }

        // check for existing device
        CustomerDevice existingDevice = customerDeviceDao.get(deviceId);
        if (existingDevice != null){
            deactivateDevice(existingDevice.getCustomerId(), existingDevice.getDeviceId());
        }
        customerDevice.setStatus(Constants.ACTIVE_STATUS);
        customerDevice.setDeviceId(deviceId);
        customerDevice.setIme(deviceId.substring(0, 15));
        customerDevice.setUpdated(new Date());
        customerDevice.setUpdatedby(customer.getId());
        customerDeviceDao.save(customerDevice);

        nativeOtpClient.registerDevice(customer.getId(), deviceId, activationCode);
        //-- TODO what if customer status is inactive ?
        return generateView(customer, null);
    }

    /**
     * {@inheritDoc}
     */
    public CustomerView activateSoftToken(Long customerId, String cardNumber,String activationCode){
        Customer customer = customerDao.get(customerId);

        if (customer == null){
            throw new BusinessException("IB-6001");
        }
        CustomerDevice customerDevice = customerDeviceDao.get(cardNumber);
        if (customerDevice == null) {
            throw new BusinessException("IB-6001");
        }

        try {
            // unregister soft token first in OTP
            nativeOtpClient.unregisterDevice(customerId, cardNumber);
        }
        catch (Exception e){

        }

        nativeOtpClient.registerDevice(customerId, cardNumber, activationCode);
        return generateView(customer, null);
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerDevice> getDevices(Long customerId) {
        return customerDeviceDao.getAll(customerId);
    }
    
    public CustomerDevice getByDeviceSoftToken(Long customerId, String deviceId){
    	return customerDeviceDao.getByDeviceSoftToken(customerId, deviceId);
    }

    /**
     * {@inheritDoc}
     */
    public CustomerView registerEmail(Long customerId, String deviceId, String activationCode, String email) {

        if (!customerDeviceDao.isValidActivationCode(customerId, activationCode, Constants.ACTIVE_STATUS)) {
            throw new BusinessException("IB-1006");
        }

        Customer c = customerDao.get(customerId);
        c.setCustomerEmail(email);
        return generateView(customerDao.save(c), new CustomerView());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterUser(Long customerId) {
        nativeOtpClient.unregisterUser(customerId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterDevice(Long customerId, String deviceId) {
        nativeOtpClient.unregisterDevice(customerId, deviceId);
    }

	@Override
	public CustomerDevice registrationSoftToken(Long customerId, String deviceId) {
	
		
		
		customerDeviceDao.deactivateSoftTokenAll(customerId);
		customerDeviceDao.activateSoftToken(customerId, deviceId);
		
		CustomerDevice customerDevice = customerDeviceDao.getByDeviceSoftToken(customerId, deviceId);

		return customerDevice;
	}
}
