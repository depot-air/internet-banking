package com.dwidasa.admin.view;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/23/11
 * Time: 00:27 am
 */
public class ProviderProductView {
    private String productCode;
    private String productName;
    private String providerName;    
    private BigDecimal fee;
    private Long id;
    private Boolean isActive;
    
    private String transactionType;

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

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
    
	
	public Boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	
    
}
