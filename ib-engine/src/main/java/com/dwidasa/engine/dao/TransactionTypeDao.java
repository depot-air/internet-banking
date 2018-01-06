package com.dwidasa.engine.dao;

import java.util.List;

import com.dwidasa.engine.model.TransactionType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/24/11
 * Time: 10:29 AM
 */
public interface TransactionTypeDao extends GenericDao<TransactionType, Long> {
    /**
     * Get transaction type by transaction type code (natural key).
     * @param transactionType transaction type code
     * @return transaction type object
     */
    public TransactionType get(String transactionType);

	public List<TransactionType> getAllInBiller();

	public List<TransactionType> getAllSortDescription();

	public List<TransactionType> getAllInTransaction();

    public List<TransactionType> getAllFinancial();
}
