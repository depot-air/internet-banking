package com.dwidasa.engine.service.otp;

/**
 * Connector interface to OTP server that serve as authenticator for
 * Native Mobile Device token.
 *
 * @author rk
 */
public interface NativeOtpClient extends OtpClient {
    /**
     * Register customer's device to OTP server.
     * @param customerid customer id
     * @param deviceId device id which is token id
     * @param activationCode activation code
     * @exception com.dwidasa.engine.BusinessException if invalid response type returned from OTP Server
     */
    public void registerDevice(Long customerid, String deviceId, String activationCode);

    /**
     * Validate token with specified challenge to OTP server.
     * @param customerId customer id
     * @param deviceId device id
     * @param token generated token
     * @param challenge challenge
     * @return true if valid token is specified else otherwise
     */
    public Boolean validateToken(Long customerId, String deviceId, String token, String challenge);

    public Boolean validateToken2(Long customerId, String deviceId, String token, String challenge);
}
