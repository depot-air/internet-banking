package com.dwidasa.ib.services;

import com.dwidasa.ib.annotations.SessionValidate;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by IntelliJ IDEA.
 * User: IB
 * Date: 2/25/12
 * Time: 11:19 AM
 */
@Path("/batch")
@NoCache
public interface BatchResource {
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("batchesPost")
    public String batchesPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("saveBatchPost")
    public String saveBatchPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("removeBatchPost")
    public String removeBatchPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("batchId") Long batchId);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("batchContentsPost")
    public String batchContentsPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("batchId") Long batchId);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("saveBatchContentPost")
    public String saveBatchContentPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId,
            @FormParam("token") String token, @FormParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("removeBatchContentPost")
    public String removeBatchContentPost(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("batchContentId") Long batchContentId);
}
