package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Merchant;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:25 PM
 */
@Component("merchantMapper")
public class MerchantMapper extends ChainedRowMapper<Merchant> implements ParameterizedRowMapper<Merchant> {
    public MerchantMapper() {
    }

    public Merchant chainRow(ResultSet rs, int index) throws SQLException {
        Merchant merchant = new Merchant();

        merchant.setId(rs.getLong(++index));
        merchant.setMerchantType(rs.getString(++index));
        merchant.setDescription(rs.getString(++index));
        merchant.setCreated(rs.getTimestamp(++index));
        merchant.setCreatedby(rs.getLong(++index));
        merchant.setUpdated(rs.getTimestamp(++index));
        merchant.setUpdatedby(rs.getLong(++index));

        return merchant;
    }
}
