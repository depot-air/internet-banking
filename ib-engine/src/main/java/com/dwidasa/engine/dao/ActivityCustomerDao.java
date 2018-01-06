package com.dwidasa.engine.dao;

import java.util.List;

import com.dwidasa.engine.model.ActivityCustomer;

public interface ActivityCustomerDao extends GenericDao<ActivityCustomer, Long> {
	public void logActivity(Long customerId, String activityType, String activityData, String referenceNumber, String merchantType, String terminalId);

	public List<ActivityCustomer> getLastLoginNonFinancialActivity(Long customerId);
}
