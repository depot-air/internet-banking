package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.Biller;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/19/11
 * Time: 4:05 PM
 */
public interface BillerService extends GenericService<Biller, Long> {

	List<Biller> getByTransactionTypeId(Long transactionTypeId);	
}
