package com.dwidasa.engine.model.airline;

import java.math.BigDecimal;

public class PacAirFare {
	private String firstAirlineCode;
	private String secondAirlineCode;
	private BigDecimal firstFare;
	private BigDecimal secondFare;
	private BigDecimal insurance;
	public String getFirstAirlineCode() {
		return firstAirlineCode;
	}
	public void setFirstAirlineCode(String firstAirlineCode) {
		this.firstAirlineCode = firstAirlineCode;
	}
	public String getSecondAirlineCode() {
		return secondAirlineCode;
	}
	public void setSecondAirlineCode(String secondAirlineCode) {
		this.secondAirlineCode = secondAirlineCode;
	}
	public BigDecimal getFirstFare() {
		return firstFare;
	}
	public void setFirstFare(BigDecimal firstFare) {
		this.firstFare = firstFare;
	}
	public BigDecimal getSecondFare() {
		return secondFare;
	}
	public void setSecondFare(BigDecimal secondFare) {
		this.secondFare = secondFare;
	}
	public BigDecimal getInsurance() {
		return insurance;
	}
	public void setInsurance(BigDecimal insurance) {
		this.insurance = insurance;
	}

}
