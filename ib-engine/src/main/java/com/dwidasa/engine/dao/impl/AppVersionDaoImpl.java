package com.dwidasa.engine.dao.impl;

import com.dwidasa.engine.dao.AppVersionDao;
import com.dwidasa.engine.dao.mapper.AppVersionMapper;
import com.dwidasa.engine.model.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 1/30/12
 * Time: 9:44 AM
 */
@Repository("appVersionDao")
public class AppVersionDaoImpl extends GenericDaoImpl<AppVersion, Long> implements AppVersionDao {

     @Autowired
    public AppVersionDaoImpl(DataSource dataSource, AppVersionMapper appVersionMapper) {
        super("m_app_version", dataSource);
        defaultMapper = appVersionMapper;

        insertSql = new StringBuilder()
            .append("insert into m_app_version ( ")
            .append("   device_type, version_id, version, mandatory, changes, url, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :deviceType, :versionId, :version, :mandatory, :changes, :url, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_app_version ")
            .append("set ")
            .append("   device_type = :deviceType, version_id = :versionId, version = :version,  ")
            .append("   mandatory = :mandatory, changes = :changes, url = :url,  ")
            .append("   created = :created, createdby = :createdby, updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     *  {@inheritDoc}
     */
    public AppVersion getLatestVersion(String deviceType, Long versionId) {

        StringBuilder sql = new StringBuilder()
                .append("select mav.* ")
                .append("from m_app_version mav ")
                .append("where mav.device_type = ? and mav.version_id > ? ")
                .append("order by version_id desc ");

        List<AppVersion> appVersions = getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, deviceType, versionId);

        AppVersion result = new AppVersion();
        result.setMandatory(0);
        result.setListOfChanges(new ArrayList<String>());

        if (appVersions != null){
            for (int i = 0; i < appVersions.size(); i++){
                AppVersion appVersion = appVersions.get(i);
                if (i == 0){
                    // latest version
                    result.setId(appVersion.getId());
                    result.setVersionId(appVersion.getVersionId());
                    result.setVersion(appVersion.getVersion());
                    result.setUrl(appVersion.getUrl());
                }

                // check version mandatory
                if (appVersion.getMandatory() > 0) {
                    result.setMandatory(1);
                }

                // populate changes;
                result.getListOfChanges().addAll(parseChanges(appVersion.getChanges()));
            }
        }

        return result;
    }


    /**
     * Parse semicolon separated version changes string into list of strings
     * @param changes string of version changes
     * @return list of version changes
     */
    private List<String> parseChanges(String changes){

        List<String> result = new ArrayList<String>();
        if (changes != null){
            StringTokenizer str = new StringTokenizer(changes, ";");

            while (str.hasMoreTokens()){
                result.add(str.nextToken());
            }
        }
        return result;
    }

}
