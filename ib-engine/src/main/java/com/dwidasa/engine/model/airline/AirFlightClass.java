package com.dwidasa.engine.model.airline;

import java.math.BigDecimal;

public class AirFlightClass {
	String classId;
	String codeName;
	BigDecimal farePrice;
	int availableSeat;
	String currency;
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public BigDecimal getFarePrice() {
		return farePrice;
	}
	public void setFarePrice(BigDecimal farePrice) {
		this.farePrice = farePrice;
	}
	public int getAvailableSeat() {
		return availableSeat;
	}
	public void setAvailableSeat(int availableSeat) {
		this.availableSeat = availableSeat;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
}
