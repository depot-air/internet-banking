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
 * Time: 12:18 PM
 */
@Path("/synch")
@NoCache
public interface SynchronizationResource {
    /**
     * Get all version.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of version
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("version")
    public String getVersions(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);

    /**
     * Get all biller.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of biller
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("biller")
    public String getBillers(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("billerNoSession")
    public String getBillersNoSession();

    /**
     * Get all biller product.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of biller product.
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("billerProduct")
    public String getBillerProducts(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("billerProductNoSession")
    public String getBillerProductsNoSession();

    /**
     * Get all product denomination.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of product denomination
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("productDenomination")
    public String getProductDenominations(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("productDenominationNoSession")
    public String getProductDenominationsNoSession();

    /**
     * Get all provider product.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of provider product
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("providerProduct")
    public String getProviderProduct(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("providerProductNoSession")
    public String getProviderProductNoSession();

    /**
     * Get all provider denomination.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of provider denomination
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("providerDenomination")
    public String getProviderDenominations(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("providerDenominationNoSession")
    public String getProviderDenominationsNoSession();

    /**
     * Get all provider.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of provider
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("provider")
    public String getProviders(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("providerNoSession")
    public String getProviderNoSession();

    /**
     * Get all currency.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of currency
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("currency")
    public String getCurrencies(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("currencyNoSession")
    public String getCurrenciesNoSession();

    /**
     * Get all transaction type.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of transaction type
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transactionType")
    public String getTransactionTypes(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("transactionTypeNoSession")
    public String getTransactionTypesNoSession();

    /**
     * Get all location type.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of location type
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("locationType")
    public String getLocationTypes(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("locationTypeNoSession")
    public String getLocationTypesNoSession();

    /**
     * Get all response code.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of response code
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("responseCode")
    public String getResponseCodes(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("responseCodeNoSession")
    public String getResponseCodesNoSession();

    /**
     * Get all cellular prefix
     * @param customerId customer id
     * @param sessionId session id
     * @return list of cellular prefix
     */
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cellularPrefix")
    public String getCellularPrefixes(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cellularPrefixNoSession")
    public String getCellularPrefixesNoSession();

    /**
     * get device application latest version for particular device/platform type and version
     * @param deviceType string represent device/platform type eg BlackBerry (10), Android (11), Iphone (12)
     * @param versionId version identification number
     * @return latest app version
     */
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("appVersion")
    public String getLatestAppVersion(@QueryParam("deviceType")String deviceType,
            @QueryParam("versionId") Long versionId);

    /**
     * get news
     * @return latest news
     */
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("news")
    public String news();

    /**
     * get news
     * @return latest news
     */
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("parameterNoSession")
    public String parameters();
}
