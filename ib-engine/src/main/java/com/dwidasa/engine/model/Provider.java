package com.dwidasa.engine.model;

import com.dwidasa.engine.ui.GenericSelectModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

public class Provider extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -5595325593037260943L;

    private String providerCode;
    private String providerName;
    private String inquiry;
    
    private String description;

    private Set<ProviderProduct> providerProducts;
    private Set<TransactionLimit> transactionLimits;
    
    private Boolean isActive;

    public Provider() {
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    public Set<ProviderProduct> getProviderProducts() {
        return providerProducts;
    }

    public void setProviderProducts(Set<ProviderProduct> providerProducts) {
        this.providerProducts = providerProducts;
    }

    public Set<TransactionLimit> getTransactionLimits() {
        return transactionLimits;
    }

    public void setTransactionLimits(Set<TransactionLimit> transactionLimits) {
        this.transactionLimits = transactionLimits;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Provider)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Provider that = (Provider) o;
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
        return getProviderCode();
    }

    public String getLabel(Locale locale) {
        return getProviderName();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getValue() {
        return getProviderCode();  //To change body of implemented methods use File | Settings | File Templates.
    }
    
    public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
    
    public Boolean getIsActive() {
		return isActive;
	}
    
}