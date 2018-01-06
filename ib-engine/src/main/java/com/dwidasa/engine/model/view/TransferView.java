package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;

/**
 * Bundled class contain transfer related data for presentation and/or object
 * state storage purpose.
 *
 * @author rk
 */
public class TransferView implements BaseView {
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
     * Bank code, mandatory field.
     */
    private String billerCode;
    /**
     * Biller name, placeholder field.
     */
    private String billerName;
    /**
     * Provider code, mandatory field if transfer for other bank,
     * otherwise optional.
     */
    private String providerCode;
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
     * Contain receiver account number, mandatory field.
     */
    private String customerReference;
    /**
     * Account type, mandatory field.
     */
    private String toAccountType;
    /**
     * Receiver name, mandatory if transfer is internal transfer (same bank),
     * otherwise is optional.
     */
    private String receiverName;
    /**
     * Transaction date, mandatory field.
     */
    private Date transactionDate;
    //untuk keperluan cetak resi IB
    protected String transactionDateString;
    /**
     * Value date, when the real transaction happened, if post dated transaction,
     * this field will hold the post dated date instead of now.
     */
    private Date valueDate;
    /**
     * Flag to test whether customer reference will be saved to registration list
     * or not, true mean saved, mandatory field for mobile web & native otherwise optional.
     */
	private Boolean save;
    /**
     * Customer input type for required information, whether from (L)ist or (M)anual entry,
     * mandatory field.
     */
    private String inputType;
    /**
     * Transfer amount, mandatory field.
     */
    private BigDecimal amount;
    /**
     * Fee indicator, output field.
     */
    private String feeIndicator;
    /**
     * Transfer fee, mandatory field.
     */
    private BigDecimal fee;
    /**
     * Customer transfer message, optional field.
     */
    private String description;
    /**
     * Transfer message description for transfer to other bank, optional field
     */
    private String news;
    /**
     * Bank branch name for transfer to other bank, optional field
     */
    private String branchName;
    /**
     * Bank branch city for transfer to other bank, optional field
     */
    private String branchCity;
    /**
     * Transfer type, 1 - right now; 2 - post dated; 3 - period; Mandatory field.
     */
    private Integer transferType;
    /**
     * Period type,<br/>
     * 1 - every N day, N is number of day difference;<br/>
     * 2 - every N, N is DAY_OF_WEEK, name of a day (Sunday, Monday..);<br/>
     * 3 - every N, N is DAY_OF_MONTH, number of date in a month (1..31)</br>
     * Mandatory if value of <code>transferType</code>  is 3 otherwise optional.
     */
    private Integer periodType;
    /**
     * Period value, meaning of this value depend on periodType being selected.</br>
     * Mandatory if value of <code>transferType</code>  is 3 otherwise optional.
     */
    private Integer periodValue;
    /**
     * End date for periodic transfer, inclusive.
     * Mandatory if value of <code>transferType</code>  is 3 otherwise optional.
     */
    private Date endDate;

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
     * Pending flag, if transfer happened outside treasury working hours then true.
     * Output field.
     */
    private Boolean pending;

    private String custRefAtmb;
    private String senderName;
    private String inquiryRefNumber;

    private String traceNumber;
    private String cardData1;
    private String cardData2;
    private BigDecimal stan;
    private String bit42;
    private String bit43;
    private String bit48;
    private String terminalAddress;
    // --- Produk untuk VA ---
    private String productName;
 

    // bsueb hasil penjumlahan amount + fee
    private BigDecimal totalDebetAmount;
    // bsueb status transaksi
    private String transactionStatus;

    // bsueb terminal id view
    private String terminalIdView;

    /**
     * transaction queue id
     */
    private Long transactionQueueId;
    
	/**
     * status
     */
    private String status;

    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public TransferView() {
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

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
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

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public Integer getPeriodType() {
        return periodType;
    }

    public void setPeriodType(Integer periodType) {
        this.periodType = periodType;
    }

    public Integer getPeriodValue() {
        return periodValue;
    }

    public void setPeriodValue(Integer periodValue) {
        this.periodValue = periodValue;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Boolean validate() {
        return Boolean.TRUE;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(String branchCity) {
        this.branchCity = branchCity;
    }

    public String getCustRefAtmb() {
        return custRefAtmb;
    }

    public void setCustRefAtmb(String custRefAtmb) {
        this.custRefAtmb = custRefAtmb;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getInquiryRefNumber() {
        return inquiryRefNumber;
    }

    public void setInquiryRefNumber(String inquiryRefNumber) {
        this.inquiryRefNumber = inquiryRefNumber;
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

    public BigDecimal getStan() {
        return stan;
    }

    public void setStan(BigDecimal stan) {
        this.stan = stan;
    }

    public String getStanSixDigit() {

        if (stan == null) return null;

        int istan = stan.intValue();
        return (istan < 10) ? "00000" + stan :
                    (istan < 100) ? "0000" + stan :
                        (istan < 1000) ? "000" + stan :
                            (istan < 10000) ? "00" + stan :
                                (istan < 100000) ? "0" + stan: "" + stan;
    }

    public void setStanSixDigit(String stan) {

        // do nothing
    }

    public String getBit42() {
        return bit42;
    }

    public void setBit42(String bit42) {
        this.bit42 = bit42;
    }

    public String getBit43() {
        return bit43;
    }

    public void setBit43(String bit43) {
        this.bit43 = bit43;
    }

    public String getTerminalAddress() {
        return terminalAddress;
    }

    public void setTerminalAddress(String terminalAddress) {
        this.terminalAddress = terminalAddress;
    }

    public String getBit48() {
		return bit48;
	}

	public void setBit48(String bit48) {
		this.bit48 = bit48;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public CustomerRegister transform() {
        CustomerRegister cr = new CustomerRegister();

        cr.setCustomerId(getCustomerId());
        cr.setTransactionType(getTransactionType());
        cr.setCustomerReference(getCustomerReference());
        cr.setData1(getBillerCode());
        cr.setData2(getToAccountType());
        cr.setData3(getReceiverName());
        cr.setData4(getBranchName());
        cr.setData5(getBranchCity());
        cr.setCreated(new Date());
        cr.setCreatedby(getCustomerId());
        cr.setUpdated(new Date());
        cr.setUpdatedby(getCustomerId());

        return cr;
    }

    // bsueb hasil penjumlahan amount + fee
    public BigDecimal getTotalDebetAmount()
    {
        if (fee != null && amount != null){
            totalDebetAmount = amount.add(fee);
        }
        else if (fee == null && amount != null){
            totalDebetAmount = amount;
        }
        else {
            totalDebetAmount = BigDecimal.ZERO;
        }
        return totalDebetAmount;
    }

    public void setTotalDebetAmount(BigDecimal totalDebetAmount){
        this.totalDebetAmount = totalDebetAmount;
    }

    // bsueb status transaksi
    public String getTransactionStatus()
    {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus)
    {
        this.transactionStatus = transactionStatus;
    }

    // bsueb getter setter terminalId View
    public String getTerminalIdView()
    {
        return terminalIdView;
    }

    public void setTerminalIdView(String terminalIdView)
    {
        this.terminalIdView = terminalIdView;
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

	public Long getTransactionQueueId() {
		return transactionQueueId;
	}

	public void setTransactionQueueId(Long transactionQueueId) {
		this.transactionQueueId = transactionQueueId;
	}
	
}
