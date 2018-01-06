package com.dwidasa.engine.service.impl;

import java.util.List;

import com.dwidasa.engine.dao.ProviderDao;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/16/11
 * Time: 12:20 PM
 */
@Service("providerService")
public class ProviderServiceImpl extends GenericServiceImpl<Provider, Long> implements ProviderService {
	private ProviderDao providerDao;
	
    @Autowired
    public ProviderServiceImpl(ProviderDao providerDao) {
        super(providerDao);
        this.providerDao = providerDao;
    }

	@Override
	public List<Provider> getAllWithOrder() {
		return providerDao.getAllWithOrder();
	}
}
