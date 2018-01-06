package com.dwidasa.engine.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ActivityCustomer extends BaseObject implements Serializable {
	private static final long serialVersionUID = 8976571773702279768L;

	private Long customerId;
	private String activityType;
	private String activityData;
	private String referenceNumber;
	private String deliveryChannel; // IB, MB, KIOSK
	private String deliveryChannelId; //terminal ID 

	private String username;
	private String transactionTypeDesc;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getActivityData() {
		return activityData;
	}

	public void setActivityData(String activityData) {
		this.activityData = activityData;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	
	public String getDeliveryChannel() {
		return deliveryChannel;
	}

	public void setDeliveryChannel(String deliveryChannel) {
		this.deliveryChannel = deliveryChannel;
	}

	public String getDeliveryChannelId() {
		return deliveryChannelId;
	}

	public void setDeliveryChannelId(String deliveryChannelId) {
		this.deliveryChannelId = deliveryChannelId;
	}

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTransactionTypeDesc() {
		return transactionTypeDesc;
	}

	public void setTransactionTypeDesc(String transactionTypeDesc) {
		this.transactionTypeDesc = transactionTypeDesc;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Inbox)) {
			return false;
		}
		if (this == o) {
			return true;
		}

		Inbox that = (Inbox) o;
		return new EqualsBuilder().append(this.getId(), that.getId())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@Override
	public String toString() {
		return String.valueOf(getId());
	}
	
	public String getStrReferenceNumber() {
		StringBuffer sb = new StringBuffer();
		String upcase = referenceNumber.toUpperCase();
		sb.append(upcase.substring(0, 4)).append(" ").append(upcase.substring(4, 8)).append(" ").append(upcase.substring(8));
		return sb.toString();
	}

}
