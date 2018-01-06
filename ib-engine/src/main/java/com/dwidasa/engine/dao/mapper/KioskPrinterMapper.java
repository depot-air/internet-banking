package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.KioskPrinter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 5:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Component("kioskPrinterMapper")
public class KioskPrinterMapper extends ChainedRowMapper<KioskPrinter>
        implements ParameterizedRowMapper<KioskPrinter> {

    public KioskPrinterMapper() {
    }

    public KioskPrinter chainRow(ResultSet rs, int index) throws SQLException {
        KioskPrinter kioskPrinter = new KioskPrinter();

        //location_id, struk_counter, struk_max, a4_counter, a4_max, a4_tinta, a4_tinta_max, sspp_tinta, sspp_tinta_max
        kioskPrinter.setId(rs.getLong(++index));
        kioskPrinter.setmKioskTerminalId(rs.getLong(++index));
        kioskPrinter.setStrukCounter(rs.getDouble(++index));
        kioskPrinter.setStrukMaxId(rs.getLong(++index));
        kioskPrinter.setA4Counter(rs.getLong(++index));
        kioskPrinter.setA4CounterTape(rs.getLong(++index));
        kioskPrinter.setA4MaxId(rs.getLong(++index));
        kioskPrinter.setSsppCounterTape(rs.getLong(++index));
        kioskPrinter.setSsppMaxId(rs.getLong(++index));

        kioskPrinter.setCreated(rs.getTimestamp(++index));
        kioskPrinter.setCreatedby(rs.getLong(++index));
        kioskPrinter.setUpdated(rs.getTimestamp(++index));
        kioskPrinter.setUpdatedby(rs.getLong(++index));

        return kioskPrinter;
    }
}
