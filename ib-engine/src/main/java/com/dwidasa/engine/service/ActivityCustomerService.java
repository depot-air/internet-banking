package com.dwidasa.engine.service;

import java.io.InputStream;
import java.util.Date;

import com.dwidasa.engine.model.ActivityCustomer;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/23/11
 * Time: 8:35 PM
 */
public interface ActivityCustomerService extends GenericService<ActivityCustomer, Long> {

	InputStream getCsvStream(Date startDate, Date endDate, String transactionType);
	
}
