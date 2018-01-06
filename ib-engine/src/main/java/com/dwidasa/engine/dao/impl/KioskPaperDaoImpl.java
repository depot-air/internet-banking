package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.KioskPaperDao;
import com.dwidasa.engine.dao.mapper.KioskPaperMapper;
import com.dwidasa.engine.model.KioskPaper;
import com.dwidasa.engine.model.KioskPrinter;
import com.dwidasa.engine.model.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/6/12
 * Time: 1:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("kioskPaperDao")
public class KioskPaperDaoImpl extends GenericDaoImpl<KioskPaper, Long> implements KioskPaperDao {

    @Autowired
    public KioskPaperDaoImpl(DataSource dataSource, KioskPaperMapper kioskPaperMapper) {
        super("m_kiosk_paper", dataSource);
        defaultMapper = kioskPaperMapper;
        insertSql = new StringBuilder()
            .append("insert into m_kiosk_paper ( ")
            .append("  printer_type, description, max_paper, max_tape, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("  :printer_type, :description, :max_paper, :max_tape, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_kiosk_paper ")
            .append("set ")
            .append("   printer_type = :printerType, description = :description, max_paper = :maxPaper, max_tape = :maxTape, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    @Override
    public List<KioskPaper> getByPrinterType(int printerType) {
        String sqlSelect =
    		"select p.* "+
            " from m_kiosk_paper p "+
            " where p.printer_type = ? "+
            " order by p.description";

        List<KioskPaper> result = null;
        result = getSimpleJdbcTemplate().query(sqlSelect, defaultMapper, printerType);
        return result;
    }
}
