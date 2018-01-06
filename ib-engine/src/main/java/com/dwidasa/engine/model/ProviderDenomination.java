package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dwidasa.engine.ui.GenericSelectModel;

@JsonIgnoreProperties({"label", "value"})
public class ProviderDenomination extends BaseObject implements Serializable, GenericSelectModel, Comparator<ProviderDenomination> {
    private static final long serialVersionUID = 6294530868775214331L;

    private BigDecimal fee;
    private BigDecimal price;

    private Long productDenominationId;
    private Long providerId;

    private ProductDenomination productDenomination;
    private Provider provider;

    public ProviderDenomination() {
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getProductDenominationId() {
        return productDenominationId;
    }

    public void setProductDenominationId(Long productDenominationId) {
        this.productDenominationId = productDenominationId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public ProductDenomination getProductDenomination() {
        return productDenomination;
    }

    public void setProductDenomination(ProductDenomination productDenomination) {
        this.productDenomination = productDenomination;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ProviderDenomination)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        ProviderDenomination that = (ProviderDenomination) o;
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
        return String.valueOf(getPrice());
    }

    public String getLabel(Locale locale) {
        if (provider != null) {
            return provider.getProviderName() + " - " + NumberFormat.getInstance(locale).format(price);
        }

        return null;
    }

    public String getValue() {
        if (provider != null) {
            return provider.getProviderCode();
        }

        return null;
    }

    public int compare(ProviderDenomination o1, ProviderDenomination o2)
    {
    	Provider prov1 = o1.getProvider();
    	Provider prov2 = o2.getProvider();
    	int result;
    	if (prov1 != null && prov2 != null ) {
    		result = prov1.getProviderName().compareTo(prov2.getProviderName());
    		if (result != 0)
            {
                return result;
            }    		
    	}        
        return o1.getPrice().compareTo(o2.getPrice());
    }
    /*
    @Override
    public int compareTo(ProviderDenomination o) {
    	if (this.getProvider() != null && o.getProvider() != null ) {    		
    		int nameCmp = this.getProvider().getProviderName().compareTo(o.getProvider().getProviderName());
    		
    	}
        int denom1 = this.getPrice().intValue();
        int denom2 = o.getPrice().intValue();

        return denom1 - denom2;
    }
    */
}