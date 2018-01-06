package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.TransactionHistory;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/20/11
 * Time: 4:39 PM
 */
public interface TransactionHistoryDao extends GenericDao<TransactionHistory, Long> {
    /**
     * Remove all transactions data with specified processing date.
     * @param processingDate processing date
     */
    public void remove(Date processingDate);

    /**
     * Get all transaction history in specified date range.
     * @param accountNumber account number
     * @param startDate start date start date
     * @param endDate end date end date
     * @return list of transaction history
     */
    public List<TransactionHistory> getAll(String accountNumber, Date startDate, Date endDate);

    /**
     * Get last n transaction from history.
     * @param accountNumber account number
     * @param n number of transaction to retrieve
     * @return list of transaction from history
     */
    public List<TransactionHistory> getLastN(String accountNumber, int n);
}
