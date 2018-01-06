package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.CustomerView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/14/11
 * Time: 7:02 PM
 */
public interface NativeAdministrationService extends AdministrationService {
    /**
     * Activate client device using provided activation code.
     * Check for status and activation expired date before performing activation
     * If activation is successful, then check for device id
     * if existing device id is already registered, then deactivate that device first
     * After that, activate device with new activation data
     * @param username customer user name
     * @param deviceId device id
     * @param activationCode activation code
     * @return customer view object
     * @exception com.dwidasa.engine.BusinessException customer can't be activated
     */
    public CustomerView activateDevice(String username, String deviceId, String activationCode);

    /**
     * Activate soft token device for a customer using provided activation code
     * @param customerId
     * @param cardNumber
     * @param activationCode
     * @return
     */
    public CustomerView activateSoftToken(Long customerId, String cardNumber, String activationCode);

    /**
     * Get all active customer devices.
     * @param customerId customer id
     * @return all available customer device
     */
    public List<CustomerDevice> getDevices(Long customerId);
    
    //penambahan cek dan registration softToken
    public CustomerDevice getByDeviceSoftToken(Long customerId, String deviceId);
    
    public CustomerDevice registrationSoftToken(Long customerId, String deviceId);

    /**
     * Register email address after device activation.
     * @param customerId customer id
     * @param deviceId device id
     * @param activationCode activation code
     * @param email customer email address
     * @return customer view object
     * @exception com.dwidasa.engine.BusinessException email can't be registered
     */
    public CustomerView registerEmail(Long customerId, String deviceId, String activationCode, String email);
}
