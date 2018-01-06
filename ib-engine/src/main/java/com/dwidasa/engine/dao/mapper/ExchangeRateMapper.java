package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.ExchangeRate;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/9/11
 * Time: 4:13 PM
 */
@Component("exchangeRateMapper")
public class ExchangeRateMapper extends ChainedRowMapper<ExchangeRate>
        implements ParameterizedRowMapper<ExchangeRate> {

    public ExchangeRateMapper() {
    }

    public ExchangeRate chainRow(ResultSet rs, int index) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();

        exchangeRate.setId(rs.getLong(++index));
        exchangeRate.setCurrencyId(rs.getLong(++index));
        exchangeRate.setCountry(rs.getString(++index));
        exchangeRate.setSoldPrice(rs.getBigDecimal(++index));
        exchangeRate.setBuyPrice(rs.getBigDecimal(++index));
        exchangeRate.setCreated(rs.getTimestamp(++index));
        exchangeRate.setCreatedby(rs.getLong(++index));
        exchangeRate.setUpdated(rs.getTimestamp(++index));
        exchangeRate.setUpdatedby(rs.getLong(++index));

        return exchangeRate;
    }
}
