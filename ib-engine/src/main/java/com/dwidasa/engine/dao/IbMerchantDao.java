package com.dwidasa.engine.dao;

import java.util.List;

import com.dwidasa.engine.model.IbMerchant;

public interface IbMerchantDao extends GenericDao<IbMerchant, Long> {
	public List<IbMerchant> getMerchantBySerialNumber(String serialNumber);
	public IbMerchant getById(Long id);
	public IbMerchant getByCustomerId(Long customerId);
}
