package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.Date;

import com.dwidasa.engine.model.CustomerRegister;

public class CashInDompetkuView extends PaymentView {
	
	
	private BigDecimal nominalTopUp;
	private String namaCustomer;
	private String msiSDN;
	private String token;
	private String status;
	private String approval;
	
	
	public CashInDompetkuView() {
		// TODO Auto-generated constructor stub
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

    public void setMsiSDN(String msiSDN) {
		this.msiSDN = msiSDN;
	}
    
    public String getMsiSDN() {
		return msiSDN;
	}
    
    public void setToken(String token) {
		this.token = token;
	}
    
    public String getToken() {
		return token;
	}
    
    public void setApproval(String approval) {
		this.approval = approval;
	}
    
    public String getApproval() {
		return approval;
	}
   
    public void setNominalTopUp(BigDecimal nominalTopUp) {
		this.nominalTopUp = nominalTopUp;
	}
    
    public BigDecimal getNominalTopUp() {
		return nominalTopUp;
	}
    
    public String getStatus() {
		return status;
	}
    
    public void setStatus(String status) {
		this.status = status;
	}
    
    public String getNamaCustomer() {
		return namaCustomer;
	}
    
    public void setNamaCustomer(String namaCustomer) {
		this.namaCustomer = namaCustomer;
	}
    
    
}
