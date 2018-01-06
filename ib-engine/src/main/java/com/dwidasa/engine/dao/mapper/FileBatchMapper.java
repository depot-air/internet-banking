package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.FileBatch;
import com.dwidasa.engine.model.Inbox;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 3:29 PM
 */
@Component("fileBatchMapper")
public class FileBatchMapper extends ChainedRowMapper<FileBatch>
        implements ParameterizedRowMapper<FileBatch> {

    public FileBatchMapper() {
    }

    public FileBatch chainRow(ResultSet rs, int index) throws SQLException {
    	FileBatch fileBatch = new FileBatch();

    	fileBatch.setId(rs.getLong(++index));
    	fileBatch.setFileName(rs.getString(++index));
    	fileBatch.setFileBin(rs.getString(++index));
    	fileBatch.setStatus(rs.getInt(++index));
    	fileBatch.setUploadDate(rs.getTimestamp(++index));
    	fileBatch.setNotifDate(rs.getTimestamp(++index));
    	fileBatch.setStartDate(rs.getTimestamp(++index));
    	fileBatch.setEndDate(rs.getTimestamp(++index));
    	fileBatch.setCreated(rs.getTimestamp(++index));
    	fileBatch.setCreatedby(rs.getLong(++index));
    	fileBatch.setUpdated(rs.getTimestamp(++index));
    	fileBatch.setUpdatedby(rs.getLong(++index));

        return fileBatch;
    }
}
