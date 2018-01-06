package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class BatchContent extends BaseObject implements Serializable {
	private static final long serialVersionUID = 2564518938107572522L;
	
	private Long batchId;
	private String accountNumber;
	private String customerName;
	private BigDecimal amount;
	private String description;
	
	public Long getBatchId() {
		return batchId;
	}
	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object o) {
		 if (!(o instanceof BatchContent)) {
	            return false;
	        }
	        if (this == o) {
	            return true;
	        }

	        BatchContent that = (BatchContent) o;
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
		return accountNumber;
	}
	
}
