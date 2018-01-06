package com.dwidasa.engine.service;

import com.dwidasa.engine.dao.CurrencyDao;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.service.impl.GenericServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:59 PM
 */
@Service("currencyService")
public class CurrencyServiceImpl extends GenericServiceImpl<Currency, Long> implements CurrencyService {
    @Autowired
    public CurrencyServiceImpl(CurrencyDao currencyDao) {
        super(currencyDao);
    }
}
