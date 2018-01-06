package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.ResultView;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 9:25 AM
 */
public interface AdministrationService {
    /**
     * Apply change to customer object based on type of operation, below is the list for all valid operation : <br/>
     *      (1) change customer email<br/>
     *      (2) change customer phone number<br/>
     *      (3) change customer pin<br/>
     * Mandatory field are : customerId, and other field depend on what to be updated. For example,
     * operation 1, change customer email, therefore email is mandatory field.
     * @param view customer view object
     * @param oldValue old value to be updated
     * @param operation valid operation type applied on customer object
     * @return result view object
     * @exception BusinessException customer not found exception
     */
    public ResultView changeCustomerInfo(CustomerView view, String oldValue, Integer operation);

    /**
     * Deactivate customer device.
     * @param customerId customer id
     * @param deviceId device id
     * @return result view object
     * @exception com.dwidasa.engine.BusinessException customer user device not found exception
     */
    public ResultView deactivateDevice(Long customerId, String deviceId);

    /**
     * Switch customer device.
     * @param customerId customer id
     * @param deviceId device id
     * @return customer device object
     */
    public CustomerDevice switchDevice(Long customerId, String deviceId);

    /**
     * Deactivate banking service for specified customer id.
     * @param customerId customer id
     * @return result view object
     * @exception BusinessException customer not found exception
     */
    public ResultView deactivateService(Long customerId);
}
