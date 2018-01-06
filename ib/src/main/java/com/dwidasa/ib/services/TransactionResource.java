package com.dwidasa.ib.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.SessionValidate;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/15/11
 * Time: 6:19 PM
 */
@Path("/transaction")
@NoCache
public interface TransactionResource {
    /**
     * Retrieve stored receipt of a transaction.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return pln payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("receipt")
    public String receipt(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
}
