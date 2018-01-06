package com.dwidasa.engine.model.view;

import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 11:33 AM
 */
public class CashOutDelimaView extends PaymentView {
    /**
     * Customer Id, mandatory field.
     */
    private Long customerId;
    /**
     * Card number, mandatory field.
     */
    private String cardNumber;
    /**
     * Customer account number used for transaction, mandatory field.
     */
    private String accountNumber;
    /**
     * Account type, mandatory field.
     */
    private String accountType;
    /**
     * Transaction type being hold, mandatory field.
     */
    private String transactionType;
    /**
     * Type of merchant, mandatory field.
     */
    private String merchantType;
    /**
     * Terminal id, mandatory field.
     */
    private String terminalId;
    /**
     * Currency code, BPRKS used value 360 for IDR, mandatory field.
     */
    private String currencyCode;
    /**
     * Transaction date, mandatory field.
     */
    private Date transactionDate;
    /**
     * Customer specific data, for this transaction type this field will contain
     * transfer code, mandatory field.
     */
    private String customerReference;
    /**
     * Amount received
     */
	private BigDecimal amount;
    /**
     * Fee indicator, output field.
     */
    private String feeIndicator;
    /**
     * Fee, output field.
     */
    private BigDecimal fee;
    /**
     * Provider fee, output field.
     */
    private BigDecimal providerFee;
    /**
     * Biller reference, output field.
     */
    private String billerReference;
    /**
     * Sender name, output field.
     */
    private String senderName;
    /**
     * Sender id number, output field.
     */
    private String senderIdNumber;
    /**
     * Sender card type (KTP, Passport, SIM), output field.
     */
    private String senderCardType;
    /**
     * Sender gender (F or M), output field.
     */
    private String senderGender;
    /**
     * Sender address, output field.
     */
    private String senderAddress;
    /**
     * Sender city, output field.
     */
    private String senderCity;
    /**
     * Sender postal code, output field.
     */
    private String senderPostalCode;
    /**
     * Sender country, output field.
     */
    private String senderCountry;
    /**
     * Sender place of birth, output field.
     */
    private String senderPob;
    /**
     * Sender date of birth, output field.
     */
    private Date senderDob;
    /**
     * Sender phone number, output field.
     */
    private String senderPhoneNumber;

    /**
     * Receiver name, output field.
     */
    private String receiverName;
    /**
     * Receiver id number, mandatory field.
     */
    private String receiverIdNumber;
    /**
     * Receiver card type (KTP, Passport, SIM), output field.
     */
    private String receiverCardType;
    /**
     * Receiver gender (F or M), output field.
     */
    private String receiverGender;
    /**
     * Receiver address, output field.
     */
    private String receiverAddress;
    /**
     * Receiver city, output field.
     */
    private String receiverCity;
    /**
     * Receiver postal code, output field.
     */
    private String receiverPostalCode;
    /**
     * Receiver country, output field.
     */
    private String receiverCountry;
    /**
     * Receiver place of birth, output field.
     */
    private String receiverPob;
    /**
     * Receiver date of birth, output field.
     */
    private Date receiverDob;
    /**
     * Receiver phone number, output field.
     */
    private String receiverPhoneNumber;

    /**
     * Response code from core banking, output field,
     * will be set during execute phase.
     */
    private String responseCode;
    /**
     * Reference number, output field, will be set during execute phase.
     */
    private String referenceNumber;
    /**
     * To account type, output field, will be set during confirm phase.
     */
    private String toAccountType;
    /**
     * Customer description entered during transaction, optional field.
     */
    private String description;
    /**
     * Bit #48 of ISO message, internal field.
     */
    private String bit48;

    public CashOutDelimaView() {
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getFeeIndicator() {
        return feeIndicator;
    }

    public void setFeeIndicator(String feeIndicator) {
        this.feeIndicator = feeIndicator;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getProviderFee() {
        return providerFee;
    }

    public void setProviderFee(BigDecimal providerFee) {
        this.providerFee = providerFee;
    }

    public String getBillerReference() {
        return billerReference;
    }

    public void setBillerReference(String billerReference) {
        this.billerReference = billerReference;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderIdNumber() {
        return senderIdNumber;
    }

    public void setSenderIdNumber(String senderIdNumber) {
        this.senderIdNumber = senderIdNumber;
    }

    public String getSenderCardType() {
        return senderCardType;
    }

    public void setSenderCardType(String senderCardType) {
        this.senderCardType = senderCardType;
    }

    public String getSenderGender() {
        return senderGender;
    }

    public void setSenderGender(String senderGender) {
        this.senderGender = senderGender;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getSenderCity() {
        return senderCity;
    }

    public void setSenderCity(String senderCity) {
        this.senderCity = senderCity;
    }

    public String getSenderPostalCode() {
        return senderPostalCode;
    }

    public void setSenderPostalCode(String senderPostalCode) {
        this.senderPostalCode = senderPostalCode;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getSenderPob() {
        return senderPob;
    }

    public void setSenderPob(String senderPob) {
        this.senderPob = senderPob;
    }

    public Date getSenderDob() {
        return senderDob;
    }

    public void setSenderDob(Date senderDob) {
        this.senderDob = senderDob;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public void setSenderPhoneNumber(String senderPhoneNumber) {
        this.senderPhoneNumber = senderPhoneNumber;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverIdNumber() {
        return receiverIdNumber;
    }

    public void setReceiverIdNumber(String receiverIdNumber) {
        this.receiverIdNumber = receiverIdNumber;
    }

    public String getReceiverCardType() {
        return receiverCardType;
    }

    public void setReceiverCardType(String receiverCardType) {
        this.receiverCardType = receiverCardType;
    }

    public String getReceiverGender() {
        return receiverGender;
    }

    public void setReceiverGender(String receiverGender) {
        this.receiverGender = receiverGender;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getReceiverCity() {
        return receiverCity;
    }

    public void setReceiverCity(String receiverCity) {
        this.receiverCity = receiverCity;
    }

    public String getReceiverPostalCode() {
        return receiverPostalCode;
    }

    public void setReceiverPostalCode(String receiverPostalCode) {
        this.receiverPostalCode = receiverPostalCode;
    }

    public String getReceiverCountry() {
        return receiverCountry;
    }

    public void setReceiverCountry(String receiverCountry) {
        this.receiverCountry = receiverCountry;
    }

    public String getReceiverPob() {
        return receiverPob;
    }

    public void setReceiverPob(String receiverPob) {
        this.receiverPob = receiverPob;
    }

    public Date getReceiverDob() {
        return receiverDob;
    }

    public void setReceiverDob(Date receiverDob) {
        this.receiverDob = receiverDob;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
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

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBit48() {
        return bit48;
    }

    public void setBit48(String bit48) {
        this.bit48 = bit48;
    }

    public Boolean validate() {
        return Boolean.TRUE;
    }

    public CustomerRegister transform() {
        return null;
    }
}
