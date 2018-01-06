package com.dwidasa.ib.services;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.Authenticate2;
import com.dwidasa.ib.annotations.NoValidate;
import com.dwidasa.ib.annotations.SessionValidate;

@Path("/train")
@NoCache
public interface TrainResource {

	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getStations")
	public String getStations();

	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("inquiryTrain")
	public String inquiryTrain(@QueryParam("json") String json);

	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("inquiryFare")
	public String inquiryFare(@QueryParam("json") String json);

	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("bookTicket")
	public String bookTicket(@QueryParam("json") String json);
	
	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("changeSeat")
	public String changeSeat(@QueryParam("json") String json,
			@QueryParam("wagonCode") String wagonCode,
			@QueryParam("wagonNumber") String wagonNumber,
			@QueryParam("strSeat") String strSeat);

	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("inquirySeat")
	public String inquirySeat(@QueryParam("json") String json);
	
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("postKAI")
    public String saveKAI(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
	
	@Authenticate2
	@PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("postKAI2")
    public String saveKAI2(@QueryParam("customerId") Long customerId,
            @QueryParam("deviceId") String deviceId,
            @QueryParam("sessionId") String sessionId,
            @QueryParam("token") String token,
            @QueryParam("json") String json);
	
	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("cancelBook")
	public void cancelBook(@QueryParam("json") String json);
	
}