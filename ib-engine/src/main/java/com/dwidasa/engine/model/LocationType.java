package com.dwidasa.engine.model;

import com.dwidasa.engine.ui.GenericSelectModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Locale;

public class LocationType extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -8599230223424267430L;

	private String description;
	private String locationType;

    public LocationType() {
    }

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocationType() {
		return this.locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocationType)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        LocationType that = (LocationType) o;
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
        return getLocationType();
    }

    public String getLabel(Locale locale) {
        return description;
    }

    public String getValue() {
        return locationType;
    }
}