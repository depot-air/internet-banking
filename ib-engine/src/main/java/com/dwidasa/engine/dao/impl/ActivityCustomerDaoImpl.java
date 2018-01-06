package com.dwidasa.engine.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ActivityCustomerDao;
import com.dwidasa.engine.dao.mapper.ActivityCustomerMapper;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.CustomerMapper;
import com.dwidasa.engine.dao.mapper.TransactionTypeMapper;
import com.dwidasa.engine.model.ActivityCustomer;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.model.view.CustomerView;

@Repository("activityCustomerDao")
public class ActivityCustomerDaoImpl extends GenericDaoImpl<ActivityCustomer, Long> implements ActivityCustomerDao {

	private final ChainedRowMapper<Customer> customerMapper;
    private final ChainedRowMapper<TransactionType> transactionTypeMapper;
    
    @Autowired
    public ActivityCustomerDaoImpl(DataSource dataSource, ActivityCustomerMapper activityCustomerMapper, 
    		CustomerMapper customerMapper, TransactionTypeMapper transactionTypeMapper) {
        super("t_activity_customer", dataSource);
        defaultMapper = activityCustomerMapper;
        this.customerMapper = customerMapper;
        this.transactionTypeMapper = transactionTypeMapper;

        insertSql = new StringBuilder()
            .append("insert into t_activity_customer ( ")
            .append("   m_customer_id, activity_type, activity_data, reference_number, delivery_channel, delivery_channel_id, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :customerId, :activityType, :activityData, :referenceNumber, :deliveryChannel, :deliveryChannelId, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_activity_customer ")
            .append("set ")
            .append("   m_customer_id=:customerId, activity_type=:activityType, activity_data=:activityData, reference_number=:referenceNumber, ")
            .append("   delivery_channel=:deliveryChannel, delivery_channel_id=:deliveryChannelId,")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

	@Override
	public void logActivity(Long customerId, String activityType, String activityData, String referenceNumber, String merchantType, String terminalId) {
		ActivityCustomer activity = new ActivityCustomer();
		activity.setCustomerId(customerId);
		activity.setActivityType(activityType);
		activity.setActivityData(activityData);
		activity.setDeliveryChannel(merchantType);
		activity.setDeliveryChannelId(terminalId);
		//activity.setReferenceNumber(RandomGenerator.generateHex(12));
		activity.setReferenceNumber(referenceNumber);
		Date currentDate = new Date();
		activity.setCreated(currentDate);
		activity.setUpdated(currentDate);
		activity.setCreatedby(customerId);
		activity.setUpdatedby(customerId);
		save(activity);
	}

	@Override
	public List<ActivityCustomer> getLastLoginNonFinancialActivity(
			Long customerId) {
		String sqlSelect = "select id, m_customer_id, activity_type, activity_data, reference_number, delivery_channel, delivery_channel_id," +
			" created, createdby, updated, updatedby from t_activity_customer " +
			" where id >= (select max(id) from t_activity_customer ac" +
			" 	where activity_type='LS' and m_customer_id=?) " +
			" and id <= (select max(id) from t_activity_customer ac" +
			" 	where activity_type='LO' and m_customer_id=?)" +
			" order by id";
		return getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, new Object[] {customerId, customerId});
	}

    @Override
    public List<ActivityCustomer> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                                  List<String> orders, Object... params) {
        String sql =
                  "select tac.*, mc.*, mtt.* "
                + "from t_activity_customer tac "
                + "join m_customer mc "
                + "   on mc.id = tac.m_customer_id "
                + "join m_transaction_type mtt "
                + "   on mtt.transaction_type = tac.activity_type ";

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

        return getSimpleJdbcTemplate().query(sql, new RowMapper<ActivityCustomer>() {
            public ActivityCustomer mapRow(ResultSet rs, int rowNum) throws SQLException {
                int index = 0;
                ActivityCustomer ac = defaultMapper.mapRow(rs, index);
                index += Constants.ACTIVITY_CUSTOMER_LENGTH;
                Customer c = customerMapper.chainRow(rs, index);
                index += Constants.CUSTOMER_LENGTH;
                TransactionType mtt = transactionTypeMapper.chainRow(rs, index);

                ac.setUsername(c.getCustomerUsername());
                ac.setTransactionTypeDesc(mtt.getTransactionType());

                return ac;
            }
        }, newParams);
    }

    @Override
    public int getRowCount(List<String> restrictions, Object... params) {
        String sql =
                  "select count(*) "
                + "from t_activity_customer tac "
              + "join m_customer mc "
              + "   on mc.id = tac.m_customer_id "
              + "join m_transaction_type mtt "
              + "   on mtt.transaction_type = tac.activity_type ";
        
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

}