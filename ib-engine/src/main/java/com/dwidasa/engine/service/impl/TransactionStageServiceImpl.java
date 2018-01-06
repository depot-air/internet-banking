package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.TransactionStageDao;
import com.dwidasa.engine.model.TransactionStage;
import com.dwidasa.engine.service.TransactionStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:45 PM
 */
@Service("transactionStageService")
public class TransactionStageServiceImpl extends GenericServiceImpl<TransactionStage, Long>
        implements TransactionStageService {

    @Autowired
    public TransactionStageServiceImpl(TransactionStageDao transactionStageDao) {
        super(transactionStageDao);
    }
}
