package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionLimit extends BaseObject implements Serializable {
    private static final long serialVersionUID = 4865695514382971049L;

	private BigDecimal maxLimit;
	private BigDecimal minLimit;

    private Long providerId;
    private Long transactionTypeId;

	private Provider provider;
	private TransactionType transactiontype;

    public TransactionLimit() {
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    public BigDecimal getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(BigDecimal minLimit) {
        this.minLimit = minLimit;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public Long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public TransactionType getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(TransactionType transactiontype) {
        this.transactiontype = transactiontype;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TransactionLimit)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        TransactionLimit that = (TransactionLimit) o;
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