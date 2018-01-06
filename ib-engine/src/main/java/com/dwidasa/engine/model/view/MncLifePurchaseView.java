package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;

public class MncLifePurchaseView implements BaseView{

	
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
    
    private String issuercode;
    private String kodeProduk;
    private String nomoPolis1;
    private Date tglAwalPolis1;
    private Date tglAkhirPolis1;
    private String namaTertanggung;
    private String nomorKtp;
    private String jenisKelamin;
    private String tempatLahir;
    private Date tanggalLahir;
    private String alamat;
    private String kota;
    private String namaAhliWaris1;
    private String namaAhliWaris2;
    private String namaAhliWaris3;
    private String noHp;
    private String emailAddress;
    
    private String nomoPolis2;
    private Date tglAwalPolis2;
    private Date tglAkhirPolis2;
    
    private String nomoPolis3;
    private Date tglAwalPolis3;
    private Date tglAkhirPolis3;
    
    private String nomoPolis4;
    private Date tglAwalPolis4;
    private Date tglAkhirPolis4;
    
    private String nomoPolis5;
    private Date tglAwalPolis5;
    private Date tglAkhirPolis5;
    
    private String nomoPolis6;
    private Date tglAwalPolis6;
    private Date tglAkhirPolis6;
    
    private String nomoPolis7;
    private Date tglAwalPolis7;
    private Date tglAkhirPolis7;
    
    private String nomoPolis8;
    private Date tglAwalPolis8;
    private Date tglAkhirPolis8;
    
    private String nomoPolis9;
    private Date tglAwalPolis9;
    private Date tglAkhirPolis9;
    private String tipeDokumen;

    public MncLifePurchaseView() {
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

   

    public Boolean validate() {
        return Boolean.TRUE;
    }
    
    public void setIssuercode(String issuercode) {
		this.issuercode = issuercode;
	}
    
    public String getIssuercode() {
		return issuercode;
	}
    
    public void setKodeProduk(String kodeProduk) {
		this.kodeProduk = kodeProduk;
	}
    
    public String getKodeProduk() {
		return kodeProduk;
	}
    
    public void setNomoPolis1(String nomoPolis1) {
		this.nomoPolis1 = nomoPolis1;
	}
    
    public String getNomoPolis1() {
		return nomoPolis1;
	}
    
    public void setTglAwalPolis1(Date tglAwalPolis1) {
		this.tglAwalPolis1 = tglAwalPolis1;
	}
    
    public Date getTglAwalPolis1() {
		return tglAwalPolis1;
	}
    
    
    public void setTglAkhirPolis1(Date tglAkhirPolis1) {
		this.tglAkhirPolis1 = tglAkhirPolis1;
	}
    
    public Date getTglAkhirPolis1() {
		return tglAkhirPolis1;
	}
    
    public void setNamaTertanggung(String namaTertanggung) {
		this.namaTertanggung = namaTertanggung;
	}
    
    public String getNamaTertanggung() {
		return namaTertanggung;
	}
    
    public void setNomorKtp(String nomorKtp) {
		this.nomorKtp = nomorKtp;
	}
    
    public String getNomorKtp() {
		return nomorKtp;
	}
    
    public void setJenisKelamin(String jenisKelamin) {
		this.jenisKelamin = jenisKelamin;
	}
    
    public String getJenisKelamin() {
		return jenisKelamin;
	}
    
    public void setTempatLahir(String tempatLahir) {
		this.tempatLahir = tempatLahir;
	}
    
    public String getTempatLahir() {
		return tempatLahir;
	}
    
    public void setTanggalLahir(Date tanggalLahir) {
		this.tanggalLahir = tanggalLahir;
	}
    
    public Date getTanggalLahir() {
		return tanggalLahir;
	}
    
    public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
    
    public String getAlamat() {
		return alamat;
	}
    
    public void setKota(String kota) {
		this.kota = kota;
	}
    
    public String getKota() {
		return kota;
	}
    
    public void setNamaAhliWaris1(String namaAhliWaris1) {
		this.namaAhliWaris1 = namaAhliWaris1;
	}
    
    public String getNamaAhliWaris1() {
		return namaAhliWaris1;
	}
    
    public void setNamaAhliWaris2(String namaAhliWaris2) {
		this.namaAhliWaris2 = namaAhliWaris2;
	}
    
    public String getNamaAhliWaris2() {
		return namaAhliWaris2;
	}
    
    public void setNamaAhliWaris3(String namaAhliWaris3) {
		this.namaAhliWaris3 = namaAhliWaris3;
	}
    
    public String getNamaAhliWaris3() {
		return namaAhliWaris3;
	}
    
    public void setNoHp(String noHp) {
		this.noHp = noHp;
	}
    
    public String getNoHp() {
		return noHp;
	}
    
    public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
    
    public String getEmailAddress() {
		return emailAddress;
	}
    
    
    public void setNomoPolis2(String nomoPolis2) {
		this.nomoPolis2 = nomoPolis2;
	}
    
    public String getNomoPolis2() {
		return nomoPolis2;
	}
    
    public void setTglAwalPolis2(Date tglAwalPolis2) {
		this.tglAwalPolis2 = tglAwalPolis2;
	}
    
    public Date getTglAwalPolis2() {
		return tglAwalPolis2;
	}
    
    public void setTglAkhirPolis2(Date tglAkhirPolis2) {
		this.tglAkhirPolis2 = tglAkhirPolis2;
	}
    
    public Date getTglAkhirPolis2() {
		return tglAkhirPolis2;
	}
    
    
    public void setNomoPolis3(String nomoPolis3) {
		this.nomoPolis3 = nomoPolis3;
	}
    
    public String getNomoPolis3() {
		return nomoPolis3;
	}
    
    public void setTglAwalPolis3(Date tglAwalPolis3) {
		this.tglAwalPolis3 = tglAwalPolis3;
	}
    
    public Date getTglAwalPolis3() {
		return tglAwalPolis3;
	}
    
    
    public void setTglAkhirPolis3(Date tglAkhirPolis3) {
		this.tglAkhirPolis3 = tglAkhirPolis3;
	}
    
    public Date getTglAkhirPolis3() {
		return tglAkhirPolis3;
	}
    
    
    
    public void setNomoPolis4(String nomoPolis4) {
		this.nomoPolis4 = nomoPolis4;
	}
    
    public String getNomoPolis4() {
		return nomoPolis4;
	}
    
    public void setTglAwalPolis4(Date tglAwalPolis4) {
		this.tglAwalPolis4 = tglAwalPolis4;
	}
    
    public Date getTglAwalPolis4() {
		return tglAwalPolis4;
	}
    
    
    public void setTglAkhirPolis4(Date tglAkhirPolis4) {
		this.tglAkhirPolis4 = tglAkhirPolis4;
	}
    
    public Date getTglAkhirPolis4() {
		return tglAkhirPolis4;
	}
    
    
    public void setNomoPolis5(String nomoPolis5) {
		this.nomoPolis5 = nomoPolis5;
	}
    
    public String getNomoPolis5() {
		return nomoPolis5;
	}
    
    public void setTglAwalPolis5(Date tglAwalPolis5) {
		this.tglAwalPolis5 = tglAwalPolis5;
	}
    
    public Date getTglAwalPolis5() {
		return tglAwalPolis5;
	}
    
    
    public void setTglAkhirPolis5(Date tglAkhirPolis5) {
		this.tglAkhirPolis5 = tglAkhirPolis5;
	}
    
    public Date getTglAkhirPolis5() {
		return tglAkhirPolis5;
	}
    
    
    public void setNomoPolis6(String nomoPolis6) {
		this.nomoPolis6 = nomoPolis6;
	}
    
    public String getNomoPolis6() {
		return nomoPolis6;
	}
    
    public void setTglAwalPolis6(Date tglAwalPolis6) {
		this.tglAwalPolis6 = tglAwalPolis6;
	}
    
    public Date getTglAwalPolis6() {
		return tglAwalPolis6;
	}
    
    
    public void setTglAkhirPolis6(Date tglAkhirPolis6) {
		this.tglAkhirPolis6 = tglAkhirPolis6;
	}
    
    public Date getTglAkhirPolis6() {
		return tglAkhirPolis6;
	}
    
    
    public void setNomoPolis7(String nomoPolis7) {
		this.nomoPolis7 = nomoPolis7;
	}
    
    public String getNomoPolis7() {
		return nomoPolis7;
	}
    
    public void setTglAwalPolis7(Date tglAwalPolis7) {
		this.tglAwalPolis7 = tglAwalPolis7;
	}
    
    public Date getTglAwalPolis7() {
		return tglAwalPolis7;
	}
    
    
    public void setTglAkhirPolis7(Date tglAkhirPolis7) {
		this.tglAkhirPolis7 = tglAkhirPolis7;
	}
    
    public Date getTglAkhirPolis7() {
		return tglAkhirPolis7;
	}
    
    
    public void setNomoPolis8(String nomoPolis8) {
		this.nomoPolis8 = nomoPolis8;
	}
    
    public String getNomoPolis8() {
		return nomoPolis8;
	}
    
    public void setTglAwalPolis8(Date tglAwalPolis8) {
		this.tglAwalPolis8 = tglAwalPolis8;
	}
    
    public Date getTglAwalPolis8() {
		return tglAwalPolis8;
	}
    
    
    public void setTglAkhirPolis8(Date tglAkhirPolis8) {
		this.tglAkhirPolis8 = tglAkhirPolis8;
	}
    
    public Date getTglAkhirPolis8() {
		return tglAkhirPolis8;
	}
    
    public void setNomoPolis9(String nomoPolis9) {
		this.nomoPolis9 = nomoPolis9;
	}
    
    public String getNomoPolis9() {
		return nomoPolis9;
	}
    
    public void setTglAwalPolis9(Date tglAwalPolis9) {
		this.tglAwalPolis9 = tglAwalPolis9;
	}
    
    public Date getTglAwalPolis9() {
		return tglAwalPolis9;
	}
    
    
    public void setTglAkhirPolis9(Date tglAkhirPolis9) {
		this.tglAkhirPolis9 = tglAkhirPolis9;
	}
    
    public Date getTglAkhirPolis9() {
		return tglAkhirPolis9;
	}
    
    
    public void setBit22(String bit22) {
		this.bit22 = bit22;
	}
    
    public String getBit22() {
		return bit22;
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

    public String getTipeDokumen() {
        return tipeDokumen;
    }

    public void setTipeDokumen(String tipeDokumen) {
        this.tipeDokumen = tipeDokumen;
    }
    
    

}
