package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.ResultView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/14/11
 * Time: 7:02 PM
 */
public interface WebAdministrationService extends AdministrationService {
    /**
     * Get all active customer device.
     * @param customerId customer id
     * @return list of customer device
     */
    public List<CustomerDevice> getDevices(Long customerId);

    /**
     * Request an activation code to be used for activating device later.
     * @param customerId customer id
     * @return activation code
     */
    public String requestDeviceActivationCode(Long customerId);

    /**
     * Provide customer personal information.
     * @param customerId customer id
     * @param email email, if null, current data will not be updated
     * @param phone phone, if null, current data will not be updated
     */
    public void providePersonalInfo(Long customerId, String email, String phone);

    /**
     * Action to activate soft token, hence activation code is generated.
     * @param customerId customer id
     * @return activation code
     */
    public String requestSoftTokenActivationCode(Long customerId);

    /**
     * Deactivate soft token.
     * @param customerId customer user id
     * @return result view object
     * @exception BusinessException deactivation failed
     */
    public ResultView deactivateSoftToken(Long customerId);

    /**
     * After request an activation code for activating soft token, then this is
     * the process to confirm whether the activation code has been entered in soft token
     * software correctly or not, through providing first token its generate.
     * @param customerId customer id
     * @param token first token
     * @return result view object
     */
    public ResultView validateFirstToken(Long customerId, String token);
}
