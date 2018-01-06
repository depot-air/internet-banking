package com.dwidasa.engine.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.IbTokenDao;
import com.dwidasa.engine.dao.mapper.IbTokenMapper;
import com.dwidasa.engine.model.IbToken;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 2:46 PM
 */
@Repository("ibTokenDao")
public class IbTokenDaoImpl extends GenericDaoImpl<IbToken, Long> implements IbTokenDao {

    @Autowired
    public IbTokenDaoImpl(DataSource dataSource, IbTokenMapper ibTokenMapper) {
        super("m_customer", dataSource);
        defaultMapper = ibTokenMapper;

        insertSql = new StringBuilder()
            .append("insert into m_ib_token ( ")
            .append("   serial_number, m_customer_id, status, ")
            .append("   created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :serialNumber, :customerId, :status, ")
            .append("   :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_ib_token ")
            .append("set ")
            .append("   serial_number = :serialNumber, m_customer_id = :customerId, status = :status, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");

        defaultMapper = new IbTokenMapper();
    }

	@Override
	public List<IbToken> getAvailableTokens() {
		StringBuilder sql = new StringBuilder()
	        .append("select t.* ")
	        .append("from m_ib_token t ")
	        .append("where t.status = ? ");
		
		List<IbToken> result = null;
		
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, Constants.HARD_TOKEN.STATUS_IMPORTED);
		} catch (Exception e) {
            e.printStackTrace();
		}		
		return result;
	}

	@Override
	public List<IbToken> getSelectedTokens() {
		StringBuilder sql = new StringBuilder()
        .append("select t.* ")
        .append("from m_ib_token t ")
        .append("where t.status = ?");
	
		List<IbToken> result = null;
		
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, Constants.HARD_TOKEN.STATUS_SELECTED);
		} catch (Exception e) {
            e.printStackTrace();
		}		
		return result;
	}

	@Override
	public List<IbToken> getSelectedTokensByCustomerId(Long userId) {
		StringBuilder sql = new StringBuilder()
        .append("select t.* ")
        .append("from m_ib_token t ")
        .append("where t.status = ? and t.m_customer_id = ?");
	
		List<IbToken> result = null;
		
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, Constants.HARD_TOKEN.STATUS_SELECTED,userId);
		} catch (Exception e) {
            e.printStackTrace();
		}		
		return result;
	}
	
	@Override
	public List<IbToken> getAvailableTokens(String serialNumber) {
		StringBuilder sql = new StringBuilder()
        .append("select t.* ")
        .append("from m_ib_token t ")
        .append("where t.status = ? and t.serial_number like '%' || ? || '%'");
	
		List<IbToken> result = null;
		
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, Constants.HARD_TOKEN.STATUS_IMPORTED, serialNumber);
		} catch (Exception e) {
            e.printStackTrace();
		}		
		return result;
	}
	@Override
	public List<IbToken> getLinkedTokensBySerialNumber(String serialNumber) {
		StringBuilder sql = new StringBuilder()
        .append("select t.* ")
        .append("from m_ib_token t ")
        .append("where t.status = ? and t.serial_number like '%' || ? || '%'");
		List<IbToken> result = null;
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, Constants.HARD_TOKEN.STATUS_LINKED, serialNumber);
		} catch (DataAccessException e) {
			System.out.println("error :" +e.getMessage());
		}		
		return result;
	}
	@Override
	public IbToken getById(Long id) {
		StringBuilder sql = new StringBuilder()
		        .append("select t.* ")
		        .append("from m_ib_token t ")
		        .append("where t.id = ? ");
		IbToken ibToken = null;
		
		try {
		    ibToken = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, id);
		} catch (Exception e) {
		    //-- actually do nothing
            e.printStackTrace();
		}
		
		return ibToken;
	}

	@Override
	public void refreshCancelledToken() {
		String sqlUpdate = "update m_ib_token set m_customer_id=?, status=? where m_customer_id=? AND status=? ";
		getJdbcTemplate().update(sqlUpdate, new Object[] {null, Constants.HARD_TOKEN.STATUS_IMPORTED, 0, Constants.HARD_TOKEN.STATUS_SELECTED});
	}

}
