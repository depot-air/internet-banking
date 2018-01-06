package com.dwidasa.engine.service;

import com.velis.vaadmin.VAAdmin;
import com.velis.vaagentapi.VAAgentAdvance;


public interface TokenAgentConnectionService {
	
	public abstract VAAgentAdvance getTokenAgent() 
			throws Exception;
			
	public abstract VAAdmin getTokenAdmin() 
			throws Exception;
}
