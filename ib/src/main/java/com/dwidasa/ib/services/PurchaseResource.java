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

import com.dwidasa.ib.annotations.Authenticate2;
import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.SessionValidate;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/8/11
 * Time: 3:44 PM
 */
@Path("/purchase")
@NoCache
public interface PurchaseResource {
    /**
     * Save cellular purchase resource.
     * Example of usage : '/ib/rest/topup?customerId=1&deviceId=2&sessionId=3&token=4&json=[]'
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json json
     * @return voucher purchase view
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("topup")
    public String saveCellular(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("topup2")
    public String saveCellular2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("topupPost")
	public String saveCellularPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);
	@NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("topupPostPin")
    public String saveCellularPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Reprint cellular purchase.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return voucher purchase view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintTopup")
    public String reprintCellular(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkTopupPost")
    public String checkTopupPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Inquiry PLN Prepaid.
     * @param customerId customer id
     * @param sessionId session id
     * @param json PLN purchase view object
     * @return PLN purchase view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryPln")
    public String inquiryPln(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryPlnPost")
    public String inquiryPlnPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint PLN Prepaid.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return PLN purchase view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintPln")
    public String reprintPln(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintPlnPost")
    public String reprintPlnPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);
    /**
     * PLN purchase posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json PLN purchase view object
     * @return Constants.OK
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pln")
    public String savePln(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pln2")
    public String savePln2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("plnPost")
    public String savePlnPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("plnPostPin")
    public String savePlnPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Getting customer registered list specific to a transaction.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionType transaction type
     * @param billerCode biller code
     * @return list of customer register
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registers")
    public String getRegisteredPurchase(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionType") String transactionType,
            @QueryParam("billerCode") String billerCode);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registersPost")
    public String getRegisteredPurchasePost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("transactionType") String transactionType,
            @FormParam("billerCode") String billerCode);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("customerRegisters")
    public String getCustomerRegisters(@QueryParam("customerId") Long customerId,
    		@QueryParam("sessionId") String sessionId,
    		@QueryParam("transactionType") String transactionType);


    /**
     * Inquiry PLN Prepaid.
     * @param customerId customer id
     * @param sessionId session id
     * @param json PLN purchase view object
     * @return PLN purchase view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryVoucherGame")
    public String inquiryVoucherGame(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    /**
     * Save cellular purchase resource.
     * Example of usage : '/ib/rest/topup?customerId=1&deviceId=2&sessionId=3&token=4&json=[]'
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json json
     * @return voucher purchase view
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voucherGame")
    public String voucherGame(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voucherGame2")
    public String voucherGame2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voucherGamePost")
	public String voucherGamePost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);
	@NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voucherGamePostPin")
    public String voucherGamePostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Reprint cellular purchase.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return voucher purchase view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkStatusVoucherGame")
    public String checkStatusVoucherGame(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkStatusVoucherGamePost")
    public String checkStatusVoucherGamePost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

}
