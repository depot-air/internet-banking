package com.dwidasa.engine.service.impl.otp;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.otp.OtpClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/15/11
 * Time: 11:12 AM
 */
public class OtpClientImpl implements OtpClient {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private Client client;

    public OtpClientImpl() {
    }

    /**
     * IPV4 of application server
     * @return ipv4
     */
    protected String getIpv4() {
        String ipv4 = null;

        try {
            ipv4 = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ipv4;
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterDevice(Long customerId, String deviceId) {
        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        WebResource webResource = client.resource(otpServer+"/registration/unregDevice")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("tokenId", deviceId)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());

        String response = webResource.put(String.class);
        if (!response.equals("250")) {
            throw new BusinessException("IB-1007", response);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void unregisterUser(Long customerId) {
        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        WebResource webResource = client.resource(otpServer+"/registration/unregUser")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());

        String response = webResource.put(String.class);

        if (!response.equals("150")) {
            throw new BusinessException("IB-1007", response);
        }
    }
}
