package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.ActivityCustomer;

@Component("activityCustomerMapper")
public class ActivityCustomerMapper extends ChainedRowMapper<ActivityCustomer> implements ParameterizedRowMapper<ActivityCustomer> {
    public ActivityCustomerMapper() {
    }

    public ActivityCustomer chainRow(ResultSet rs, int index) throws SQLException {
        ActivityCustomer activityCustomer = new ActivityCustomer();

        activityCustomer.setId(rs.getLong(++index));
        activityCustomer.setCustomerId(rs.getLong(++index));
        activityCustomer.setActivityType(rs.getString(++index));
        activityCustomer.setActivityData(rs.getString(++index));
        activityCustomer.setReferenceNumber(rs.getString(++index));
        activityCustomer.setDeliveryChannel(rs.getString(++index));
        activityCustomer.setDeliveryChannelId(rs.getString(++index));
        activityCustomer.setCreated(rs.getTimestamp(++index));
        activityCustomer.setCreatedby(rs.getLong(++index));
        activityCustomer.setUpdated(rs.getTimestamp(++index));
        activityCustomer.setUpdatedby(rs.getLong(++index));
        return activityCustomer;
    }
}
