package com.dwidasa.engine.model.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/8/11
 * Time: 5:17 PM
 */
public class TiketKeretaDjatiPurchaseView implements BaseView {
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
    private Date tglKeberangkatan;
    private String dari;
    private String tujuan;
    private String kodeJurusan;
    private BigDecimal diskon;
    private BigDecimal hargaAkhir;
    private String fasilitas;
    private String lokasiKeberangkatan;
    private String namaPenumpang1;
    private String namaPenumpang2;
    
   
    private String payeeId;
    private String transactionID;
    private String DepartID;
    private int totalDirecetion;
   
    private List<TiketKeretaDjatiPurchaseView> djatiPurchaseViews = new ArrayList<TiketKeretaDjatiPurchaseView>();
    
    private BigDecimal totalTicketPrice;
    private int totalLocation;
    private List<String> ListKursi = new ArrayList<String>();
    private List<String> ListLocation = new ArrayList<String>();
    
    private String locationID;
    private String departLocation;
   
    //KotaAsal
    private int originData;
    private String dataKotaAsal;
    private List<String> ListKotaAsal = new ArrayList<String>();
    
    //KotaTujuan
    private int destinationData;
    private String destinationKotaTujuan;
    private List<String> ListKotaTujuan = new ArrayList<String>();
    
    private int totalSchedule;
    private String scheduleCodeId;
    private String departBranch;
    private String destinationBranch;
    private String departHour;
    private int totalSheat;
    private int totalEmptySheat;
    private BigDecimal ticketPricePerSheat;
    private String noKursi;
    
    private String noIdentitas;
    private String pemesan;
    private String noHp;
    private String alamatPemesan;
    private String seat;
    private String name;
    private String noTicket;
    private String bookingCode;
    private String paymentCode;
    
    private String noKursiPenumpang1;
    private String noKursiPenumpang2;
    
    private String gspRef;
    private String jenisLayanan;
    private String vechileTypeCode;
    private int index;
    

    public TiketKeretaDjatiPurchaseView() {
    }

    public void setIndex(int index) {
		this.index = index;
	}
    
    public int getIndex() {
		return index;
	}
    
    public void setVechileTypeCode(String vechileTypeCode) {
		this.vechileTypeCode = vechileTypeCode;
	}
    
    public String getVechileTypeCode() {
		return vechileTypeCode;
	}
    
    public void setJenisLayanan(String jenisLayanan) {
		this.jenisLayanan = jenisLayanan;
	}
    
    public String getJenisLayanan() {
		return jenisLayanan;
	}
    
    public void setNoKursiPenumpang1(String noKursiPenumpang1) {
		this.noKursiPenumpang1 = noKursiPenumpang1;
	}
    
    public String getNoKursiPenumpang2() {
		return noKursiPenumpang2;
	}
    
    public void setNoKursiPenumpang2(String noKursiPenumpang2) {
		this.noKursiPenumpang2 = noKursiPenumpang2;
	}
    
    public String getNoKursiPenumpang1() {
		return noKursiPenumpang1;
	}
    
    public void setNoTicket(String noTicket) {
		this.noTicket = noTicket;
	}
    
    public String getNoTicket() {
		return noTicket;
	}
    
    public void setBookingCode(String bookingCode) {
		this.bookingCode = bookingCode;
	}
    
    public String getBookingCode() {
		return bookingCode;
	}
    
    public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
    
    public String getPaymentCode() {
		return paymentCode;
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
    
    public Date getTglKeberangkatan() {
		return tglKeberangkatan;
	}
    
    public void setTglKeberangkatan(Date tglKeberangkatan) {
		this.tglKeberangkatan = tglKeberangkatan;
	}
    
    public String getDari() {
		return dari;
	}
    
    public String getTujuan() {
		return tujuan;
	}
    
    public void setDari(String dari) {
		this.dari = dari;
	}
    
    
    public void setTujuan(String tujuan) {
		this.tujuan = tujuan;
	}
    
    public void setKodeJurusan(String kodeJurusan) {
		this.kodeJurusan = kodeJurusan;
	}
    
    public String getKodeJurusan() {
		return kodeJurusan;
	}
    
    public void setDiskon(BigDecimal diskon) {
		this.diskon = diskon;
	}
    
    public BigDecimal getDiskon() {
		return diskon;
	}
    
    public void setHargaAkhir(BigDecimal hargaAkhir) {
		this.hargaAkhir = hargaAkhir;
	}
    
    public BigDecimal getHargaAkhir() {
		return hargaAkhir;
	}
    
    public void setFasilitas(String fasilitas) {
		this.fasilitas = fasilitas;
	}
    
    public String getFasilitas() {
		return fasilitas;
	}
    
    public void setLokasiKeberangkatan(String lokasiKeberangkatan) {
		this.lokasiKeberangkatan = lokasiKeberangkatan;
	}
    
    
    public String getPemesan() {
		return pemesan;
	}
    
    public void setPemesan(String pemesan) {
		this.pemesan = pemesan;
	}
    
    public String getAlamatPemesan() {
		return alamatPemesan;
	}
    
    public void setAlamatPemesan(String alamatPemesan) {
		this.alamatPemesan = alamatPemesan;
	}
    
    public void setNoIdentitas(String noIdentitas) {
		this.noIdentitas = noIdentitas;
	}
    
    public String getNoIdentitas() {
		return noIdentitas;
	}
    
    public String getNoHp() {
		return noHp;
	}
    
    public void setNoHp(String noHp) {
		this.noHp = noHp;
	}
    
    public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}
    
    public String getPayeeId() {
		return payeeId;
	}
    
    public String getNoKursi() {
		return noKursi;
	}
    
    public void setNoKursi(String noKursi) {
		this.noKursi = noKursi;
	}
    
    public String getLokasiKeberangkatan() {
		return lokasiKeberangkatan;
	}
    
    
    public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
    
    public String getTransactionID() {
		return transactionID;
	}

    
    public void setTotalDirecetion(int totalDirecetion) {
		this.totalDirecetion = totalDirecetion;
	}
    
    public int getTotalDirecetion() {
		return totalDirecetion;
	}
    
    public void setTotalSheat(int totalSheat) {
		this.totalSheat = totalSheat;
	}
    
    public int getTotalSheat() {
		return totalSheat;
	}
    
    public void setTotalEmptySheat(int totalEmptySheat) {
		this.totalEmptySheat = totalEmptySheat;
	}
    
    public int getTotalEmptySheat() {
		return totalEmptySheat;
	}
    
    public void setTicketPricePerSheat(BigDecimal ticketPricePerSheat) {
		this.ticketPricePerSheat = ticketPricePerSheat;
	}
    
    public BigDecimal getTicketPricePerSheat() {
		return ticketPricePerSheat;
	}
    
    public void setDepartID(String departID) {
		DepartID = departID;
	}
    
    public String getDepartID() {
		return DepartID;
	}
    
    public void setDjatiPurchaseViews(
			List<TiketKeretaDjatiPurchaseView> djatiPurchaseViews) {
		this.djatiPurchaseViews = djatiPurchaseViews;
	}
    
    public List<TiketKeretaDjatiPurchaseView> getDjatiPurchaseViews() {
		return djatiPurchaseViews;
	}
    
    public void setTotalTicketPrice(BigDecimal totalTicketPrice) {
		this.totalTicketPrice = totalTicketPrice;
	}
    
    public BigDecimal getTotalTicketPrice() {
		return totalTicketPrice;
	}
    
    public void setTotalLocation(int totalLocation) {
		this.totalLocation = totalLocation;
	}
    
    public int getTotalLocation() {
		return totalLocation;
	}
    
    public void setListKursi(List<String> listKursi) {
		ListKursi = listKursi;
	}
    
    public List<String> getListKursi() {
		return ListKursi;
	}
    
    
    public void setListLocation(List<String> listLocation) {
		ListLocation = listLocation;
	}
    
    public List<String> getListLocation() {
		return ListLocation;
	}
    
    
    public void setLocationID(String locationID) {
		this.locationID = locationID;
	}
    
    public String getLocationID() {
		return locationID;
	}
    
    public void setDepartLocation(String departLocation) {
		this.departLocation = departLocation;
	}
    
    public String getDepartLocation() {
		return departLocation;
	}
    
    public void setDepartHour(String departHour) {
		this.departHour = departHour;
	}
    
    
    public String getDepartHour() {
		return departHour;
	}
    
    public void setNamaPenumpang1(String namaPenumpang1) {
		this.namaPenumpang1 = namaPenumpang1;
	}
    
    public String getNamaPenumpang1() {
		return namaPenumpang1;
	}
    
    public void setNamaPenumpang2(String namaPenumpang2) {
		this.namaPenumpang2 = namaPenumpang2;
	}
    
    public String getNamaPenumpang2() {
		return namaPenumpang2;
	}
    
    
    public void setTotalSchedule(int totalSchedule) {
		this.totalSchedule = totalSchedule;
	}
    
    public int getTotalSchedule() {
		return totalSchedule;
	}
    
    
    public void setScheduleCodeId(String scheduleCodeId) {
		this.scheduleCodeId = scheduleCodeId;
	}
    
    public String getScheduleCodeId() {
		return scheduleCodeId;
	}
    
    public void setDepartBranch(String departBranch) {
		this.departBranch = departBranch;
	}
    
    public String getDepartBranch() {
		return departBranch;
	}
    
    public void setDestinationBranch(String destinationBranch) {
		this.destinationBranch = destinationBranch;
	}
    
    public String getDestinationBranch() {
		return destinationBranch;
	}
    
    public Long getCustomerId() {
        return customerId;
    }
    
    
    public void setOriginData(int originData) {
		this.originData = originData;
	}
    
    public int getOriginData() {
		return originData;
	}
    
    public void setDataKotaAsal(String dataKotaAsal) {
		this.dataKotaAsal = dataKotaAsal;
	}
    
    public String getDataKotaAsal() {
		return dataKotaAsal;
	}
    
    public void setListKotaAsal(List<String> listKotaAsal) {
		ListKotaAsal = listKotaAsal;
	}
    
    public List<String> getListKotaAsal() {
		return ListKotaAsal;
	}
    
    
    public void setDestinationData(int destinationData) {
		this.destinationData = destinationData;
	}
    
    public int getDestinationData() {
		return destinationData;
	}
    
    public void setDestinationKotaTujuan(String destinationKotaTujuan) {
		this.destinationKotaTujuan = destinationKotaTujuan;
	}
    
    public String getDestinationKotaTujuan() {
		return destinationKotaTujuan;
	}
    
    public void setListKotaTujuan(List<String> listKotaTujuan) {
		ListKotaTujuan = listKotaTujuan;
	}
    
    public List<String> getListKotaTujuan() {
		return ListKotaTujuan;
	}
    
    public void setSeat(String seat) {
		this.seat = seat;
	}
    
    public String getSeat() {
		return seat;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    public String getName() {
		return name;
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
