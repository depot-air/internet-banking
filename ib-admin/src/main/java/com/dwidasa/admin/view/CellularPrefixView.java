package com.dwidasa.admin.view;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/13/11
 * Time: 6:44 PM
 */
public class CellularPrefixView {
    private Long id;
    private String prefix;
    private Integer status;
    private String productName;

    public CellularPrefixView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
