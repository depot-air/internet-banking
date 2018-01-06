package com.dwidasa.engine.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.BatchContentDao;
import com.dwidasa.engine.dao.mapper.BatchContentMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.BatchContent;

@Repository("batchContentDao")
public class BatchContentDaoImpl extends GenericDaoImpl<BatchContent, Long> implements BatchContentDao {
    @Autowired
    public BatchContentDaoImpl(DataSource dataSource, BatchContentMapper batchContentMapper,
                         TransactionTypeMapper transactionTypeMapper) {
        super("m_batch_content", dataSource);
        defaultMapper = batchContentMapper;

        insertSql = new StringBuilder()
            .append("insert into m_batch_content ( ")
            .append("   m_batch_id, account_number, customer_name, amount, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :batchId, :accountNumber, :customerName, :amount, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_batch_content ")
            .append("set ")
            .append("   m_batch_id = :batchId, account_number = :accountNumber, customer_name = :customerName, amount = :amount, description = :description,  ")
            .append("   created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

	@Override
	public List<BatchContent> getAll(Long batchId) {
		String sqlSelect = "select * " +
				" from m_batch_content where m_batch_id=?";
		return getJdbcTemplate().query(sqlSelect, new Object[] {batchId}, defaultMapper);
	}

    @Override
    public void removeAll(Long batchId) {
        String deleteSql = "delete from " + tableName + " where m_batch_id = ? ";
		getSimpleJdbcTemplate().update(deleteSql, batchId);
    }

}

