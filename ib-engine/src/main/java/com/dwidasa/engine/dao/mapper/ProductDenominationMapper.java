package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.ProductDenomination;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:09 PM
 */
@Component("productDenominationMapper")
public class ProductDenominationMapper extends ChainedRowMapper<ProductDenomination>
        implements ParameterizedRowMapper<ProductDenomination> {

    public ProductDenominationMapper() {
    }

    public ProductDenomination chainRow(ResultSet rs, int index) throws SQLException {
        ProductDenomination productDenomination = new ProductDenomination();

        productDenomination.setId(rs.getLong(++index));
        productDenomination.setBillerProductId(rs.getLong(++index));
        productDenomination.setDenomination(rs.getString(++index));
        productDenomination.setCreated(rs.getTimestamp(++index));
        productDenomination.setCreatedby(rs.getLong(++index));
        productDenomination.setUpdated(rs.getTimestamp(++index));
        productDenomination.setUpdatedby(rs.getLong(++index));
        productDenomination.setDefaultProviderId(rs.getLong(++index));
        productDenomination.setIsActive(rs.getBoolean(++index));

        return productDenomination;
    }
}
