package com.dwidasa.engine.service.impl;

import java.util.List;

import com.dwidasa.engine.dao.TransactionTypeDao;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.TransactionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 10:00 PM
 */
@Service("transactionTypeService")
public class TransactionTypeServiceImpl extends GenericServiceImpl<TransactionType, Long>
        implements TransactionTypeService {

    private final TransactionTypeDao transactionTypeDao;

    @Autowired
    public TransactionTypeServiceImpl(TransactionTypeDao transactionTypeDao) {
        super(transactionTypeDao);

        this.transactionTypeDao = transactionTypeDao;
    }

    /**
     * {@inheritDoc}
     */
    public TransactionType get(String transactionType) {
        return transactionTypeDao.get(transactionType);
    }

	public List<TransactionType> getAllInBiller() {
		return transactionTypeDao.getAllInBiller();
	}

	public List<TransactionType> getAllSortDescription() {		
		return transactionTypeDao.getAllSortDescription();
	}

	public List<TransactionType> getAllInTransaction() {
		return transactionTypeDao.getAllInTransaction();
	}

    public List<TransactionType> getAllFinancial(){
        return transactionTypeDao.getAllFinancial();
    }
}
