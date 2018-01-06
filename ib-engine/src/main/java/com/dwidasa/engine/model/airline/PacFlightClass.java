package com.dwidasa.engine.model.airline;

import java.math.BigDecimal;

public class PacFlightClass {
	private String code;
	private String name;
	private Integer availableSeat;
	private BigDecimal fare;
	private String currency;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAvailableSeat() {
		return availableSeat;
	}
	public void setAvailableSeat(Integer availableSeat) {
		this.availableSeat = availableSeat;
	}
	public BigDecimal getFare() {
		return fare;
	}
	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public  String toString()
	{
	    return " [VoltrasClass: " + code + " " +
			availableSeat + " " +
			fare + " " +
			currency + "] ";
	} 
}
