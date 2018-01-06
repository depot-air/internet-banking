package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Balance;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/3/11
 * Time: 3:21 PM
 */
public interface BalanceDao extends GenericDao<Balance, Long> {
    /**
     * Get balance from specified account number and date.
     * @param accountNumber account number
     * @param date balance date
     * @return balance object
     */
    public Balance get(String accountNumber, Date date);

    /**
     * Remove all transactions data with specified processing date.
     * @param processingDate processing date
     */
    public void remove(Date processingDate);

    public Balance getEndingBalance(String accountNumber, Date startDate, Date endDate);
    
    public Balance getLastBalance(String accountNumber);
}
