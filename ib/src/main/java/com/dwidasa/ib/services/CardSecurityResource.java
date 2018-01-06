package com.dwidasa.ib.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.annotations.cache.NoCache;

import com.dwidasa.ib.annotations.NoValidate;

@Path("/cardsecurity")
@NoCache
public interface CardSecurityResource {
	/**
	 * authenticate customer card existence
	 * @param cardData
	 * @param cardPin
	 * @return
	 */
	@NoValidate
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("authentication")
	String authenticateCustomerCard(@FormParam("card_data") String cardData, 
			   @FormParam("card_pin") String cardPin);
	
	/**
	 * authorise customer information based on card data
	 * @param trackNo2
	 * @param cardPin
     * @param json
	 * @return  json accountView
	 */
	@NoValidate
	@POST
	@Produces(MediaType.APPLICATION_JSON)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("authorisation")
	String authoriseCustomerFromCard(@FormParam("trackNo2") String trackNo2,
				@FormParam("cardPin") String cardPin, @FormParam("json") String json );
}
