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
public class CashInDelimaView extends PaymentView {
    /**
     * Provider fee, output field.
     */
    private BigDecimal providerFee;
    /**
     * Biller reference, output field.
     */
    private String billerReference;
    /**
     * Sender name, mandatory field.
     */
    private String senderName;
    /**
     * Sender id number, mandatory field.
     */
    private String senderIdNumber;
    /**
     * Sender card type (KTP, Passport, SIM), mandatory field.
     */
    private String senderCardType;
    /**
     * Sender gender (F or M), mandatory field.
     */
    private String senderGender;
    /**
     * Sender address, mandatory field.
     */
    private String senderAddress;
    /**
     * Sender city, mandatory field.
     */
    private String senderCity;
    /**
     * Sender postal code, mandatory field.
     */
    private String senderPostalCode;
    /**
     * Sender country, mandatory field.
     */
    private String senderCountry;
    /**
     * Sender place of birth, mandatory field.
     */
    private String senderPob;
    /**
     * Sender date of birth, mandatory field.
     */
    private Date senderDob;
    /**
     * Sender phone number, mandatory field.
     */
    private String senderPhoneNumber;

    private String senderProfession;
    private String senderNationality;
    private String newsMoneySource; 
    private String newsMoneyPurpose;
    private String receiverProfession;
    private String receiverNationality;
    
    /**
     * Receiver name, mandatory field.
     */
    private String receiverName;
    /**
     * Receiver id number, mandatory field.
     */
    private String receiverIdNumber;
    /**
     * Receiver card type (KTP, Passport, SIM), mandatory field.
     */
    private String receiverCardType;
    /**
     * Receiver gender (F or M), mandatory field.
     */
    private String receiverGender;
    /**
     * Receiver address, mandatory field.
     */
    private String receiverAddress;
    /**
     * Receiver city, mandatory field.
     */
    private String receiverCity;
    /**
     * Receiver postal code, mandatory field.
     */
    private String receiverPostalCode;
    /**
     * Receiver country, mandatory field.
     */
    private String receiverCountry;
    /**
     * Receiver place of birth, mandatory field.
     */
    private String receiverPob;
    /**
     * Receiver date of birth, mandatory field.
     */
    private Date receiverDob;
    /**
     * Receiver phone number, mandatory field.
     */
    private String receiverPhoneNumber;

    private String receiverBank;
    private String receiverAccount;
    
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
    private String traceNumber;
    private String cardData1;
    private String cardData2;

    public CashInDelimaView() {
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

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
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

    public Boolean validate() {
        return Boolean.TRUE;
    }

    public CustomerRegister transform() {
        return null;
    }

	public String getSenderProfession() {
		return senderProfession;
	}

	public void setSenderProfession(String senderProfession) {
		this.senderProfession = senderProfession;
	}

	public String getSenderNationality() {
		return senderNationality;
	}

	public void setSenderNationality(String senderNationality) {
		this.senderNationality = senderNationality;
	}

	public String getNewsMoneySource() {
		return newsMoneySource;
	}

	public void setNewsMoneySource(String newsMoneySource) {
		this.newsMoneySource = newsMoneySource;
	}

	public String getNewsMoneyPurpose() {
		return newsMoneyPurpose;
	}

	public void setNewsMoneyPurpose(String newsMoneyPurpose) {
		this.newsMoneyPurpose = newsMoneyPurpose;
	}

	public String getReceiverBank() {
		return receiverBank;
	}

	public void setReceiverBank(String receiverBank) {
		this.receiverBank = receiverBank;
	}

	public String getReceiverAccount() {
		return receiverAccount;
	}

	public void setReceiverAccount(String receiverAccount) {
		this.receiverAccount = receiverAccount;
	}

	public String getReceiverProfession() {
		return receiverProfession;
	}

	public void setReceiverProfession(String receiverProfession) {
		this.receiverProfession = receiverProfession;
	}

	public String getReceiverNationality() {
		return receiverNationality;
	}

	public void setReceiverNationality(String receiverNationality) {
		this.receiverNationality = receiverNationality;
	}

}
