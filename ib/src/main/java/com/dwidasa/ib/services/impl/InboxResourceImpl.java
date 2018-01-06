package com.dwidasa.ib.services.impl;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.InboxCustomer;
import com.dwidasa.engine.service.InboxService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.InboxResource;
import org.apache.tapestry5.ioc.annotations.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 11:30 AM
 */
@PublicPage
public class InboxResourceImpl implements InboxResource {
    @Inject
    private InboxService inboxService;

    public InboxResourceImpl() {
    }

    public String getInbox(Long customerId, String sessionId, String lastRequestDate) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        List<InboxCustomer> ibs = inboxService.getCustomerInboxes(customerId,
                lastRequestDate.equals("null") ? null : sdf.parse(lastRequestDate));
        return PojoJsonMapper.toJson(ibs);
    }

    
    public String markMessage(Long customerId, String sessionId, Long inboxCustomerId, Integer status) {

        inboxService.updateStatusInboxCustomer(inboxCustomerId, customerId, status);
        return Constants.OK;
    }
}
