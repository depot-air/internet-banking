package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

public class Merchant extends BaseObject implements Serializable {
    private static final long serialVersionUID = -8850288282774910086L;

	private String description;
	private String merchantType;

    public Merchant() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Merchant)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Merchant that = (Merchant) o;
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
        return getMerchantType();
    }
}