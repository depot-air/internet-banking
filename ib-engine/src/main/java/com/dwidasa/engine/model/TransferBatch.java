package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.Constants;

public class TransferBatch extends BaseObject implements Serializable {
    private static final long serialVersionUID = 2071238049699359438L;

    private Long customerId;    
    private String accountNumber;
    private String batchName;
    private String batchDescription;
    private Integer transferType; //1 - right now; 2 - post dated; Mandatory field.
	private Date transactionDate;
	private Date valueDate;
    private String referenceNumber;
    private String status;
    private BigDecimal totalAmount;
    
	private Long batchId;
    private List<TransferBatchContent> transferBatchContentList;        

  //untuk keperluan cetak resi IB
    private Date endDate;
    protected String transactionDateString;
    
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

	public Integer getTransferType() {
		return transferType;
	}

	public void setTransferType(Integer transferType) {
		this.transferType = transferType;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getBatchDescription() {
		return batchDescription;
	}

	public void setBatchDescription(String batchDescription) {
		this.batchDescription = batchDescription;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setTransferBatchContentList(List<TransferBatchContent> transferBatchContentList) {
		this.transferBatchContentList = transferBatchContentList;
	}
	
	public List<TransferBatchContent> getTransferBatchContentList() {
		return transferBatchContentList;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

    public String getTransactionDateString() {
    	if (transactionDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat(Constants.RESI_DATETIME);
    		this.transactionDateString = sdf.format(transactionDate);    		 
    	}
		return transactionDateString;
	}

	public void setTransactionDateString(String transactionDateString) {
		this.transactionDateString = transactionDateString;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TransferBatch)) {
			return false;
		}
		if (this == o) {
			return true;
		}

		TransferBatch that = (TransferBatch) o;
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