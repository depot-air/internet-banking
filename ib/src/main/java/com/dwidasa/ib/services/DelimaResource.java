package com.dwidasa.ib.services;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.dwidasa.ib.annotations.NoValidate;
import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.SessionValidate;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/1/11
 * Time: 3:29 PM
 */
@Path("/delima")
@NoCache
public interface DelimaResource {
    /**
     * Posting cash in.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json cash in delima view object
     * @return cash in delima view object
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cashIn")
    public String saveCashIn(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cashInPost")
    public String saveCashInPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cashInPostPin")
    public String saveCashInPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    /**
     * Inquiry delima cash in
     * @param customerId customer id
     * @param sessionId session id
     * @param json cash in delima view object
     * @return cash in delima view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryCashIn")
    public String inquiryCashIn(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryCashInPost")
    public String inquiryCashInPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint cash in
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return delima cash in view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintCashIn")
    public String reprintCashIn(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintCashInPost")
    public String reprintCashInPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Status delima cash in
     * @param customerId customer id
     * @param sessionId session id
     * @param json cash in delima view object
     * @return cash in delima view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("statusCashIn")
    public String statusCashIn(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("statusCashInPost")
    public String statusCashInPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Posting cash out.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json cash out delima view object
     * @return cash out delima view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cashOut")
    public String saveCashOut(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    /**
     * Inquiry delima cash out
     * @param customerId customer id
     * @param sessionId session id
     * @param json cash out delima view object
     * @return cash out delima view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryCashOut")
    public String inquiryCashOut(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    /**
     * Reprint delima cash out
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return cash out delima view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintCashOut")
    public String reprintCashOut(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);

    /**
     * Status delima cash out
     * @param customerId customer id
     * @param sessionId session id
     * @param json delima cash IN view object
     * @return delima cash IN view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("statusCashOut")
    public String statusCashOut(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    /**
     * Posting refund.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json refund delima view object
     * @return refund delima view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("refund")
    public String saveRefund(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    /**
     * Inquiry delima refund.
     * @param customerId customer id
     * @param sessionId session id
     * @param json refund delima view object
     * @return refund delima view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryRefund")
    public String inquiryRefund(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    /**
     * Reprint delima refund.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return refund delima view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintRefund")
    public String reprintRefund(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);

    /**
     * Status delima refund
     * @param customerId customer id
     * @param sessionId session id
     * @param json delima cash IN view object
     * @return delima cash IN view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("statusRefund")
    public String statusRefund(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
}
