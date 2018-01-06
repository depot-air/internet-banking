package com.dwidasa.engine.service;

import com.dwidasa.engine.model.Balance;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/15/11
 * Time: 3:34 PM
 */
public interface BalanceService extends GenericService<Balance, Long> {

    /**
     * Remove all balances data with specified processing date
     * @param processingDate processing date
     */
    public void remove(Date processingDate);

	public Balance get(String accountNumber, Date startReadBl);
}
