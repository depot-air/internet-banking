package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.CustomerSessionDao;
import com.dwidasa.engine.model.CustomerSession;
import com.dwidasa.engine.service.CustomerSessionService;

@Service("customerSessionService")
public class CustomerSessionServiceImpl extends GenericServiceImpl<CustomerSession, Long> implements CustomerSessionService {
	private CustomerSessionDao customerSessionDao;
	
	@Autowired
	public CustomerSessionServiceImpl(CustomerSessionDao customerSessionDao) {
		super(customerSessionDao);
		this.customerSessionDao = customerSessionDao;
	}

	@Override
	public void removeAll() {
		customerSessionDao.removeAll();
	}
}
