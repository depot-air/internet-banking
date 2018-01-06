package com.dwidasa.engine.service;

import com.dwidasa.engine.model.AppVersion;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 1/30/12
 * Time: 10:18 AM
 */
public interface AppVersionService extends GenericService<AppVersion, Long> {

    /**
     * Get latest version for particular device/platform and version
     * @param deviceType string represent device/platform type, eg BlackBerry (10), Android (11), Iphone (12)
     * @param versionId version identification number
     * @return app version of the latest version
     */
    public AppVersion getLatestVersion(String deviceType, Long versionId);
}
