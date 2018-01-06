package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProviderProduct extends BaseObject implements Serializable {
    private static final long serialVersionUID = 8622647497012505260L;

    private BigDecimal fee;

    private Long billerProductId;
    private Long providerId;

	private BillerProduct billerProduct;
	private Provider provider;
	private Boolean isActive;
	
	//lookup
	private String transactionType;

    public ProviderProduct() {
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Long getBillerProductId() {
        return billerProductId;
    }

    public void setBillerProductId(Long billerProductId) {
        this.billerProductId = billerProductId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public BillerProduct getBillerProduct() {
        return billerProduct;
    }

    public void setBillerProduct(BillerProduct billerProduct) {
        this.billerProduct = billerProduct;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }
    
    public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof ProviderProduct)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        ProviderProduct that = (ProviderProduct) o;
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
    
    
    public Boolean getIsActive() {
		return isActive;
	}
    
    public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
    
    
}