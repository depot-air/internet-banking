package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.ProductDenomination;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:07 PM
 */
public interface ProductDenominationDao extends GenericDao<ProductDenomination, Long> {
    /**
     * Get all product's denomination with indirect relation to transaction type is initialized.
     * @return list of productDenomination
     */
    public List<ProductDenomination> getAllWithTransactionType();
    public List<ProductDenomination> getAllWithTransactionType(String transactionType, String BillerCode, String productCode);

    /**
     * Get all biller product denomination from biller product id.
     * Object returned will contain corresponding providerDenominantion and provider object.
     * @param productCode product code
     * @return list of productDenomination
     */
    public List<ProductDenomination> getAllWithProvider(String productCode);

    public List<ProductDenomination> getAllOrderedByProductIdDenom();
    public List<ProductDenomination> getAllOrderedByProductIdDenomNoActive();
    public ProductDenomination getByProductCodeDenom(String productCode, String denom);

    public List<ProductDenomination> getNonMerchantOrderedByProductIdDenom();
    
    public void updateStatusBiller(Long id, boolean status, Date createdBy, Long created, Date UpdateBy, Long updated);
}
