package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.KioskTerminalDao;
import com.dwidasa.engine.dao.mapper.KioskTerminalMapper;
import com.dwidasa.engine.model.KioskTerminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 10:54 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("kioskTerminalDao")
public class KioskTerminalDaoImpl extends GenericDaoImpl<KioskTerminal, Long> implements KioskTerminalDao {

    @Autowired
    public KioskTerminalDaoImpl(DataSource dataSource, KioskTerminalMapper kioskTerminalMapper) {
        super("m_kiosk_terminal", dataSource);
        defaultMapper = kioskTerminalMapper;
        insertSql = new StringBuilder()
            .append("insert into m_kiosk_terminal ( ")
            .append("   terminal_id, m_location_id_terminal, m_location_id_branch, m_user_id, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :terminalId, :mLocationIdTerminal;\n" +
                    "    private Long mLocationIdBranch;\n" +
                    "    private Long mUserId, :mLocationIdBranch, mUserId, :userIdAdmin, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_kiosk_terminal ")
            .append("set ")
            .append("   terminal_id = :terminalId, " +
                    "m_location_id_terminal = :locationIdTerminal, " +
                    "m_location_id_branch = :locationIdBranch, " +
                    "m_user_id = :userIdAdmin, " +
                    "description = :description, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    public KioskTerminal getByTerminalId(String terminalId) {
        StringBuilder sql = new StringBuilder()
                .append("select t.* ")
                .append("from m_kiosk_terminal t ")
                .append("where t.terminal_id = ? ");
        return getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, terminalId);
    }
}
