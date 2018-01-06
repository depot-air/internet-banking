package com.dwidasa.engine.model.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/8/11
 * Time: 5:17 PM
 */
public class PlnPurchaseView implements BaseView {
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
     * customer's meter number
     */
    private String customerReference;
    /**
     * Customer name, output field.
     */
    private String referenceName;
    /**
     * Provider price, mandatory field.
     */
    private BigDecimal amount;
    /**
     * Fee indicator, output field.
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
     * To account type, output field, will be set during confirm phase.
     */
    private String toAccountType;
    /**
     * Customer description entered during transaction, optional field.
     */
    private String description;

    /**
     * Meter number. Output field.
     */
    private String meterNumber;
    /**
     * Power consumption category. Output field.
     */
    private String powerCategory;
    /**
     * Token avail 1, output field.
     */
    private BigDecimal unsold1;
    /**
     * Token avail 2, output field.
     */
    private BigDecimal unsold2;
    /**
     * Biller reference number, output field.
     */
    private String billerReference;
    /**
     * Payment amount, output field.
     */
    private BigDecimal paymentAmount;
    /**
     * Stamp, output field.
     */
    private BigDecimal stamp;
    /**
     * PPn, output field.
     */
    private BigDecimal ppn;
    /**
     * PPJ, output field.
     */
    private BigDecimal ppj;
    /**
     * Installment, output field.
     */
    private BigDecimal installment;
    /**
     * Token amount, output field.
     */
    private BigDecimal tokenAmount;
    /**
     * KWH, output field.
     */
    private String kwh;
    /**
     * Token number, output field.
     */
    private String tokenNumber;
    /**
     * If customer choose unsold, fill buying option with 1 else 0.
     * Conditional field - mandatory for execute phase.
     */
    private String buyingOption;
    /**
     * UPJ phone number, output field.
     */
    private String upjPhoneNumber;
    /**
     * Bit #48 of ISO message, internal field.
     */
    private String bit48;
    /**
     * Bit #62 of ISO message, internal field.
     */
    private String bit62;
    private String traceNumber;
    private String cardData1;
    private String cardData2;
    private String informasiStruk;

    //bsueb GSPRef

    private String gspRef;


    public PlnPurchaseView() {
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

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getPowerCategory() {
        return powerCategory;
    }

    public void setPowerCategory(String powerCategory) {
        this.powerCategory = powerCategory;
    }

    public BigDecimal getUnsold1() {
        return unsold1;
    }

    public void setUnsold1(BigDecimal unsold1) {
        this.unsold1 = unsold1;
    }

    public BigDecimal getUnsold2() {
        return unsold2;
    }

    public void setUnsold2(BigDecimal unsold2) {
        this.unsold2 = unsold2;
    }

    public String getBillerReference() {
        return billerReference;
    }

    public void setBillerReference(String billerReference) {
        this.billerReference = billerReference;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getStamp() {
        return stamp;
    }

    public void setStamp(BigDecimal stamp) {
        this.stamp = stamp;
    }

    public BigDecimal getPpn() {
        return ppn;
    }

    public void setPpn(BigDecimal ppn) {
        this.ppn = ppn;
    }

    public BigDecimal getPpj() {
        return ppj;
    }

    public void setPpj(BigDecimal ppj) {
        this.ppj = ppj;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public BigDecimal getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(BigDecimal tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public String getKwh() {
        return kwh;
    }

    public void setKwh(String kwh) {
        this.kwh = kwh;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getBuyingOption() {
        return buyingOption;
    }

    public void setBuyingOption(String buyingOption) {
        this.buyingOption = buyingOption;
    }

     public String getUpjPhoneNumber() {
        return upjPhoneNumber;
    }

    public void setUpjPhoneNumber(String upjPhoneNumber) {
        this.upjPhoneNumber = upjPhoneNumber;
    }

    public String getBit48() {
        return bit48;
    }

    public void setBit48(String bit48) {
        this.bit48 = bit48;
    }

    public String getBit62() {
        return bit62;
    }

    public void setBit62(String bit62) {
        this.bit62 = bit62;
    }

    public Boolean validate() {
        return Boolean.TRUE;
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

    public String getInformasiStruk() {
        return informasiStruk;
    }

    public void setInformasiStruk(String informasiStruk) {
        this.informasiStruk = informasiStruk;
    }

    public CustomerRegister transform() {
        CustomerRegister cr = new CustomerRegister();

        cr.setCustomerId(getCustomerId());
        cr.setTransactionType(getTransactionType());
        cr.setCustomerReference(getCustomerReference());
        cr.setData1(getBillerCode());
        cr.setData2(getReferenceName());
        cr.setCreated(new Date());
        cr.setCreatedby(getCustomerId());
        cr.setUpdated(new Date());
        cr.setUpdatedby(getCustomerId());

        return cr;
    }

    public String getGspRef()
    {
        //gspRef = getBit48().substring(86, 86+32);
        return gspRef;
    }

    public void setGspRef(String gspRef)
    {
        this.gspRef = gspRef;
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
