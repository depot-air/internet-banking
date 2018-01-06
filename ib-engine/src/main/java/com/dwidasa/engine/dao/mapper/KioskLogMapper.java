package com.dwidasa.engine.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import com.dwidasa.engine.model.KioskLog;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Component("kioskLogMapper")
public class KioskLogMapper extends ChainedRowMapper<KioskLog>
        implements ParameterizedRowMapper<KioskLog> {

    public KioskLogMapper() {
    }

    public KioskLog chainRow(ResultSet rs, int index) throws SQLException {
        KioskLog kioskLog = new KioskLog();

        //id, m_kiosk_terminal_id, service_type, service_time, service_response, description
        kioskLog.setId(rs.getLong(++index));
        kioskLog.setmKioskTerminalId(rs.getLong(++index));
        kioskLog.setServiceType(rs.getString(++index));
        kioskLog.setServiceTime(rs.getDate(++index));
        kioskLog.setServiceResponse(rs.getString(++index));
        kioskLog.setDescription(rs.getString(++index));
        
        kioskLog.setCreated(rs.getTimestamp(++index));
        kioskLog.setCreatedby(rs.getLong(++index));
        kioskLog.setUpdated(rs.getTimestamp(++index));
        kioskLog.setUpdatedby(rs.getLong(++index));

        return kioskLog;
    }
}
