package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.VersionDao;
import com.dwidasa.engine.model.BaseObject;
import com.dwidasa.engine.model.Version;
import com.dwidasa.engine.service.GenericService;
import com.dwidasa.engine.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/19/11
 * Time: 10:49 AM
 */
@Service("versionService")
public class VersionServiceImpl extends GenericServiceImpl<Version, Long> implements VersionService {
    private VersionDao versionDao;

    @Autowired
    public VersionServiceImpl(VersionDao versionDao) {
        super(versionDao);
        this.versionDao = versionDao;
    }

    /**
     * Deduce underlying table name from provided class name.
     * Only master table is assumed.
     * @param className class name
     * @return table name
     */
    private String deduceTableName(String className) {
        String result = "m";

        for (int i = 0; i < className.length(); i++) {
            if (className.charAt(i) >= 'A' && className.charAt(i) <= 'Z') {
                result += "_" + className.charAt(i);
            }
            else {
                result += className.charAt(i);
            }
        }

        return result.toLowerCase();
    }

    /**
     * Increase version of specified table name
     * @param tableName table name
     */
    private void increaseVersion(String tableName) {
        Version v = versionDao.get(tableName);
        v.setVersion(v.getVersion()+1);
        v.setUpdated(new Date());
        save(v);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public BaseObject versionedSave(BaseObject object, GenericService service) {
        object = service.save(object);
        increaseVersion(deduceTableName(object.getClass().getSimpleName()));

        return object;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void versionedRemove(BaseObject object, GenericService service, Long userId) {
        service.remove(object.getId(), userId);
        increaseVersion(deduceTableName(object.getClass().getSimpleName()));
    }
}
