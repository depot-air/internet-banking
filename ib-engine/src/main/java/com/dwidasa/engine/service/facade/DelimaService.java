package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.model.view.CashInDelimaView;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 5:58 PM
 */
public interface DelimaService extends BaseTransactionService {
    /**
     * This method will be used to check the status of cash in, cash out and refund, cash to bank, cash out elmo.
     * What differing each other is transaction type inside the view.
     * @param view inside CashInDelimaView object, these fields are mandatory, customerId, cardNumber, accountNumber,
     * accountType, transactionType, currencyCode, customerReference, merchantType, and terminalId
     * @return CashInDelimaView object
     */
    public CashInDelimaView checkStatus(CashInDelimaView view);
}
