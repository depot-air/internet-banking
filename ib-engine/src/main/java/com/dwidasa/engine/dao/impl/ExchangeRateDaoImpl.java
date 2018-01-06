package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ExchangeRateDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.CurrencyMapper;
import com.dwidasa.engine.dao.mapper.ExchangeRateMapper;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/9/11
 * Time: 3:51 PM
 */
@Repository("exchangeRateDao")
public class ExchangeRateDaoImpl extends GenericDaoImpl<ExchangeRate, Long> implements ExchangeRateDao {
    private final ChainedRowMapper<Currency> currencyMapper;

    @Autowired
    public ExchangeRateDaoImpl(DataSource dataSource, ExchangeRateMapper exchangeRateMapper,
                               CurrencyMapper currencyMapper) {
        super("m_exchange_rate", dataSource);
        defaultMapper = exchangeRateMapper;
        this.currencyMapper = currencyMapper;

        insertSql = new StringBuilder()
            .append("insert into m_exchange_rate ( ")
            .append("   m_currency_id, country, sold_price, buy_price, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :currencyId, :country, :soldPrice, :buyPrice, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_exchange_rate ")
            .append("set ")
            .append("   m_currency_id = :currencyId, country = :country, sold_price = :soldPrice, ")
            .append("   buy_price = :buyPrice, created = :created, createdby = :createdby, updated = :updated ")
            .append("   updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public List<ExchangeRate> getAllWithCurrency() {
        StringBuilder sql = new StringBuilder()
                .append("select mer.*, mc.* ")
                .append("from m_exchange_rate mer ")
                .append("join m_currency mc ")
                .append("   on mc.id = mer.m_currency_id ");

        return getSimpleJdbcTemplate().query(sql.toString(), new RowMapper<ExchangeRate>() {
            public ExchangeRate mapRow(ResultSet rs, int i) throws SQLException {
                int index = 0;
                ExchangeRate er = defaultMapper.mapRow(rs, index);
                index += Constants.EXCHANGE_RATE_LENGTH;
                Currency c = currencyMapper.chainRow(rs, index);

                er.setCurrency(c);
                return er;
            }
        });
    }
}
