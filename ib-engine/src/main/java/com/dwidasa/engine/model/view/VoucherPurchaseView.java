package com.dwidasa.engine.model.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Bundled class contain voucher purchase related data for presentation and/or object
 * state storage purpose. Instead of dirtying our POJO, we place transient
 * information here.
 *
 * @author rk
 */
public class VoucherPurchaseView implements BaseView {
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
     * Biller code, mandatory field.
     */
    private String billerCode;
    /**
     * Biller's product code, mandatory field.
     */
    private String productCode;
    /**
     * Product denomination, mandatory field.
     */
    private String denomination;
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
     * Biller name, placeholder field, its initialization is not required
     * for core engine.
     */
    private String billerName;
    /**
     * Product name, placeholder field, its initialization is not required
     * for core engine.
     */
    private String productName;
    /**
     * Provider name, mandatory field.
     */
    private String providerCode;
    /**
     * Provider name, placeholder field, its initialization is not required
     * for core engine.
     */
    private String providerName;

    /**
     * Transaction date, mandatory field.
     */
    private Date transactionDate;
    //untuk keperluan cetak resi IB
    protected String transactionDateString;
    /**
     * Flag to test whether customer reference will be saved to registration list
     * or not, true mean saved, mandatory field.
     */
	private Boolean save;
    /**
     * Customer input type for required information, whether from (L)ist or (M)anual entry,
     * mandatory field.
     */
    private String inputType;
    /**
     * Customer specific data, for this transaction type this field will contain
     * customer phone number being TOP UP, mandatory field.
     */
    private String customerReference;
    /**
     * Provider price, mandatory field.
     */
	private BigDecimal amount;
    /**
     * Fee indicator, mandatory field.
     */
    private String feeIndicator;
    /**
     * Provider fee, mandatory field.
     */
    private BigDecimal fee;
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
     * Serial number from biller, output field.
     */
    private String serialNumber;
    /**
     * Voucher expiration date, output field.
     */
    private Date windowPeriod;
    /**
     * To account type, output field, will be set during confirm phase.
     */
    private String toAccountType;
    /**
     * Customer description entered during transaction, optional field.
     */
    private String description;
    private String traceNumber;
    private String statusCode;
	private String cardData1;
    private String cardData2;
    private String bit22;
    private String ip;

    public VoucherPurchaseView() {
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

    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(String billerCode) {
        this.billerCode = billerCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
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

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
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

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getWindowPeriod() {
        return windowPeriod;
    }

    public void setWindowPeriod(Date windowPeriod) {
        this.windowPeriod = windowPeriod;
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

    public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }


    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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

    public String getBit22() {
        return bit22;
    }

    public void setBit22(String bit22) {
        this.bit22 = bit22;
    }

    public Boolean validate() {
        return Boolean.TRUE;
    }
    
    public void setIp(String ip) {
		this.ip = ip;
	}
    
    public String getIp() {
		return ip;
	}

    public CustomerRegister transform() {
        CustomerRegister cr = new CustomerRegister();

        cr.setCustomerId(getCustomerId());
        cr.setTransactionType(getTransactionType());
        cr.setCustomerReference(getCustomerReference());
        cr.setData1(getBillerCode());
        //cr.setData2(getProductCode());
        cr.setCreated(new Date());
        cr.setCreatedby(getCustomerId());
        cr.setUpdated(new Date());
        cr.setUpdatedby(getCustomerId());

        return cr;
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
