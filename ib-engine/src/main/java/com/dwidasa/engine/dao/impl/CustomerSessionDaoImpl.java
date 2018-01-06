package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerSessionDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.CustomerMapper;
import com.dwidasa.engine.dao.mapper.CustomerSessionMapper;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerSession;
import com.dwidasa.engine.service.CacheManager;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 9:49 AM
 */
@Repository("customerSessionDao")
public class CustomerSessionDaoImpl extends GenericDaoImpl<CustomerSession, Long> implements CustomerSessionDao {
    @Autowired
    private CacheManager cacheManager;
    
    private final ChainedRowMapper<Customer> customerMapper;

    @Autowired
    public CustomerSessionDaoImpl(DataSource dataSource, CustomerSessionMapper customerSessionMapper, CustomerMapper customerMapper) {
        super("m_customer_session", dataSource);
        defaultMapper = customerSessionMapper;
        this.customerMapper = customerMapper;

        insertSql = new StringBuilder()
            .append("insert into m_customer_session ( ")
            .append("   m_customer_id, device_id, session_id, challenge, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :customerId, :deviceId, :sessionId, :challenge, :created, :createdby, :updated, :updatedby")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_customer_session ")
            .append("set ")
            .append("   m_customer_id = :customerId, device_id = :deviceId, session_id = :sessionId, ")
            .append("   challenge = :challenge, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public CustomerSession get(Long customerId, String deviceId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcs.* ")
                .append("from m_customer_session mcs ")
                .append("where mcs.m_customer_id = ? ")
                .append("and mcs.device_id = ? ");

        CustomerSession customerSession = null;
        try {
//            customerSession = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper,
//                    customerId, deviceId);
            List<CustomerSession> custSessions = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, new Object[] {customerId, deviceId});
        	if (custSessions.size() > 0) {
        		customerSession = custSessions.get(custSessions.size() - 1);
        	}

        } catch (EmptyResultDataAccessException e) {
//        	List<CustomerSession> custSessions = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, new Object[] {customerId, deviceId});
        	
        }

        return customerSession;
    }

    /**
     * {@inheritDoc}
     */
    public CustomerSession force(Long customerId, String deviceId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcs.* ")
                .append("from m_customer_session mcs ")
                .append("where mcs.m_customer_id = ? ")
                .append("and mcs.device_id = ? ");

        CustomerSession customerSession = null;
        try {
//            customerSession = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId, deviceId);
        	List<CustomerSession> custSessions = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, new Object[] {customerId, deviceId});
        	if (custSessions.size() > 0) {
        		customerSession = custSessions.get(custSessions.size() - 1);	
        	}

        } catch (EmptyResultDataAccessException e) {
        	
        }

        if (customerSession == null) {
            customerSession = new CustomerSession();
            customerSession.setDeviceId(deviceId);
            customerSession.setCustomerId(customerId);
            customerSession.setCreated(new Date());
            customerSession.setCreatedby(customerId);
            customerSession.setUpdated(new Date());
            customerSession.setUpdatedby(customerId);
        }

        return customerSession;
    }

    /**
     * {@inheritDoc}
     */
    public Integer validate(Long customerId, String sessionId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcs.* ")
                .append("from m_customer_session mcs ")
                .append("where mcs.m_customer_id = ? ")
                .append("and mcs.session_id = ? ");

        Integer result = 1;
        CustomerSession customerSession = null;
        try {
            customerSession = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, customerId, sessionId);
        } catch (EmptyResultDataAccessException e) {
            result = 0;
        }

        if (customerSession != null) {
            Long currentTime = System.currentTimeMillis();

            Calendar inputCalendar = Calendar.getInstance();
            inputCalendar.setTime(customerSession.getUpdated());

            Long lastActivityTime = inputCalendar.getTimeInMillis();
            Long sessionTimeout = Long.valueOf(cacheManager.getParameter("SESSION_TIMEOUT").
                    getParameterValue())*60*1000;

            if (currentTime - lastActivityTime > sessionTimeout) {
                result = -1;
            }

            customerSession.setUpdated(new Date());
            customerSession.setUpdatedby(customerId);
            save(customerSession);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void invalidate(Long customerId, String deviceId) {
        StringBuilder sql = new StringBuilder()
            .append("delete from m_customer_session ")
            .append("where m_customer_id = ? ")
            .append("and device_id != ? ");

        getSimpleJdbcTemplate().update(sql.toString(), customerId, deviceId);
    }
    
    @Override
    public List<CustomerSession> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                   List<String> orders, Object... params) {
        String sql =
                  "select mcs.*, mc.* "
                + "from m_customer_session mcs "
                + "join m_customer mc "
                + "   on mc.id = mcs.m_customer_id ";

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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<CustomerSession>() {
            public CustomerSession mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                CustomerSession cs = defaultMapper.mapRow(rs, index);
                index += Constants.CUSTOMER_SESSION_LENGTH;
                Customer customer = customerMapper.chainRow(rs, index);
                cs.setCustomer(customer);
                return cs;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from m_customer_session mcs "
                + "join m_customer mc "
                + "   on mc.id=mcs.m_customer_id ";

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
	public void removeAll() {
		String sqlDelete = "delete from m_customer_session";
		getSimpleJdbcTemplate().update(sqlDelete);
	}

	@Override
	public void deleteByCustomerId(Long customerId) {
        StringBuilder sql = new StringBuilder()
        .append("delete from m_customer_session ")
        .append("where m_customer_id = ? ");

    getSimpleJdbcTemplate().update(sql.toString(), customerId);
	}

}