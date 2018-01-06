package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 9/4/12
 */
public class MobileRegistrationView implements BaseView {
    public Long customerId;
    private String cardNumber;
    private String accountNumber;
    private String merchantType;
    private String terminalId;
    private String currencyCode;
    private String accountType;
    private String cifNumber;

    private String customerReference;
    private Date transactionDate;
    //untuk keperluan cetak resi IB
    protected String transactionDateString;
    private String transactionType;
    private String responseCode;
    private String referenceNumber;
    private String tinMobile;
    private String encryptedTinMobile;
    private String cardData1;
    private String cardData2;

//    private String phoneNumber;
    private String activationCode;
    private int totAccount;
    List<AccountView> accountViews;        //selalu 1 ga dipake
    //private No Rekening   an10  Nomor rekening -Rata kiri -Padding with space
    //Nama Pemilik Rekiening an30  -Rata kiri -Space  leading padding
    private String referenceName;
    //Tipe Rekening  an1  -Rata kiri -Spcae  leading padding
    //Produk Tabungan   an3  -Rata kiri -Space  leading padding
    private String productCode;
    private String productName;
    private String traceNumber;

    /**
     * Bit #48 of ISO message. Internal field.
     */
    protected String bit48;

    public MobileRegistrationView() {
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

    public String getCifNumber() {
        return cifNumber;
    }

    public void setCifNumber(String cifNumber) {
        this.cifNumber = cifNumber;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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
        return transactionType;
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

//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public int getTotAccount() {
        return totAccount;
    }

    public void setTotAccount(int totAccount) {
        this.totAccount = totAccount;
    }

    public List<AccountView> getAccountViews() {
        return accountViews;
    }

    public void setAccountViews(List<AccountView> accountViews) {
        this.accountViews = accountViews;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getTinMobile() {
        return tinMobile;
    }

    public void setTinMobile(String tinMobile) {
        this.tinMobile = tinMobile;
    }

    public String getEncryptedTinMobile() {
        return encryptedTinMobile;
    }

    public void setEncryptedTinMobile(String encryptedTinMobile) {
        this.encryptedTinMobile = encryptedTinMobile;
    }

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
    }

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

    public String getTransactionDateString() {
    	if (transactionDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat(Constants.RESI_DATETIME);
    		this.transactionDateString = sdf.format(transactionDate);    		 
    	}
		return transactionDateString;
	}

	public void setTransactionDateString(String transactionDateString) {
		this.transactionDateString = transactionDateString;
	}

}
