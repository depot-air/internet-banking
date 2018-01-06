package com.dwidasa.engine.model.view;

public class CustomerMerchant {
	
	private String customerUsername;
	private String terminalId;
	private int status;
	private String exception;
	
	public CustomerMerchant() {
		// TODO Auto-generated constructor stub
	}
	
	public String getCustomerUsername() {
		return customerUsername;
	}
	
	public String getTerminalId() {
		return terminalId;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getException() {
		return exception;
	}
	
	public void setCustomerUsername(String customerUsername) {
		this.customerUsername = customerUsername;
	}
	
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public void setException(String exception) {
		this.exception = exception;
	}

}
