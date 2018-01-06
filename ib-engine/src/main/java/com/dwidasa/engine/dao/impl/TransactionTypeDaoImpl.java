package com.dwidasa.engine.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import com.dwidasa.engine.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.TransactionTypeDao;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.TransactionType;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/24/11
 * Time: 10:30 AM
 */
@Repository("transactionTypeDao")
public class TransactionTypeDaoImpl extends GenericDaoImpl<TransactionType, Long> implements TransactionTypeDao {
    @Autowired
    public TransactionTypeDaoImpl(DataSource dataSource, TransactionTypeMapper transactionTypeMapper) {
        super("m_transaction_type", dataSource);
        defaultMapper = transactionTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into m_transaction_type ( ")
            .append("   transaction_type, description, created, createdby, updated, updatedby, financial ")
            .append(") ")
            .append("values ( ")
            .append("   :transactionType, :description, :created, :createdby, :updated, :updatedby, :financial ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_transaction_type ")
            .append("set ")
            .append("   transaction_type = :transactionType, description = :description, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby, financial = :financial ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public TransactionType get(String transactionType) {
        StringBuilder sql = new StringBuilder()
                .append("select mtt.* ")
                .append("from m_transaction_type mtt ")
                .append("where mtt.transaction_type = ? ");

        TransactionType result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, transactionType);
        } catch (EmptyResultDataAccessException e) {
            //-- do nothing
        }

        return result;
    }
    
    public List<TransactionType> getAllInBiller() {
    	String sqlSelect = 
    		"select mtt.* "+
            " from m_transaction_type mtt "+
            " where exists (select id from m_biller b where b.m_transaction_type_id=mtt.id)"+
            " order by mtt.description";	
    			
        List<TransactionType> result = null;
        result = getSimpleJdbcTemplate().query(sqlSelect, defaultMapper);
        return result;
    }
    
	public List<TransactionType> getAllInTransaction() {
		String sqlSelect = 
			"select tt.* from m_transaction_type tt"+
			" where exists (select id from t_transaction t where t.transaction_type=tt.transaction_type)"+
			" order by description";
		List<TransactionType> result = null;
        result = getSimpleJdbcTemplate().query(sqlSelect, defaultMapper);
        return result;
	}

    public List<TransactionType> getAllFinancial() {
        String sqlSelect =
			"select tt.* from m_transaction_type tt"+
			" where tt.financial = ? "+
			" order by tt.description";
		List<TransactionType> result = null;
        result = getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, Constants.TRANSACTION_TYPE.FINANCIAL);
        return result;
    }

	public List<TransactionType> getAllSortDescription() {
		StringBuilder sql = new StringBuilder()
        .append("select mtt.* ")
        .append("from m_transaction_type mtt ")
        .append("order by mtt.description");
		List<TransactionType> result = null;
		result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
		return result;
	}

	
}
