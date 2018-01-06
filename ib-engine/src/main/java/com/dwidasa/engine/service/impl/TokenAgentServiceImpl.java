package com.dwidasa.engine.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.service.TokenAgentConnectionService;
import com.dwidasa.engine.service.TokenAgentService;
import com.dwidasa.engine.service.UserService;
import com.velis.vaadmin.VAAdmin;
import com.velis.vaagentapi.VAAgentAdvance;

@Service("tokenAgentService")
public class TokenAgentServiceImpl implements TokenAgentService {
	private static Logger logger = Logger.getLogger( TokenAgentServiceImpl.class );
	@Autowired
	private TokenAgentConnectionService tokenAgentConnectionManager;
	
	@Autowired
	private UserService userService;
 
 
	public boolean verifyTokenNoChallenge(String userName, String sResponse) {
//		logger.info("userName : " + userName);
//		logger.info("sResponse : " + sResponse);
		try {
			VAAgentAdvance tokenAgent = this.tokenAgentConnectionManager.getTokenAgent();
			if (!tokenAgent.verifyRetailRO(generateTransId(), userName, 1, sResponse)) {
				logger.info("token.verifyRetailRO : " + tokenAgent.getError() + " " + userName);
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("tokenException", e);
		}
		return false;
   }

	public boolean verifyTokenWithChallenge(String userName, String challenge, String userInput) {
		logger.info("userName : " + userName);
		logger.info("challenge : " + challenge);
		logger.info("userInput : " + userInput);
		try {
			VAAgentAdvance tokenAgent = this.tokenAgentConnectionManager.getTokenAgent();
			if (!tokenAgent.verifyRetailCR(generateTransId(), userName, 2, challenge, userInput, tokenAgent.NOT_CHECK_CHALLENGE)) {
				logger.info("token.verifyRetailCR : " + tokenAgent.getError() + " " + userName);
				return false;
			}
			return true;
		} catch (Exception e) {
			logger.error("tokenException", e);
		}
		return false;
	}

	private String generateTransId() {
		return String.valueOf(System.currentTimeMillis());
	}

	@Override
	public String addCustomerRetailRegistration(String username, String customerName, String cif, String serialToken) {
		try {
			VAAdmin tokenAdmin = this.tokenAgentConnectionManager.getTokenAdmin();

			boolean result = tokenAdmin.addCustomerRetailChannel(username, username, "IBS", "1", serialToken);
//			boolean result = tokenAdmin.addCustomerRetail(username, tokenAdmin.CUSTOMER_STATUS_NOT_ACTIVE, customerName);
			String error = tokenAdmin.getError();
			if (result == false) {
				logger.error("Error addCustomerRetailRegistration: " + error);
				logger.error("Username : " + username);
				logger.error("Customer Name : " + customerName);
				logger.error("CIF : " + cif);
				logger.error("Serial Token : " + serialToken);
				return error;
			}
		} catch (Exception e) {
			logger.error("tokenException", e);
		}
		return "OK";
	}

	@Override
	public void addCustomerRetail(String username, String status, String customerName) {
		try {
			VAAdmin tokenAdmin = this.tokenAgentConnectionManager.getTokenAdmin();
			tokenAdmin.addCustomerRetail(username, "1", customerName);
		} catch (Exception e) {
			logger.error("tokenException", e);
		}
	}
	
//	@Override
//	public String isTokenValidForRetailRegistration(String serialToken) {
//		VAAdminMASPION admin = getVAAdmin();
//		boolean result = admin.isTokenValidForRetailRegistration(serialToken);
//		String error = admin.getError();
//		if (result == false) {
//			log.error("Error isTokenValidForRetailRegistration: " + admin.getError());
//			log.error("Serial Token : " + serialToken);
//		}
//		return error;
//	}

}
