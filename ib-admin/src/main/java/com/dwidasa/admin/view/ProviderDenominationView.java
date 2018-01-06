package com.dwidasa.admin.view;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 20/11/11
 * Time: 21:10
 */
public class ProviderDenominationView {
    private String providerCode;
    private Long id;
    private String providerName;
    private BigDecimal fee;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

}
