package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TransferBatchContent extends BaseObject implements Serializable {
	private static final long serialVersionUID = 7535840756902541836L;
	
	private Long transferBatchId;
	private String accountNumber;
	private String customerName;
	private BigDecimal amount;
	private Date valueDate;
	private String status;
	
	public Long getTransferBatchId() {
		return transferBatchId;
	}
	public void setTransferBatchId(Long transferBatchId) {
		this.transferBatchId = transferBatchId;
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
	public Date getValueDate() {
		return valueDate;
	}
	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TransferBatchContent)) {
			return false;
		}
		if (this == o) {
			return true;
		}

		TransferBatchContent that = (TransferBatchContent) o;
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
