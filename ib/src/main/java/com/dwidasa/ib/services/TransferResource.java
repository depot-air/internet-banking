package com.dwidasa.ib.services;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.dwidasa.ib.annotations.Authenticate2;
import com.dwidasa.ib.annotations.NoValidate;
import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.SessionValidate;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 2:12 PM
 */
@Path("/transfer")
@NoCache
public interface TransferResource {
    /**
     * Transfer between customer account.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json transfer view object
     * @return Constants.OK
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transfer")
    public String saveTransfer(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transfer2")
    public String saveTransfer2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transferPost")
    public String saveTransferPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transferPostPin")
    public String saveTransferPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transferBatch")
    public String saveTransferBatch(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transferBatch2")
    public String saveTransferBatch2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
	
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transferBatchPost")
    public String saveTransferBatchPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transferPostBatchPin")
    public String saveTransferBatchPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Register an account to customer list
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json json
     * @return Constants.OK
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register")
    public String register(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("register2")
    public String register2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registerPost")
    public String registerPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registerPostPin")
    public String registerPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Unregister an account to customer list.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param customerRegisterId customer register id
     * @return Constants.OK
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("unregister")
    public String unregister(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("customerRegisterId") Long customerRegisterId);

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("unregisterPost")
    public String unregisterPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("customerRegisterId") Long customerRegisterId);

    @NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("unregisterPostPin")	
    public String unregisterPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("customerRegisterId") Long customerRegisterId);

    @SessionValidate
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("unregister")
    public String unregisterGet(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("customerRegisterId") Long customerRegisterId);

    @SessionValidate
	@GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("unreg")
    public String unregisterJson(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("customerRegisterId") Long customerRegisterId);

    /**
     * Get registered customer list.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionType transaction type
     * @param billerCode biller code
     * @return list of customer regiter
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registers")
    public String getRegisteredTransfer(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionType") String transactionType,
            @QueryParam("billerCode") String billerCode);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registersPost")
    public String getRegisteredTransferPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("transactionType") String transactionType,
            @FormParam("billerCode") String billerCode);

    /**
     * Inquiry transfer.
     * @param customerId customer id
     * @param sessionId session id
     * @param json transfer view object
     * @return transfer view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiry")
    public String inquiryTransfer(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryPost")
    public String inquiryTransferPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryBatch")
    public String inquiryTransferBatch(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryBathcPost")
    public String inquiryTransferBatchPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkStatusTransfer")
    public String checkStatusTransfer(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkStatusTransferPost")
    public String checkStatusTransferPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

}
