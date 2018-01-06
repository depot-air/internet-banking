package com.dwidasa.engine.model;

import java.util.Date;

public class HardTokenModel {
	private Long customerId;
	private String serialNumber;
	private String token;
	private String apply;
	private Date created;
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setApply(String apply) {
		this.apply = apply;
	}
	public String getapply() {
		return apply;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}	
}
