package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.view.CellularPrefixView;

import java.util.List;

/**
 * Facade service for synchronization,
 *
 * @author rk
 */
public interface SynchronizationService {
    /**
     * Get all version.
     * @return list of version
     */
    public List<Version> getVersions();

    /**
     * Get all available biller.
     * @return list of biller
     */
    public List<Biller> getBillers();
    
    public List<Biller> getBillersNoActive();

    /**
     * Get all available biller product.
     * @return list of billerProduct
     */
    public List<BillerProduct> getBillerProducts();

    /**
     * Get all product denomination.
     * @return list of productDenomination
     */
    public List<ProductDenomination> getProductDenominations();
    public List<ProductDenomination> getProductDenominationsNoActive();

    /**
     * Get non merchant product denomination
     * @return
     */
    public List<ProductDenomination> getDefaultProductDenominations();

    /**
     * Get all provider product.
     * @return list of providerProduct
     */
    public List<ProviderProduct> getProviderProducts();

    /**
     * Get all provider denomination.
     * @return list of providerDenomination
     */
    public List<ProviderDenomination> getProviderDenominations();
    
    /**
    * Get all default provider denomination.
    * @return list of providerDenomination
    */
   public List<ProviderDenomination>  getDefaultProvidersDenominations();
   
    /**
     * Get all provider.
     * @return list of provider
     */
    public List<Provider> getProviders();

    /**
     * Get all currency.
     * @return list of currency
     */
    public List<Currency> getCurrencies();

    /**
     * Get all transaction type.
     * @return list of transactionType
     */
    public List<TransactionType> getTransactionTypes();

    /**
     * Get all location type.
     * @return list of locationType
     */
    public List<LocationType> getLocationTypes();

    /**
     * Get all response code.
     * @return list of responseCode.
     */
    public List<ResponseCode> getResponseCodes();

    /**
     * Get all cellular prefix.
     * @return list of cellularPrefix.
     */
    public List<CellularPrefix> getCellularPrefixes();

     /**
     * Get all cellular prefix simplified
     * @return list of cellularPrefixView
     */
    public List<CellularPrefixView> getCellularPrefixesView();

}
