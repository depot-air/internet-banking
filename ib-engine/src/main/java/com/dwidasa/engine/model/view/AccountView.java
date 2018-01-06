package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.CustomerRegister;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/19/11
 * Time: 7:23 PM
 */
public class AccountView implements BaseView {
    /**
     * Customer id, mandatory field.
     */
    private Long customerId;
    /**
     * Customer name, output field.
     */
    private String customerName;
    /**
     * Account number, conditional field.
     * See {@link com.dwidasa.engine.service.facade.AccountService} to decide whether
     * mandatory or optional.
     */
    private String accountNumber;
    /**
     * Card number, conditional field.
     * See {@link com.dwidasa.engine.service.facade.AccountService} to decide whether
     * mandatory or optional.
     */
    private String cardNumber;
    /**
     * Account status, output field.
     * Based on {@link com.dwidasa.engine.service.facade.AccountService#getAccountBalance(AccountView)}
     * will have 3 different value,<br/>
     * 0 : Account closed<br/>
     * 1 : Account Registered and active<br/>
     * 2 : Account Registered and not active.
     */
    private Integer accountStatus;
    /**
     * Account type name, output field.
     */
    private String accountName;
    /**
     * Account type, conditional field.
     * See {@link com.dwidasa.engine.service.facade.AccountService} to decide whether
     * mandatory or optional.
     */
    private String accountType;
    /**
     * Merchant type, conditional field.
     * See {@link com.dwidasa.engine.service.facade.AccountService} to decide whether
     * mandatory or optional.
     */
    private String merchantType;
    /**
     * Terminal id, mandatory field.
     */
    private String terminalId;
    /**
     * Currency code, conditional field.
     * See {@link com.dwidasa.engine.service.facade.AccountService} to decide whether
     * mandatory or optional.
     */
    private String currencyCode;
    /**
     * Product code, output field.
     */
    private String productCode;
    /**
     * Product name, output field.
     */
    private String productName;
    /**
     * Product id, internal field.
     */
    private Long productId;
    /**
     * Transaction type, conditional field.
     * See {@link com.dwidasa.engine.service.facade.AccountService} to decide whether
     * mandatory or optional.
     */
    private String transactionType;
    /**
     * Transaction date, optional field.
     */
    private Date transactionDate;
    /**
     * Available balance, output field.
     */
    private BigDecimal availableBalance;
    /**
     * Blocked balance, output field.
     */
    private BigDecimal blockedBalance;
    /**
     * Minimum balance, output field.
     */
    private BigDecimal minimumBalance;
    /**
     * Reference number, output field.
     */
    private String referenceNumber;
    /**
     * Response code, output field.
     */
    private String responseCode;
    /**
     * Generated message, internal field.
     */
    private String generated;

    /**
     * Account statement date from
     */
    private Date statementDateFrom;

    /**
     * Account statement date to
     */
    private Date statementDateTo;
    
    private String isDefault;
    
    private String transactionStatus;
    
    /**
     * List of account statement, output field.
     */
    
    //Deposito
    private String jangkaWaktu;
    private String tglEfektif;
    private String jatuhTempo;
    private double annualRate;
    private BigDecimal nominalDeposito;
    
    
    //Loan
    private BigDecimal pokokPinjaman;
    private BigDecimal bungaPinjaman;
    private int tenor;
    private BigDecimal jumlahAngsuran;
    private BigDecimal sisaPinjaman;
    private String effectiveDate;
    private String dueDate;
    private BigDecimal tunggakanPokok;
    private BigDecimal tunggakanBunga;
    private String tanggalBayarPokok;
    private String tanggalBayarBunga;
    
    private List<AccountStatementView> statements;

    public AccountView() {
    }

    public AccountView(AccountView that) {
        this.customerId = that.getCustomerId();
        this.customerName = that.getCustomerName();
        this.accountNumber = that.getAccountNumber();
        this.cardNumber = that.getCardNumber();
        this.accountStatus = that.getAccountStatus();
        this.accountName = that.getAccountName();
        this.accountType = that.getAccountType();
        this.merchantType = that.getMerchantType();
        this.terminalId = that.getTerminalId();
        this.currencyCode = that.getCurrencyCode();
        this.productCode = that.getProductCode();
        this.productName = that.getProductName();
        this.transactionType = that.getTransactionType();
        this.transactionDate = that.getTransactionDate();
        this.availableBalance = that.getAvailableBalance();
        this.referenceNumber = that.getReferenceNumber();
        this.responseCode = that.getResponseCode();
        this.generated = that.getGenerated();
        this.statementDateFrom = that.getStatementDateFrom();
        this.statementDateTo = that.getStatementDateTo();

        if (that.getStatements() != null) {
            this.statements = new ArrayList<AccountStatementView>();
            for (AccountStatementView stat : that.getStatements()) {
                this.statements.add(new AccountStatementView(stat));
            }
        }
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public BigDecimal getBlockedBalance() {
        return blockedBalance;
    }

    public void setBlockedBalance(BigDecimal blockedBalance) {
        this.blockedBalance = blockedBalance;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getGenerated() {
        return generated;
    }

    public void setGenerated(String generated) {
        this.generated = generated;
    }

    public List<AccountStatementView> getStatements() {
        return statements;
    }

    public void setStatements(List<AccountStatementView> statements) {
        this.statements = statements;
    }

    public BigDecimal getAmount() {
        return getAvailableBalance();
    }

    public Boolean validate() {
        return Boolean.TRUE;
    }

    public CustomerRegister transform() {
        return null;
    }

    public Date getStatementDateFrom() {
        return statementDateFrom;
    }

    public void setStatementDateFrom(Date statementDateFrom) {
        this.statementDateFrom = statementDateFrom;
    }

    public Date getStatementDateTo() {
        return statementDateTo;
    }

    public void setStatementDateTo(Date statementDateTo) {
        this.statementDateTo = statementDateTo;
    }

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
	public String getTransactionStatus() {
		return transactionStatus;
	}
	
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	
	
	//Deposito
	public void setAnnualRate(Double annualRate) {
		this.annualRate = annualRate;
	}
	
	public Double getAnnualRate() {
		return annualRate;
	}
	
	public void setJangkaWaktu(String jangkaWaktu) {
		this.jangkaWaktu = jangkaWaktu;
	}
	
	public String getJangkaWaktu() {
		return jangkaWaktu;
	}
	
	public void setTglEfektif(String tglEfektif) {
		this.tglEfektif = tglEfektif;
	}
	
	public String getTglEfektif() {
		return tglEfektif;
	}
	
	public void setJatuhTempo(String jatuhTempo) {
		this.jatuhTempo = jatuhTempo;
	}
	
	public String getJatuhTempo() {
		return jatuhTempo;
	}
	
	public void setNominalDeposito(BigDecimal nominalDeposito) {
		this.nominalDeposito = nominalDeposito;
	}
	
	public BigDecimal getNominalDeposito() {
		return nominalDeposito;
	}

	
	//Loan
	 public void setPokokPinjaman(BigDecimal pokokPinjaman) {
		this.pokokPinjaman = pokokPinjaman;
	}
	 
	 public BigDecimal getPokokPinjaman() {
		return pokokPinjaman;
	}
	 
	 public void setBungaPinjaman(BigDecimal bungaPinjaman) {
		this.bungaPinjaman = bungaPinjaman;
	}
	 
	 public BigDecimal getBungaPinjaman() {
		return bungaPinjaman;
	}
	 
	 public void setTenor(int tenor) {
		this.tenor = tenor;
	}
	 
	 public int getTenor() {
		return tenor;
	}
	 
	 public void setJumlahAngsuran(BigDecimal jumlahAngsuran) {
		this.jumlahAngsuran = jumlahAngsuran;
	}
	 
	 public BigDecimal getJumlahAngsuran() {
		return jumlahAngsuran;
	}
	 
	 public void setSisaPinjaman(BigDecimal sisaPinjaman) {
		this.sisaPinjaman = sisaPinjaman;
	}
	 
	 public BigDecimal getSisaPinjaman() {
		return sisaPinjaman;
	}
	 
	 public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	 
	 public String getEffectiveDate() {
		return effectiveDate;
	}
	 
	 
	 public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	 
	 public String getDueDate() {
		return dueDate;
	}
	 
	 public void setTunggakanPokok(BigDecimal tunggakanPokok) {
		this.tunggakanPokok = tunggakanPokok;
	}
	 
	 public BigDecimal getTunggakanPokok() {
		return tunggakanPokok;
	}
	 
	 public void setTunggakanBunga(BigDecimal tunggakanBunga) {
		this.tunggakanBunga = tunggakanBunga;
	}
	 
	public BigDecimal getTunggakanBunga() {
		return tunggakanBunga;
	}
	
	public void setTanggalBayarPokok(String tanggalBayarPokok) {
		this.tanggalBayarPokok = tanggalBayarPokok;
	}
	
	public String getTanggalBayarPokok() {
		return tanggalBayarPokok;
	}
	
	
	public void setTanggalBayarBunga(String tanggalBayarBunga) {
		this.tanggalBayarBunga = tanggalBayarBunga;
	}
	
	public String getTanggalBayarBunga() {
		return tanggalBayarBunga;
	}
	
    
}