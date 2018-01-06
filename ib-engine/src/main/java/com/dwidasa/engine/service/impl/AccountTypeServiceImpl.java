package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.AccountTypeDao;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.service.AccountTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:58 PM
 */
@Service("accountTypeService")
public class AccountTypeServiceImpl extends GenericServiceImpl<AccountType, Long> implements AccountTypeService {
    @Autowired
    public AccountTypeServiceImpl(AccountTypeDao accountTypeDao) {
        super(accountTypeDao);
    }
}
