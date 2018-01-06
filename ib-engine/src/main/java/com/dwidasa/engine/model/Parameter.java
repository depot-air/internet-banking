package com.dwidasa.engine.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

public class Parameter extends BaseObject implements Serializable {
    private static final long serialVersionUID = -4287524894171433948L;

	private String description;
	private String parameterName;
	private String parameterValue;

    public Parameter() {
    }

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParameterName() {
		return this.parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Parameter)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        Parameter that = (Parameter) o;
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
        return getParameterName();
    }
}