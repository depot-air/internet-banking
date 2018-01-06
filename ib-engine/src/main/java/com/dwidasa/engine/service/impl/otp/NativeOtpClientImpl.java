package com.dwidasa.engine.service.impl.otp;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.impl.facade.NativeAuthenticationServiceImpl;
import com.dwidasa.engine.service.otp.NativeOtpClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of NativeOtpClient interface.
 *
 * @author rk
 */
@Service("nativeOtpClient")
public class NativeOtpClientImpl extends OtpClientImpl implements NativeOtpClient {
    private static Logger logger = Logger.getLogger(  NativeOtpClientImpl.class);
    @Autowired
    private Client client;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CacheManager cacheManager;

    public NativeOtpClientImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public void registerDevice(Long customerid, String deviceId, String activationCode) {
        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        WebResource webResource = client.resource(otpServer+"/registration/register")
                .queryParam("userId", String.valueOf(customerid))
                .queryParam("tokenId", deviceId)
                .queryParam("activationCode", activationCode)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());

        String response = webResource.put(String.class);
        if (!response.equals("100")) {
            throw new BusinessException("IB-1007", response);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validateToken(Long customerId, String deviceId, String token, String challenge) {
    	logger.info("customerId=" + customerId + " deviceId=" + deviceId + " token=" + token + " challenge=" + challenge);
        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        logger.info("otpServer=" + otpServer);
        WebResource webResource;
        if (token.length() == 6){
            // use vcr
            webResource = client.resource(otpServer+"/validation/vcr")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("tokenId", deviceId)
                .queryParam("challenge", constructScramblePin(customerId, challenge))
                .queryParam("token", token)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());
        }
        else {
        	logger.info("url=" + otpServer+"/validation/vro");
        	logger.info("?userId=" + customerId + "&tokenId=" + deviceId + "&token=" + token + "&channelId=2" + "&ipv4=" + getIpv4());
            // use vro
            webResource = client.resource(otpServer+"/validation/vro")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("tokenId", deviceId)
                .queryParam("token", token)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());
        }


        String response = webResource.get(String.class);
        logger.info("response=" + response);
        if (!response.equals("500") && !response.equals("600")) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    public Boolean validateToken2(Long customerId, String deviceId, String token, String challenge){
        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        WebResource webResource;
        if (token.length() == 6){
            // use vcr
            webResource = client.resource(otpServer+"/validation/vcr")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("tokenId", deviceId)
                .queryParam("challenge", challenge)
                .queryParam("token", token)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());
        }
        else {
            // use vro
            webResource = client.resource(otpServer+"/validation/vro")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("tokenId", deviceId)
                .queryParam("token", token)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());
        }


        String response = webResource.get(String.class);

        if (!response.equals("500") && !response.equals("600")) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    /**
     * Construct scramble pin based on challenge.
     * @param customerId customer id
     * @param challenge challenge
     * @return scrambled pin
     */
    private String constructScramblePin(Long customerId, String challenge) {
        Customer customer = customerDao.get(customerId);
        String pin = customer.getCustomerPin();

        String scramblePin = "";
        for (int i = 0; i < challenge.length(); i++) {
            int index = Integer.valueOf(String.valueOf(challenge.charAt(i))) ;
            scramblePin += pin.charAt(index-1);
        }

        return scramblePin;
    }
}
