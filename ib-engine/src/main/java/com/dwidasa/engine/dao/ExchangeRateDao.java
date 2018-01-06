package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.ExchangeRate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/9/11
 * Time: 3:32 PM
 */
public interface ExchangeRateDao extends GenericDao<ExchangeRate, Long> {
    /**
     * Get all exchange rate with currency object eagerly initialized.
     * @return list of exchange rate
     */
    public List<ExchangeRate> getAllWithCurrency();
}
