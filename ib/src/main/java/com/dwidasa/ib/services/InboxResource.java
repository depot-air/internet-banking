package com.dwidasa.ib.services;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.SessionValidate;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 11:30 AM
 */
@Path("/inbox")
@NoCache
public interface InboxResource {
    /**
     * Get customer inbox.
     * @param customerId customer id
     * @param sessionId session id
     * @param lastRequestDate time from last message in list return of previous request.
     * @return list of customer inbox
     * @throws Exception parse exception
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    public String getInbox(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("lastRequestDate") String lastRequestDate) throws Exception;

    /**
     * Mark message as read or so.
     * @param customerId customer id
     * @param sessionId session id
     * @param inboxCustomerId inbox customer id
     * @param status status
     * @return Constants.OK
     */
    @SessionValidate
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mark")
    public String markMessage(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("inboxCustomerId") Long inboxCustomerId,
            @QueryParam("status") Integer status);
}
