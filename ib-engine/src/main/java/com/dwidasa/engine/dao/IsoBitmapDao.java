package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.IsoBitmap;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/30/11
 * Time: 10:24 AM
 */
public interface IsoBitmapDao extends GenericDao<IsoBitmap, Long> {
    /**
     * Get all of isoBitmap with relation to transaction type already intialized
     * @return list of transactionType
     */
    public List<IsoBitmap> getAllWithTransactionType();
    
    public IsoBitmap getByTransactionType(String transactionType);
}
