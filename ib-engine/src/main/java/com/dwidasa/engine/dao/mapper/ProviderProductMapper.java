package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.ProviderProduct;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:27 PM
 */
@Component("providerProductMapper")
public class ProviderProductMapper extends ChainedRowMapper<ProviderProduct>
        implements ParameterizedRowMapper<ProviderProduct> {

    public ProviderProductMapper() {
    }

    public ProviderProduct chainRow(ResultSet rs, int index) throws SQLException {
        ProviderProduct providerProduct = new ProviderProduct();

        providerProduct.setId(rs.getLong(++index));
        providerProduct.setProviderId(rs.getLong(++index));
        providerProduct.setBillerProductId(rs.getLong(++index));
        providerProduct.setFee(rs.getBigDecimal(++index));
        providerProduct.setCreated(rs.getTimestamp(++index));
        providerProduct.setCreatedby(rs.getLong(++index));
        providerProduct.setUpdated(rs.getTimestamp(++index));
        providerProduct.setUpdatedby(rs.getLong(++index));
        providerProduct.setIsActive(rs.getBoolean(++index));

        return providerProduct;
    }
}
