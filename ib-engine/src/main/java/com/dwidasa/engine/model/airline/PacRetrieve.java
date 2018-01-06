package com.dwidasa.engine.model.airline;

import java.math.BigDecimal;
import java.util.Date;

public class PacRetrieve {	
	private String bookingCode;
	private String airlineCode;
	private String status;
	private Date timeLimit;
	private BigDecimal agentFare;
	private BigDecimal agentInsurance;
	private BigDecimal publishFare;
	private BigDecimal publishInsurance;

	public String getBookingCode() {
		return bookingCode;
	}
	public void setBookingCode(String bookingCode) {
		this.bookingCode = bookingCode;
	}
	public String getAirlineCode() {
		return airlineCode;
	}
	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Date timeLimit) {
		this.timeLimit = timeLimit;
	}
	public BigDecimal getAgentFare() {
		return agentFare;
	}
	public void setAgentFare(BigDecimal agentFare) {
		this.agentFare = agentFare;
	}
	public BigDecimal getAgentInsurance() {
		return agentInsurance;
	}
	public void setAgentInsurance(BigDecimal agentInsurance) {
		this.agentInsurance = agentInsurance;
	}
	public BigDecimal getPublishFare() {
		return publishFare;
	}
	public void setPublishFare(BigDecimal publishFare) {
		this.publishFare = publishFare;
	}
	public BigDecimal getPublishInsurance() {
		return publishInsurance;
	}
	public void setPublishInsurance(BigDecimal publishInsurance) {
		this.publishInsurance = publishInsurance;
	}
	
}
