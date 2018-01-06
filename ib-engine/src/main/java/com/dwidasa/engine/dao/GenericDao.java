package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.BaseObject;

import java.util.List;

/**
 * Generic DAO (Data Access Object) with common methods to CRUD POJOs.
 *
 * <p>Extend this interface if you want typesafe (no casting necessary) DAO's for your
 * domain objects.
 *
 * @author rk
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public interface GenericDao<T extends BaseObject, PK> {
    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     * @return List of populated objects
     */
    List<T> getAll();

    /**
     * Generic method to get an object based on class and identifier.
     * If no object corresponding to specified PK is exists, null value will be returned
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     */
    T get(PK id);

    /**
     * Checks for existence of an object of type T using the id arg.
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    Boolean exists(PK id);

    /**
     * Generic method to save an object - handles both update and insert.
     * @param object the object to save
     * @return the persisted object
     */
    T save(T object);

    /**
     * Generic method to delete an object based on class and id.
     * @param id the identifier (primary key) of the object to remove
     */
    void remove(PK id, Long userId);

    /**
     * Get current page rows after sort and restriction being applied.
     * @param startIndex starting index from resultset
     * @param pageSize how many rows to be retrieved
     * @param restrictions valid sql predicate
     * @param orders valid order by column
     * @param params bind variable value
     */
    List<T> getCurrentPageRows(int startIndex, int pageSize, List<String> restrictions,
                               List<String> orders, Object... params);

    /**
     * Get total row from resultset after restrictions being applied.
     * @param restrictions valid sql predicate
     * @param params bind variable value
     * @return total rows in resultset
     */
    int getRowCount(List<String> restrictions, Object... params);
    
    /**
     * update field updatedby with userId. 
     * Call this method before deleting record, so the delete trigger can record who delete the record into t_audit_log table
     * @param id field id of record
     * @param userId userId of person who want to delete the record
     */
    public void updateUpdatedBy(PK id, Long userId);

}
