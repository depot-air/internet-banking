package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.service.CustomerService;

@Service("customerService")
public class CustomerServiceImpl extends GenericServiceImpl<Customer, Long> implements CustomerService {
	private CustomerDao customerDao;
	private CustomerAccountDao customerAccountDao;
	
	@Autowired
	public CustomerServiceImpl(CustomerDao customerDao, CustomerAccountDao customerAccountDao) {
		super(customerDao);
		this.customerDao = customerDao;
		this.customerAccountDao = customerAccountDao;
	}

	@Override
	public void updateStatus(Long id, int status) {
		customerDao.updateStatus(id, status);
	}
	
	
	@Override
	public void updateStatusUnblockOrblock(Long id, String tin, int status) {
		customerDao.updateStatusUnblockOrblock(id, tin, status);
	}
	
	@Override
	public void updateStatusUnblockOrblockDevice(Long id, String tin, int status) {
		customerDao.updateStatusUnblockOrblockCustomerDevice(id, tin, status);
	}
	
	@Override
	public void updateStatusUnblockOrblockIBMerchant(Long id, String tin, int status) {
		customerDao.updateStatusUnblockOrblockIbMerchant(id, tin, status);
	}
	
	@Override
	public void updateStatusUnblockOrblockIBToken(Long id, String tin, int status) {
		customerDao.updateStatusUnblockOrblockIbToken(id, tin, status);
	}

	@Override
	public Customer getByUsername(String username) {
		return customerDao.get(username);
	}
	
	@Override
	public Customer getByTID(String TerminalId){
		return customerDao.getByTID(TerminalId);
	}

	@Override
	public CustomerAccount getCustomerAccount(Long customerId, String accountNumber) {
		return customerAccountDao.get(customerId, accountNumber);
	}

    @Override
    public int checkCustomerStatus(Long id){
        return customerDao.checkCustomerStatus(id);
    }

	@Override
	public Long getCustomerIdByUsername(String username) {
		return customerDao.getCustomerIdByUsername(username);
	}

	@Override
	public Customer getByUsernameAndByTID(String username, String TerminalId) {
		
		return customerDao.getByUsernameAndByTID(username, TerminalId);
	}
}