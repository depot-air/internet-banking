package com.dwidasa.engine.dao.impl;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dwidasa.engine.dao.KioskLogDao;
import com.dwidasa.engine.dao.mapper.KioskLogMapper;
import com.dwidasa.engine.model.KioskLog;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("kioskLogDao")
public class KioskLogDaoImpl extends GenericDaoImpl<KioskLog, Long> implements KioskLogDao {

    @Autowired
    public KioskLogDaoImpl(DataSource dataSource, KioskLogMapper kioskLogMapper) {
        super("m_kiosk_printer", dataSource);
        defaultMapper = kioskLogMapper;
        insertSql = new StringBuilder()
            .append("insert into m_kiosk_log ( ")
            .append("   m_kiosk_terminal_id, service_type, service_time, service_response, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :mKioskTerminalId, :serviceType, :serviceTime, :serviceResponse, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_kiosk_log ")
            .append("set ")
            .append("   m_kiosk_terminal_id = :mKioskTerminalId, " +
                    "service_type = :serviceType, " +
                    "service_time = :serviceTime, " +
                    "service_response = :serviceResponse, " +
                    "description = :description,  ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

}
