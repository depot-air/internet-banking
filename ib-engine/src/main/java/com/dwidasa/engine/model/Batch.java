package com.dwidasa.engine.model;

import java.io.Serializable;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ui.GenericSelectModel;

public class Batch extends BaseObject implements Serializable, GenericSelectModel {
	private static final long serialVersionUID = -5323328537293022996L;
	
	private String batchName;
	private String description;
	private Long customerId;
	
	public String getBatchName() {
		return batchName;
	}
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	@Override
	public boolean equals(Object o) {
		 if (!(o instanceof Batch)) {
	            return false;
	        }
	        if (this == o) {
	            return true;
	        }

	        Batch that = (Batch) o;
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
		return batchName;
	}
	@Override
	public String getLabel(Locale locale) {
		return batchName + " - " + description;
	}
	@Override
	public String getValue() {
		return String.valueOf(getId());
	}
	
	
}
