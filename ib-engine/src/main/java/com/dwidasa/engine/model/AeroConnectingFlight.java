package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AeroConnectingFlight extends BaseObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8647241697680415855L;
	
	private String connectingFlightFrom;
    private String connectingFlightTo;
    private String connectingFlightCityFrom;
    private String connectingFlightCityTo;
    private Date connectingFlightDate;
    private String connectingFlightFno;
    private Date connectingFlightEtd;
    private Date connectingFlightEta;
    private String connectingFlightClassname;
    private BigDecimal connectingFlightPrice;
    private Integer connectingFlightSeat;
    private boolean throughFare;
    
	public String getConnectingFlightFrom() {
		return connectingFlightFrom;
	}
	public void setConnectingFlightFrom(String connectingFlightFrom) {
		this.connectingFlightFrom = connectingFlightFrom;
	}
	public String getConnectingFlightTo() {
		return connectingFlightTo;
	}
	public void setConnectingFlightTo(String connectingFlightTo) {
		this.connectingFlightTo = connectingFlightTo;
	}	
	public String getConnectingFlightCityFrom() {
		return connectingFlightCityFrom;
	}
	public void setConnectingFlightCityFrom(String connectingFlightCityFrom) {
		this.connectingFlightCityFrom = connectingFlightCityFrom;
	}
	public String getConnectingFlightCityTo() {
		return connectingFlightCityTo;
	}
	public void setConnectingFlightCityTo(String connectingFlightCityTo) {
		this.connectingFlightCityTo = connectingFlightCityTo;
	}
	public Date getConnectingFlightDate() {
		return connectingFlightDate;
	}
	public void setConnectingFlightDate(Date connectingFlightDate) {
		this.connectingFlightDate = connectingFlightDate;
	}
	public String getConnectingFlightFno() {
		return connectingFlightFno;
	}
	public void setConnectingFlightFno(String connectingFlightFno) {
		this.connectingFlightFno = connectingFlightFno;
	}
	public Date getConnectingFlightEtd() {
		return connectingFlightEtd;
	}
	public void setConnectingFlightEtd(Date connectingFlightEtd) {
		this.connectingFlightEtd = connectingFlightEtd;
	}
	public Date getConnectingFlightEta() {
		return connectingFlightEta;
	}
	public void setConnectingFlightEta(Date connectingFlightEta) {
		this.connectingFlightEta = connectingFlightEta;
	}
	public String getConnectingFlightClassname() {
		return connectingFlightClassname;
	}
	public void setConnectingFlightClassname(String connectingFlightClassname) {
		this.connectingFlightClassname = connectingFlightClassname;
	}

	public BigDecimal getConnectingFlightPrice() {
		return connectingFlightPrice;
	}
	public void setConnectingFlightPrice(BigDecimal connectingFlightPrice) {
		this.connectingFlightPrice = connectingFlightPrice;
	}
	
	public Integer getConnectingFlightSeat() {
		return connectingFlightSeat;
	}
	public void setConnectingFlightSeat(Integer connectingFlightSeat) {
		this.connectingFlightSeat = connectingFlightSeat;
	}
	public boolean isThroughFare() {
		return throughFare;
	}
	public void setThroughFare(boolean throughFare) {
		this.throughFare = throughFare;
	}
	//	@Override
//	public String getLabel(Locale locale) {
//		return connectingFlightFno;
//	}
//	@Override
//	public String getValue() {
//		return connectingFlightFno;
//	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AeroConnectingFlight)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AeroConnectingFlight that = (AeroConnectingFlight) o;
        return new EqualsBuilder()
                .append(this.getConnectingFlightFno(), that.getConnectingFlightFno())
                .isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getConnectingFlightFno()).toHashCode();
	}
	@Override
	public String toString() {
		return connectingFlightFno;
	}
	
	
}
