package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.TransactionLimitDao;
import com.dwidasa.engine.dao.mapper.TransactionLimitMapper;
import com.dwidasa.engine.model.TransactionLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:50 PM
 */
@Repository("transactionLimitDao")
public class TransactionLimitDaoImpl extends GenericDaoImpl<TransactionLimit, Long> implements TransactionLimitDao {
    @Autowired
    public TransactionLimitDaoImpl(DataSource dataSource, TransactionLimitMapper transactionLimitMapper) {
        super("m_transaction_limit", dataSource);
        defaultMapper = transactionLimitMapper;

        insertSql = new StringBuilder()
            .append("insert into m_transaction_limit ( ")
            .append("   m_transaction_type_id, m_provider_id, min_limit, max_limit, created, createdby, ")
            .append("   updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :transactionTypeId, :providerId, :minLimit, :maxLimit, :created, :createdby, ")
            .append("   :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_transaction_limit ")
            .append("set ")
            .append("   m_transaction_type_id = :transactionTypeId, m_provider_id = :providerId, ")
            .append("   min_limit = :minLimit, max_limit = :maxLimit, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
}
