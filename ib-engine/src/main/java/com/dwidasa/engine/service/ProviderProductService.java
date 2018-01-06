package com.dwidasa.engine.service;

import com.dwidasa.engine.model.ProviderProduct;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/18/11
 * Time: 10:29 AM
 */
public interface ProviderProductService extends GenericService<ProviderProduct, Long> {

	ProviderProduct getWithTransactionType(Long id);
    ProviderProduct getByProviderCodeProductCode(String providerCode, String productCode);
}
