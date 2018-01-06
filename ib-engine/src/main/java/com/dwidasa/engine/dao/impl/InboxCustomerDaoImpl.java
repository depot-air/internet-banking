package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.InboxCustomerDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.InboxCustomerMapper;
import com.dwidasa.engine.dao.mapper.InboxMapper;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.Inbox;
import com.dwidasa.engine.model.InboxCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 7:15 PM
 */
@Repository("inboxCustomerDao")
public class InboxCustomerDaoImpl extends GenericDaoImpl<InboxCustomer, Long> implements InboxCustomerDao {
    private final ChainedRowMapper<Inbox> inboxMapper;

    @Autowired
    public InboxCustomerDaoImpl(DataSource dataSource, InboxCustomerMapper inboxCustomerMapper,
                                InboxMapper inboxMapper) {
        super("t_inbox_customer", dataSource);
        defaultMapper = inboxCustomerMapper;
        this.inboxMapper = inboxMapper;

        insertSql = new StringBuilder()
            .append("insert into t_inbox_customer ( ")
            .append("   t_inbox_id, m_customer_id, status, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :inboxId, :customerId, :status, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_inbox_customer ")
            .append("set ")
            .append("   t_inbox_id = :inboxId, m_customer_id = :customerId, status = :status, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public InboxCustomer markMessage(Long inboxCustomerId, Integer status) {
        InboxCustomer ic = get(inboxCustomerId);
        ic.setStatus(status);
        
        return save(ic);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<InboxCustomer> getAllWithInbox(Long customerId, Date lastRequestDate) {
        StringBuilder sql;

        List params = new ArrayList();
        params.add(customerId);

        if (lastRequestDate == null) {
            sql = new StringBuilder()
                .append("select tic.*, ti.* ")
                .append("from t_inbox_customer tic ")
                .append("join t_inbox ti ")
                .append("   on ti.id = tic.t_inbox_id ")
                .append("where m_customer_id = ? ")
                .append("order by tic.created ");
            
        }
        else {
            sql = new StringBuilder()
                .append("select  tic.*, ti.* ")
                .append("from t_inbox_customer tic ")
                .append("join t_inbox ti ")
                .append("   on ti.id = tic.t_inbox_id ")
                .append("where tic.m_customer_id = ? ")
                .append("and tic.created > ? ")
                .append("order by tic.created ");
            

            params.add(lastRequestDate);
        }

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<InboxCustomer>() {
            public InboxCustomer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                InboxCustomer ic = defaultMapper.mapRow(rs, index);
                index += Constants.INBOX_CUSTOMER_LENGTH;
                Inbox i = inboxMapper.chainRow(rs, index);
                ic.setInbox(i);

                return ic;
            }
        }, params.toArray());
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<InboxCustomer> getAllWithInboxList(Long customerId, int awal, int akhir, Date lastRequestDate) {
        StringBuilder sql;

        List params = new ArrayList();
        params.add(customerId);
        params.add(awal);
        params.add(akhir);

        if (lastRequestDate == null) {
            sql = new StringBuilder()

            .append("select tic.*, ti.* from t_inbox_customer tic " +
            		"join t_inbox ti on ti.id = tic.t_inbox_id " +
            		"where tic.id in(" +
            		"select id from (" +
            		"select * from (" +
            		"select row_number() over (ORDER BY tic.id) as row, tic.id, " +
            		"tic.m_customer_id, tic.created from t_inbox_customer tic join " +
            		"t_inbox ti on ti.id = tic.t_inbox_id where m_customer_id = ? order by tic.created )data " +
            		"where row >= ? and row <= ?" +
            		")" +
            		"data1)" +
            		"order by tic.created");
        }
        else {
            sql = new StringBuilder()

            .append("select tic.*, ti.* from t_inbox_customer tic " +
            		"join t_inbox ti on ti.id = tic.t_inbox_id " +
            		"where tic.id in(" +
            		"select id from (" +
            		"select * from (" +
            		"select row_number() over (ORDER BY tic.id) as row, tic.id, " +
            		"tic.m_customer_id, tic.created from t_inbox_customer tic join " +
            		"t_inbox ti on ti.id = tic.t_inbox_id order by tic.created )data " +
            		"where m_customer_id = ? and created > ? " +
            		")" +
            		"data1)" +
            		"order by tic.created");

            params.add(lastRequestDate);
        }

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<InboxCustomer>() {
            public InboxCustomer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                InboxCustomer ic = defaultMapper.mapRow(rs, index);
                index += Constants.INBOX_CUSTOMER_LENGTH;
                Inbox i = inboxMapper.chainRow(rs, index);
                ic.setInbox(i);

                return ic;
            }
        }, params.toArray());
    }
    /**
     * {@inheritDoc}
     */
    public InboxCustomer getWithInbox(Long id) {
        StringBuilder sql = new StringBuilder()
                .append("select tic.*, ti.* ")
                .append("from t_inbox_customer tic ")
                .append("join t_inbox ti ")
                .append("   on ti.id = tic.t_inbox_id ")
                .append("where tic.id = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), new RowMapper<InboxCustomer>() {
            public InboxCustomer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                InboxCustomer ic = defaultMapper.mapRow(rs, index);
                index += Constants.INBOX_CUSTOMER_LENGTH;
                Inbox i = inboxMapper.chainRow(rs, index);
                ic.setInbox(i);

                return ic;
            }
        }, id);
    }
    
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<InboxCustomer> getAllWithInboxId(Long inboxID, Long customerID, Date lastRequestDate) {
        StringBuilder sql;

        List params = new ArrayList();
        params.add(inboxID);
        params.add(customerID);

        if (lastRequestDate == null) {
            sql = new StringBuilder()
                .append("select tic.*, ti.* ")
                .append("from t_inbox_customer tic ")
                .append("join t_inbox ti ")
                .append("   on ti.id = tic.t_inbox_id ")
                .append("where tic.t_inbox_id = ? and tic.m_customer_id = ?")
                .append("order by tic.created ");
        }
        else {
            sql = new StringBuilder()
                .append("select tic.*, ti.* ")
                .append("from t_inbox_customer tic ")
                .append("join t_inbox ti ")
                .append("   on ti.id = tic.t_inbox_id ")
                .append("where tic.t_inbox_id = ? and tic.m_customer_id = ?")
                .append("and tic.created > ? ")
                .append("order by tic.created ");

            params.add(lastRequestDate);
        }

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<InboxCustomer>() {
            public InboxCustomer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                InboxCustomer ic = defaultMapper.mapRow(rs, index);
                index += Constants.INBOX_CUSTOMER_LENGTH;
                Inbox i = inboxMapper.chainRow(rs, index);
                ic.setInbox(i);

                return ic;
            }
        }, params.toArray());
    }
    
    
    public void removeByIdCus(Long inboxId, Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("delete from t_inbox_customer ")
                .append("where t_inbox_id = ? and m_customer_id = ?");

        getSimpleJdbcTemplate().update(sql.toString(), inboxId, customerId);
    }
    
    
    public void removeAllById(Long inboxId) {
        StringBuilder sql = new StringBuilder()
                .append("delete from t_inbox_customer ")
                .append("where t_inbox_id = ?");

        getSimpleJdbcTemplate().update(sql.toString(), inboxId);
    }
    
    public void updateStatusById(Long inboxId, Long customerId, int status){
    	
    	StringBuilder sql = new StringBuilder()
        .append("update t_inbox_customer ")
        .append("set status = ? where t_inbox_id = ? and m_customer_id= ?");

        getSimpleJdbcTemplate().update(sql.toString(), status, inboxId, customerId);
    	
    }
    
    public List<InboxCustomer> getAllUnreadMessage(Long customerId) {
        StringBuilder sql = new StringBuilder()
                .append("select mcd.* ")
                .append("from t_inbox_customer mcd ")
                .append("where mcd.m_customer_id = ? ")
                .append("and mcd.status = 0 ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, customerId);
    }
    
    
}
