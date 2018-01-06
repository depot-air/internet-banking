package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Class to hold historical data of transactions. This class name is exceptional,
 * the underlying table name is h_transaction instead of t_transaction_history.
 *
 * @author rk
 */
public class TransactionHistory extends BaseObject implements Serializable {
    private static final long serialVersionUID = -5358117387091397311L;

    private String accountNumber;
    private String currency;
    private String transactionIndicator;
    private BigDecimal transactionAmount;
    private String referenceNumber;
    private Date entryDate;
    private Date postingDate;
    private Date processingDate;
    private String description;

    public TransactionHistory() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionIndicator() {
        return transactionIndicator;
    }

    public void setTransactionIndicator(String transactionIndicator) {
        this.transactionIndicator = transactionIndicator;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public Date getPostingDate() {
        return postingDate;
    }

    public void setPostingDate(Date postingDate) {
        this.postingDate = postingDate;
    }

    public Date getProcessingDate() {
        return processingDate;
    }
    
	public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TransactionHistory)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        TransactionHistory that = (TransactionHistory) o;
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
