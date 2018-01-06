package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Set;

public class TransactionType extends BaseObject implements Serializable {
    private static final long serialVersionUID = 8412567494828984618L;

	private String description;
	private String transactionType;
    private Integer financial;

	private Set<Biller> billers;

    public TransactionType() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Integer getFinancial() {
        return financial;
    }

    public void setFinancial(Integer financial) {
        this.financial = financial;
    }

    public Set<Biller> getBillers() {
        return billers;
    }

    public void setBillers(Set<Biller> billers) {
        this.billers = billers;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AccountType)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AccountType that = (AccountType) o;
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
        return getTransactionType();
    }
}