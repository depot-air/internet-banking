package com.dwidasa.engine.model;

import com.dwidasa.engine.ui.GenericSelectModel;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

public class Biller extends BaseObject implements Serializable, GenericSelectModel, Comparable<Biller>  {
   
	private static final long serialVersionUID = -4419835094306381303L;

	private String billerCode;
	private String billerName;

    private Long transactionTypeId;

    private TransactionType transactionType;

	private Set<BillerProduct> billerProducts;
	private Set<CustomerRegister> customerRegisters;
	private Boolean isActive;
	
    public Biller() {
    }

    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(String billerCode) {
        this.billerCode = billerCode;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    public Long getTransactionTypeId() {
        return transactionTypeId;
    }

    public void setTransactionTypeId(Long transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Set<BillerProduct> getBillerProducts() {
        return billerProducts;
    }

    public void setBillerProducts(Set<BillerProduct> billerProducts) {
        this.billerProducts = billerProducts;
    }

    public Set<CustomerRegister> getCustomerRegisters() {
        return customerRegisters;
    }

    public void setCustomerRegisters(Set<CustomerRegister> customerRegisters) {
        this.customerRegisters = customerRegisters;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Biller)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Biller that = (Biller) o;
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
        return getBillerCode();
    }

    public String getLabel(Locale locale) {
        return getBillerCode() +" - "+ getBillerName();
    }

    public String getValue() {
			return getBillerCode();
    }
    
    
    
    public String getTransactionTypeName() {
    	if (transactionType == null || transactionType.getDescription() == null) return "-";
    	return transactionType.getDescription();
    }

    @Override
   	public int compareTo(Biller blr) {
    	return billerName.compareTo(blr.billerName);
   	}

    public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
    
    public Boolean getIsActive() {
		return isActive;
	}

}