package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Transaction extends BaseObject implements Serializable {
	private static final long serialVersionUID = -8312619724314735763L;

	private String approvalNumber;
	private String balance;
	private String cardNumber;
	private BigDecimal conversionRate;
	private String currencyCode;
	private String customerReference;
	private String description;
    private BigDecimal fee;
    private String feeIndicator;
	private String freeData1;
	private String freeData2;
	private String freeData3;
	private String freeData4;
	private String freeData5;
	private String bit110;
	private String bit121;
	private String bit123;
	private String fromAccountNumber;
	private String fromAccountType;
	private String executionType;
	private String merchantType;
    private String terminalId;
	private String mti;
	private String referenceNumber;
	private String responseCode;
	private BigDecimal stan;
    private String status;
	private String toAccountNumber;
	private String toAccountType;
	private String toBankCode;
	private BigDecimal transactionAmount;
	private Date transactionDate;
	private String transactionType;
    private String translationCode;
	private Date transmissionDate;
	private Date valueDate;
	private String cardData1;   //track No2
    private String cardData2;   //encripted PIN
    private String cardData3;   //encripted new PIN (for changePIN)
    private String bit22;
    private String bit42;
    private String bit43;
    private String bit47;
    private String terminalAddress;
    private String billerCode;
    private String productCode;
    private String providerCode;
    private String denomination;
    
	//lookup
	private TransactionType transactionTypeModel;

    public Transaction() {
    }
    
	public String getApprovalNumber() {
		return this.approvalNumber;
	}

	public void setApprovalNumber(String approvalNumber) {
		this.approvalNumber = approvalNumber;
	}

	public String getBalance() {
		return this.balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public BigDecimal getConversionRate() {
		return this.conversionRate;
	}

	public void setConversionRate(BigDecimal conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getCurrencyCode() {
		return this.currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCustomerReference() {
		return this.customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getFeeIndicator() {
        return feeIndicator;
    }

    public void setFeeIndicator(String feeIndicator) {
        this.feeIndicator = feeIndicator;
    }

    public String getFreeData1() {
        return freeData1;
    }

    public void setFreeData1(String freeData1) {
        this.freeData1 = freeData1;
    }

    public String getFreeData2() {
        return freeData2;
    }

    public void setFreeData2(String freeData2) {
        this.freeData2 = freeData2;
    }

    public String getFreeData3() {
        return freeData3;
    }

    public void setFreeData3(String freeData3) {
        this.freeData3 = freeData3;
    }

    public String getFreeData4() {
        return freeData4;
    }

    public void setFreeData4(String freeData4) {
        this.freeData4 = freeData4;
    }

    public String getFreeData5() {
        return freeData5;
    }

    public void setFreeData5(String freeData5) {
        this.freeData5 = freeData5;
    }

    public String getFromAccountNumber() {
		return this.fromAccountNumber;
	}

	public void setFromAccountNumber(String fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public String getFromAccountType() {
		return this.fromAccountType;
	}

	public void setFromAccountType(String fromAccountType) {
		this.fromAccountType = fromAccountType;
	}

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getMerchantType() {
		return this.merchantType;
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

    public String getMti() {
		return this.mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}

	public String getReferenceNumber() {
		return this.referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

    public String getStanSixDigit() {
        int istan = stan.intValue();
		return (istan < 10) ? "00000" + stan :
                (istan < 100) ? "0000" + stan :
                        (istan < 1000) ? "000" + stan :
                                (istan < 10000) ? "00" + stan :
                                        (istan < 100000) ? "0" + stan: "" + stan;
	}

	public BigDecimal getStan() {
		return this.stan;
	}

	public void setStan(BigDecimal stan) {
		this.stan = stan;
	}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToAccountNumber() {
		return this.toAccountNumber;
	}

	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getToAccountType() {
		return this.toAccountType;
	}

	public void setToAccountType(String toAccountType) {
		this.toAccountType = toAccountType;
	}

	public String getToBankCode() {
		return this.toBankCode;
	}

	public void setToBankCode(String toBankCode) {
		this.toBankCode = toBankCode;
	}

	public BigDecimal getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionType() {
		return this.transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

    public String getTranslationCode() {
        return translationCode;
    }

    public void setTranslationCode(String translationCode) {
        this.translationCode = translationCode;
    }

    public Date getTransmissionDate() {
		return this.transmissionDate;
	}

	public void setTransmissionDate(Date transmissionDate) {
		this.transmissionDate = transmissionDate;
	}

	public Date getValueDate() {
		return this.valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}
	
    public TransactionType getTransactionTypeModel() {
		return transactionTypeModel;
	}

	public void setTransactionTypeModel(TransactionType transactionTypeModel) {
		this.transactionTypeModel = transactionTypeModel;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof Transaction)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Transaction that = (Transaction) o;
        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return String.valueOf("id=" + getId() + ", bit48=" +getFreeData1());
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

    public String getCardData3() {
        return cardData3;
    }

    public void setCardData3(String cardData3) {
        this.cardData3 = cardData3;
    }

    public String getBit22() {
        return bit22;
    }

    public void setBit22(String bit22) {
        this.bit22 = bit22;
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

    public String getBit47() {
        return bit47;
    }

    public void setBit47(String bit47) {
        this.bit47 = bit47;
    }

    public String getTerminalAddress() {
        return terminalAddress;
    }

    public void setTerminalAddress(String terminalAddress) {
        this.terminalAddress = terminalAddress;
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
	
    public String getProviderCode() {
		return providerCode;
	}

	public void setProviderCode(String providerCode) {
		this.providerCode = providerCode;
	}
    public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}

	public String getBit110() {
		return bit110;
	}

	public void setBit110(String bit110) {
		this.bit110 = bit110;
	}

	public String getBit121() {
		return bit121;
	}

	public void setBit121(String bit121) {
		this.bit121 = bit121;
	}

	public String getBit123() {
		return bit123;
	}

	public void setBit123(String bit123) {
		this.bit123 = bit123;
	}

}