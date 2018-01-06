package com.dwidasa.engine.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;

public class ProductDenomination extends BaseObject implements Serializable, GenericSelectModel, Comparable<ProductDenomination> {

	private static final long serialVersionUID = -8108630492502807955L;

	private String denomination;

    private Long billerProductId;

    private BillerProduct billerProduct;

    private Long defaultProviderId;
    
    private Provider defaultProvider;
    
	private Set<ProviderDenomination> providerDenominations;
	private String billerProductCode;
	private Boolean isActive;
	
	public ProductDenomination() {
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public Long getBillerProductId() {
        return billerProductId;
    }

    public void setBillerProductId(Long billerProductId) {
        this.billerProductId = billerProductId;
    }

    public BillerProduct getBillerProduct() {
        return billerProduct;
    }

    public void setBillerProduct(BillerProduct billerProduct) {
        this.billerProduct = billerProduct;
    }

    public Set<ProviderDenomination> getProviderDenominations() {
        return providerDenominations;
    }

    public void setProviderDenominations(Set<ProviderDenomination> providerDenominations) {
        this.providerDenominations = providerDenominations;
    }

	public Long getDefaultProviderId() {
		return defaultProviderId;
	}

	public void setDefaultProviderId(Long defaultProviderId) {
		this.defaultProviderId = defaultProviderId;
	}

	public Provider getDefaultProvider() {
		return defaultProvider;
	}

	public void setDefaultProvider(Provider defaultProvider) {
		this.defaultProvider = defaultProvider;
	}

    public String getBillerProductCode() {
		return billerProductCode;
	}

	public void setBillerProductCode(String billerProductCode) {
		this.billerProductCode = billerProductCode;
	}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProductDenomination)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        ProductDenomination that = (ProductDenomination) o;
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
        return getDenomination();
    }

    public String getLabel(Locale locale) {
        try {
            return NumberFormat.getInstance(locale).format(Long.valueOf(getDenomination()));
        } catch(Exception ex) {
            return getDenomination();
        }
    }

    public String getValue() {
        return getDenomination();
    }

    @Override
    public int compareTo(ProductDenomination o) {
        try {
            int denom1 = Integer.parseInt(this.getDenomination());
            int denom2 = Integer.parseInt(o.getDenomination());

            return denom1 - denom2;
        } catch(Exception ex) {
            return this.getDenomination().compareTo(o.getDenomination());
        }
    }
    
    public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
    
    public Boolean getIsActive() {
		return isActive;
	}
    
}