package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.KioskPaper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/6/12
 * Time: 1:52 PM
 */
@Component("kioskPaperMapper")
public class KioskPaperMapper extends ChainedRowMapper<KioskPaper>
        implements ParameterizedRowMapper<KioskPaper> {

    public KioskPaperMapper() {
    }

    public KioskPaper chainRow(ResultSet rs, int index) throws SQLException {
        KioskPaper kioskPaper = new KioskPaper();

        kioskPaper.setId(rs.getLong(++index));
        kioskPaper.setPrinterType(rs.getInt(++index));
        kioskPaper.setDescription(rs.getString(++index));
        kioskPaper.setMaxPaper(rs.getLong(++index));
        kioskPaper.setMaxTape(rs.getLong(++index));

        kioskPaper.setCreated(rs.getTimestamp(++index));
        kioskPaper.setCreatedby(rs.getLong(++index));
        kioskPaper.setUpdated(rs.getTimestamp(++index));
        kioskPaper.setUpdatedby(rs.getLong(++index));

        return kioskPaper;
    }

}
