package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.ProviderDenominationDao;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.service.ProviderDenominationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/20/11
 * Time: 10:14 PM
 */
@Service("providerDenominationService")
public class ProviderDenominationServiceImpl extends GenericServiceImpl<ProviderDenomination, Long>
        implements ProviderDenominationService {
    @Autowired
    public ProviderDenominationServiceImpl(ProviderDenominationDao providerDenominationDao) {
        super(providerDenominationDao);
    }
}
