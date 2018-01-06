package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TransactionQueue extends BaseObject implements Serializable {
	private static final long serialVersionUID = -127023726073694942L;

	public TransactionQueue() {
	}
	
	private Long customerId;
	private String accountNumber;
	private String billerName;
	private String customerReference;
	private BigDecimal amount;
	private BigDecimal fee;
	private String referenceNumber;
	
	/**
	 * Possible values:
	 * Constants.TRANSFER_CODE
	 * Constants.TRANSFER_OTHER_CODE
	 * Constants.TRANSFER_BATCH
	 */
	private String transactionType;
	
	/**
	 * Possible values:
	 * Constants.POSTDATED_ET (Transfer pada Tanggal xxxxxx) 
	 * Constants.PERIODIC_ET (Transfer Berkala)
	 */
	private String executionType;
	
	/**
	 * Possible values:
	 * if transactionType = Constants.TRANSFER_CODE or Constants.TRANSFER_OTHER_CODE, this value is json of TransferView object
	 * if transactionType = Constants.TRANSFER_BATCH, this value is t_transfer_batch id
	 */
	private String transactionData;
	
	/**
	 * transaction input date
	 */
	private Date transactionDate;
	
	/**
	 * transaction execution date
	 */
	private Date valueDate;
	
	
	private String deliveryChannel;
	private String deliveryChannelId;
	
	/**
	 * required if executionType equals to Constants.PERIODIC_ET
	 * Possible values:
	 * 1:every xx day
	 * 2:once in a week
	 * 3:once in a month
	 */
	private Integer periodType;
	
	/**
	 * required if executionType equals to Constants.PERIODIC_ET
	 * if periodType == 1, this value is number of day difference;
	 * if periodType == 2, this value is name of day in week;
	 * if periodType == 3, this value is date of month;
	 */
	private Integer periodValue;
	
	/**
	 * required if executionType equals to Constants.PERIODIC_ET
	 * Fill with end date of periodic transaction 
	 */
	private Date endDate;
	
	/**
	 * Possible values:
	 * Constants.QUEUED_STATUS = "QUEUED";
	 * Constants.CANCELED_STATUS = "CANCELED";
     * Constants.EXECUTED_STATUS = "EXECUTED"; 
	 */
	private String status;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getTransactionData() {
		return transactionData;
	}

	public void setTransactionData(String transactionData) {
		this.transactionData = transactionData;
	}

	public String getDeliveryChannel() {
		return deliveryChannel;
	}

	public void setDeliveryChannel(String deliveryChannel) {
		this.deliveryChannel = deliveryChannel;
	}

	public String getDeliveryChannelId() {
		return deliveryChannelId;
	}

	public void setDeliveryChannelId(String deliveryChannelId) {
		this.deliveryChannelId = deliveryChannelId;
	}
	
	public Integer getPeriodType() {
		return periodType;
	}

	public void setPeriodType(Integer periodType) {
		this.periodType = periodType;
	}

	public Integer getPeriodValue() {
		return periodValue;
	}

	public void setPeriodValue(Integer periodValue) {
		this.periodValue = periodValue;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
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
	
	public BigDecimal getAmountAndFee() {
		if (amount == null) {
			return fee;
		} else {
			if (fee == null)
				return amount;
			return amount.add(fee);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TransactionQueue)) {
			return false;
		}
		if (this == o) {
			return true;
		}

		TransactionQueue that = (TransactionQueue) o;
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
