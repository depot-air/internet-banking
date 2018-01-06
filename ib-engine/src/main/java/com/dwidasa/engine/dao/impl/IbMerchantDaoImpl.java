package com.dwidasa.engine.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.IbMerchantDao;
import com.dwidasa.engine.dao.mapper.IbMerchantMapper;
import com.dwidasa.engine.model.IbMerchant;
import com.dwidasa.engine.model.IbToken;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 2:46 PM
 */
@Repository("ibMerchantDao")
public class IbMerchantDaoImpl extends GenericDaoImpl<IbMerchant, Long> implements IbMerchantDao {

    @Autowired
    public IbMerchantDaoImpl(DataSource dataSource, IbMerchantMapper ibMerchantMapper) {
        super("m_ib_merchant", dataSource);
        defaultMapper = ibMerchantMapper;

        insertSql = new StringBuilder()
            .append("insert into m_ib_merchant ( ")
            .append("   m_customer_id, terminal_id, status, ")
            .append("   created, createdby, updated, updatedby, serial_number, soft_token ")
            .append(") ")
            .append("values ( ")
            .append("   :customerId, :terminalId, :status, ")
            .append("   :created, :createdby, :updated, :updatedby, :serialNumber, :softToken ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_ib_merchant ")
            .append("set ")
            .append("   m_customer_id = :customerId, terminal_id = :terminalId, status = :status, serial_number = :serialNumber, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby, soft_token = :softToken ")
            .append("where id = :id ");

        defaultMapper = new IbMerchantMapper();
    }
    public List<IbMerchant> getMerchantBySerialNumber(String serialNumber){
    	StringBuilder sql = new StringBuilder()
        .append("select t.* ")
        .append("from m_ib_merchant t ")
        .append("where t.status = ? and t.serial_number like '%' || ? || '%'");
		List<IbMerchant> result = null;
		System.out.println("updateMerchant :" +sql +" withSN: " +serialNumber);
		try {
		    result = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, Constants.HARD_TOKEN.STATUS_LINKED, serialNumber);
		} catch (DataAccessException e) {
			System.out.println("error :" +e.getMessage());
		}		
		System.out.println("result Merchant :" +result);
		return result;
    }
   
	public IbMerchant getById(Long id){
		StringBuilder sql = new StringBuilder()
        .append("select t.* ")
        .append("from m_ib_merchant t ")
        .append("where t.id = ? ");
		IbMerchant ibMerchant = null;
		
		try {
		    ibMerchant = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, id);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		}
		
		return ibMerchant;
	}
	@Override
	public IbMerchant getByCustomerId(Long customerId) {
		StringBuilder sql = new StringBuilder()
        .append("select t.* ")
        .append("from m_ib_merchant t ")
        .append("inner join m_customer_device mcd ")
        .append("on t.terminal_id = mcd.terminal_id ")
        .append("where t.m_customer_id = ? ");
		IbMerchant ibMerchant = null;
		
		try {
		    ibMerchant = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId);
		} catch (EmptyResultDataAccessException e) {
		    //-- actually do nothing
		}
		
		return ibMerchant;
	}
}
