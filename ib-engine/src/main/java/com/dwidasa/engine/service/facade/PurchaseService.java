package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.model.*;

import java.util.List;

/**
 * Facade interface that define all purchase related function.
 *
 * @author rk
 */
public interface PurchaseService extends BaseTransactionService {
    /**
     * Get all account own by specified customer
     * @param customerId customer id
     * @return list of customerAccount
     */
    public List<CustomerAccount> getCustomerAccounts(Long customerId);

    /**
     * Get all biller for specified transaction type id
     * @param transactionTypeId transaction type id
     * @return list of biller
     */
    public List<Biller> getBillers(Long transactionTypeId);

    /**
     * Get all biller's product for specifed biller code
     * @param billerCode biller code
     * @return list of billerProduct
     */
    public List<BillerProduct> getBillerProducts(String billerCode);

    /**
     * Get all biller product denomination from biller product code.
     * Object returned will contain corresponding providerDenominantion and provider object.
     * @param productCode product code
     * @return list of productDenomination
     */
    public List<ProductDenomination> getProductDenominations(String productCode);

    /**
     * Get all provider product from biller product id.
     * Object returned will contain corresponding provider object.
     * @param productCode biller product code
     * @return list of providerProduct
     */
    public List<ProviderProduct> getProviderProducts(String productCode);
}
