package com.dwidasa.engine.service;

import com.dwidasa.engine.model.IsoBitmap;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/23/11
 * Time: 6:12 PM
 */
public interface IsoBitmapService {
    /**
     * Get all of isoBitmap with relation to transaction type already intialized
     * @return list of transactionType
     */
    List<IsoBitmap> getAllWithTransactionType();
    IsoBitmap getByTransactionType(String transactionType);
}
