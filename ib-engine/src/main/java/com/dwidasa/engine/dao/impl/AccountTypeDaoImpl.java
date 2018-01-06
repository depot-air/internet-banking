package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.AccountTypeDao;
import com.dwidasa.engine.dao.mapper.AccountTypeMapper;
import com.dwidasa.engine.model.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/21/11
 * Time: 2:38 PM
 */
@Repository("accountTypeDao")
public class AccountTypeDaoImpl extends GenericDaoImpl<AccountType, Long> implements AccountTypeDao {
    @Autowired
    public AccountTypeDaoImpl(DataSource dataSource, AccountTypeMapper accountTypeMapper) {
        super("m_account_type", dataSource);
        defaultMapper = accountTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_account_type ( ")
            .append("   account_type, account_name, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :accountType, :accountName, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_account_type ")
            .append("set ")
            .append("   account_type = :accountType, account_name = :accountName, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public AccountType get(String accountType) {
        StringBuilder sql = new StringBuilder()
                .append("select mat.* ")
                .append("from m_account_type mat ")
                .append("where mat.account_type = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, accountType);
    }
}
