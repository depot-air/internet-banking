package com.dwidasa.engine.dao;

import java.util.List;

import com.dwidasa.engine.model.AccountException;

public interface AccountExceptionDao extends GenericDao<AccountException, Long> {
	public List<AccountException> getAccountByAccountNumber(String accountNumber);
	public AccountException getById(Long id);
	public Boolean isAccountException(String accountNumber);
}