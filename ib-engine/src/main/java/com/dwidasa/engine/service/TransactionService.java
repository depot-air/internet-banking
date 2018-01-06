package com.dwidasa.engine.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import com.dwidasa.engine.model.Transaction;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/23/11
 * Time: 8:35 PM
 */
public interface TransactionService extends GenericService<Transaction, Long> {

	InputStream getCsvStream(Date startDate, Date endDate,
			String transactionType);

    //untuk cek status ATMB
    Transaction getByTransType_AccountNo_CustRef_Amount(String transactionType, String accountNumber, String custReference, BigDecimal amount);
    
    void executeTransactionQueue(Date processingDate);

    public BigDecimal getTotalTransferInOneDay(String fromAccount, String toAccount, Date transferDate);
}
