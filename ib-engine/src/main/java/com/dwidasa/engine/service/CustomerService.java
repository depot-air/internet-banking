package com.dwidasa.engine.service;

import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;

public interface CustomerService extends GenericService<Customer, Long> {

	/**
	 * update field status
	 * @param id primary key of m_customer
	 * @param status (1=active, 0=block)
	 */
	void updateStatus(Long id, int status);
	void updateStatusUnblockOrblock(Long id, String tin, int status);
	void updateStatusUnblockOrblockDevice(Long id, String tin, int status);
	void updateStatusUnblockOrblockIBMerchant(Long id, String tin, int status);
	void updateStatusUnblockOrblockIBToken(Long id, String tin, int status);
	Customer getByUsername(String username);
	Customer getByTID(String TerminalId);
	Customer getByUsernameAndByTID(String username, String TerminalId);
	CustomerAccount getCustomerAccount(Long customerId, String accountNumber);
    public int checkCustomerStatus(Long id);
    public Long getCustomerIdByUsername(String username);
}
