package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.service.ProviderProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/18/11
 * Time: 10:30 AM
 */
@Service("providerProductService")
public class ProviderProductServiceImpl extends GenericServiceImpl<ProviderProduct, Long>
        implements ProviderProductService {
	private ProviderProductDao providerProductDao;
	
    @Autowired
    public ProviderProductServiceImpl(ProviderProductDao providerProductDao) {
        super(providerProductDao);
        this.providerProductDao = providerProductDao;
    }

	@Override
	public ProviderProduct getWithTransactionType(Long id) {
		return providerProductDao.getWithTransactionType(id);
	}

    @Override
    public ProviderProduct getByProviderCodeProductCode(String providerCode, String productCode) {
        return providerProductDao.getByProviderCodeProductCode(providerCode, productCode);
    }
}
