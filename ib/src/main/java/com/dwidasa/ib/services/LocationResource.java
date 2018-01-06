package com.dwidasa.ib.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.SessionValidate;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 11:38 AM
 */
@Path("/location")
@NoCache
public interface LocationResource {
    /**
     * Get nearest location based on user provided location.
     * @param customerId customer id
     * @param sessionId session id
     * @param locationTypeId location type id
     * @param distance distance
     * @param longitude longitude
     * @param latitude latitude
     * @param size size of list
     * @return list of location
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nearest")
    public String getLocations(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("locationTypeId") Long locationTypeId,
            @QueryParam("distance") String distance,
            @QueryParam("longitude") String longitude,
            @QueryParam("latitude") String latitude,
            @QueryParam("size") Integer size);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nearest2")
    public String getLocations(
            @QueryParam("locationTypeId") Long locationTypeId,
            @QueryParam("distance") String distance,
            @QueryParam("longitude") String longitude,
            @QueryParam("latitude") String latitude,
            @QueryParam("size") Integer size);

    /**
     * Get location based on provided criteria.
     * @param customerId customer id
     * @param sessionId session id
     * @param locationTypeId location type id
     * @param searchCriteria search criteria
     * @param size size of list
     * @return list of location
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("criteria")
    public String getLocations(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("locationTypeId") Long locationTypeId,
            @QueryParam("searchCriteria") String searchCriteria,
            @QueryParam("size") Integer size);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("criteria2")
    public String getLocations(
            @QueryParam("locationTypeId") Long locationTypeId,
            @QueryParam("searchCriteria") String searchCriteria,
            @QueryParam("size") Integer size);
}
