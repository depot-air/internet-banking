package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.TransactionData;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 3:50 PM
 */
public interface TransactionDataDao extends GenericDao<TransactionData, Long> {
    /**
     * Get transaction data by transaction id.
     * @param transactionId transaction id
     * @return transaction data
     */
    public TransactionData getByTransactionFk(Long transactionId);
    
    /**
     * Get transaction data by transaction id and account number.
     * @param id transaction id
     * @return transaction data, or null if transaction doesn't belong to specified account number
     */
	public TransactionData getByTransactionFk(Long id, String accountNumber);

    /**
     * Get transaction data by transaction id and account number.
     * @param denomination
     * @param accountNumber
     * @param idPelNoMeter
     * @return transaction data, or null if transaction doesn't belong to specified account number
     */
	public TransactionData getForPlnReprint(String denomination, String accountNumber, String idPelNoMeter);
	
	public void insertTransactionData(TransactionData transactionData);
	public void updateTransactionData(TransactionData transactionData);
}
