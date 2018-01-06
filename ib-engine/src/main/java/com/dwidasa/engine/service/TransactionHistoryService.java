package com.dwidasa.engine.service;

import com.dwidasa.engine.model.TransactionHistory;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/15/11
 * Time: 3:35 PM
 */
public interface TransactionHistoryService extends GenericService<TransactionHistory, Long> {
    /**
     * Remove all transactions data with specified processing date.
     * @param processingDate processing date
     */
    public void remove(Date processingDate);
}
