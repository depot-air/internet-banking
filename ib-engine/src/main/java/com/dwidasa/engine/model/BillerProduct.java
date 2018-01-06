package com.dwidasa.engine.model;

import com.dwidasa.engine.ui.GenericSelectModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

public class BillerProduct extends BaseObject implements Serializable, GenericSelectModel, Comparable<BillerProduct> {
	private static final long serialVersionUID = 6512595407918631384L;

	private String productCode;
	private String productName;

    private Long billerId;

    private Biller biller;

	private Set<ProviderProduct> providerProducts;
	private Set<ProductDenomination> productDenominations;

    public BillerProduct() {
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getBillerId() {
        return billerId;
    }

    public void setBillerId(Long billerId) {
        this.billerId = billerId;
    }

    public Biller getBiller() {
        return biller;
    }

    public void setBiller(Biller biller) {
        this.biller = biller;
    }

    public Set<ProviderProduct> getProviderProducts() {
        return providerProducts;
    }

    public void setProviderProducts(Set<ProviderProduct> providerProducts) {
        this.providerProducts = providerProducts;
    }

    public Set<ProductDenomination> getProductDenominations() {
        return productDenominations;
    }

    public void setProductDenominations(Set<ProductDenomination> productDenominations) {
        this.productDenominations = productDenominations;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BillerProduct)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        BillerProduct that = (BillerProduct) o;
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
        return getProductCode();
    }

    public String getLabel(Locale locale) {
        return getProductName();
    }

    public String getValue() {
        return getProductCode();
    }

    @Override
   	public int compareTo(BillerProduct o) {
    	return productName.compareTo(o.productName);
   	}
}