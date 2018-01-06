package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TransferSchedule extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int batchId;
	private String accountFrom;
	private String accountTo;
	private BigDecimal amount;
	private String news;
	private String batchType;
	private String transferDate;
	private String transferEnd;
	private int flagSent;
	  
	private Customer customer;
	private Batch batch;   

	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public String getAccountFrom() {
		return accountFrom;
	}
	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}
	public String getAccountTo() {
		return accountTo;
	}
	public void setAccountTo(String accountTo) {
		this.accountTo = accountTo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getNews() {
		return news;
	}
	public void setNews(String news) {
		this.news = news;
	}
	public String getBatchType() {
		return batchType;
	}
	public void setBatchType(String batchType) {
		this.batchType = batchType;
	}
	public String getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(String transferDate) {
		this.transferDate = transferDate;
	}
	public String getTransferEnd() {
		return transferEnd;
	}
	public void setTransferEnd(String transferEnd) {
		this.transferEnd = transferEnd;
	}
	public int getFlagSent() {
		return flagSent;
	}
	public void setFlagSent(int flagSent) {
		this.flagSent = flagSent;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Batch getBatch() {
		return batch;
	}
	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TransferSchedule)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        TransferSchedule that = (TransferSchedule) o;
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
