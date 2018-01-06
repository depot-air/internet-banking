package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.TreasuryStageDao;
import com.dwidasa.engine.model.TreasuryStage;
import com.dwidasa.engine.service.TreasuryStageService;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/26/11
 * Time: 2:45 PM
 */
@Service("treasuryStageService")
public class TreasuryStageServiceImpl extends GenericServiceImpl<TreasuryStage, Long>
        implements TreasuryStageService {

    @Autowired
    public TreasuryStageServiceImpl(TreasuryStageDao treasuryStageDao) {
        super(treasuryStageDao);
    }
}
