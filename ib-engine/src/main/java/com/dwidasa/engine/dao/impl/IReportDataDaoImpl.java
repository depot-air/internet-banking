package com.dwidasa.engine.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.IReportDataDao;
import com.dwidasa.engine.dao.mapper.IReportDataMapper;
import com.dwidasa.engine.model.IReportData;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:21 PM
 */
@Repository("iReportDataDao")
public class IReportDataDaoImpl extends GenericDaoImpl<IReportData, Long> implements IReportDataDao {
    @Autowired
    public IReportDataDaoImpl(DataSource dataSource, IReportDataMapper iReportDataMapper) {
        super("r_ireport_data", dataSource);
        defaultMapper = iReportDataMapper;

        insertSql = new StringBuilder()
            .append("insert into r_ireport_data ( ")
            .append("   m_customer_id, transaction_type, transaction_date, reference_key, report_type, data1, data2, data3, data4, data5, data6, data7, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :customerId, :transactionType, :transactionDate, :referenceKey, :reportType, :data1, :data2, :data3, :data4, :data5, :data6, :data7, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_airline ")
            .append("set ")
            .append("   m_customer_id = :customerId, transaction_type = :transactionType, transaction_date = :transactionDate, reference_key = :referenceKey, report_type = :reportType, ")
    		.append(" data1 = :data1, data2 = :data2, data3 = :data3, data4 = :data4, data5 = :data5, data6 = :data6, data7 = :data7, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public IReportData get(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select ri.* ")
                .append("from r_ireport_data ri ")
                .append("where mc.airline_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }

}
