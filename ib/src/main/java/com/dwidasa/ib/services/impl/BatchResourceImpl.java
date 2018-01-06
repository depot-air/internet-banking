package com.dwidasa.ib.services.impl;


import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Batch;
import com.dwidasa.engine.model.BatchContent;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.*;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.BatchResource;
import org.apache.tapestry5.ioc.annotations.Inject;

import javax.ws.rs.FormParam;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 2/25/12
 * Time: 11:24 AM
 */
@PublicPage
public class BatchResourceImpl implements BatchResource {
    @Inject
    private CacheManager cacheManager;

    @Inject
    private BatchService batchService;

    @Inject
    private BatchContentService batchContentService;

    public BatchResourceImpl() {
    }

    public String batchesPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId) {
        List<Batch> batches = batchService.getAll(customerId);
        return PojoJsonMapper.toJson(batches);
    }

    public String saveBatchPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("json") String json) {
        Batch batch = PojoJsonMapper.fromJson(json, Batch.class);
        //jika id = 0 maka = null, karena json dibaca 0
        if (batch.getId() == 0) batch.setId(null);
        batchService.save(batch);
        return PojoJsonMapper.toJson(batch);
    }

    public String removeBatchPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("batchId") Long batchId) {
        batchService.remove(batchId, customerId);
        //remove all batchContents by batch Id
        batchContentService.removeAll(batchId);
        return Constants.OK;
    }

    public String batchContentsPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("batchId") Long batchId) {
        List<BatchContent> batchContents = batchContentService.getAll(batchId);
        return PojoJsonMapper.toJson(batchContents);
    }

    public String saveBatchContentPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("token") String token, @FormParam("json") String json) {
        BatchContent batchContent = PojoJsonMapper.fromJson(json, BatchContent.class);
        //jika id = 0 maka = null, karena json dibaca 0
        if (batchContent.getId() == 0) batchContent.setId(null);
        batchContentService.save(batchContent);
        return PojoJsonMapper.toJson(batchContent);
    }

    public String removeBatchContentPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("batchContentId") Long batchContentId) {
        batchContentService.remove(batchContentId, customerId);
        return Constants.OK;
    }
}
