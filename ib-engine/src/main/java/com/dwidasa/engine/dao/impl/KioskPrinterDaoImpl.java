package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CurrencyDao;
import com.dwidasa.engine.dao.KioskPrinterDao;
import com.dwidasa.engine.dao.mapper.ChainedRowMapper;
import com.dwidasa.engine.dao.mapper.CurrencyMapper;
import com.dwidasa.engine.dao.mapper.KioskPrinterMapper;
import com.dwidasa.engine.dao.mapper.LocationMapper;
import com.dwidasa.engine.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("kioskPrinterDao")
public class KioskPrinterDaoImpl extends GenericDaoImpl<KioskPrinter, Long> implements KioskPrinterDao {

    @Autowired
    public KioskPrinterDaoImpl(DataSource dataSource, KioskPrinterMapper kioskPrinterMapper) {
        super("m_kiosk_printer", dataSource);
        defaultMapper = kioskPrinterMapper;
        insertSql = new StringBuilder()
            .append("insert into m_kiosk_printer ( ")
            .append("   m_kiosk_terminal_id, struk_counter, struk_max_id, a4_counter, a4_counter_tape, a4_max_id, sspp_counter_tape, sspp_max_id, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :locationId, :strukCounter, :strukMaxId, :a4Counter, :a4CounterTape, :a4MaxId, :ssppCounterTape, :ssppMaxId, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_kiosk_printer ")
            .append("set ")
            .append("   m_kiosk_terminal_id = :locationId, " +
                    "struk_counter = :strukCounter, " +
                    "struk_max_id = :strukMaxId, " +
                    "a4_counter = :a4Counter, " +
                    "a4_counter_tape = :a4CounterTape, " +
                    "a4_max_id = :a4MaxId, " +
                    "sspp_counter_tape = :ssppCounterTape, " +
                    "sspp_max_id = :ssppMaxId, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    @Override
    public KioskPrinter getByKioskTerminalId(Long kioskTerminalId) {
        StringBuilder sql = new StringBuilder()
                .append("select p.* ")
                .append("from m_kiosk_printer p ")
                .append("where p.m_kiosk_terminal_id = ? ");
        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, kioskTerminalId);
    }

    public Boolean incrementPrinting(Long terminalId, Integer counterType, Double incr) {
        KioskPrinter kioskPrinter = getByKioskTerminalId(terminalId);

        String qUpdate = "update m_kiosk_printer ";
        String qSet = "set ";
        String qWhere = " where m_kiosk_terminal_id = ? ";
        if (counterType.intValue() == KioskPaper.COUNTER_TYPE.STRUK.intValue()) {
            qSet += "struk_counter = " + (kioskPrinter.getStrukCounter() + incr) + " ";
        } else if (counterType.intValue() == KioskPaper.COUNTER_TYPE.MUTASI_STRUK.intValue()) {
            qSet += "a4_counter = " + (kioskPrinter.getA4Counter() + incr) + " ";
        } else if (counterType.intValue() == KioskPaper.COUNTER_TYPE.MUTASI_PITA.intValue()) {
            qSet += "a4_counter_tape = " + (kioskPrinter.getA4CounterTape() + incr) + " ";
        } else {
            qSet += "sspp_counter_tape = " + (kioskPrinter.getSsppCounterTape() + incr) + " ";
        }
        String sqlUpdate = qUpdate + qSet + qWhere;
        getSimpleJdbcTemplate().update(sqlUpdate, terminalId);
        return true;
    }
}
