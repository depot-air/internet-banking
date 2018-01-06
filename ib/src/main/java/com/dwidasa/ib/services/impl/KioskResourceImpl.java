package com.dwidasa.ib.services.impl;

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
import com.dwidasa.ib.services.KioskResource;
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
public class KioskResourceImpl implements KioskResource {
    @Inject
    private CacheManager cacheManager;

    @Inject
    private CustomerRegisterService customerRegisterService;

    @Inject
    private LocationService locationService;

    @Inject
    private ExchangeRateService exchangeRateService;

    @Inject
    private KioskPrinterService kioskPrinterService;

    public KioskResourceImpl() {
    }

    public String customerRegisters(Long customerId, String sessionId, String transactionType) {
        List<CustomerRegister> customerRegisters = customerRegisterService.getByTransactionType(customerId, transactionType);

        return PojoJsonMapper.toJson(customerRegisters);
    }

    public String incrementPrinter(String terminalId, Integer counterType, Double incrementValue) {
        kioskPrinterService.incrementPrinter(terminalId, counterType, incrementValue);
        return Constants.OK;
    }

    public String getLocations() {
        List<Location> locations = locationService.getAll();
        return PojoJsonMapper.toJson(locations);
    }

    public String getExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateService.getAllWithCurrency();
        return PojoJsonMapper.toJson(exchangeRates);
    }

    @Override
    public String addRestartLog(String terminalId, String restartTime) {
        return null;
    }

    @Override
    public String noteCloseApp(String terminalId, String closeTime) {
        return null;
    }
}
