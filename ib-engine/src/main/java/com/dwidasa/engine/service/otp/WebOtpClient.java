package com.dwidasa.engine.service.otp;

/**
 * Connector interface to OTP server that serve as authenticator for
 * Web and mobile web token.
 *
 * @author rk
 */
public interface WebOtpClient extends OtpClient {
    /**
     * Register soft token to OTP server.
     * @param customerId customer id
     * @param tokenId token id
     * @param activationCode activation code
     * @return token id generated from OTP server
     * @exception com.dwidasa.engine.BusinessException if invalid response type returned from OTP Server
     */
    public String registerSoftToken(Long customerId, String tokenId, String activationCode);

    /**
     * Unregister soft token from OTP server.
     * @param customerId customer id
     * @return response type, valid response type is 250
     * @exception com.dwidasa.engine.BusinessException if invalid response type returned from OTP Server
     */
    public String unregisterSoftToken(Long customerId);

    /**
     * Check token validity.
     * @param customerId customer id
     * @param token token to validate
     * @return true if valid token is specified else otherwise
     */
    public Boolean validateToken(Long customerId, String token);
}
