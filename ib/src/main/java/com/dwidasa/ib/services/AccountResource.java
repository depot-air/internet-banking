package com.dwidasa.ib.services;

import com.dwidasa.ib.annotations.Authenticate2;
import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.SessionValidate;

import java.text.ParseException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 10:41 AM
 */
@Path("/account")
@NoCache
public interface AccountResource {
    /**
     * Get customer account.
     * @param customerId customer id
     * @param sessionId session id
     * @return account view object
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list")
    public String getCustomerAccounts(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("listPost")
    public String getCustomerAccountsPost(@FormParam("customerId") Long customerId,
                                      @FormParam("sessionId") String sessionId);

    /**
     * Get account balance of a customer account.
     * @param customerId customer id
     * @param sessionId session id
     * @param json account view object
     * @return account view object
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("balance")
    public String getAccountBalance(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("balancePost")
    public String getAccountBalancePost(@FormParam("customerId") Long customerId,
                                    @FormParam("sessionId") String sessionId,
                                    @FormParam("json") String json);

    /**
     * Get balance for all accounts that the customer has
     * @param customerId customer id
     * @param sessionId session id
     * @param json account view object
     * @return account view object
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("balances")
    public String getAccountBalances(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("balancesPost")
    public String getAccountBalancesPost(@FormParam("customerId") Long customerId,
                                    @FormParam("sessionId") String sessionId,
                                    @FormParam("json") String json);
    
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("multiRekening")
    public String getMultiRekening(@QueryParam("customerId") Long customerId,
    							   @QueryParam("sessionId") String sessionId,
    							   @QueryParam("json") String json);

    /**
     * Get account statements of a customer from specified date.
     * @param customerId customer id
     * @param sessionId session id
     * @param startDate start date
     * @param endDate end date
     * @param json account view object
     * @return list of account statement view
     * @throws ParseException parse exception
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("statement")
    public String getAccountStatement(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("json") String json) throws ParseException;


    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("statementPost")
    public String getAccountStatementPost(@FormParam("customerId") Long customerId,
                                      @FormParam("sessionId") String sessionId,
                                      @FormParam("startDate") String startDate,
                                      @FormParam("endDate") String endDate,
                                      @FormParam("json") String json) throws ParseException;

    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("statementPostDemo")
    public String getAccountStatementDemoPost(@FormParam("customerId") Long customerId,
                                      @FormParam("sessionId") String sessionId,
                                      @FormParam("startDate") String startDate,
                                      @FormParam("endDate") String endDate,
                                      @FormParam("json") String json) throws ParseException;

    @NoValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("statementApp")
    public String getAccountStatementApp( @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("json") String json) throws ParseException;

    @NoValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("statementAppPost")
    public String getAccountStatementAppPost( @FormParam("startDate") String startDate,
                                      @FormParam("endDate") String endDate,
                                      @FormParam("json") String json) throws ParseException;
    /**
     * Get account statements of a customer from specified date.
     * @param customerId customer id
     * @param sessionId session id
     * @param json account view object
     * @param n n transaction
     * @return list of account statement view
     * @throws Exception parse exception
     */
    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("lastNStatementPost")
    public String getLastNStatementPost(@FormParam("customerId") Long customerId,
                                      @FormParam("sessionId") String sessionId,
                                      @FormParam("n") Integer n,
                                      @FormParam("json") String json) throws ParseException;

    /**
     * Checking tranasction status of a customer from specified date.
     * @param customerId customer id
     * @param sessionId session id
     * @param startDate start date
     * @param endDate end date
     * @param json account view object
     * @return list of account statement view object
     * @throws ParseException parse exception
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("oldTransactionStatus")
    public String getTransactionStatus(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("json") String json) throws ParseException;

    /**
     * Checking tranasction status of a customer from specified date.
     * @param customerId customer id
     * @param sessionId session id
     * @param startDate start date
     * @param endDate end date
     * @param json account view object
     * @return list of account statement view object
     * @throws ParseException parse exception
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("transactionStatus")
    public String checkTransactionStatus(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("json") String json) throws ParseException;


    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("transactionStatusPost")
    public String getTransactionStatusPost(@FormParam("customerId") Long customerId,
                                       @FormParam("sessionId") String sessionId,
                                       @FormParam("startDate") String startDate,
                                       @FormParam("endDate") String endDate,
                                       @FormParam("json") String json) throws ParseException;

    /**
     * Get transaction history of a customer from specified date.
     * @param customerId customer id
     * @param sessionId session id
     * @param startDate start date
     * @param endDate end date
     * @param json account view object
     * @return list account statement view object
     * @throws ParseException parse exception
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("transactionHistory")
    public String getTransactionHistory(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate,
            @QueryParam("json") String json) throws ParseException;


    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("transactionHistoryPost")
    public String getTransactionHistoryPost(@FormParam("customerId") Long customerId,
                                        @FormParam("sessionId") String sessionId,
                                        @FormParam("startDate") String startDate,
                                        @FormParam("endDate") String endDate,
                                        @FormParam("json") String json) throws ParseException;

    /**
     * Deactivate customer card.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param json account view object
     * @return account view object
     */
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("deactivateCard")
    public String deactivateCard(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Path("deactivateCard2")
    public String deactivateCard2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("deactivateCardPost")
    public String deactivateCardPost(@FormParam("customerId") Long customerId,
                                 @FormParam("deviceId") String deviceId,
                                 @FormParam("sessionId") String sessionId,
                                 @FormParam("token") String token,
                                 @FormParam("json") String json);
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("deactivateCardPostPin")
    public String deactivateCardPostPin(@FormParam("customerId") Long customerId,
                                 @FormParam("deviceId") String deviceId,
                                 @FormParam("sessionId") String sessionId,
                                 @FormParam("pin") String pin,
                                 @FormParam("json") String json);

    /**
     * Get all customer card number and account number. This result provided by messaging to core system.
     * @param customerId customer id
     * @param sessionId session id
     * @param json account view object
     * @return list of account view
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryAccount")
    public String inquiryAccount(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);


    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("inquiryAccountPost")
    public String inquiryAccountPost(@FormParam("customerId") Long customerId,
                                 @FormParam("sessionId") String sessionId,
                                 @FormParam("json") String json);

    /**
     * Mail of provided account statement.
     * @param customerId custoemer id
     * @param sessionId session id
     * @param json list of account statement view
     * @return Constants.OK
     * @throws Exception messaging exception
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mailStatement")
    public String mailStatement(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json) throws Exception;


    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mailStatementPost")
    public String mailStatementPost(@FormParam("customerId") Long customerId,
                                @FormParam("sessionId") String sessionId,
                                @FormParam("json") String json) throws Exception;

    /**
     * Mail of provided account statement.
     * @param customerId custoemer id
     * @param sessionId session id
     * @param json of lottery view
     * @return json of lottery view with startPoint and endPoint
     */
    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("lotteryViewPointsPost")
    public String lotteryViewPointsPost(@FormParam("customerId") Long customerId,
                                    @FormParam("sessionId") String sessionId,
                                    @FormParam("json") String json);

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("lotteryViewPointsPostPin")
    public String lotteryViewPointsPostPin(@FormParam("customerId") Long customerId,
                                    @FormParam("sessionId") String sessionId,
                                    @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("accountSsppPost")
    public String accountSsppPost(@FormParam("json") String json);
    

    /**
     * get all portfolios including saving, checking, time deposit and loan
     * @param customerId customer identifier
     * @param sessionId session identifier
     * @param json request data
     * @return portfolio data
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("portfolio")
    public String getPortfolio( @QueryParam( "customerId" ) Long customerId,
                                @QueryParam( "sessionId" ) String sessionId, 
                                @QueryParam( "json" ) String json );

    @SessionValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("portfolioPost")
    public String getPortfolioPost( @FormParam( "customerId" ) Long customerId,
                                @FormParam( "sessionId" ) String sessionId,
                                @FormParam( "json" ) String json );

    @NoValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("ssppTemenos")
    public String getSsppTemenos( @FormParam( "json" ) String json );
    
    @NoValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("ssppUpdate")
    public String ssppUpdate( @FormParam( "json" ) String json );

    @NoValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("ssppTemenosDemo")
    public String getSsppTemenosDemo( @FormParam( "json" ) String json );

    @NoValidate
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Path("ssppUpdateDemo")
    public String ssppUpdateDemo( @FormParam( "json" ) String json );

//    @NoValidate
//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path("ssppTemenosGet")
//    public String getSsppTemenosGet( @QueryParam( "json" ) String json );

//    @NoValidate
//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    @Path("ssppUpdateGet")
//    public String ssppUpdateGet( @FormParam( "json" ) String json );

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("jenisUndian")
    public String getJenisUndian(@QueryParam("customerId") Long customerId, 
    							 @QueryParam( "sessionId" ) String sessionId,
    							 @QueryParam("json") String json) throws ParseException;
    
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nomorUndian")
    public String getNomorUndian(@QueryParam("customerId") Long customerId, 
    							 @QueryParam( "sessionId" ) String sessionId,
    		                     @QueryParam("accountNumber") String accountNumber, 
    							 @QueryParam("kodeUndian") String kodeUndian,
    							 @QueryParam("json") String json) throws ParseException;

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("jenisUndianPost")
    public String getJenisUndianPost(@FormParam("customerId") Long customerId, 
    		@FormParam( "sessionId" ) String sessionId,
			@FormParam("json") String json) throws ParseException;

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nomorUndianPost")
    public String getNomorUndianPost(@FormParam("customerId") Long customerId,
    		@FormParam( "sessionId" ) String sessionId,
    		@FormParam("accountNumber") String accountNumber, 
    		@FormParam("kodeUndian") String kodeUndian,
    		@FormParam("json") String json) throws ParseException;

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("jenisUndianPostPin")
    public String getJenisUndianPostPin(@FormParam("customerId") Long customerId,
    		@FormParam( "sessionId" ) String sessionId,
			 @FormParam("json") String json) throws ParseException;

    @SessionValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nomorUndianPostPin")
    public String getNomorUndianPostPin(@FormParam("customerId") Long customerId,
    		@FormParam( "sessionId" ) String sessionId,
    		@FormParam("accountNumber") String accountNumber, 
    		@FormParam("kodeUndian") String kodeUndian,
    		@FormParam("json") String json) throws ParseException;

    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("unblockorblock")
    public String getUnblock(@QueryParam("username") String customerUsername,
    		@QueryParam("TID") String TerminalID,
            @QueryParam("status") int status, @QueryParam("userLog") String userLog) throws Exception;
    
}
