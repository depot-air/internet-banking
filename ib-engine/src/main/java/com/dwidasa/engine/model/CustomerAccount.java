package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;

public class CustomerAccount extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -2183050362544997759L;

	private String accountNumber;
	private String cardNumber;
	private String isDefault;
	private Integer status;

    private Long accountTypeId;
    private Long currencyId;
    private Long customerId;
    private Long productId;

    private AccountType accountType;
	private Currency currency;
	private Customer customer;
	private Product product;

    public CustomerAccount() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String aDefault) {
        isDefault = aDefault;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Long accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CustomerAccount)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        CustomerAccount that = (CustomerAccount) o;
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
        return getAccountNumber();
    }

    public String getLabel(Locale locale) {
        return getAccountNumber();
    }

    public String getValue() {
        return getAccountNumber();
    }
}