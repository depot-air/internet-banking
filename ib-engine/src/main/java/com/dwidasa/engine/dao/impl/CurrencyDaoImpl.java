package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.CurrencyDao;
import com.dwidasa.engine.dao.mapper.CurrencyMapper;
import com.dwidasa.engine.model.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/24/11
 * Time: 4:29 PM
 */
@Repository("currencyDao")
public class CurrencyDaoImpl extends GenericDaoImpl<Currency, Long> implements CurrencyDao {
    @Autowired
    public CurrencyDaoImpl(DataSource dataSource, CurrencyMapper currencyMapper) {
        super("m_currency", dataSource);
        defaultMapper = currencyMapper;

        insertSql = new StringBuilder()
            .append("insert into m_currency ( ")
            .append("   currency_code, swift_code, currency_name, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :currencyCode, :swiftCode, :currencyName, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_currency ")
            .append("set ")
            .append("   currency_code = :currencyCode, swift_code = :swiftCode, currency_name = :currencyName, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public Currency get(String swiftCode) {
        StringBuilder sql = new StringBuilder()
                .append("select mc.* ")
                .append("from m_currency mc ")
                .append("where mc.swift_code = ? ");

        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, swiftCode);
    }
}
