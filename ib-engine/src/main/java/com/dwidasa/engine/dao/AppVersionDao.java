package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.AppVersion;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 1/30/12
 * Time: 9:32 AM
 */
public interface AppVersionDao extends GenericDao<AppVersion, Long> {

    /**
     * Get latest version for particular device/platform and version
     * @param deviceType device type indicating device/platform type; eg BlackBerry (10), Android (11), Iphone (12)
     * @param versionId version identification number
     * @return
     */
    public AppVersion getLatestVersion(String deviceType, Long versionId);

}
