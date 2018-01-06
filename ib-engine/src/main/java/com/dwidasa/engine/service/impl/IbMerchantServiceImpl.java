package com.dwidasa.engine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.IbMerchantDao;
import com.dwidasa.engine.model.IbMerchant;
import com.dwidasa.engine.service.IbMerchantService;

@Service("ibMerchantService")
public class IbMerchantServiceImpl extends GenericServiceImpl<IbMerchant, Long> implements IbMerchantService {
	private IbMerchantDao ibMerchantDao;
	
    @Autowired
    public IbMerchantServiceImpl(IbMerchantDao ibMerchantDao) {
        super(ibMerchantDao);
        this.ibMerchantDao = ibMerchantDao;
    }
    public List<IbMerchant> getMerchantBySerialNumber(String serialNumber){
    	return ibMerchantDao.getMerchantBySerialNumber(serialNumber);
    }
	public IbMerchant getById(Long id){
		return ibMerchantDao.getById(id);
	}
}
