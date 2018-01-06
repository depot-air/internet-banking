package com.dwidasa.engine.service.facade;

import java.util.Map;

/**
 * facade class for transaction confirmation authentication, whether it's use token or atm card mechanism
 * @author gandos
 *
 */
public interface TxAuthenticationService {
	/**
	 * 
	 * @param param
	 * @return
	 */
	boolean authenticate(Map<String, Object> param);
}
