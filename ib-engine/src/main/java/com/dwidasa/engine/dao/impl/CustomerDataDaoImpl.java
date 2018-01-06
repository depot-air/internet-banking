package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.CustomerDataDao;
import com.dwidasa.engine.dao.mapper.CustomerDataMapper;
import com.dwidasa.engine.model.CustomerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 2:44 PM
 */
@Repository("customerDataDao")
public class CustomerDataDaoImpl extends GenericDaoImpl<CustomerData, Long> implements CustomerDataDao {
    @Autowired
    public CustomerDataDaoImpl(DataSource dataSource, CustomerDataMapper customerDataMapper) {
        super("m_customer_data", dataSource);
        defaultMapper = customerDataMapper;

        insertSql = new StringBuilder()
            .append("insert into m_customer_data ( ")
            .append("   m_customer_id, customer_fax, customer_address, register_date, personal_identity_number, ")
            .append("   description, limit_amount, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :customerId, :customerFax, :customerAddress, :registerDate, :personalIdentityNumber, ")
            .append("   :description, :limitAmount, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_customer_data ")
            .append("set ")
            .append("   m_customer_id = :customerId, customer_address = :customerAddress, ")
            .append("   register_date = :registerDate, personal_identity_number = :personalIdentityNumber, ")
            .append("   description = :description, limit_amount = :limitAmount, created = :created, ")
            .append("   createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }
}
