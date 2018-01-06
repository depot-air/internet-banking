package com.dwidasa.ib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

public class LayoutPrint {
	@Property
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String title;
	
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String referenceNumber;

	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String transactionDateString;
	
	private String datetime;

    @Inject
    private ThreadLocale threadLocale;

	public String getDatetime() {
		/*
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.RESI_DATETIME, threadLocale.getLocale());
		this.datetime = sdf.format(new Date());
		*/
		return datetime;
	}

	public String setReferenceNumber(String referenceNumber) {
		return this.referenceNumber = referenceNumber;
	}
	
	public String getReferenceNumber() {
		return referenceNumber;
	}

	public String getTransactionDateString() {
		return transactionDateString;
	}

	public void setTransactionDateString(String transactionDateString) {
		this.transactionDateString = transactionDateString;
	}
	
}
