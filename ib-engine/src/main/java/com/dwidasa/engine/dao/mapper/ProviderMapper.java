package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Provider;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:22 PM
 */
@Component("providerMapper")
public class ProviderMapper extends ChainedRowMapper<Provider> implements ParameterizedRowMapper<Provider> {
    public ProviderMapper() {
    }

    public Provider chainRow(ResultSet rs, int index) throws SQLException {
        Provider provider = new Provider();

        provider.setId(rs.getLong(++index));
        provider.setProviderCode(rs.getString(++index));
        provider.setProviderName(rs.getString(++index));
        provider.setInquiry(rs.getString(++index));
        provider.setCreated(rs.getTimestamp(++index));
        provider.setCreatedby(rs.getLong(++index));
        provider.setUpdated(rs.getTimestamp(++index));
        provider.setUpdatedby(rs.getLong(++index));
        provider.setDescription(rs.getString(++index));
        provider.setIsActive(rs.getBoolean(++index));
        return provider;
    }
}
