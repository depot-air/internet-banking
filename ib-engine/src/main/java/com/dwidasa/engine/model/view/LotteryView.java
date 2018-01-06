package com.dwidasa.engine.model.view;

import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 1/31/12
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotteryView implements BaseView {
    public Long customerId;
    private String cardNumber;
    private String accountNumber;
    private String merchantType;
    private String terminalId;
    private String currencyCode;
    private String accountType;
//    private String customerName;
    private String periodMonth;
    private String periodYear;
    private long startPointNumber;
    private long endPointNumber;
    private Date transactionDate;
    private String transactionType;
    private String responseCode;
    private String referenceNumber;
    private String cardData1;
    private String cardData2;

    /**
     * Bit #48 of ISO message. Internal field.
     */
    protected String bit48;

    public LotteryView() {
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
/*
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
*/
    public String getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(String periodMonth) {
        this.periodMonth = periodMonth;
    }

    public String getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(String periodYear) {
        this.periodYear = periodYear;
    }

    public long getStartPointNumber() {
        return startPointNumber;
    }

    public void setStartPointNumber(long startPointNumber) {
        this.startPointNumber = startPointNumber;
    }

    public long getEndPointNumber() {
        return endPointNumber;
    }

    public void setEndPointNumber(long endPointNumber) {
        this.endPointNumber = endPointNumber;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getBit48() {
        return bit48;
    }

    public void setBit48(String bit48) {
        this.bit48 = bit48;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
    @Override
    public String getTransactionType() {
        return transactionType;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public BigDecimal getAmount() {
        return null;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
    @Override
    public String getResponseCode() {
        return responseCode;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }
    @Override
    public String getReferenceNumber() {
        return referenceNumber;
    }

    @Override
    public Boolean validate() {
        return Boolean.TRUE;
    }

    @Override
    public CustomerRegister transform() {
        return null;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }


    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCardData1() {
        return cardData1;
    }

    public void setCardData1(String cardData1) {
        this.cardData1 = cardData1;
    }

    public String getCardData2() {
        return cardData2;
    }

    public void setCardData2(String cardData2) {
        this.cardData2 = cardData2;
    }
}
