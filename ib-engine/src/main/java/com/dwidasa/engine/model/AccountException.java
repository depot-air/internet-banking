package com.dwidasa.engine.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

//import com.dwidasa.engine.model.view.CustomerView;

public class AccountException extends BaseObject implements Serializable {
    private static final long serialVersionUID = -7657046284541817145L;

    private String accountNumber;
	
//	private CustomerView customerView;

//	public CustomerView getCustomerView() {
//		return customerView;
//	}
//	public void setCustomerView(CustomerView customerView) {
//		this.customerView = customerView;
//	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@Override
	public boolean equals(Object o) {
        if (!(o instanceof AccountException)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AccountException that = (AccountException) o;
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
