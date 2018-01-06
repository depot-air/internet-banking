package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TreasuryStageDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.CustomerMapper;
import com.dwidasa.engine.dao.mapper.CustomerRegisterMapper;
import com.dwidasa.engine.dao.mapper.TransactionMapper;
import com.dwidasa.engine.dao.mapper.TreasuryStageMapper;
import com.dwidasa.engine.dao.mapper.UserMapper;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TreasuryStage;
import com.dwidasa.engine.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:38 PM
 */
@Repository("treasuryStageDao")
public class TreasuryStageDaoImpl extends GenericDaoImpl<TreasuryStage, Long> implements TreasuryStageDao {
    private final ChainedRowMapper<Transaction> transactionMapper;
    private final ChainedRowMapper<CustomerRegister> customerRegisterMapper;
    private final ChainedRowMapper<Customer> customerMapper;
    private final ChainedRowMapper<User> userMapper;

    @Autowired
    public TreasuryStageDaoImpl(DataSource dataSource, TreasuryStageMapper treasuryStageMapper,
                                   TransactionMapper transactionMapper, CustomerRegisterMapper customerRegisterMapper, CustomerMapper customerMapper, UserMapper userMapper) {
        super("t_treasury_stage", dataSource);
        defaultMapper = treasuryStageMapper;
        this.transactionMapper = transactionMapper;
        this.customerRegisterMapper = customerRegisterMapper;
        this.customerMapper = customerMapper;
        this.userMapper = userMapper;
        
        insertSql = new StringBuilder()
            .append("insert into t_treasury_stage ( ")
            .append("   t_transaction_id, m_customer_register_id, sender_id, officer_id, status, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :transactionId, :customerRegisterId, :senderId, :officerId, :status, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_treasury_stage ")
            .append("set ")
            .append("   t_transaction_id = :transactionId, m_customer_register_id = :customerRegisterId, sender_id = :senderId, officer_id = :officerId, status = :status, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public TreasuryStage getByTransactionFk(Long transactionId) {
        StringBuilder sql = new StringBuilder()
                .append("select tts.* ")
                .append("from t_treasury_stage tts ")
                .append("where tts.t_transaction_id = ? ");
    	
        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, transactionId);
    }

    public TreasuryStage get(Long id) {
    	String sql = "select ts.*, tt.*, cr.*, c.*, u.* "
              + "from t_treasury_stage ts "
              + "join t_transaction tt on tt.id = ts.t_transaction_id "
              + "join m_customer_register cr on cr.id = ts.m_customer_register_id "
              + "join m_customer c on c.id = ts.sender_id "
              + "left join m_user u on u.id = ts.officer_id "
              + "where ts.id = ? ";
  	
    	return getSimpleJdbcTemplate().queryForObject(sql.toString(), new RowMapper<TreasuryStage>() {
            public TreasuryStage mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                TreasuryStage ts = defaultMapper.mapRow(rs, index);
                index += Constants.TREASURY_STAGE_LENGTH;
                Transaction t = transactionMapper.chainRow(rs, index);
                ts.setTransaction(t);
                
                index += Constants.TRANSACTION_LENGTH;
                CustomerRegister cr = customerRegisterMapper.chainRow(rs, index);
                ts.setCustomerRegister(cr);
                index += Constants.CUSTOMER_REGISTER_LENGTH;
                Customer c = customerMapper.chainRow(rs, index);
                ts.setSender(c);
                index += Constants.CUSTOMER_LENGTH;                        
                User officer = userMapper.chainRow(rs, index);
                if (officer.getUsername() != null && !officer.getUsername().equals("") && !officer.getUsername().equals("0")) {
                	ts.setOfficer(officer);
                }

                return ts;
            }
        }, id);
    }
    
    @Override
    public List<TreasuryStage> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                     List<String> orders, Object... params) {
        String sql =
                  "select ts.*, tt.*, cr.*, c.*, u.* "
                + "from t_treasury_stage ts "
                + "join t_transaction tt on tt.id = ts.t_transaction_id "
                + "join m_customer_register cr on cr.id = ts.m_customer_register_id "
                + "join m_customer c on c.id = ts.sender_id "
                + "left join m_user u on u.id = ts.officer_id ";

        if (restrictions != null && restrictions.size() != 0) {
            sql += "where ";
            for (String restriction : restrictions) {
                sql += restriction;
                sql += " and ";
            }

            sql = sql.substring(0, sql.length()-4);
        }

        if (orders != null && orders.size() != 0) {
            sql += "order by ";
            for (String order : orders) {
                sql += order;
                sql += ", ";
            }

            sql = sql.substring(0, sql.length()-2);
        }

        sql += " limit ? offset ?";
        int pageNo = startIndex / pageSize + (startIndex % pageSize == 0 ? 0 : 1) + 1;

        Object[] newParams;
        int index;

        if (params != null) {
            newParams = new Object[params.length + 2];
            index = params.length;

            System.arraycopy(params, 0, newParams, 0, params.length);
        }
        else {
            newParams = new Object[2];
            index = 0;
        }

        newParams[index++] = pageSize;
        newParams[index] = (pageNo - 1) * pageSize;

        return getSimpleJdbcTemplate().query(sql, new RowMapper<TreasuryStage>() {
                    public TreasuryStage mapRow(ResultSet rs, int rowNum) throws SQLException {
                        int index = 0;
                        TreasuryStage ts = defaultMapper.mapRow(rs, index);
                        index += Constants.TREASURY_STAGE_LENGTH;
                        Transaction t = transactionMapper.chainRow(rs, index);
                        ts.setTransaction(t);
                        
                        index += Constants.TRANSACTION_LENGTH;
                        CustomerRegister cr = customerRegisterMapper.chainRow(rs, index);
                        ts.setCustomerRegister(cr);
                        index += Constants.CUSTOMER_REGISTER_LENGTH;
                        Customer c = customerMapper.chainRow(rs, index);
                        ts.setSender(c);
                        index += Constants.CUSTOMER_LENGTH;                        
                        User u = userMapper.chainRow(rs, index);
                        if (u != null) {
                        	ts.setOfficer(u);
                        }

                        return ts;
                    }
                }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from t_transaction_stage ts "
                + "join t_transaction tt "
                + "   on tt.id = ts.t_transaction_id ";

        if (restrictions != null && restrictions.size() != 0) {
            sql += "where ";
            for (String restriction : restrictions) {
                sql += restriction;
                sql += " and ";
            }

            sql = sql.substring(0, sql.length()-4);
        }

        return getSimpleJdbcTemplate().queryForInt(sql, params);
    }

	@Override
	public void updateOfficerById(Long id, Long officerId) {
		String sqlUpdate = "update t_treasury_stage set status=?, officer_id=? where id=? ";
		getJdbcTemplate().update(sqlUpdate, new Object[] {Constants.HANDLED_STATUS, officerId, id});
	}
}
