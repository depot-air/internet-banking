package com.dwidasa.engine.service;

import com.dwidasa.engine.model.TransactionData;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/22/11
 * Time: 11:58 PM
 */
public interface TransactionDataService extends GenericService<TransactionData, Long> {
    /**
     * Get transaction data by transaction id.
     * @param transactionId transaction id
     * @return transaction data
     */
    TransactionData getByTransactionFk(Long transactionId);
    
    /**
     * Get transaction data by transaction id and account number.
     * @param id transaction id
     * @param accountNumber
     * @return transaction data, or null if transaction doesn't belong to specified account number
     */
    TransactionData getByTransactionFk(Long id, String accountNumber);

    /**
     * Get transaction data by transaction id and account number.
     * @param denomination
     * @param accountNumber
     * @param idPelNoMeter
     * @return transaction data, or null if transaction doesn't belong to specified account number
     */
    TransactionData getForPlnReprint(String denomination, String accountNumber, String idPelNoMeter);
    
    public void insertTransactionData(TransactionData transactionData);
	public void updateTransactionData(TransactionData transactionData);
}
