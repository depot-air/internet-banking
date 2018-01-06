package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.TransferBatchContent;

/**
 * Bundled class contain transfer related data for presentation and/or object
 * state storage purpose.
 *
 * @author ib
 */
public class TransferBatchView implements BaseView {
	private Long customerId;    
    private String accountNumber;
    private String batchName;
    private String batchDescription;
    private Integer transferType; //1 - right now; 2 - post dated; Mandatory field.
	private Date transactionDate;
	private Date valueDate;
    private String referenceNumber;
    private String status;
    private BigDecimal totalAmount;
    
	private Long batchId;
    private List<TransferBatchContent> transferBatchContentList;
    private List<TransferView> transferViews;     

    private String transactionType;
    private BigDecimal amount;
    private String responseCode;
    private String cardNumber;
    private String accountType;
    private String merchantType;
    private String terminalId;
    private Integer periodType;
    private Integer periodValue;
    
    //untuk keperluan cetak resi IB
	private Date endDate;
	protected String transactionDateString;
      
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
  	
  	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

  	public Integer getTransferType() {
  		return transferType;
  	}

  	public void setTransferType(Integer transferType) {
  		this.transferType = transferType;
  	}

  	public Long getBatchId() {
  		return batchId;
  	}

  	public void setBatchId(Long batchId) {
  		this.batchId = batchId;
  	}

  	public String getBatchName() {
  		return batchName;
  	}

  	public void setBatchName(String batchName) {
  		this.batchName = batchName;
  	}

  	public String getBatchDescription() {
  		return batchDescription;
  	}

  	public void setBatchDescription(String batchDescription) {
  		this.batchDescription = batchDescription;
  	}

  	public String getStatus() {
  		return status;
  	}

  	public void setStatus(String status) {
  		this.status = status;
  	}

      public BigDecimal getTotalAmount() {
  		return totalAmount;
  	}

  	public void setTotalAmount(BigDecimal totalAmount) {
  		this.totalAmount = totalAmount;
  	}

  	public void setTransferBatchContentList(List<TransferBatchContent> transferBatchContentList) {
  		this.transferBatchContentList = transferBatchContentList;
  	}
  	
  	public List<TransferBatchContent> getTransferBatchContentList() {
  		return transferBatchContentList;
  	}
  	
  	public List<TransferView> getTransferViews() {
		return transferViews;
	}

	public void setTransferViews(List<TransferView> transferViews) {
		this.transferViews = transferViews;
	}

	public String getReferenceNumber() {
  		return referenceNumber;
  	}

  	public void setReferenceNumber(String referenceNumber) {
  		this.referenceNumber = referenceNumber;
  	}

  	public Date getEndDate() {
  		return endDate;
  	}

  	public void setEndDate(Date endDate) {
  		this.endDate = endDate;
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

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public Boolean validate() {
		return true;
	}

	@Override
	public CustomerRegister transform() {
        CustomerRegister cr = new CustomerRegister();

        cr.setCustomerId(getCustomerId());
        cr.setTransactionType(getTransactionType());
//        cr.setCustomerReference(getCustomerReference());
//        cr.setData1(getBillerCode());
//        cr.setData2(getToAccountType());
//        cr.setData3(getReceiverName());
//        cr.setData4(getBranchName());
//        cr.setData5(getBranchCity());
        cr.setCreated(new Date());
        cr.setCreatedby(getCustomerId());
        cr.setUpdated(new Date());
        cr.setUpdatedby(getCustomerId());

        return cr;
	}

}
