package com.dwidasa.engine.service;

import com.dwidasa.engine.model.BaseObject;
import com.dwidasa.engine.model.Version;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/19/11
 * Time: 10:49 AM
 */
public interface VersionService extends GenericService<Version, Long> {
    /**
     * Saving object with this method will increase version number of underlying table
     * stored in version table.
     * @param object the object to save
     * @param service service of specified object
     * @return the persisted object
     */
    @SuppressWarnings("rawtypes")
	BaseObject versionedSave(BaseObject object, GenericService service);

    /**
     * Remove object with this method will increase version number of underlying table
     * stored in version table.
     * @param object the object to remove
     * @param service service of specified object
     */
    @SuppressWarnings("rawtypes")
	void versionedRemove(BaseObject object, GenericService service, Long userId);
}
