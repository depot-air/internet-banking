package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.ProductDao;
import com.dwidasa.engine.model.Product;
import com.dwidasa.engine.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:59 PM
 */
@Service("productService")
public class ProductServiceImpl extends GenericServiceImpl<Product, Long> implements ProductService {
    @Autowired
    public ProductServiceImpl(ProductDao productDao) {
        super(productDao);
    }
}
