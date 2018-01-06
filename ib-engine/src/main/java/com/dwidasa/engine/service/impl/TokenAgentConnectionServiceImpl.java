package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.service.TokenAgentConnectionService;
import com.velis.vaadmin.VAAdmin;
import com.velis.vaagentapi.VAAgentAdvance;

@Service("tokenAgentConnectionService")
public class TokenAgentConnectionServiceImpl implements TokenAgentConnectionService, InitializingBean {

    @Autowired
    private ParameterDao parameterDao;
    
   private String ip;
   private int port;
   private int timeout;
   private String clientId;
   private String clientKey;
   private String adminId;
   private String adminKey;
   private int adminPort;

   private void init() throws Exception {
	   Parameter parIp = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_AGENT_IP);
	   Parameter parPort = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_AGENT_PORT);
	   Parameter parTimeout = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_TIMEOUT);
	   Parameter parAgentId = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_AGENT_ID);
	   Parameter parAgentKey = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_AGENT_KEY);
	   Parameter parAdminId = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_ADMIN_ID);
	   Parameter parAdminKey = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_ADMIN_KEY);
	   Parameter parAdminPort = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.TOKEN_ADMIN_PORT);
	   
	   this.ip = parIp.getParameterValue();
	   this.port = Integer.parseInt(parPort.getParameterValue() );
	   this.timeout = Integer.parseInt(parTimeout.getParameterValue());
	   this.clientId = parAgentId.getParameterValue();
       this.clientKey = parAgentKey.getParameterValue();
       this.adminId = parAdminId.getParameterValue();
       this.adminKey = parAdminKey.getParameterValue();
       this.adminPort = Integer.parseInt(parAdminPort.getParameterValue() );
   }
 
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
		
	}

	@Override
	public VAAgentAdvance getTokenAgent() throws Exception {
		VAAgentAdvance agent = new VAAgentAdvance(this.ip, this.port, this.timeout, this.clientId, this.clientKey);
		return agent;
	}

	@Override
	public VAAdmin getTokenAdmin() throws Exception {
		VAAdmin admin = new VAAdmin(this.ip, this.adminPort, this.timeout, this.adminId, this.adminKey);
		return admin;
	}
	
}
