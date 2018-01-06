package com.dwidasa.engine.service.impl.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.dao.BillerProductDao;
import com.dwidasa.engine.dao.CellularPrefixDao;
import com.dwidasa.engine.dao.CurrencyDao;
import com.dwidasa.engine.dao.LocationTypeDao;
import com.dwidasa.engine.dao.ProductDenominationDao;
import com.dwidasa.engine.dao.ProviderDao;
import com.dwidasa.engine.dao.ProviderDenominationDao;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.dao.ResponseCodeDao;
import com.dwidasa.engine.dao.TransactionTypeDao;
import com.dwidasa.engine.dao.VersionDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.LocationType;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.model.ResponseCode;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.model.Version;
import com.dwidasa.engine.model.view.CellularPrefixView;
import com.dwidasa.engine.service.facade.SynchronizationService;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/6/11
 * Time: 10:35 PM
 */
@Service("synchronizationService")
public class SynchronizationServiceImpl implements SynchronizationService {
    @Autowired
    private VersionDao versionDao;

    @Autowired
    private BillerDao billerDao;

    @Autowired
    private BillerProductDao billerProductDao;

    @Autowired
    private ProductDenominationDao productDenominationDao;

    @Autowired
    private ProviderProductDao providerProductDao;

    @Autowired
    private ProviderDenominationDao providerDenominationDao;

    @Autowired
    private ProviderDao providerDao;

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private TransactionTypeDao transactionTypeDao;

    @Autowired
    private LocationTypeDao locationTypeDao;

    @Autowired
    private ResponseCodeDao responseCodeDao;

    @Autowired
    private CellularPrefixDao cellularPrefixDao;

    public SynchronizationServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public List<Version> getVersions() {
        return versionDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<Biller> getBillers() {
        return billerDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<BillerProduct> getBillerProducts() {
        return billerProductDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<ProductDenomination> getProductDenominations() {
        //return productDenominationDao.getAll();
        return productDenominationDao.getAllOrderedByProductIdDenom();
    }

    public List<ProductDenomination> getDefaultProductDenominations(){
        return productDenominationDao.getNonMerchantOrderedByProductIdDenom();
    }

    /**
     * {@inheritDoc}
     */
    public List<ProviderProduct> getProviderProducts() {
        return providerProductDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<ProviderDenomination> getProviderDenominations() {
        //return providerDenominationDao.getAll();
        return providerDenominationDao.getAllOrderedByProviderPrice();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<ProviderDenomination> getDefaultProvidersDenominations() {
        return providerDenominationDao.getAllDefaultOrderedByProviderPrice();
    }
    

    /**
     * {@inheritDoc}
     */
    public List<Provider> getProviders() {
        return providerDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<Currency> getCurrencies() {
        return currencyDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<TransactionType> getTransactionTypes() {
        return transactionTypeDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<LocationType> getLocationTypes() {
        return locationTypeDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<ResponseCode> getResponseCodes() {
        return responseCodeDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<CellularPrefix> getCellularPrefixes() {
        return cellularPrefixDao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public List<CellularPrefixView> getCellularPrefixesView() {
        List<CellularPrefix> prefixes = cellularPrefixDao.getAll();
        List<CellularPrefixView> result = new ArrayList<CellularPrefixView>();
        for (CellularPrefix prefix : prefixes){
            result.add(new CellularPrefixView(prefix));
        }

        return result;
    }

}
