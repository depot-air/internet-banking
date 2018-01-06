package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;

public class City extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -5595325593037260943L;

    private String cityCode;
    private String cityName;
    private String cityFullname;
    
    public City() {
    }


    public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityFullname() {
		return cityFullname;
	}

	public void setCityFullname(String cityFullname) {
		this.cityFullname = cityFullname;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof City)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        City that = (City) o;
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
        return getCityFullname();
    }

    public String getLabel(Locale locale) {
        return getCityFullname();  
    }

    public String getValue() {
        return getCityCode();  
    }
}