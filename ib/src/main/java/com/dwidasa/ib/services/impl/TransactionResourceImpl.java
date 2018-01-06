package com.dwidasa.ib.services.impl;

import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.TransactionResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/15/11
 * Time: 6:19 PM
 */
@PublicPage
public class TransactionResourceImpl implements TransactionResource {
    @Inject
    private TransactionDataService transactionDataService;

    public TransactionResourceImpl() {
    }

    public String receipt(Long customerId, String sessionId, Long transactionId) {
        BaseView view = (BaseView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));

        return PojoJsonMapper.toJson(view);
    }
}
