package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.TreasuryStage;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:33 PM
 */
@Component("treasuryStageMapper")
public class TreasuryStageMapper extends ChainedRowMapper<TreasuryStage>
        implements ParameterizedRowMapper<TreasuryStage> {

    public TreasuryStageMapper() {
    }

    @Override
    public TreasuryStage chainRow(ResultSet rs, int index) throws SQLException {
        TreasuryStage ts = new TreasuryStage();

        ts.setId(rs.getLong(++index));
        ts.setTransactionId(rs.getLong(++index));
        ts.setCustomerRegisterId(rs.getLong(++index));
        ts.setSenderId(rs.getLong(++index));
        ts.setOfficerId(rs.getLong(++index));
        ts.setStatus(rs.getString(++index));
        
        ts.setCreated(rs.getTimestamp(++index));
        ts.setCreatedby(rs.getLong(++index));
        ts.setUpdated(rs.getTimestamp(++index));
        ts.setUpdatedby(rs.getLong(++index));

        return ts;
    }
}
