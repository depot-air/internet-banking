package com.dwidasa.engine.service.facade;

import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TransferBatchView;
import com.dwidasa.engine.model.view.TransferView;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/13/11
 * Time: 9:50 AM
 */
public interface TransferService extends BaseTransactionService {
    /**
     * Get all transaction for specified status and value date.
     * @param status status
     * @param valueDate value date
     * @return list of transaction
     */
    public List<Transaction> getAll(String status, Date valueDate);

    /**
     * Execute all regular pending transfer such as post-dated transfer, periodic transfer,
     * treasury pending transfer.
     * @param transaction transaction object
     * @return transfer view object
     */
    public TransferView execute(Transaction transaction);

    public BaseView executeNow(TransferView tv);

    public TransferView inquiryATMB(TransferView view);
    public TransferView executeATMB(TransferView view);

    public TransferView inquiryALTO(TransferView view);
    public TransferView executeALTO(TransferView view);

    public TransferBatchView inquiryBatch(TransferBatchView view);
    public TransferBatchView executeBatch(TransferBatchView view);
}
