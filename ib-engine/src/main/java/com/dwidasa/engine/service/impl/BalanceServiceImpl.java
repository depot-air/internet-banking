package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.BalanceDao;
import com.dwidasa.engine.model.Balance;
import com.dwidasa.engine.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/15/11
 * Time: 3:34 PM
 */
@Service("balanceService")
public class BalanceServiceImpl extends GenericServiceImpl<Balance, Long> implements BalanceService {

    private final BalanceDao balanceDao;

    @Autowired
    public BalanceServiceImpl(BalanceDao balanceDao) {
        super(balanceDao);
        this.balanceDao = balanceDao;
    }

    /**
     * {@inheritDoc}
     */
    public void remove(Date processingDate) {
         balanceDao.remove(processingDate);
    }

	@Override
	public Balance get(String accountNumber, Date postingDate) {
		return balanceDao.get(accountNumber, postingDate);
	}
}
