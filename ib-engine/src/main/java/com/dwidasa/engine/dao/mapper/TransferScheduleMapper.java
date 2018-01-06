package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.TransferSchedule;

@Component("transferScheduleMapper")
public class TransferScheduleMapper extends ChainedRowMapper<TransferSchedule> implements ParameterizedRowMapper<TransferSchedule> {
    public TransferScheduleMapper() {
    }

    public TransferSchedule chainRow(ResultSet rs, int index) throws SQLException {
    	TransferSchedule transferSchedule = new TransferSchedule();

    	transferSchedule.setId(rs.getLong(++index));    	
    	transferSchedule.setBatchId(rs.getInt(++index));
    	transferSchedule.setAccountFrom(rs.getString(++index));
    	transferSchedule.setAccountTo(rs.getString(++index));
    	transferSchedule.setAmount(rs.getBigDecimal(++index));
    	transferSchedule.setNews(rs.getString(++index));
    	transferSchedule.setBatchType(rs.getString(++index));
    	transferSchedule.setTransferDate(rs.getString(++index));
    	transferSchedule.setTransferEnd(rs.getString(++index));
    	transferSchedule.setFlagSent(rs.getInt(++index));

    	transferSchedule.setCreated(rs.getTimestamp(++index));
    	transferSchedule.setCreatedby(rs.getLong(++index));
    	transferSchedule.setUpdated(rs.getTimestamp(++index));
    	transferSchedule.setUpdatedby(rs.getLong(++index));

        return transferSchedule;
    }
}
