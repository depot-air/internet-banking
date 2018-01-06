package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.TransactionHistoryDao;
import com.dwidasa.engine.model.TransactionHistory;
import com.dwidasa.engine.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/15/11
 * Time: 3:36 PM
 */
@Service("transactionHistoryService")
public class TransactionHistoryServiceImpl extends GenericServiceImpl<TransactionHistory, Long>
        implements TransactionHistoryService {
    private final TransactionHistoryDao transactionHistoryDao;

    @Autowired
    public TransactionHistoryServiceImpl(TransactionHistoryDao transactionHistoryDao) {
        super(transactionHistoryDao);

        this.transactionHistoryDao = transactionHistoryDao;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(Date processingDate) {
        transactionHistoryDao.remove(processingDate);
    }
}
