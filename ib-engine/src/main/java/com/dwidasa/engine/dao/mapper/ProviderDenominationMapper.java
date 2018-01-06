package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.ProviderDenomination;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:16 PM
 */
@Component("providerDenominationMapper")
public class ProviderDenominationMapper extends ChainedRowMapper<ProviderDenomination>
        implements ParameterizedRowMapper<ProviderDenomination> {

    public ProviderDenominationMapper() {
    }

    public ProviderDenomination chainRow(ResultSet rs, int index) throws SQLException {
        ProviderDenomination providerDenomination = new ProviderDenomination();

        providerDenomination.setId(rs.getLong(++index));
        providerDenomination.setProviderId(rs.getLong(++index));
        providerDenomination.setProductDenominationId(rs.getLong(++index));
        providerDenomination.setPrice(rs.getBigDecimal(++index));
        providerDenomination.setFee(rs.getBigDecimal(++index));
        providerDenomination.setCreated(rs.getTimestamp(++index));
        providerDenomination.setCreatedby(rs.getLong(++index));
        providerDenomination.setUpdated(rs.getTimestamp(++index));
        providerDenomination.setUpdatedby(rs.getLong(++index));

        return providerDenomination;
    }
}
