package com.dwidasa.ib.services;

import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.SessionValidate;
import org.jboss.resteasy.annotations.cache.NoCache;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;


@Path("/aero")
@NoCache
public interface AeroticketingResource {

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("priceDetail")
    public String getAeroPriceDetail(@FormParam("jsonView") String jsonView, @FormParam("jsonFlight") String jsonFlight);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("aeroPriceDetail")
    public String aeroPriceDetail(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("jsonView") String jsonView,
            @QueryParam("jsonFlight") String jsonFlight);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voltrasAirFare")
    public String getVoltrasAirFare(@FormParam("json") String json, @FormParam("jsonDepart") String jsonDepart, @FormParam("jsonReturn") String jsonReturn);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voltrasAirFareDetail")
    public String voltrasAirFareDetail(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json, @QueryParam("jsonDepart") String jsonDepart, @QueryParam("jsonReturn") String jsonReturn);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("officeInformation")
    public String getOfficeInformation();

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("search")
    public String search(@QueryParam("customerId") Long customerId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("aeroSearchDetail")
    public String aeroSearchDetail(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("jsonView") String jsonView,
                @QueryParam("jsonDepart") String jsonDepart,
                @QueryParam("jsonReturn") String jsonReturn);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("booking")
    public String booking(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voltrasRetrieve")
    public String voltrasRetrieve(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voltrasBooking")
    public String voltrasBooking(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voltrasTicket")
    public String voltrasTicket(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("voltrasCancel")
    public String voltrasCancel(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("aeroIssue")
    public String aeroIssue(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("json") String json);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("sendEmail")
    public String sendEmail(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId,
                @QueryParam("json") String json);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAirports")
    public String getAirports();

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAirlines")
    public String getAirlines();

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getAirportsPost")
    public String getAirportsPost();

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getOftenAirports")
    public String getOftenAirports(@QueryParam("customerId") Long customerId, @QueryParam("fromOrTo") String fromOrTo);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getOftenRoutes")
    public String getOftenRoutes(@QueryParam("customerId") Long customerId);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getFlightList")
    public String getFlightList(@QueryParam("json") String json);


    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getOftenAirportsPost")
    public String getOftenAirportsPost(@FormParam("customerId") Long customerId, @FormParam("fromOrTo") String fromOrTo);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getOftenRoutesPost")
    public String getOftenRoutesPost(@FormParam("customerId") Long customerId);

    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getFlightListPost")
    public String getFlightListPost(@FormParam("json") String json);

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getComission")
    public String getComission();

    @NoValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getCountries")
    public String getCountries();

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getRecentContacts")
    public String getRecentContacts(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId);

    @SessionValidate
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("getRecentPassengers")
    public String getRecentPassengers(@QueryParam("customerId") Long customerId,
                @QueryParam("sessionId") String sessionId, @QueryParam("passengerType") String passengerType);
}
