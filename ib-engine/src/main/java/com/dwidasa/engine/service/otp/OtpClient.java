package com.dwidasa.engine.service.otp;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/15/11
 * Time: 11:11 AM
 */
public interface OtpClient {
    /**
     * Unregister customer's device from OTP server.
     * @param customerId customer id
     * @param deviceId device id
     * @exception com.dwidasa.engine.BusinessException if invalid response type returned from OTP Server
     */
    public void unregisterDevice(Long customerId, String deviceId);

    /**
     * Unregister user from OTP server.
     * @param customerId customer id
     * @exception com.dwidasa.engine.BusinessException if invalid response type returned from OTP Server
     */
    public void unregisterUser(Long customerId);
}
