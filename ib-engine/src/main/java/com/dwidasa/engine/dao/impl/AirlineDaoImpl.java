package com.dwidasa.engine.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.AirlineDao;
import com.dwidasa.engine.dao.mapper.AirlineMapper;
import com.dwidasa.engine.model.Airline;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:21 PM
 */
@Repository("airlineDao")
public class AirlineDaoImpl extends GenericDaoImpl<Airline, Long> implements AirlineDao {
    @Autowired
    public AirlineDaoImpl(DataSource dataSource, AirlineMapper airlineMapper) {
        super("m_airline", dataSource);
        defaultMapper = airlineMapper;

        insertSql = new StringBuilder()
            .append("insert into m_airline ( ")
            .append("   airline_id, airline_code, airline_name, airline_type, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :airlineId, :airlineCode, :airlineName, :airlineType, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_airline ")
            .append("set ")
            .append("   airline_id = :airlineId, airline_code = :airlineCode, airline_name = :airlineName, airline_type = :airlineType, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Airline get(String code) {
        StringBuilder sql = new StringBuilder()
                .append("select ma.* ")
                .append("from m_airline ma ")
                .append("where mc.airline_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, code);
    }

}
