package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;

public class Airport extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -5595325593037260943L;

    private String airportCode;
    private String airportName;
    private String airportCity;
    private String airportCountry;
    private String airportFullname;
    
    public Airport() {
    }

	public String getAirportCode() {
		return airportCode;
	}


	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}


	public String getAirportName() {
		return airportName;
	}


	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getAirportCity() {
		return airportCity;
	}

	public void setAirportCity(String airportCity) {
		this.airportCity = airportCity;
	}

	public String getAirportCountry() {
		return airportCountry;
	}

	public void setAirportCountry(String airportCountry) {
		this.airportCountry = airportCountry;
	}

	public String getAirportFullname() {
		return airportFullname;
	}


	public void setAirportFullname(String airportFullname) {
		this.airportFullname = airportFullname;
	}


	@Override
    public boolean equals(Object o) {
        if (!(o instanceof Airport)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Airport that = (Airport) o;
        return new EqualsBuilder()
                .append(this.getId(), that.getId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public String toString() {
        return getAirportFullname();
    }

    public String getLabel(Locale locale) {
        return getAirportFullname();  
    }

    public String getValue() {
        return getAirportCode();  
    }
}