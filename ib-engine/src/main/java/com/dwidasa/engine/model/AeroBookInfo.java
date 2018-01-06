package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AeroBookInfo extends BaseObject implements Serializable, GenericSelectModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7173680268910850439L;
	public static final class BOOK_STATUS {
	    public static final String FAILED_CODE = "0";
	    public static final String CONFIRMED_CODE = "1";
	    public static final String FAILED_DESC = "Failed";
	    public static final String CONFIRMED_DESC = "Confirmed";
	    
	    public static final String VOLTRAS_BOOKED = "BOOKED";
	    
    }
	private String bookCode; 
	private Date timeLimit; 
	private String status; 
	
	private String airlineCode;
	private BigDecimal agentFare;
	private BigDecimal agentInsurance;
	private BigDecimal publishFare;
	private BigDecimal publishInsurance;
	
	
	public String getBookCode() {
		return bookCode;
	}
	public void setBookCode(String bookCode) {
		this.bookCode = bookCode;
	}
	public Date getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Date timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAirlineCode() {
		return airlineCode;
	}
	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
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
	@Override
	public String getLabel(Locale locale) {
		return getBookCode();
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AeroBookInfo)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AeroBookInfo that = (AeroBookInfo) o;
        return new EqualsBuilder()
                .append(this.getBookCode(), that.getBookCode())
                .isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getBookCode()).toHashCode();
	}
	@Override
	public String toString() {
		return getBookCode();
	}
	@Override
	public String getValue() {
		return getBookCode();
	}

	
}
