package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.CustomerDevice;

public interface CustomerDeviceService extends GenericService<CustomerDevice, Long> {
	public Boolean isIbFirstLogin(Long customerId);
	public List<CustomerDevice> getNotActiveByCustomerId(Long customerId);
    public CustomerDevice getHardTokenDevice(Long customerId);
	public CustomerDevice getDeviceSoftToken(Long customerId);//
	public List<CustomerDevice> getDeviceActive(Long customerId);
}
