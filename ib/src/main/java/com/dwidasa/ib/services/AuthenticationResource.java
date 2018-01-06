package com.dwidasa.ib.services;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.NoValidate;

/**
 * Interface that responsible for native device authentication.
 *
 * @author rk
 */
@Path("/authentication")
@NoCache
public interface AuthenticationResource {
    /**
     * Authenticate customer using provided token value.
     * @param customerId customer id
     * @param deviceId device id
     * @param token token
     * @return customer data when authenticated
     */
    @NoValidate
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("authenticate")
    public String authenticateCustomer(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("token") String token);
    
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("authenticatePost")
    public String authenticateCustomerPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("token") String token);

    /**
     * Request challenge being used to genarate token.
     * @param customerId customer id
     * @param deviceId device id
     * @return challenge
     */
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("challenge")
    public String requestChallenge(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId);
    
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("challengePost")
    public String requestChallengePost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId);

    /**
     * Authenticate customer credentials, called when customer login using username and pin.
     * This resource is restricted only to kiosk server.
     * @param username customer username
     * @param tin pin / password of customer
     * @return customer view object
     */
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("kioskAuth")
    public String authenticateCustomer(@QueryParam("username") String username,
            @QueryParam("tin") String tin);
    
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("kioskAuthPost")
    public String authenticateCustomerPost(@FormParam("username") String username,
            @FormParam("tin") String tin);

    /**
     * Single round trip authentication
     * @param customerId customer id
     * @param deviceId device id
     * @param token token
     * @return customer data when authenticated
     */
    @NoValidate
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("authenticate2")
    public String authenticateCustomer2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("token") String token,
            @QueryParam("challenge") String challenge,
            @QueryParam("deviceType") String deviceType,
            @QueryParam("versionId") Long versionId);
}
