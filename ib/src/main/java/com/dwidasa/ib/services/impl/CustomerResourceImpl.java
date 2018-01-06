package com.dwidasa.ib.services.impl;

import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.ExchangeRate;
import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.service.*;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.annotations.SessionValidate;
import com.dwidasa.ib.services.CustomerResource;
import com.dwidasa.interlink.utility.Constant;
import org.apache.tapestry5.ioc.annotations.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 2/8/12
 * Time: 10:18 AM
 */
@PublicPage
public class CustomerResourceImpl implements CustomerResource {
    @Inject
    private CacheManager cacheManager;

    @Inject
    private CustomerDeviceDao customerDeviceDao;


    public CustomerResourceImpl() {
    }

    public String setPushId(Long customerId, Long customerDeviceId, String pushId) {
        int recordAffected = customerDeviceDao.setPushId(customerId, customerDeviceId, pushId);
        
        if (recordAffected > 0)
        	return "{ \"status\" : \"1\" }";
        else
        	return "{ \"status\" : \"0\" }";
    }


}
