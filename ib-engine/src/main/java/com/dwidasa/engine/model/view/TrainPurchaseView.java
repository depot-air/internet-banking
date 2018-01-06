package com.dwidasa.engine.model.view;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.train.TrainPassenger;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainPurchaseView implements BaseView {
	private Long customerId;
	private String cardNumber;
	private String accountNumber;
	private Date transactionDate;
	private String transactionType;
	private BigDecimal amount;
	private String responseCode;
	private String referenceNumber;
	
	private int adult;
	private int child;
	private int infant;
	
	private String originCode;
	private String destinationCode;
	
	private String originName;
	private String destinationName;
	
	private Date departureDate;
	private Date arrivalDate;
	
	private String trainNumber;
	private String trainName;
	private String seatClass;
	private String subclass;
	
	private BigDecimal adultFare;
	private BigDecimal childFare;
	private BigDecimal infantFare;
	
	private String contactTitle;
	private String contactFirstName;
	private String contactLastName;
	private String contactPhone;
	
	private String bookingCode;
	private BigDecimal fee;
	private BigDecimal bookBalance; //book balance sesuai dari KAI
	private BigDecimal discount;
	
	private String accountType;
	private String merchantType;
	private String terminalId;
	
	//kalau dilihat dari API change seat, maka seluruh penumpang di 1 kode booking pasti berada di 1 gerbong 
	private String wagonCode;
	private String wagonNumber;
	
	private List<TrainPassenger> passengerList;
	
	public String getOriginCode() {
		return originCode;
	}

	public void setOriginCode(String originCode) {
		this.originCode = originCode;
	}

	public String getDestinationCode() {
		return destinationCode;
	}

	public void setDestinationCode(String destinationCode) {
		this.destinationCode = destinationCode;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	
	public Date getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public int getAdult() {
		return adult;
	}

	public void setAdult(int adult) {
		this.adult = adult;
	}

	public int getChild() {
		return child;
	}

	public void setChild(int child) {
		this.child = child;
	}

	public int getInfant() {
		return infant;
	}

	public void setInfant(int infant) {
		this.infant = infant;
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

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
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

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getSeatClass() {
		return seatClass;
	}

	public void setSeatClass(String seatClass) {
		this.seatClass = seatClass;
	}

	public String getSubclass() {
		return subclass;
	}

	public void setSubclass(String subclass) {
		this.subclass = subclass;
	}

	public BigDecimal getAdultFare() {
		return adultFare;
	}

	public void setAdultFare(BigDecimal adultFare) {
		this.adultFare = adultFare;
	}

	public BigDecimal getChildFare() {
		return childFare;
	}

	public void setChildFare(BigDecimal childFare) {
		this.childFare = childFare;
	}

	public BigDecimal getInfantFare() {
		return infantFare;
	}

	public void setInfantFare(BigDecimal infantFare) {
		this.infantFare = infantFare;
	}
	
	public String getContactTitle() {
		return contactTitle;
	}

	public void setContactTitle(String contactTitle) {
		this.contactTitle = contactTitle;
	}

	public String getContactFirstName() {
		return contactFirstName;
	}

	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	public String getContactLastName() {
		return contactLastName;
	}
	
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}
	
	public String getBookingCode() {
		return bookingCode;
	}

	public void setBookingCode(String bookingCode) {
		this.bookingCode = bookingCode;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
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

	public BigDecimal getBookBalance() {
		return bookBalance;
	}

	public void setBookBalance(BigDecimal bookBalance) {
		this.bookBalance = bookBalance;
	}
	
	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getWagonCode() {
		return wagonCode;
	}

	public void setWagonCode(String wagonCode) {
		this.wagonCode = wagonCode;
	}

	public String getWagonNumber() {
		return wagonNumber;
	}

	public void setWagonNumber(String wagonNumber) {
		this.wagonNumber = wagonNumber;
	}

	public List<TrainPassenger> getPassengerList() {
		return passengerList;
	}

	public void setPassengerList(List<TrainPassenger> passengerList) {
		this.passengerList = passengerList;
	}

	public String getStrClass() {
		if ("E".equals(seatClass)) {
			return "Eksekutif";
		} else if ("B".equals(seatClass)) {
			return "Bisnis";
		} else if ("K".equals(seatClass)) {
			return "Ekonomi";
		}
		return "-";
	}
	
	public String getStrContactName() {
		if (contactFirstName == null) contactFirstName = "";
		if (contactLastName == null) contactLastName = "";
		return contactTitle + " " + contactFirstName + " " + contactLastName;
	}
	
	public String getStrContactPhone() {
		return "+62 " + contactPhone;
	}
	
	public String getStrPassenger() {
		StringBuilder sb = new StringBuilder();
		if (adult > 0) {
			sb.append(adult).append(" Dewasa");
		}
		if (child > 0) {
			sb.append(", ").append(child).append(" Anak");
		}
		if (infant > 0) {
			sb.append(", ").append(infant).append(" Infant");
		}
		if (", ".equals(sb.substring(0,1))) {
			sb.delete(0,1);
		}
		return sb.toString();
	}
	
	public BigDecimal getTotalAdultFare() {
		if ((adult == 0) || adultFare == null) return BigDecimal.ZERO;
		return adultFare.multiply(new BigDecimal(adult));
	}
	
	public BigDecimal getTotalChildFare() {
		if ((child == 0) || childFare == null) return BigDecimal.ZERO;
		return childFare.multiply(new BigDecimal(child));
	}
	public BigDecimal getTotalInfantFare() {
		if ((infant == 0) || infantFare == null) return BigDecimal.ZERO;
		return infantFare.multiply(new BigDecimal(infant));
	}
	
	public String getTransactionDateString() {
    	if (transactionDate != null) {
    		SimpleDateFormat sdf = new SimpleDateFormat(Constants.RESI_DATETIME);
    		return sdf.format(transactionDate);    		 
    	}
    	return "";
	}
	
	public BigDecimal getTotalPay() {
		BigDecimal bb = bookBalance == null ? BigDecimal.ZERO : bookBalance;
		BigDecimal f = fee == null ? BigDecimal.ZERO : fee;
		return bb.add(f);
	}

	
	@Override
	public CustomerRegister transform() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Boolean validate() {	
		return Boolean.TRUE;
	}
	

}
