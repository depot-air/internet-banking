package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Product;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/25/11
 * Time: 3:17 PM
 */
@Component("productMapper")
public class ProductMapper extends ChainedRowMapper<Product> implements ParameterizedRowMapper<Product> {
    public ProductMapper() {
    }

    public Product chainRow(ResultSet rs, int index) throws SQLException {
        Product product = new Product();

        product.setId(rs.getLong(++index));
        product.setProductCode(rs.getString(++index));
        product.setProductName(rs.getString(++index));
        product.setCreated(rs.getTimestamp(++index));
        product.setCreatedby(rs.getLong(++index));
        product.setUpdated(rs.getTimestamp(++index));
        product.setUpdatedby(rs.getLong(++index));

        return product;
    }
}
