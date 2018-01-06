package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.AccountExceptionDao;
import com.dwidasa.engine.model.AccountException;
import com.dwidasa.engine.service.AccountExceptionService;

@Service("accountExceptionService")
public class AccountExceptionServiceImpl extends GenericServiceImpl<AccountException, Long> implements AccountExceptionService {
	private AccountExceptionDao accountExceptionDao;

	@Autowired
	public AccountExceptionServiceImpl(AccountExceptionDao accountExceptionDao) {
		super(accountExceptionDao);
		this.accountExceptionDao = accountExceptionDao;
	}

	@Override
	public Boolean isAccountException(String accountNumber) {		
		return accountExceptionDao.isAccountException(accountNumber);
	}

}
