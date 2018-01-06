package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Set;

public class Currency extends BaseObject implements Serializable {
    private static final long serialVersionUID = 3018649883933373944L;

	private String currencyCode;
	private String currencyName;
	private String swiftCode;

	private Set<CustomerAccount> customerAccounts;

    public Currency() {
    }

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return this.currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getSwiftCode() {
		return this.swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

    public Set<CustomerAccount> getCustomerAccounts() {
        return customerAccounts;
    }

    public void setCustomerAccounts(Set<CustomerAccount> customerAccounts) {
        this.customerAccounts = customerAccounts;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Currency)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Currency that = (Currency) o;
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
        return getCurrencyCode();
    }
}