package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.util.Date;

import com.dwidasa.engine.model.CustomerRegister;

/**
 * @author prayugo
 */
public class PortfolioView implements BaseView {

	private Long customerId;
	private Date transactionDate;
	private String transactionType;
	private String responseCode;
	private String referenceNumber;
	
    private String accountNumber;
    private Integer accountStatus;
    private String accountName;
    private String accountType;
    private String currencyCode;
    private String productCode;
    private String productName;
    private BigDecimal availableBalance;
    private BigDecimal blockedBalance;
    private BigDecimal minimumBalance;
    private String branchCode;
    private BigDecimal plafond;
    private BigDecimal kliring;
    
    private String depositoNumber;
    private BigDecimal nominalDeposito;
    private String jangkaWaktu;
    private String annualRate;
    private String effectiveDate;
    private String dueDate;
    private String creditAccount;
    private String instruction;
    
    private String loanNumber;
    private BigDecimal pokokPinjaman;
    private BigDecimal bungaPinjaman;
    private int tenor;
    private BigDecimal jumlahAngsuran;
    private BigDecimal sisaPinjaman;
    private BigDecimal tunggakanPokok;
    private BigDecimal tunggakanBunga;
    private String tanggalBayarBunga;
    private String tanggalBayarPokok;
    private String satuanWaktu;
    private String frekuensiBayar;
    private int bayarKe;
    private BigDecimal nilaiBunga;

    private BigDecimal principlePaidAmount;
    private BigDecimal duePrincipleAmount;
    private BigDecimal penaltyPaidAmount;
    
    private String cardNumber;
    private String merchantType;
    private String terminalId;
    private String flagAgain;
    private String transactionStatus;
    
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
	
	public String getBranchCode() {
		return branchCode;
	}
	
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	
	public BigDecimal getPlafond() {
		return plafond;
	}
	
	public void setPlafond(BigDecimal plafond) {
		this.plafond = plafond;
	}
	
	public BigDecimal getKliring() {
		return kliring;
	}
	
	public void setKliring(BigDecimal kliring) {
		this.kliring = kliring;
	}
	
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}
	
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}
	
	public String getDepositoNumber() {
		return depositoNumber;
	}
	
	public void setDepositoNumber(String depositoNumber) {
		this.depositoNumber = depositoNumber;
	}
	
	public BigDecimal getNominalDeposito() {
		return nominalDeposito;
	}
	
	public void setNominalDeposito(BigDecimal nominalDeposito) {
		this.nominalDeposito = nominalDeposito;
	}
	
	public String getJangkaWaktu() {
		return jangkaWaktu;
	}
	
	public void setJangkaWaktu(String jangkaWaktu) {
		this.jangkaWaktu = jangkaWaktu;
	}
	
	public String getAnnualRate() {
		return annualRate;
	}
	
	public void setAnnualRate(String annualRate) {
		this.annualRate = annualRate;
	}
	
	public String getEffectiveDate() {
		return effectiveDate;
	}
	
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public String getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getCreditAccount() {
		return creditAccount;
	}
	
	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}
	
	public String getInstruction() {
		return instruction;
	}
	
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public BigDecimal getPokokPinjaman() {
		return pokokPinjaman;
	}

	public void setPokokPinjaman(BigDecimal pokokPinjaman) {
		this.pokokPinjaman = pokokPinjaman;
	}

	public BigDecimal getBungaPinjaman() {
		return bungaPinjaman;
	}

	public void setBungaPinjaman(BigDecimal bungaPinjaman) {
		this.bungaPinjaman = bungaPinjaman;
	}

	public int getTenor() {
		return tenor;
	}

	public void setTenor(int tenor) {
		this.tenor = tenor;
	}

	public BigDecimal getJumlahAngsuran() {
		return jumlahAngsuran;
	}

	public void setJumlahAngsuran(BigDecimal jumlahAngsuran) {
		this.jumlahAngsuran = jumlahAngsuran;
	}

	public BigDecimal getSisaPinjaman() {
		return sisaPinjaman;
	}

	public void setSisaPinjaman(BigDecimal sisaPinjaman) {
		this.sisaPinjaman = sisaPinjaman;
	}

	public BigDecimal getTunggakanPokok() {
		return tunggakanPokok;
	}

	public void setTunggakanPokok(BigDecimal tunggakanPokok) {
		this.tunggakanPokok = tunggakanPokok;
	}

	public BigDecimal getTunggakanBunga() {
		return tunggakanBunga;
	}

	public void setTunggakanBunga(BigDecimal tunggakanBunga) {
		this.tunggakanBunga = tunggakanBunga;
	}

	public String getTanggalBayarBunga() {
		return tanggalBayarBunga;
	}

	public void setTanggalBayarBunga(String tanggalBayarBunga) {
		this.tanggalBayarBunga = tanggalBayarBunga;
	}

	public String getTanggalBayarPokok() {
		return tanggalBayarPokok;
	}

	public void setTanggalBayarPokok(String tanggalBayarPokok) {
		this.tanggalBayarPokok = tanggalBayarPokok;
	}

	public String getSatuanWaktu() {
		return satuanWaktu;
	}

	public void setSatuanWaktu(String satuanWaktu) {
		this.satuanWaktu = satuanWaktu;
	}

	public String getFrekuensiBayar() {
		return frekuensiBayar;
	}

	public void setFrekuensiBayar(String frekuensiBayar) {
		this.frekuensiBayar = frekuensiBayar;
	}

	public int getBayarKe() {
		return bayarKe;
	}

	public void setBayarKe(int bayarKe) {
		this.bayarKe = bayarKe;
	}

	public BigDecimal getNilaiBunga() {
		return nilaiBunga;
	}

	public void setNilaiBunga(BigDecimal nilaiBunga) {
		this.nilaiBunga = nilaiBunga;
	}

	public BigDecimal getPrinciplePaidAmount() {
		return principlePaidAmount;
	}

	public void setPrinciplePaidAmount(BigDecimal principlePaidAmount) {
		this.principlePaidAmount = principlePaidAmount;
	}

	public BigDecimal getDuePrincipleAmount() {
		return duePrincipleAmount;
	}

	public void setDuePrincipleAmount(BigDecimal duePrincipleAmount) {
		this.duePrincipleAmount = duePrincipleAmount;
	}

	public BigDecimal getPenaltyPaidAmount() {
		return penaltyPaidAmount;
	}

	public void setPenaltyPaidAmount(BigDecimal penaltyPaidAmount) {
		this.penaltyPaidAmount = penaltyPaidAmount;
	}

	@Override
	public Long getCustomerId() {
		return customerId;
	}
	
	@Override
	public String getAccountNumber() {
		return accountNumber;
	}
	
	@Override
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	@Override
	public String getTransactionType() {
		return transactionType;
	}
	
	@Override
	public BigDecimal getAmount() {
		return getAvailableBalance();
	}
	
	@Override
	public String getResponseCode() {
		return responseCode;
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
    
	public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
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

	public String getFlagAgain() {
		return flagAgain;
	}

	public void setFlagAgain(String flagAgain) {
		this.flagAgain = flagAgain;
	}
	
	public String getTransactionStatus() {
		return transactionStatus;
	}
	
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
		
}