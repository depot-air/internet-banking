package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Version;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/6/11
 * Time: 10:51 PM
 */
public interface VersionDao extends GenericDao<Version, Long> {
    /**
     * Get version object by table name
     * @param tableName table name
     * @return version object
     */
    Version get(String tableName);
}
