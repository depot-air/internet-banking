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
import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.SessionValidate;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 11:11 AM
 */
@Path("/administration")
@NoCache
public interface AdministrationResource {
    /**
     * Change customer data.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @param operation operation mode
     * @param oldValue old value
     * @param json customer view object
     * @return Constants.OK
     */
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("changeCustomer")
    public String changeCustomer(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("operation") Integer operation,
            @QueryParam("oldValue") String oldValue,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("changeCustomer2")
    public String changeCustomer2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("operation") Integer operation,
            @QueryParam("oldValue") String oldValue,
            @QueryParam("json") String json);

    @NoValidate
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("changeCustomerPost")
    public String changeCustomerPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("operation") Integer operation,
            @FormParam("oldValue") String oldValue,
            @FormParam("json") String json);
	
    //String trackNo2, String oldValue, String newValue, String json
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("changeCustomerPostPin")
    public String changeCustomerPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("trackNo2") String trackNo2,
            @FormParam("oldValue") String oldValue,
            @FormParam("newValue") String newValue,
            @FormParam("json") String json);

    /**
     * Register customer email.
     * @param customerId customer id
     * @param deviceId device id
     * @param activationCode activation code
     * @param email email
     * @return Constants.OK
     */
    @NoValidate
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("registerEmail")
    public String registerEmail(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("activationCode") String activationCode,
            @QueryParam("email") String email);

    /**
     * Activate customer device.
     * @param username username
     * @param deviceId device id
     * @param activationCode activation code
     * @return customer view object
     */
    @NoValidate
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("activateDevice")
    public String activateDevice(@QueryParam("username") String username,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("activationCode") String activationCode);

    /**
     * Deactivate customer device.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @return Constants.OK
     */
    @NoValidate
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deactivateDevice")
    public String deactivateDevice(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deactivateDevice2")
    public String deactivateDevice2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    /**
     * Switch customer device.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @return customer device object
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("switchDevice")
    public String switchDevice(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("switchDevice2")
    public String switchDevice2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    /**
     * Deactivate customer internet banking service.
     * @param customerId customer id
     * @param deviceId device id
     * @param sessionId session id
     * @param token token
     * @return Constants.OK
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deactivateService")
    public String deactivateService(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token);

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("deactivateService2")
    public String deactivateService2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);


    /**
     * Get customer devices.
     * @param customerId customer id
     * @param sessionId session id
     * @return list of customer device
     */
    @SessionValidate
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("devices")
    public String getDevices(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId);

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("smsRegistration")
    public String smsRegistration(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);

    @Authenticate2
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("smsRegistration2")
    public String smsRegistration2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
    @NoValidate
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("smsRegistrationPost")
    public String smsRegistrationPost(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("token") String token,
            @FormParam("json") String json);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("smsRegistrationPostPin")
    public String smsRegistrationPostPin(@FormParam("customerId") Long customerId,
            @FormParam("deviceId") String deviceId,
            @FormParam("sessionId") String sessionId,
            @FormParam("pin") String pin,
            @FormParam("json") String json);
    
    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checkSoftToken")
    public String cekSoftToken(@QueryParam("customerId") Long customerId, 
    		@QueryParam("deviceId") String deviceId);

    
    
    @NoValidate
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("softTokenRegistration")
    public String registrationSoftToken(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId);
    
    
    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("mobileRegistration")
    public String mobilRegistration(@QueryParam("customerId") Long customerId, 
    		@QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);
    
    
}
