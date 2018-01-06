package com.dwidasa.engine.model;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ui.GenericSelectModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Locale;

public class CustomerRegister extends BaseObject implements Serializable, GenericSelectModel {
    private static final long serialVersionUID = -3943913838265625419L;

    private String transactionType;
	private String customerReference;
	private String data1;
	private String data2;
	private String data3;
	private String data4;
	private String data5;

    private Long customerId;
    
    //lookup
    private String billerName;

    private Customer customer;

    public CustomerRegister() {
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }

    public String getData4() {
        return data4;
    }

    public void setData4(String data4) {
        this.data4 = data4;
    }

    public String getData5() {
        return data5;
    }

    public void setData5(String data5) {
        this.data5 = data5;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CustomerRegister)) {
            return false;
        }
        if (this == o) {
            return true;
        }

        CustomerRegister that = (CustomerRegister) o;
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
        return getCustomerReference();
    }
    
    public String getBillerName() {
		return billerName;
	}

	public void setBillerName(String billerName) {
		this.billerName = billerName;
	}

	public String getLabel(Locale locale) {
        if (transactionType.equals(Constants.TRANSFER_CODE) ||
                transactionType.equals(Constants.TRANSFER_TREASURY_CODE) || 
                transactionType.equals(Constants.ALTO.TT_INQUIRY) || transactionType.equals(Constants.ALTO.TT_POSTING)) {
            return getCustomerReference() + " - " + getData3();
        }

        return getCustomerReference();
    }

    public String getValue() {
        if (transactionType.equals(Constants.TRANSFER_CODE) ||
                transactionType.equals(Constants.TRANSFER_TREASURY_CODE) || 
                transactionType.equals(Constants.ALTO.TT_INQUIRY) || transactionType.equals(Constants.ALTO.TT_POSTING)) {
            return StringUtils.rightPad(getCustomerReference(), 50) + getData3();
        }

        return getCustomerReference();
    }
}