package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.CustomerRegisterDao;
import com.dwidasa.engine.dao.mapper.CustomerRegisterMapper;
import com.dwidasa.engine.model.CustomerRegister;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 2:59 PM
 */
@Repository("customerRegisterDao")
public class CustomerRegisterDaoImpl extends GenericDaoImpl<CustomerRegister, Long>
        implements CustomerRegisterDao {
    @Autowired
    public CustomerRegisterDaoImpl(DataSource dataSource, CustomerRegisterMapper customerRegisterMapper) {
        super("m_customer_register", dataSource);
        defaultMapper = customerRegisterMapper;

        insertSql = new StringBuilder()
            .append("insert into m_customer_register ( ")
            .append("   m_customer_id, transaction_type, customer_reference, data1, data2, data3, data4, data5, ")
            .append("   created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :customerId, :transactionType, :customerReference, :data1, :data2, :data3, :data4, :data5, ")
            .append("   :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_customer_register ")
            .append("set ")
            .append("   m_customer_id = :customerId, transaction_type = :transactionType, ")
            .append("   customer_reference = :customerReference, data1 = :data1, data2 = :data2, ")
            .append("   data3 = :data3, data4 = :data4, data5 = :data5, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerRegister> getAll(Long customerId, String transactionType, String billerCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mcr.* ")
                .append("from m_customer_register mcr ")
                .append("where mcr.m_customer_id = ? ")
                .append("and mcr.transaction_type = ? ")
                .append("and mcr.data1 = ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId, transactionType, billerCode);
    }
    
    public List<CustomerRegister> getAllOrderBy(Long customerId, String transactionType, String billerCode, String orderBy) {
        StringBuilder sql = new StringBuilder()
                .append("select mcr.* ")
                .append("from m_customer_register mcr ")
                .append("where mcr.m_customer_id = ? ")
                .append("and mcr.transaction_type = ? ")
                .append("and mcr.data1 = ? ")
                .append("order by ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId, transactionType, billerCode, orderBy);
    }

    /**
     * {@inheritDoc}
     */
    public List<CustomerRegister> getByTransactionType(Long customerId, String transactionType) {
        StringBuilder sql = new StringBuilder()
                .append("select mcr.* ")
                .append("from m_customer_register mcr ")
                .append("where mcr.m_customer_id = ? ")
                .append("and mcr.transaction_type = ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId, transactionType);
    }

    /**
     * {@inheritDoc}
     */
    public CustomerRegister get(Long customerId, String transactionType, String customerReference) {
        StringBuilder sql = new StringBuilder()
                .append("select mcr.* ")
                .append("from m_customer_register mcr ")
                .append("where mcr.m_customer_id = ? ")
                .append("and mcr.transaction_type = ? ")
                .append("and mcr.customer_reference = ? ");

        CustomerRegister result = null;

        try {
            //result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId, transactionType, customerReference);
        	List<CustomerRegister> crs = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId, transactionType, customerReference);
        	if (crs.size() > 0) result = crs.get(0);	//object yg pertama
        } catch (DataAccessException e) {
        }

        return result;
    }

    public List<CustomerRegister> getRegisterList(List<String> restrictions, List<String> orders) {
    	String listSql = "select r.id, r.m_customer_id, r.transaction_type, r.customer_reference, r.data1, r.data2, r.data3, r.data4, r.data5, b.biller_name, bp.product_name from m_customer_register r"
				+ " inner join m_transaction_type t on t.transaction_type=r.transaction_type"
				+ " inner join m_biller b on b.biller_code=r.data1 and b.m_transaction_type_id=t.id"
				+ " left join m_biller_product bp on bp.product_code=r.data2 and bp.m_biller_id=b.id";
    	
		StringBuffer sbSelect = new StringBuffer(listSql).append(" ");
		if (restrictions != null && restrictions.size() != 0) {
            sbSelect.append("where ");
            for (String restriction : restrictions) {
            	sbSelect.append(restriction).append(" and ");
            }
            sbSelect.delete(sbSelect.length()-4, sbSelect.length());
        }

        if (orders != null && orders.size() != 0) {
            sbSelect.append("order by ");
            for (String order : orders) {
            	sbSelect.append(order).append(", ");
            }
            sbSelect.delete(sbSelect.length()-2, sbSelect.length());
        }
        
        return getSimpleJdbcTemplate().query(sbSelect.toString(), new ListMapper());
	}
    
    private static final class ListMapper implements RowMapper<CustomerRegister> {
		public CustomerRegister mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			CustomerRegister row = new CustomerRegister();
			row.setId(rs.getLong("id"));
			row.setCustomerReference(rs.getString("customer_reference"));
			row.setCustomerId(rs.getLong("m_customer_id"));
			row.setData1(rs.getString("data1"));
			row.setData2(rs.getString("data2"));
			row.setData3(rs.getString("data3"));
			row.setData4(rs.getString("data4"));
			row.setData5(rs.getString("data5"));
			String productName = rs.getString("product_name");
			if (productName != null && !"null".equals(productName)) {
				row.setBillerName(productName);
			} else {
				row.setBillerName(rs.getString("biller_name"));
			}
			row.setTransactionType(rs.getString("transaction_type"));
			return row;
		}
    }
    
    public void remove(Long id, Long customerId) {
    	updateUpdatedBy(id, customerId);
    	String sqlSelect = "delete from m_customer_register where id=? and m_customer_id=?";
		int count = getSimpleJdbcTemplate().update(sqlSelect, new Object[] {id, customerId}); 
		if (count == 0) {
			throw new BusinessException("IB-5000", "Data Not Found");
		}        
    }

    @Override
    public int deleteCustomerRegisters(Long customerId) {

        StringBuilder sql = new StringBuilder()
                .append("delete from m_customer_register mcr ")
                .append("where mcr.m_customer_id = ? ");

        try {

            return getSimpleJdbcTemplate().update(sql.toString(), customerId);

        }
        catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }

	@Override
	public List<CustomerRegister> getAirportsFrom() {
		StringBuilder sql = new StringBuilder()
	        .append("SELECT mc.* FROM m_customer_register mc ") 
	        .append("INNER JOIN m_airport ap ")
	        .append("ON mc.data2 = ap.airport_code ")
	        .append("order by mc.customer_reference ");
	
	    List<CustomerRegister> crs = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
	    
	    return crs;
	}

	@Override
	public List<CustomerRegister> getAirportsTo() {
		StringBuilder sql = new StringBuilder()
	        .append("SELECT mc.* FROM m_customer_register mc ") 
	        .append("INNER JOIN m_airport ap ")
	        .append("ON mc.data3 = ap.airport_code ")
	        .append("order by mc.customer_reference ");
	
	    List<CustomerRegister> crs = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
	    
	    return crs;
	}

	@Override
	public List<CustomerRegister> getAirportsFromTo(String customerId, String transactionType, String billerCode, String fromTo) {
		StringBuilder sql = new StringBuilder()
        .append("SELECT mc.* FROM m_customer_register mc ") 
        .append("INNER JOIN m_airport ap ");
		if (fromTo.equals("FROM")) {
			sql = sql.append("ON mc.data2 = ap.airport_code ");
		} else {
			sql = sql.append("ON mc.data3 = ap.airport_code ");
		}
		sql = sql.append("order by mc.customer_reference ");

	    List<CustomerRegister> crs = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper);
	    
	    return crs;
	}
}
