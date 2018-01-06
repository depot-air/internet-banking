package com.dwidasa.engine.service;

import com.dwidasa.engine.model.ExchangeRate;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/9/11
 * Time: 4:47 PM
 */
public interface ExchangeRateService extends GenericService<ExchangeRate, Long> {
    /**
     * Get all exchange rate with currency object eagerly initialized.
     * @return list of exchange rate
     */
    public List<ExchangeRate> getAllWithCurrency();
}
