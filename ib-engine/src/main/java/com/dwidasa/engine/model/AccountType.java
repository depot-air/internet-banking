package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Set;

public class AccountType extends BaseObject implements Serializable {
    private static final long serialVersionUID = -236091830984600285L;

	private String accountName;
	private String accountType;

	private Set<CustomerAccount> customerAccounts;

    public AccountType() {
    }

    public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return this.accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public Set<CustomerAccount> getCustomerAccounts() {
		return this.customerAccounts;
	}

	public void setCustomerAccounts(Set<CustomerAccount> customerAccounts) {
		this.customerAccounts = customerAccounts;
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
        return getAccountType();
    }
}