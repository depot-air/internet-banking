package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.IbMerchant;

public interface IbMerchantService extends GenericService<IbMerchant, Long> {
	public List<IbMerchant> getMerchantBySerialNumber(String serialNumber);
	public IbMerchant getById(Long id);
}
