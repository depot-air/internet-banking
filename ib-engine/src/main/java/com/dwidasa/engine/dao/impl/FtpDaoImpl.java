package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.FtpDao;
import com.dwidasa.engine.dao.mapper.FtpMapper;
import com.dwidasa.engine.model.Ftp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/14/11
 * Time: 11:20 AM
 */
@Repository("ftpDao")
public class FtpDaoImpl extends GenericDaoImpl<Ftp, Long> implements FtpDao {
    @Autowired
    public FtpDaoImpl(DataSource dataSource, FtpMapper ftpMapper) {
        super("m_ftp", dataSource);
        this.defaultMapper = ftpMapper;

        insertSql = new StringBuilder()
            .append("insert into m_ftp ( ")
            .append("   server_address, server_port, username, password, transfer_mode, local_folder, ")
            .append("   remote_folder, description, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :serverAddress, :serverPort, :username, :password, :transferMode, :localFolder, ")
            .append("   :remoteFolder, :description, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_ftp ")
            .append("set ")
            .append("   server_address = :serverAddress, server_port = :serverPort, username = :username, ")
            .append("   password = :password, transfer_mode = :transferMode, local_folder = :localFolder, ")
            .append("   transfer_mode = :transferMode, local_folder = :localFolder, remote_folder = :remoteFolder, ")
            .append("   description = :description, created = :created, updated = :updated, ")
            .append("   updatedby = :updatedby ")
            .append("where id = :id ");
    }
}
