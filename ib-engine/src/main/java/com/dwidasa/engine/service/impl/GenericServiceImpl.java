package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.GenericDao;
import com.dwidasa.engine.model.BaseObject;
import com.dwidasa.engine.service.GenericService;

import java.util.List;

/**
 * This class serves as the Base class for all other Services - namely to hold
 * common CRUD methods that they might all use.
 *
 * @author rk
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericServiceImpl<T extends BaseObject, PK> implements GenericService<T, PK> {
    protected GenericDao<T, PK> dao;

    public GenericServiceImpl(GenericDao<T, PK> dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getAll() {
        return dao.getAll();
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        return dao.get(id);
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(PK id) {
        return dao.exists(id);
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return dao.save(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id, Long userId) {
        dao.remove(id, userId);
    }

    /**
     * {@inheritDoc}
     */
    public List<T> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                                      List<String> orders, Object... params) {
        return dao.getCurrentPageRows(startIndex, pageSize, restrictions, orders, params);
    }

    /**
     * {@inheritDoc}
     */
    public int getRowCount(List<String> restrictions, Object... params) {
        return dao.getRowCount(restrictions, params);
    }
}
