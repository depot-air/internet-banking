package com.dwidasa.engine.service.task;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.TransferService;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/15/11
 * Time: 10:00 AM
 */
public class TransferTask implements Executable {
    private final TransferService transferService;

    public TransferTask() {
        transferService = (TransferService) ServiceLocator.getService("transferService");
    }

    public void execute(Date processingDate, Long userId) throws Exception{
        List<Transaction> transactions = transferService.getAll(Constants.QUEUED_STATUS, processingDate);
        for (Transaction transaction : transactions) {
            transferService.execute(transaction);
        }

    }

    public void cleanup(Date processingDate) {
    }
}
