package com.dwidasa.engine.service.impl.facade;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.facade.AtmCardAuthenticationService;
import com.dwidasa.engine.service.facade.TxAuthenticationService;
import com.dwidasa.engine.service.facade.WebAuthenticationService;
/**
 * 
 * @author gandos
 *
 */
@Service("txAuthenticationService")
public class TxAuthenticationServiceImpl implements TxAuthenticationService{
	
	@Autowired
	private AtmCardAuthenticationService atmCardAuthenticationService;
	
	@SuppressWarnings("unused")
	@Autowired
	private WebAuthenticationService webAuthenticationService;
	/**
	 * in a normal case i would hate using Map as a method argument, 
	 * it break class specialization. and in the end it will also 
	 * complicate the code,	but .....
	 */
	@SuppressWarnings("unused")
	@Override
	public boolean authenticate(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Long customerId = (Long) param.get("customerId");
		String deviceId = (String) param.get("deviceId");
		String sessionId = (String) param.get("sessionId");
		
		if(!isNullValue(param, "token")) {			
			String token = (String) param.get("token");
			
			return true;//webAuthenticationService.authenticateToken(customerId, deviceId, sessionId, token);
		}
		else if(!isNullValue(param, "pin")) {
			String cardData = (String) param.get("cardData");
			String pin = (String) param.get("pin");
			
			return atmCardAuthenticationService.authenticate(customerId, deviceId, sessionId, cardData, pin);
		}
		else {
			return false;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private boolean isNullValue(Map param, String key) {
		return param.get(key) == null ? true : false;
	}

}
