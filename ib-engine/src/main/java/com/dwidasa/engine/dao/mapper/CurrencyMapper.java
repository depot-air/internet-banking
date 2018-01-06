package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Currency;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/24/11
 * Time: 4:31 PM
 */
@Component("currencyMapper")
public class CurrencyMapper extends ChainedRowMapper<Currency> implements ParameterizedRowMapper<Currency> {
    public CurrencyMapper() {
    }

    public Currency chainRow(ResultSet rs, int index) throws SQLException {
        Currency currency = new Currency();

        currency.setId(rs.getLong(++index));
        currency.setCurrencyCode(rs.getString(++index));
        currency.setSwiftCode(rs.getString(++index));
        currency.setCurrencyName(rs.getString(++index));
        currency.setCreated(rs.getTimestamp(++index));
        currency.setCreatedby(rs.getLong(++index));
        currency.setUpdated(rs.getTimestamp(++index));
        currency.setUpdatedby(rs.getLong(++index));

        return currency;
    }
}
