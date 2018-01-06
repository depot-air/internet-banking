package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.KioskTerminal;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 10:56 AM
 * To change this template use File | Settings | File Templates.
 */
@Component("kioskTerminalMapper")
public class KioskTerminalMapper extends ChainedRowMapper<KioskTerminal>
        implements ParameterizedRowMapper<KioskTerminal> {

    public KioskTerminalMapper() {
    }

    public KioskTerminal chainRow(ResultSet rs, int index) throws SQLException {
        KioskTerminal kioskTerminal = new KioskTerminal();

        kioskTerminal.setId(rs.getLong(++index));
        kioskTerminal.setTerminalId(rs.getString(++index));
        kioskTerminal.setmLocationIdTerminal(rs.getLong(++index));
        kioskTerminal.setmLocationIdBranch(rs.getLong(++index));
        kioskTerminal.setmUserId(rs.getLong(++index));
        kioskTerminal.setDescription(rs.getString(++index));

        kioskTerminal.setCreated(rs.getTimestamp(++index));
        kioskTerminal.setCreatedby(rs.getLong(++index));
        kioskTerminal.setUpdated(rs.getTimestamp(++index));
        kioskTerminal.setUpdatedby(rs.getLong(++index));

        return kioskTerminal;
    }
}
