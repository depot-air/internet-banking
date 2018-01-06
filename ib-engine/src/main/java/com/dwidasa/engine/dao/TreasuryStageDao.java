package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.TreasuryStage;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:37 PM
 */
public interface TreasuryStageDao extends GenericDao<TreasuryStage, Long> {
    /**
     * Get transaction stage by transaction id.
     * @param transactionId transaction id
     * @return transaction stage
     */
    public TreasuryStage getByTransactionFk(Long transactionId);
    public TreasuryStage get(Long id);
    public void updateOfficerById(Long id, Long officerId);
}
