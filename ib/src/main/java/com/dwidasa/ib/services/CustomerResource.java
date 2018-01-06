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

import com.dwidasa.ib.annotations.NoValidate;

/**
 * Interface that responsible for native device authentication.
 *
 * @author rk
 */
@Path("/customer")
@NoCache
public interface CustomerResource {
	
    @NoValidate
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("setPushId")
    public String setPushId(
    		@FormParam("customerId") Long customerId,
    		@FormParam("customerDeviceId") Long customerDeviceId,
            @FormParam("pushId") String pushId);

}
