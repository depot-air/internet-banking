package com.dwidasa.ib.services;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.NoValidate;

@Path("/server")
@NoCache
public interface ServerResource {
	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @Path("executeTransactionQueue")
	public String executeTransactionQueue(@Context HttpServletRequest request);
		
	@NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @Path("reloadCache")
	public String reloadCache(@Context HttpServletRequest request);

    @NoValidate
	@GET
	@Produces(MediaType.APPLICATION_JSON)
    @Path("ping")
    /**
     * Ping to check connection between client and server.
     * Will just return Constants.OK
     */
	public String ping(@Context HttpServletRequest request);

    @NoValidate
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
    @Path("ping2")
    /**
     * Ping to check connection between client and server.
     * Will just return Constants.OK
     */
	public String ping2(@Context HttpServletRequest request);
}
