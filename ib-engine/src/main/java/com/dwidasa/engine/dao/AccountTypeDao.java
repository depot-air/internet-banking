package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.AccountType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/21/11
 * Time: 2:37 PM
 */
public interface AccountTypeDao extends GenericDao<AccountType, Long> {
    /**
     * Get account type by account type code.
     * @param accountType account type
     * @return account type object
     */
    public AccountType get(String accountType);
}
