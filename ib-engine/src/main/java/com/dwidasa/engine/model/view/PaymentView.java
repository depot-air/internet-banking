package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;

/**
 * Base payment view class.
 * 
 * @author rk
 */
public class PaymentView implements BaseView {
    
    /**
     * Customer Id, mandatory field.
     */
    protected Long customerId;
    /**
     * Card number, mandatory field.
     */
    protected String cardNumber;
    /**
     * Customer account number used for transaction, mandatory field.
     */
    protected String accountNumber;
    /**
     * Account type, mandatory field.
     */
    protected String accountType;
    /**
     * Biller code, mandatory field.
     */
    protected String billerCode;
    /**
     * Biller name, placeholder field, its initialization is not required
     * for core engine.
     */
    protected String billerName;
    /**
     * Biller's product code, mandatory field.
     */
    protected String productCode;
    /**
     * Product name, placeholder field, its initialization is not required
     * for core engine.
     */
    protected String productName;
    /**
     * Provider code, mandatory field if transfer for other bank,
     * otherwise optional.
     */
    protected String providerCode;
    /**
     * Provider name, placeholder field, its initialization is not required
     * for core engine.
     */
    protected String providerName;
    /**
     * Transaction type being hold, mandatory field.
     */
    protected String transactionType;
    /**
     * Type of merchant, mandatory field.
     */
    protected String merchantType;
    /**
     * Terminal id, mandatory field.
     */
    protected String terminalId;
    /**
     * Currency code, BPRKS used value 360 for IDR, mandatory field.
     */
    protected String currencyCode;
    /**
     * Transaction date, mandatory field.
     */
    protected Date transactionDate;
    //untuk keperluan cetak resi IB
    protected String transactionDateString;
    /**
     * Flag to test whether customer reference will be saved to registration list
     * or not, true mean saved, mandatory field.
     */
	protected Boolean save;
    /**
     * Customer input type for required information, whether from (L)ist or (M)anual entry,
     * mandatory field.
     */
    protected String inputType;
    /**
     * Customer specific data, for this transaction type this field will contain
     * customer account number (account number registered at payee company), mandatory field.
     */
    protected String customerReference;
    /**
     * Customer specific data, initialize during inquiry phase, output field.
     * Example usage of this field is card holder name, got from inquiry of card number stored in
     * customer reference field in Credit Card Payment transaction.
     */
    protected String referenceName;
    /**
     * Amount being paid.
     */
	protected BigDecimal amount;
    /**
     * Fee indicator, output field.
     */
    protected String feeIndicator;
    /**
     * Fee, optional field.
     */
    protected BigDecimal fee;
    /**
     * Response code from core banking, output field,
     * will be set during execute phase.
     */
    protected String responseCode;
    /**
     * Reference number, output field, will be set during execute phase.
     */
    protected String referenceNumber;
    /**
     * To account type, output field, will be set during confirm phase.
     */
    protected String toAccountType;
    /**
     * Customer description entered during transaction, optional field.
     */
    protected String description;
    /**
     * Total amount to be paid. Output field.
     */
    protected BigDecimal total;
    /**
     * Bit #48 of ISO message. Internal field.
     */
    protected String bit48;
    private String traceNumber;
    private String cardData1;
    private String cardData2;
    private String bit22;

    public PaymentView() {
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

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
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

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName = referenceName;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
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

    public String getBit22() {
        return bit22;
    }

    public void setBit22(String bit22) {
        this.bit22 = bit22;
    }

    public Boolean validate() {
        return Boolean.TRUE;
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

	public CustomerRegister transform() {
        CustomerRegister cr = new CustomerRegister();

        cr.setCustomerId(getCustomerId());
        cr.setTransactionType(getTransactionType());
        cr.setCustomerReference(getCustomerReference());
        cr.setData1(getBillerCode());
        cr.setData2(getToAccountType());
        cr.setData3(getBillerName());
        cr.setData4(getReferenceName());
        cr.setCreated(new Date());
        cr.setCreatedby(getCustomerId());
        cr.setUpdated(new Date());
        cr.setUpdatedby(getCustomerId());

        return cr;
    }
}
