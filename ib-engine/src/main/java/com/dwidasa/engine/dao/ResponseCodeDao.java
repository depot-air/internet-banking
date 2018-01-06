package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.ResponseCode;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 8:38 PM
 */
public interface ResponseCodeDao extends GenericDao<ResponseCode, Long> {
    /**
     * Get response code by its natural key which is response code string.
     * @param responseCode response code
     * @return response code object
     */
    ResponseCode get(String responseCode);
}
