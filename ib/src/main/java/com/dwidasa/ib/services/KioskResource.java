package com.dwidasa.ib.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.NoValidate;

import java.util.Date;

/**
 * Created by IB
 * User: IB
 * Date: 2/8/12
 * Time: 10:18 AM
 */
@Path("/kiosk")
@NoCache
public interface KioskResource {

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("customerRegisters")
    public String customerRegisters(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("transactionType") String transactionType);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("incrementPrinter")
    public String incrementPrinter(@FormParam("terminalId") String terminalId, @FormParam("counterType") Integer counterType, @FormParam("incrementValue") Double incrementValue);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("locations")
    public String getLocations();

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("exchangeRates")
    public String getExchangeRates();

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("addRestartLog")
    public String addRestartLog(@QueryParam("terminalId") String terminalId, @QueryParam("restartTime") String restartTime);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("noteCloseApp")
    public String noteCloseApp(@QueryParam("terminalId") String terminalId, @QueryParam("closeTime") String closeTime);
}
