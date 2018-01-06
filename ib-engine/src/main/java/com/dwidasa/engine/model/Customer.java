package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.util.EngineUtils;

public class Customer extends BaseObject implements Serializable {
    private static final long serialVersionUID = -7657046284541817144L;

	private String cifNumber;
	private String customerEmail;
	private String customerName;
	private String customerPhone;
	private String customerPin;
	private String customerUsername;
	private Integer failedAuthAttempts;
	private Date lastLogin;
	private String lastTokenId;
	private String firstLogin;
	private String mobileWebTokenId;
	private Integer status;
	private String tokenActivated;
    private String encryptedCustomerPin;

	private Set<CustomerAccount> customerAccounts;
	private Set<CustomerData> customerData;
	private Set<CustomerDevice> customerDevices;
	private Set<CustomerSession> customerSessions;

    public Customer() {
    }

    public String getCifNumber() {
        return cifNumber;
    }

    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPin() {
        return customerPin;
    }

    public void setCustomerPin(String customerPin) {
        this.customerPin = customerPin;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public Integer getFailedAuthAttempts() {
        return failedAuthAttempts;
    }

    public void setFailedAuthAttempts(Integer failedAuthAttempts) {
        this.failedAuthAttempts = failedAuthAttempts;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getLastTokenId() {
        return lastTokenId;
    }

    public void setLastTokenId(String lastTokenId) {
        this.lastTokenId = lastTokenId;
    }

    public String getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(String firstLogin) {
        this.firstLogin = firstLogin;
    }

    public String getMobileWebTokenId() {
        return mobileWebTokenId;
    }

    public void setMobileWebTokenId(String mobileWebTokenId) {
        this.mobileWebTokenId = mobileWebTokenId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTokenActivated() {
        return tokenActivated;
    }

    public void setTokenActivated(String tokenActivated) {
        this.tokenActivated = tokenActivated;
    }

    public Set<CustomerAccount> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(Set<CustomerAccount> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }

    public Set<CustomerData> getCustomerData() {
        return customerData;
    }

    public void setCustomerData(Set<CustomerData> customerData) {
        this.customerData = customerData;
    }

    public Set<CustomerDevice> getCustomerDevices() {
        return customerDevices;
    }

    public void setCustomerDevices(Set<CustomerDevice> customerDevices) {
        this.customerDevices = customerDevices;
    }

    public Set<CustomerSession> getCustomerSessions() {
        return customerSessions;
    }

    public void setCustomerSessions(Set<CustomerSession> customerSessions) {
        this.customerSessions = customerSessions;
    }

    public String getEncryptedCustomerPin() {
    	if (customerPin != null) {
    		encryptedCustomerPin = EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, customerPin);
    	}
        return encryptedCustomerPin;
    }

    public void setEncryptedCustomerPin(String encryptedCustomerPin) {
        this.encryptedCustomerPin = encryptedCustomerPin;
        if (encryptedCustomerPin.length() == 16) {
        	customerPin = EngineUtils.decrypt(Constants.SERVER_SECRET_KEY, this.encryptedCustomerPin);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Customer)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Customer that = (Customer) o;
        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}