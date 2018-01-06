package com.dwidasa.engine.dao;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.CustomerSession;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 9:46 AM
 */
public interface CustomerSessionDao extends GenericDao<CustomerSession, Long> {
    /**
     * Get customer session object
     * @param customerId customer id
     * @param deviceId device id
     * @return customer session object
     * @exception BusinessException invalid session id, if no customer session
     *            could be identified based on input parameters
     */
    public CustomerSession get(Long customerId, String deviceId);

    /**
     * Get current session if present but create new one otherwise. This method will also
     * invalidate other session of the same user.
     * @param customerId customer id
     * @param deviceId device id
     * @return customer session object
     */
    public CustomerSession force(Long customerId, String deviceId);

    /**
     * Validate customer session object based on session id
     * @param customerId customer id
     * @param sessionId session id
     * @return 1 : valid, -1 : expired, 0 : invalidate
     */
    public Integer validate(Long customerId, String sessionId);

    /**
     * invalidate customer session except specified session's device id
     * @param customerId customer id
     * @param deviceId device id
     */
    public void invalidate(Long customerId, String deviceId);

    /**
     * remove all record in database
     */
	public void removeAll();
	
	public void deleteByCustomerId(Long customerId);
}
