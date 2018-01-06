package com.dwidasa.engine.model.view;

import java.math.BigDecimal;

public class ColumbiaPaymentView extends PaymentView {

	private String columbiaCode;

	private BigDecimal nilaiAngsuran;
	
	private String paymentPeriod;
	
	private Integer numOfBill;
	
	private String jatuhTempo;
	
	private BigDecimal penaltyFee; 

	private BigDecimal minimumPayment;

	private String transType;

	public String getColumbiaCode() {
		return columbiaCode;
	}

	public void setColumbiaCode(String columbiaCode) {
		this.columbiaCode = columbiaCode;
	}

	public BigDecimal getNilaiAngsuran() {
		return nilaiAngsuran;
	}

	public void setNilaiAngsuran(BigDecimal nilaiAngsuran) {
		this.nilaiAngsuran = nilaiAngsuran;
	}

	public String getPaymentPeriod() {
		return paymentPeriod;
	}

	public void setPaymentPeriod(String paymentPeriod) {
		this.paymentPeriod = paymentPeriod;
	}

	public Integer getNumOfBill() {
		return numOfBill;
	}

	public void setNumOfBill(Integer numOfBill) {
		this.numOfBill = numOfBill;
	}

	public String getJatuhTempo() {
		return jatuhTempo;
	}

	public void setJatuhTempo(String jatuhTempo) {
		this.jatuhTempo = jatuhTempo;
	}

	public BigDecimal getPenaltyFee() {
		return penaltyFee;
	}

	public void setPenaltyFee(BigDecimal penaltyFee) {
		this.penaltyFee = penaltyFee;
	}

	public BigDecimal getMinimumPayment() {
		return minimumPayment;
	}

	public void setMinimumPayment(BigDecimal minimumPayment) {
		this.minimumPayment = minimumPayment;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}
	
	
	
	

}
