package com.dwidasa.engine.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class TrainStation extends BaseObject implements Serializable {
	private static final long serialVersionUID = -7304137066819138324L;
	
	private String cityName;
	private String stationCode;
	private String stationName;
	
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof TrainStation)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        TrainStation that = (TrainStation) o;
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
    	return stationCode;
    }

}
