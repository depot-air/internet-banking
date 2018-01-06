package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Product;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:31 PM
 */
public interface ProductDao extends GenericDao<Product, Long> {
    /**
     * Get product by product code.
     * @param productCode product code
     * @return product object
     */
    public Product get(String productCode);
}
