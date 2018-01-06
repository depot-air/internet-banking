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
 * Time: 11:55 AM
 */
@Path("/payment")
@NoCache
public interface PaymentResource {
    /**
     * PLN payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json pln payment view object
     * @return pln payment view object
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pln")
    public String savePLN(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pln2")
    public String savePLN2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);


	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("plnPost")
    public String savePLNPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
	@POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("plnPostPin")
    public String savePLNPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry PLN.
     * @param customerId customer id
     * @param sessionId session id
     * @param json pln payment view
     * @return pln payment view
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
     * Reprint PLN.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return pln payment view
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
     * CC payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json cc payment view object
     * @return cc payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cc")
    public String saveCc(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ccPost")
    public String saveCcPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("ccPostPin")
    public String saveCcPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry CC.
     * @param customerId customer id
     * @param sessionId session id
     * @param json cc payment view object
     * @return cc payment view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryCc")
    public String inquiryCc(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryCcPost")
    public String inquiryCcPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
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
    public String getRegisteredPayment(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionType") String transactionType,
            @QueryParam("billerCode") String billerCode);

    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registersPost")
    public String getRegisteredPaymentPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("transactionType") String transactionType,
            @FormParam("billerCode") String billerCode);

    /**
     * HP payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json hp payment view object
     * @return hp payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hp")
    public String saveHp(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hp2")
    public String saveHp2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryHpPost")
    public String inquiryHpPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);


    /**
     * Inquiry HP Payment.
     * @param customerId customer id
     * @param sessionId session id
     * @param json hp payment view object
     * @return hp payment view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryHp")
    public String inquiryHp(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintHpPost")
    public String reprintHpPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint HP Payment.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return hp payment view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintHp")
    public String reprintHp(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hpPost")
    public String saveHpPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("hpPostPin")
    public String saveHpPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    
    /**
     * Telkom payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json telkom payment view object
     * @return telkom payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("telkom")
    public String saveTelkom(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("telkom2")
    public String saveTelkom2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("telkomPost")
    public String saveTelkomPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("telkomPostPin")
    public String saveTelkomPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry Telkom.
     * @param customerId customer id
     * @param sessionId session id
     * @param json telkom payment view
     * @return telkom payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryTelkom")
    public String inquiryTelkom(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryTelkomPost")
    public String inquiryTelkomPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint Telkom.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return telkom payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintTelkom")
    public String reprintTelkom(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintTelkomPost")
    public String reprintTelkomPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Train payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json train payment view object
     * @return train payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("train")
    public String saveTrain(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("train2")
    public String saveTrain2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("trainPost")
    public String saveTrainPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("trainPostPin")
    public String saveTrainPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry Train.
     * @param customerId customer id
     * @param sessionId session id
     * @param json train payment view
     * @return train payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryTrain")
    public String inquiryTrain(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryTrainPost")
    public String inquiryTrainPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Inquiry Train.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return train payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintTrain")
    public String reprintTrain(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintTrainPost")
    public String reprintTrainPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * TV payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json tv payment view object
     * @return tv payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tv")
    public String saveTv(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tv2")
    public String saveTv2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tvPost")
    public String saveTvPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tvPostPin")
    public String saveTvPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry TV.
     * @param customerId customer id
     * @param sessionId session id
     * @param json tv payment view
     * @return tv payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryTv")
    public String inquiryTv(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryTvPost")
    public String inquiryTvPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint TV.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return tv payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintTv")
    public String reprintTv(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintTvPost")
    public String reprintTvPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Internet payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json internet payment view object
     * @return internet payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("internet")
    public String saveInternet(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("internet2")
    public String saveInternet2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("internetPost")
    public String saveInternetPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("internetPostPin")
    public String saveInternetPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry Internet.
     * @param customerId customer id
     * @param sessionId session id
     * @param json internet payment view
     * @return internet payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryInternet")
    public String inquiryInternet(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryInternetPost")
    public String inquiryInternetPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint Internet.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return internet payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintInternet")
    public String reprintInternet(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintInternetPost")
    public String reprintInternetPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Nontaglist payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json nontaglist payment view object
     * @return nontaglist payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nonTagList")
    public String saveNonTagList(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nonTagList2")
    public String saveNonTagList2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nonTagListPost")
    public String saveNonTagListPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nonTagListPostPin")
    public String saveNonTagListPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry Nontaglist.
     * @param customerId customer id
     * @param sessionId session id
     * @param json nontaglist payment view
     * @return nontaglist payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryNonTagList")
    public String inquiryNonTagList(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryNonTagListPost")
    public String inquiryNonTagListPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint Nontaglist.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return nontaglist payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintNonTagList")
    public String reprintNonTagList(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);
    
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintNonTagListPost")
    public String reprintNonTagListPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Palyja payment posting.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json water payment view object
     * @return palyja payment view object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("water")
    public String saveWater(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("water2")
    public String saveWater2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("waterPost")
    public String saveWaterPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("waterPostPin")
    public String saveWaterPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);

    /**
     * Inquiry Palyja.
     * @param customerId customer id
     * @param sessionId session id
     * @param json palyja payment view
     * @return palyja payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryWater")
    public String inquiryWater(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryWaterPost")
    public String inquiryWaterPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);

    /**
     * Reprint Palyja.
     * @param customerId customer id
     * @param sessionId session id
     * @param transactionId transaction id
     * @return palyja payment view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintWater")
    public String reprintWater(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("transactionId") Long transactionId);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reprintWaterPost")
    public String reprintWaterPost(@FormParam("customerId") Long customerId,
            @FormParam("sessionId") String sessionId,
            @FormParam("json") String json);
    
    
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("indosatDompetku")
    public String indosatDompetku(@QueryParam("customerId") Long customerId, 
    		@QueryParam("sessionId") String sessionId, 
    		@QueryParam("json") String json);
    
   
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("multiFinanceInq")
    public String multiFinanceInq(@QueryParam("customerId") Long customerId, 
    		@QueryParam("sessionId") String sessionId, 
    		@QueryParam("json") String json, @QueryParam("jenis") String jenis);
    
    
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("multiFinancePay")
    public String multiFinancePay(@QueryParam("customerId") Long customerId, 
            @QueryParam("deviceId") String deviceId,
    		@QueryParam("sessionId") String sessionId, 
            @QueryParam("token") String token,
    		@QueryParam("json") String json, @QueryParam("jenis") String jenis);
    
    
    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("multiFinancePay2")
    public String multiFinancePay2(@QueryParam("customerId") Long customerId, 
            @QueryParam("deviceId") String deviceId,
    		@QueryParam("sessionId") String sessionId, 
            @QueryParam("token") String token,
    		@QueryParam("json") String json, @QueryParam("jenis") String jenis);


}