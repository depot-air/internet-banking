package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.ProviderDenomination;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:14 PM
 */
public interface ProviderDenominationDao extends GenericDao<ProviderDenomination, Long> {
    /**
     * Get all provider's denomination with direct relation to provider and
     * indirect relation to transaction type is initialized.
     * @return list of providerDenomination
     */
    public List<ProviderDenomination> getAllWithTransactionTypeAndProvider();

    public List<ProviderDenomination> getAllOrderedByProviderPrice();
    
    public List<ProviderDenomination> getAllDefaultOrderedByProviderPrice();

}
