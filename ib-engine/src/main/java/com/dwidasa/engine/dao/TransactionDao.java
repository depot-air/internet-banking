package com.dwidasa.engine.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountStatementView;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/23/11
 * Time: 3:09 PM
 */
public interface TransactionDao extends GenericDao<Transaction, Long> {
    /**
     * Generate stan value.
     * @return stan value
     */
    public Long nextStanValue();

    /**
     * Generate next rrn.
     * @return rrn value
     */
    public Long nextRrn();

    /**
     * Get transaction object for specified transaction type and customerReference.
     * @param transactionType transaction type
     * @param customerReference customer reference
     * @return transaction object
     */
    public Transaction get(String transactionType, String customerReference);

    /**
     * Get all transaction for specified transaction type, status and dates. If transaction type
     * is not specified then pending and failed transactions will be returned.
     * @param accountNumber account number
     * @param transactionType transaction type
     * @param status transaction execution status
     * @param startDate start date
     * @param endDate end date
     * @return list of transaction
     */
    public List<Transaction> getAll(String accountNumber, String transactionType, String status,
                                    Date startDate, Date endDate);

    /**
     * Get all completed transaction, for success and pending transaction only, into list of AccountStatementView
     * based from provided parameters.
     * @param accountNumber account number
     * @param transactionType transaction type
     * @param startDate start date
     * @param endDate end date
     * @return list of account view statement
     */
    public List<AccountStatementView> getAll(String accountNumber, String transactionType, Date startDate,
                                             Date endDate);

    /**
     * Get all transaction for specified status and value date.
     * @param status status
     * @param valueDate value date
     * @return list of transaction
     */
    public List<Transaction> getAll(String status, Date valueDate);


    /**
     * Get all transaction for specific accountNumber, transactionType and period
     * @param accountNumber
     * @param transactionType
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Transaction> getAllForAccount(String accountNumber, String transactionType, Date startDate, Date endDate);

    //untuk cek status ATMB
    Transaction getByTransType_AccountNo_CustRef_Amount(String transactionType, String accountNumber, String custReference, BigDecimal amount);

    // bsueb tambah func utk cek status transfer atmb
    public Transaction getTransactionForTransferAMTBStatus
            (String fromAccNumber,String toAccountNumber, String toBankCode, BigDecimal transAmount);

    // bsueb check provider denom di hari yg sama
    public Boolean checkProviderDenomAtSameDay
            (String fromAccNumber,String cellularNo, BigDecimal transAmount, Date transactionDate);
    
    public List<Transaction> getTransferInOnDay(String fromAccount, String toAccount, Date transferDate);
    
    public List<Transaction> getCustomerReference(String CustomerReference);
}
