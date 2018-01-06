package com.dwidasa.engine.service.impl.facade;

import com.dwidasa.engine.dao.*;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.service.facade.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of <code>PurchaseService</code>. This class will serve
 * as a facade for all purchasing feature. Basic transaction functionality
 * is implemented through <code>BaseTransactionServiceImpl</code>.
 *
 * @author rk
 */
@Service("purchaseService")
public class PurchaseServiceImpl extends BaseTransactionServiceImpl implements PurchaseService {
    @Autowired
    private BillerDao billerDao;

    @Autowired
    private BillerProductDao billerProductDao;

    @Autowired
    private ProductDenominationDao productDenominationDao;

    @Autowired
    private ProviderProductDao providerProductDao;

    @Autowired
    private CustomerAccountDao customerAccountDao;

    public PurchaseServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerAccount> getCustomerAccounts(Long customerId) {
        return customerAccountDao.getAll(customerId);
    }

    /**
     * {@inheritDoc}
     */
    public List<Biller> getBillers(Long transactionTypeId) {
        return billerDao.getAll(transactionTypeId);
    }

    /**
     * {@inheritDoc}
     */
    public List<BillerProduct> getBillerProducts(String billerCode) {
        return billerProductDao.getAll(billerCode);
    }

    /**
     * {@inheritDoc}
     */
    public List<ProductDenomination> getProductDenominations(String productCode) {
        return productDenominationDao.getAllWithProvider(productCode);
    }

    /**
     * {@inheritDoc}
     */
    public List<ProviderProduct> getProviderProducts(String productCode) {
        return providerProductDao.getAll(productCode);
    }
}
