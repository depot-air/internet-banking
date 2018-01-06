package com.dwidasa.engine.service;

import com.dwidasa.engine.model.AccountException;

public interface AccountExceptionService extends GenericService<AccountException, Long> {
	public Boolean isAccountException(String accountNumber);

}
