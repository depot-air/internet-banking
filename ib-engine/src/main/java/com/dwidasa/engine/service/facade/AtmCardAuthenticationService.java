package com.dwidasa.engine.service.facade;

/**
 * authenticate customer atm card validity
 * @author gandos
 *
 */
public interface AtmCardAuthenticationService {
	/**
	 * called when customer login with card
	 * @param cardData card iso track data
	 * @param pin card pin
	 * @return
	 */
	boolean authenticate(Long customerId, String deviceId, String sessionId, String cardData, String pin);
}
