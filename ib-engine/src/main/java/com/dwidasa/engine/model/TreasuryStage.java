package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

public class TreasuryStage extends BaseObject implements Serializable {
    private static final long serialVersionUID = -7892877740109475079L;

    private String status;

    private Long transactionId;

    private Transaction transaction;

    private Long customerRegisterId;
    private CustomerRegister customerRegister;
    private Long senderId;
    private Customer sender;
    
    private Long officerId;
    private User officer;
    
    public TreasuryStage() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
    public Long getCustomerRegisterId() {
		return customerRegisterId;
	}

	public void setCustomerRegisterId(Long customerRegisterId) {
		this.customerRegisterId = customerRegisterId;
	}

	public CustomerRegister getCustomerRegister() {
		return customerRegister;
	}

	public void setCustomerRegister(CustomerRegister customerRegister) {
		this.customerRegister = customerRegister;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Customer getSender() {
		return sender;
	}

	public void setSender(Customer sender) {
		this.sender = sender;
	}

	public Long getOfficerId() {
		return officerId;
	}

	public void setOfficerId(Long officerId) {
		this.officerId = officerId;
	}

	public User getOfficer() {
		return officer;
	}

	public void setOfficer(User officer) {
		if (officer.getUsername() != null && !officer.getUsername().equals("") && !officer.getUsername().equals("0")) {
			this.officer = officer;	
		}
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof TreasuryStage)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        TreasuryStage that = (TreasuryStage) o;
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
