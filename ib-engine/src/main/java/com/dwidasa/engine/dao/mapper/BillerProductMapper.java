package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.BillerProduct;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:05 PM
 */
@Component("billerProductMapper")
public class BillerProductMapper extends ChainedRowMapper<BillerProduct>
        implements ParameterizedRowMapper<BillerProduct> {

    public BillerProductMapper() {
    }

    public BillerProduct chainRow(ResultSet rs, int index) throws SQLException {
        BillerProduct billerProduct = new BillerProduct();

        billerProduct.setId(rs.getLong(++index));
        billerProduct.setBillerId(rs.getLong(++index));
        billerProduct.setProductCode(rs.getString(++index));
        billerProduct.setProductName(rs.getString(++index));
        billerProduct.setCreated(rs.getTimestamp(++index));
        billerProduct.setCreatedby(rs.getLong(++index));
        billerProduct.setUpdated(rs.getTimestamp(++index));
        billerProduct.setUpdatedby(rs.getLong(++index));
        
        return billerProduct;
    }
}
