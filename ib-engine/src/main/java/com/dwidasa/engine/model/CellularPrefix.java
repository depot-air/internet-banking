package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

public class CellularPrefix extends BaseObject implements Serializable {
    private static final long serialVersionUID = 4414653027984259752L;

    private String prefix;
    private Integer status;

    private Long billerProductId;

    private BillerProduct billerProduct;
    //lookup
    private String transactionType;

    public CellularPrefix() {
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
    
    public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof CellularPrefix)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        CellularPrefix that = (CellularPrefix) o;
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
