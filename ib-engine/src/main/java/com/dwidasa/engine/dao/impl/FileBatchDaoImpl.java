package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.FileBatchDao;
import com.dwidasa.engine.dao.InboxDao;
import com.dwidasa.engine.dao.mapper.FileBatchMapper;
import com.dwidasa.engine.dao.mapper.InboxMapper;
import com.dwidasa.engine.model.FileBatch;
import com.dwidasa.engine.model.Inbox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 3:28 PM
 */
@Repository("fileBatchDao")
public class FileBatchDaoImpl extends GenericDaoImpl<FileBatch, Long> implements FileBatchDao {
    @Autowired
    public FileBatchDaoImpl(DataSource dataSource, FileBatchMapper inboxMapper) {
        super("t_file_batch", dataSource);
        defaultMapper = inboxMapper;

        insertSql = new StringBuilder()
            .append("insert into t_file_batch ( ")
            .append("   file_name, file_bin, status, upload_date, notif_date, start_date, end_date, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :fileName, :fileBin, :status, :uploadDate, :notifDate, :startDate, :endDate, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update t_file_batch ")
            .append("set ")
            .append("   file_name = :fileName, file_bin = :fileBin, status = :status, upload_date = :uploadDate, notif_date = :notifDate, start_date = :startDate, end_date = :endDate, ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

}
