package com.dwidasa.engine.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.BatchDao;
import com.dwidasa.engine.dao.mapper.BatchMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.Batch;

@Repository("batchDao")
public class BatchDaoImpl extends GenericDaoImpl<Batch, Long> implements BatchDao {
    @Autowired
    public BatchDaoImpl(DataSource dataSource, BatchMapper batchMapper,
                         TransactionTypeMapper transactionTypeMapper) {
        super("m_batch", dataSource);
        defaultMapper = batchMapper;

        insertSql = new StringBuilder()
            .append("insert into m_batch ( ")
            .append("   batch_name, description, m_customer_id, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :batchName, :description, :customerId, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_batch ")
            .append("set ")
            .append("   batch_name = :batchName, description = :description, m_customer_id=:customerId,  ")
            .append("   created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

	@Override
	public List<Batch> getAll(Long customerId) {
		String sqlSelect = "select * from m_batch b where m_customer_id=?";
		return getJdbcTemplate().query(sqlSelect, new Object[] {customerId}, defaultMapper);
	}

    
}

