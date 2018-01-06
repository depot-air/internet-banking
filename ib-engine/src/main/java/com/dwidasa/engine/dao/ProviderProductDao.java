package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.ProviderProduct;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:24 PM
 */
public interface ProviderProductDao extends GenericDao<ProviderProduct, Long> {
    /**
     * Get all provider product with direct relation to provider and
     * indirect relation to transaction type is initialized.
     * @return list of provider product
     */
    public List<ProviderProduct> getAllWithTransactionTypeAndProvider();

    /**
     * Get all provider product from biller product id.
     * Object returned will contain corresponding provider object.
     * @param productCode product code
     * @return list of providerProduct
     */
    public List<ProviderProduct> getAll(String productCode);

    /**
     * Get Provider Product with transaction type field loaded
     * @param id
     * @return
     */
	public ProviderProduct getWithTransactionType(Long id);

    public ProviderProduct getByProviderCodeProductCode(String providerCode, String productCode);
}
