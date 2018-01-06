package com.dwidasa.engine.service.impl.otp;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dto.RegistrationDto;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.otp.WebOtpClient;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/4/11
 * Time: 3:00 PM
 */
@Service("webOtpClient")
public class WebOtpClientImpl extends OtpClientImpl implements WebOtpClient {
    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private Client client;

    public WebOtpClientImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public String registerSoftToken(Long customerId, String tokenId, String activationCode) {
        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        WebResource webResource = client.resource(otpServer+"/registration/register")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("tokenId", tokenId)
                .queryParam("activationCode", activationCode)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());

        String json = webResource.put(String.class);
        RegistrationDto registrationDto = PojoJsonMapper.fromJson(json, RegistrationDto.class);

        if (registrationDto.getResponseType() != 100) {
            throw new BusinessException("IB-1007", registrationDto.getResponseType());
        }

        return registrationDto.getTokenId();
    }

    /**
     * {@inheritDoc}
     */
    public String unregisterSoftToken(Long customerId) {
        Customer customer = customerDao.get(customerId);

        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        WebResource webResource = client.resource(otpServer+"/registration/unregDevice")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("tokenId", customer.getMobileWebTokenId())
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());

        String response = webResource.put(String.class);

        if (!response.equals("250")) {
            throw new BusinessException("IB-1007", response);
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validateToken(Long customerId, String token) {
        String otpServer = cacheManager.getParameter("OTP_SERVER").getParameterValue();
        WebResource webResource = client.resource(otpServer+"/preReq/validateToken")
                .queryParam("userId", String.valueOf(customerId))
                .queryParam("token", token)
                .queryParam("channelId", "2")
                .queryParam("ipv4", getIpv4());

        String response = webResource.put(String.class);

        if (!response.equals("500") && !response.equals("600")) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
