package com.dwidasa.engine.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.dwidasa.engine.ui.GenericSelectModel;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:39
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AeroFlightClass extends BaseObject implements Serializable, GenericSelectModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7173680268910850439L;
	
	private String classId;
	private String classLabel;
	private String className;
	private BigDecimal price;
	private Integer avaliableSeat;
	private String value;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassLabel() {
		return classLabel;
	}
	public void setClassLabel(String classLabel) {
		this.classLabel = classLabel;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getAvaliableSeat() {
		return avaliableSeat;
	}
	public void setAvaliableSeat(Integer avaliableSeat) {
		this.avaliableSeat = avaliableSeat;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String getLabel(Locale locale) {
		return getClassId() + getClassName();
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AeroFlightClass)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AeroFlightClass that = (AeroFlightClass) o;
        return new EqualsBuilder()
                .append(this.getClassId() + this.getClassName(), that.getClassId() + that.getClassName())
                .isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getClassId() + getClassName()).toHashCode();
	}
	@Override
	public String toString() {
		return getClassId() + getClassName();
	}

}
