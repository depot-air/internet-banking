package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.service.TransactionDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/22/11
 * Time: 11:59 PM
 */
@Service("transactionDataService")
public class TransactionDataServiceImpl extends GenericServiceImpl<TransactionData, Long>
        implements TransactionDataService {

    private final TransactionDataDao transactionDataDao;

    @Autowired
    public TransactionDataServiceImpl(TransactionDataDao transactionDataDao) {
        super(transactionDataDao);
        this.transactionDataDao = transactionDataDao;
    }

    public TransactionData getByTransactionFk(Long transactionId) {
        return transactionDataDao.getByTransactionFk(transactionId);
    }
    
    @Override
	public TransactionData getByTransactionFk(Long id, String accountNumber) {
		return transactionDataDao.getByTransactionFk(id, accountNumber);
	}

    @Override
    public TransactionData getForPlnReprint(String denomination, String accountNumber, String idPelNoMeter) {
		return transactionDataDao.getForPlnReprint(denomination, accountNumber, idPelNoMeter);
    }

	@Override
	public void insertTransactionData(TransactionData transactionData) {
		transactionDataDao.insertTransactionData(transactionData);
	}

	@Override
	public void updateTransactionData(TransactionData transactionData) {
		transactionDataDao.updateTransactionData(transactionData);
	}
}
