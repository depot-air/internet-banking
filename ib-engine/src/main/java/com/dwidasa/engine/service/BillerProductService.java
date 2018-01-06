package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.BillerProduct;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/11/11
 * Time: 11:28 AM
 */
public interface BillerProductService extends GenericService<BillerProduct, Long> {

	List<BillerProduct> getAllByTransactionType(String transactionType);
}
