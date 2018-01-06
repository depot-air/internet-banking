package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.ExchangeRateDao;
import com.dwidasa.engine.model.ExchangeRate;
import com.dwidasa.engine.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/9/11
 * Time: 4:48 PM
 */
@Service("exchangeRateService")
public class ExchangeRateServiceImpl extends GenericServiceImpl<ExchangeRate, Long> implements ExchangeRateService {
    private ExchangeRateDao exchangeRateDao;

    @Autowired
    public ExchangeRateServiceImpl(ExchangeRateDao exchangeRateDao) {
        super(exchangeRateDao);
        this.exchangeRateDao = exchangeRateDao;
    }

    /**
     * {@inheritDoc}
     */
    public List<ExchangeRate> getAllWithCurrency() {
        return exchangeRateDao.getAllWithCurrency();
    }
}
