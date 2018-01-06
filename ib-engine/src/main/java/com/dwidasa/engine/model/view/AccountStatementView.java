package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.Date;

import com.dwidasa.engine.model.CustomerRegister;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/19/11
 * Time: 7:26 PM
 */
public class AccountStatementView implements BaseView {
	/**
     * Customer id, output field.
     */
    private Long customerId;
    /**
     * Account number, output field.
     */
    private String accountNumber;
    /**
     * Transaction type, output field.
     */
    private String transactionType;
    /**
     * Transaction name, output field.
     */
    private String transactionName;
    /**
     * To account number, output field.
     */
    private String toAccountNumber;
    /**
     * Currency code, output field.
     */
    private String currencyCode;
    /**
     * Amount, output field.
     */
    private BigDecimal amount;
    //tambah kurawal untuk Db
    private String formattedAmount;
    /**
     * Running balance, output field.
     */
    private BigDecimal runningBalance;
    /**
     * Description, output field.
     */
    private String description;
    /**
     * Transaction date, output field.
     */
    private Date transactionDate;
    /**
     * Transaction indicator, i.e. debet; credit. Output field
     */
    private String transactionIndicator;
    /**
     * Value date, output field.
     */
    private Date valueDate;
    /**
     * Status after checking transaction to core, output field.
     * Possible value (0) Failed; (1) Succeed; (2) Pending; (3) Canceled.
     */
    private Integer status;
    /**
     * Response code, output field.
     */
    private String responseCode;
    /**
     * Reference number, output field.
     */
    private String referenceNumber;
    /**
     * Customer reference specific to a transaction, output field.
     */
    private String customerReference;
    /**
     * Transaction PK from t_transaction, output field.
     * Used for transaction history feature.
     */
    private Long transactionId;
    
    public AccountStatementView() {
    }

    public AccountStatementView(AccountStatementView that) {
        this.customerId = that.getCustomerId();
        this.accountNumber = that.getAccountNumber();
        this.transactionType = that.getTransactionType();
        this.transactionName = that.getTransactionName();
        this.toAccountNumber = that.getToAccountNumber();
        this.currencyCode = that.getCurrencyCode();
        this.amount = that.getAmount();
        this.description = that.getDescription();
        this.transactionDate = that.getTransactionDate();
        this.valueDate = that.getValueDate();
        this.status = that.getStatus();
        this.responseCode = that.getResponseCode();
        this.referenceNumber = that.getReferenceNumber();
        this.customerReference = that.getCustomerReference();
        this.transactionId = that.getTransactionId();
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getFormattedAmount() {    	
		return formattedAmount;
	}

	public void setFormattedAmount(String formattedAmount) {
		this.formattedAmount = formattedAmount;
	}

    public BigDecimal getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionIndicator() {
        return transactionIndicator;
    }

    public void setTransactionIndicator(String transactionIndicator) {
        this.transactionIndicator = transactionIndicator;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Boolean validate() {
        return Boolean.TRUE;
    }

    public CustomerRegister transform() {
        return null;
    }
    
    public String getStrTransactionIndicator() {
    	if ("D".equals(transactionIndicator)) return "Db";
    	if ("C".equals(transactionIndicator)) return "Cr";
    	return "";
    } 
}
