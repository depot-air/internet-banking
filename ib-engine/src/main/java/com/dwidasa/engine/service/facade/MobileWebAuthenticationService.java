package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.BusinessException;

import java.util.List;

/**
 * This class is combination from <code>WebAuthenticationService</code> and
 * <code>NativeAuthenticationService</code>. Both pin and token are used to authenticate.
 *
 * @author rk
 */
public interface MobileWebAuthenticationService extends WebAuthenticationService {
    /**
     * request token for mobile web application
     * @param customerId customer id
     * @param tokenNumber number of token to return
     * @return list of token
     * @exception BusinessException if invalid response type returned from OTP Server
     */
    public List<String> requestToken(Long customerId, Integer tokenNumber);
}
