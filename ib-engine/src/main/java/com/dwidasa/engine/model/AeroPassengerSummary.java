package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dwidasa.engine.ui.GenericSelectModel;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AeroPassengerSummary extends BaseObject implements Serializable, GenericSelectModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7173680268910850439L;
	
	private String passengerType;
	private Integer pax;
	private BigDecimal basic;
	private BigDecimal tax;
	private BigDecimal iwjr;
	private BigDecimal service;
	private BigDecimal total;
	
	public String getPassengerType() {
		return passengerType;
	}
	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}
	public Integer getPax() {
		return pax;
	}
	public void setPax(Integer pax) {
		this.pax = pax;
	}
	public BigDecimal getBasic() {
		return basic;
	}
	public void setBasic(BigDecimal basic) {
		this.basic = basic;
	}
	public BigDecimal getTax() {
		return tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}
	public BigDecimal getIwjr() {
		return iwjr;
	}
	public void setIwjr(BigDecimal iwjr) {
		this.iwjr = iwjr;
	}
	public BigDecimal getService() {
		return service;
	}
	public void setService(BigDecimal service) {
		this.service = service;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	@Override
	public String getLabel(Locale locale) {
		return getPassengerType() + " " + getPax();
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AeroPassengerSummary)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AeroPassengerSummary that = (AeroPassengerSummary) o;
        return new EqualsBuilder()
                .append(this.getPassengerType() + " " + this.getPax(), that.getPassengerType() + " " + that.getPax	())
                .isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getPassengerType() + " " + getPax()).toHashCode();
	}
	@Override
	public String toString() {
		return getPassengerType() + " " + getPax();
	}
	@Override
	public String getValue() {
		return getPassengerType() + " " + getPax();
	}

}
