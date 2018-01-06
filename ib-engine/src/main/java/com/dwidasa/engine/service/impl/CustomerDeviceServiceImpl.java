package com.dwidasa.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.service.CustomerDeviceService;

@Service("customerDeviceService")
public class CustomerDeviceServiceImpl extends GenericServiceImpl<CustomerDevice, Long> implements CustomerDeviceService {
	private CustomerDeviceDao customerDeviceDao;
	
	@Autowired
	public CustomerDeviceServiceImpl(CustomerDeviceDao customerDeviceDao) {
		super(customerDeviceDao);
		this.customerDeviceDao = customerDeviceDao;
	}

	@Override
	public Boolean isIbFirstLogin(Long customerId) {		
		return customerDeviceDao.isIbFirstLogin(customerId);
	}

	@Override
	public List<CustomerDevice> getNotActiveByCustomerId(Long customerId) {
		return customerDeviceDao.getAllNotActiveYet(customerId);
	}

	
    @Override
    public CustomerDevice getHardTokenDevice(Long customerId) {
        return customerDeviceDao.getHardTokenDevice(customerId);
    }

	@Override
	public CustomerDevice getDeviceSoftToken(Long customerId) { //
		return customerDeviceDao.getDeviceSoftToken(customerId);
	}

	@Override
	public  List<CustomerDevice> getDeviceActive(Long customerId) {
		// TODO Auto-generated method stub
		return customerDeviceDao.getAllActive(customerId);
	}
}