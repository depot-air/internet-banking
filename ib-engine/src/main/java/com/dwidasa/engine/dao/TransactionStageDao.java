package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.TransactionStage;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:37 PM
 */
public interface TransactionStageDao extends GenericDao<TransactionStage, Long> {
    /**
     * Get transaction stage by transaction id.
     * @param transactionId transaction id
     * @return transaction stage
     */
    public TransactionStage getByTransactionFk(Long transactionId);
}
