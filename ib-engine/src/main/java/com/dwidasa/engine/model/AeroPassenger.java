package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.dwidasa.engine.ui.GenericSelectModel;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AeroPassenger extends BaseObject implements Serializable, GenericSelectModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7173680268910850439L;

    public static final class PASSENGER_TYPE {
	    public static final String ADULT = "Adult";
	    public static final String CHILD = "Child";
	    public static final String INFANT = "Infant";
    }
    public static final class PASSENGER_TITLE {
	    public static final String CHILD = "chd";
	    public static final String INFANT = "inf";
    }
	private String passengerType; 
	private String passengerTitle; 
	private String passengerFirstName; 
	private String passengerLastName; 
	private String passengerIdCard; 
	private Date passengerDob; 
	private Boolean paspor;
	private Boolean expirePaspor;
	private Boolean countryPaspor;
	private String parent;
	private String country;
	
	public String getPassengerType() {
		return passengerType;
	}
	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}
	public String getPassengerTitle() {
		return passengerTitle;
	}
	public void setPassengerTitle(String passengerTitle) {
		this.passengerTitle = passengerTitle;
	}
	public String getPassengerFirstName() {
		return passengerFirstName;
	}
	public void setPassengerFirstName(String passengerFirstName) {
		this.passengerFirstName = passengerFirstName;
	}
	public String getPassengerLastName() {
		return passengerLastName;
	}
	public void setPassengerLastName(String passengerLastName) {
		this.passengerLastName = passengerLastName;
	}
	public String getPassengerIdCard() {
		return passengerIdCard;
	}
	public void setPassengerIdCard(String passengerIdCard) {
		this.passengerIdCard = passengerIdCard;
	}
	public Date getPassengerDob() {
		return passengerDob;
	}
	public void setPassengerDob(Date passengerDob) {
		this.passengerDob = passengerDob;
	}
	public Boolean getPaspor() {
		return paspor;
	}
	public void setPaspor(Boolean paspor) {
		this.paspor = paspor;
	}
	public Boolean getExpirePaspor() {
		return expirePaspor;
	}
	public void setExpirePaspor(Boolean expirePaspor) {
		this.expirePaspor = expirePaspor;
	}
	public Boolean getCountryPaspor() {
		return countryPaspor;
	}
	public void setCountryPaspor(Boolean countryPaspor) {
		this.countryPaspor = countryPaspor;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	@Override
	public String getLabel(Locale locale) {
		return getPassengerFirstName() + " " + getPassengerLastName();
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AeroPassenger)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AeroPassenger that = (AeroPassenger) o;
        return new EqualsBuilder()
                .append(this.getPassengerFirstName() + " " + this.getPassengerLastName(), that.getPassengerFirstName() + " " + that.getPassengerLastName())
                .isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getPassengerFirstName() + " " + getPassengerLastName()).toHashCode();
	}
	@Override
	public String toString() {
		return getPassengerFirstName() + " " + getPassengerLastName();
	}
	@Override
	public String getValue() {
		return getPassengerFirstName() + " " + getPassengerLastName();
	}

	
}
