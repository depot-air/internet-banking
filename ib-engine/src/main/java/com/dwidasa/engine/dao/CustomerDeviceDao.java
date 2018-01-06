package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.CustomerDevice;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/4/11
 * Time: 12:06 PM
 */
public interface CustomerDeviceDao extends GenericDao<CustomerDevice, Long> {//
	
	public CustomerDevice getSoftToken(Long customerId);
	
	public CustomerDevice getDeviceSoftToken(Long customerId);
    /**
     * Get customer device by customer id and device id.
     * @param customerId customer id
     * @param deviceId device id
     * @return customer device object
     */
    public CustomerDevice get(Long customerId, String deviceId);

    /**
     * Get customer device by device id.
     * @param deviceId device id
     * @return customer device object
     */
    public CustomerDevice get(String deviceId);
    public CustomerDevice getDevice(String deviceId);

    /**
     * Get all customer devuce by customer id.
     * @param customerId customer id
     * @return list of customer device
     */
    public List<CustomerDevice> getAll(Long customerId);
    public List<CustomerDevice> getAllNotActiveYet(Long customerId);
    public List<CustomerDevice> getAllActiveByInboxId(Long inboxId);
//    public List<CustomerDevice> getAllById(Long id);
    public Boolean isIbFirstLogin(Long customerId);
    public Boolean isEulaAgreed(Long customerId);
    /**
     * Remove all customer devices
     * @param customerId customer id
     */
    public void removeAll(Long customerId);

    /**
     * Check whether specified customer device still active.
     * @param customerId customer id
     * @param tokenId token id
     * @return true if specified customer still active (status = 1)
     */
    public Boolean isActive(Long customerId, String tokenId);

    /**
     * Deactivate all customer devices.
     * @param customerId customer id
     */
    public void deactivateAll(Long customerId);
    public void deactivateSoftTokenAll(Long customerId);
    
    public void activateSoftToken(Long customerId, String deviceId);

    /**
     * Validate activation code.
     * @param customerId customer id
     * @param activationCode activation code
     * @param status required customer device status for validation process
     * @return customer device object
     */
    public CustomerDevice validateActivationCode(Long customerId, String activationCode, Integer status);


    /**
     * Remove inactive device
     * @param customerId customer id
     */
    public void removeInactiveDevices(Long customerId);

    /**
     * Validate if certain string of terminalId has been used/taken
     * @param terminalId terminalId of device
     * @return
     * @deprecated as terminalId might not unique as since March 1st 2012 generated based on customer account number
     */
    public boolean isTerminalIdAlreadyTaken(String terminalId);

    /**
     * Validate if it is a valid activation code
     * @param customerId
     * @param activationCode
     * @param status
     * @return
     */
    public boolean isValidActivationCode(Long customerId, String activationCode, Integer status);

    /**
     * return hard token device or null if not found
     * @param customerId
     * @return
     */
    public CustomerDevice getHardTokenDevice(Long customerId);

    /**
     * return record affected by update statement
     * @param customerId, customerDeviceId, pushId
     * @return
     */
    public int setPushId(Long customerId, Long customerDeviceId, String pushId);

	List<CustomerDevice> getAllActive();
	
	List<CustomerDevice> getAllActive(Long customerId);
	
	List<CustomerDevice>getAllActiveCustomerId();
	
	public CustomerDevice getByDeviceSoftToken(Long customerId, String deviceId);
}
