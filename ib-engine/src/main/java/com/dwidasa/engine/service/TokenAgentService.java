package com.dwidasa.engine.service;

public interface TokenAgentService {

	  public abstract boolean verifyTokenNoChallenge(String paramString1, String paramString2);

	  public abstract boolean verifyTokenWithChallenge(String paramString1, String paramString2, String paramString3);

	  public abstract void addCustomerRetail(String username, String status, String customerName);
	  
	  public abstract String addCustomerRetailRegistration(String username, String customerName, String cif, String serialToken);
	  
}
