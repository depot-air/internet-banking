package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.ProductDenominationDao;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.service.ProductDenominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/19/11
 * Time: 11:39 AM
 */
@Service("productDenominationService")
public class ProductDenominationServiceImpl extends GenericServiceImpl<ProductDenomination, Long>
        implements ProductDenominationService {
    @Autowired
    public ProductDenominationServiceImpl(ProductDenominationDao productDenominationDao) {
        super(productDenominationDao);
    }
}
