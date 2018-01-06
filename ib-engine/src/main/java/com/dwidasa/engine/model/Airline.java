package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;

public class Airline extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -5595325593037260943L;

    private String airlineId;
    private String airlineCode;
    private String airlineName;
    private String airlineType;
    private String image;
    //untuk airline connect
    private String airlineConnect;
    
    public Airline() {
    }

	public String getAirlineId() {
		return airlineId;
	}


	public void setAirlineId(String airlineId) {
		this.airlineId = airlineId;
	}


	public String getAirlineCode() {
		return airlineCode;
	}


	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}


	public String getAirlineName() {
		return airlineName;
	}


	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}


	public String getAirlineType() {
		return airlineType;
	}

	public void setAirlineType(String airlineType) {
		this.airlineType = airlineType;
	}

	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAirlineConnect() {
		return airlineConnect;
	}

	public void setAirlineConnect(String airlineConnect) {
		this.airlineConnect = airlineConnect;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof Airline)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Airline that = (Airline) o;
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
        return getAirlineName();
    }

    public String getLabel(Locale locale) {
        return getAirlineName();  
    }

    public String getValue() {
        return getAirlineCode();  
    }
}