package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.model.view.CustomerView;

public class IbMerchant extends BaseObject implements Serializable {
    private static final long serialVersionUID = -7657046284541817145L;

    private Long customerId;
	private String terminalId;
	private String serialNumber;
	private String status;
	
	private CustomerView customerView;

	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public String getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public CustomerView getCustomerView() {
		return customerView;
	}
	public void setCustomerView(CustomerView customerView) {
		this.customerView = customerView;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	@Override
	public boolean equals(Object o) {
        if (!(o instanceof IbMerchant)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        IbMerchant that = (IbMerchant) o;
        return new EqualsBuilder().append(this.getId(), that.getId())
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
