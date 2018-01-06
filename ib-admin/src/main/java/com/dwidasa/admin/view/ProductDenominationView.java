package com.dwidasa.admin.view;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 18/11/11
 * Time: 16:21
 */
public class ProductDenominationView {
    private String productCode;
    private String productName;
    private BigDecimal denomination;
    private Long id;
    private Boolean isActive;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDenomination() {
        return denomination;
    }

    public void setDenomination(BigDecimal denomination) {
        this.denomination = denomination;
    }
    
    public Boolean getIsActive() {
		return isActive;
	}
    
    public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
