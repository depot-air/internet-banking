package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.TransactionType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:09 PM
 */
public interface TransactionTypeService extends GenericService<TransactionType, Long> {
    /**
     * Get transaction type by transaction type code (natural key).
     * @param transactionType transaction type code
     * @return transaction type object
     */
    public TransactionType get(String transactionType);
    
    public List<TransactionType> getAllInBiller();
    
    public List<TransactionType> getAllInTransaction();

	public List<TransactionType> getAllSortDescription();

    public List<TransactionType> getAllFinancial();


}
