package com.dwidasa.engine.model;

import java.io.Serializable;
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
@JsonIgnoreProperties(ignoreUnknown=true)
public class AeroCustomer extends BaseObject implements Serializable, GenericSelectModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7173680268910850439L;

	private String title; 
	private String customerName; 
	private String firstName;
	private String lastName;
	private String customerPhone;
	private String customerEmail; 
		
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCustomerPhone() {
		return customerPhone;
	}
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	public String getCustomerEmail() {
		return customerEmail;
	}
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
	@Override
	public String getLabel(Locale locale) {
		return getCustomerName() + " " + getCustomerPhone();
	}
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AeroCustomer)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        AeroCustomer that = (AeroCustomer) o;
        return new EqualsBuilder()
                .append(this.getCustomerName() + " " + this.getCustomerPhone(), that.getCustomerName() + " " + that.getCustomerPhone())
                .isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getCustomerName() + " " + getCustomerPhone()).toHashCode();
	}
	@Override
	public String toString() {
		return getCustomerName() + " " + getCustomerPhone();
	}
	@Override
	public String getValue() {
		return getCustomerName() + " " + getCustomerPhone();
	}

}
