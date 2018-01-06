package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.AppVersionDao;
import com.dwidasa.engine.model.AppVersion;
import com.dwidasa.engine.service.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 1/30/12
 * Time: 10:22 AM
 */
@Service("appVersionService")
public class AppVersionServiceImpl extends GenericServiceImpl<AppVersion, Long> implements AppVersionService {

    private AppVersionDao appVersionDao;

    @Autowired
    public AppVersionServiceImpl(AppVersionDao appVersionDao) {
        super(appVersionDao);
        this.appVersionDao = appVersionDao;
    }

    @Override
    public AppVersion getLatestVersion(String deviceType, Long versionId) {
        return appVersionDao.getLatestVersion(deviceType, versionId);
    }
}
