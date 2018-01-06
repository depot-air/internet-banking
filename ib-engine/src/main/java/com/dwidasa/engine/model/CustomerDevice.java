package com.dwidasa.engine.model;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ui.GenericSelectModel;
import com.dwidasa.engine.util.EngineUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class CustomerDevice extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = 5601594820445566039L; //

	private String activatePin;
	private String deviceId;
    private String terminalId;
    private Date expiredDate;
	private String ime;
	private Integer status;
    private String encryptedActivatePin;
	private String pushId;
	private boolean softToken;

    private Long customerId;

	private Customer customer;

    public CustomerDevice() {
    }

    public String getActivatePin() {
        return activatePin;
    }

    public void setActivatePin(String activatePin) {
        this.activatePin = activatePin;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getEncryptedActivatePin() {
        encryptedActivatePin = EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, this.activatePin);
        return encryptedActivatePin;
    }

    public void setEncryptedActivatePin(String encryptedActivatePin) {
        this.encryptedActivatePin = encryptedActivatePin;
        this.activatePin = EngineUtils.decrypt(Constants.SERVER_SECRET_KEY, this.encryptedActivatePin);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CustomerDevice)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        CustomerDevice that = (CustomerDevice) o;
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

    public String getLabel(Locale locale) {
        String label = "";
        if (getDeviceId().endsWith(Constants.BLACKBERRY)) {
            label = "Blackberry";
        }
        else if (getDeviceId().endsWith(Constants.ANDROID)) {
            label = "Android";
        }
        else if (getDeviceId().endsWith(Constants.IPHONE)) {
            label = "iPhone";
        }
        else if(getDeviceId().endsWith(Constants.BB10)){
        	label = "BB10";
        }

        label += " - " + getIme();
        return label;
    }
    
    public void setSoftToken(boolean softToken) {
		this.softToken = softToken;
	}
    
    public boolean isSoftToken() {
		return softToken;
	}

    public String getValue() {
        return String.format("%1$-20s", getDeviceId()) + getLabel(null);
    }

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}
}