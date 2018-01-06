package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Currency;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/24/11
 * Time: 4:22 PM
 */
public interface CurrencyDao extends GenericDao<Currency, Long> {
    /**
     * Get currency by swift code.
     * @param swiftCode swift code
     * @return currency object
     */
    public Currency get(String swiftCode);
}
