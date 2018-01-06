package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.IReportData;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:22 PM
 */
@Component("iReportDataMapper")
public class IReportDataMapper extends ChainedRowMapper<IReportData> implements ParameterizedRowMapper<IReportData> {
    public IReportDataMapper() {
    }

    public IReportData chainRow(ResultSet rs, int index) throws SQLException {
        IReportData iReportData = new IReportData();

        iReportData.setId(rs.getLong(++index));
        iReportData.setCustomerId(rs.getLong(++index));
        iReportData.setTransactionDate(rs.getDate(++index));
        iReportData.setReferenceKey(rs.getString(++index));
        iReportData.setReportType(rs.getString(++index));
        iReportData.setData1(rs.getString(++index));
        iReportData.setData2(rs.getString(++index));
        iReportData.setData3(rs.getString(++index));
        iReportData.setData4(rs.getString(++index));
        iReportData.setData5(rs.getString(++index));
        iReportData.setData6(rs.getString(++index));
        iReportData.setData7(rs.getString(++index));
        
        iReportData.setCreated(rs.getTimestamp(++index));
        iReportData.setCreatedby(rs.getLong(++index));
        iReportData.setUpdated(rs.getTimestamp(++index));
        iReportData.setUpdatedby(rs.getLong(++index));
        return iReportData;
    }
}
