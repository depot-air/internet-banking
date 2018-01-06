package com.dwidasa.engine.service.impl.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.dao.CustomerDeviceDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.view.CustomerView;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AdministrationService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 9:37 AM
 */
public abstract class AdministrationServiceImpl implements AdministrationService {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerDeviceDao customerDeviceDao;

    @Autowired
    private CacheManager cacheManager;

    public AdministrationServiceImpl() {
    }

    /**
     * {@inheritDoc}
     */
    public ResultView changeCustomerInfo(CustomerView view, String oldValue, Integer operation) {
        Customer c = customerDao.get(view.getId());
        if (c == null) {
            throw new BusinessException("IB-1004");
        }

        switch (operation) {
            case 1 :
//                if (c.getCustomerEmail() != null && !c.getCustomerEmail().equals(oldValue)) {
//                    throw new BusinessException("IB-1016");
//                }
                c.setCustomerEmail(view.getEmail());
                break;
            case 2 :
//                if (c.getCustomerPhone() != null && !c.getCustomerPhone().equals(oldValue)) {
//                    throw new BusinessException("IB-1016");
//                }
                c.setCustomerPhone(view.getPhone());
                break;
            case 3 :
                if (c.getEncryptedCustomerPin().length() == 16){
                    if (!c.getCustomerPin().equals(oldValue)) {
                        throw new BusinessException("IB-1016");
                    }
                }

                c.setCustomerPin(view.getPin());		//view.getPin()
                c.setEncryptedCustomerPin(EngineUtils.encrypt(Constants.SERVER_SECRET_KEY, view.getPin()));		//view.getPin()
        }

        customerDao.save(c);

        ResultView rv = new ResultView();
        rv.setReferenceNumber(ReferenceGenerator.generate());
        rv.setResponseCode(Constants.SUCCESS_CODE);
        return rv;
    }

    /**
     * Generate view object for presentation/client layer
     * @param customer customer object
     * @return customerView object
     */
    protected CustomerView generateView(Customer customer, CustomerView view) {
        if (view == null) {
            view =  new CustomerView();
        }
        view.setId(customer.getId());
        view.setUsername(customer.getCustomerUsername());
        view.setName(customer.getCustomerName());
        view.setEmail(customer.getCustomerEmail());
        view.setPhone(customer.getCustomerPhone());
        view.setLastLogin(customer.getLastLogin());
        view.setLastDeviceId(customer.getLastTokenId());

        return view;
    }

    /**
     * {@inheritDoc}
     */
    public ResultView deactivateDevice(Long customerId, String deviceId) {
        CustomerDevice customerDevice = customerDeviceDao.get(customerId, deviceId);

        if (customerDevice == null) {
            throw new BusinessException("IB-1006");
        }

        unregisterDevice(customerId, deviceId);

        customerDeviceDao.remove(customerDevice.getId(), customerId);

        ResultView rv = new ResultView();
        rv.setReferenceNumber(ReferenceGenerator.generate());
        rv.setResponseCode(Constants.SUCCESS_CODE);
        return rv;
    }

    /**
     * {@inheritDoc}
     */
    public CustomerDevice switchDevice(Long customerId, String deviceId) {
        CustomerDevice customerDevice = customerDeviceDao.get(customerId, deviceId);

        if (customerDevice == null) {
            throw new BusinessException("IB-1006");
        }

        unregisterDevice(customerId, deviceId);

        int expiredDays = Integer.parseInt(cacheManager.getParameter("REGISTRATION_EXPIRED_DAYS").getParameterValue());

        customerDevice.setDeviceId(null);
        customerDevice.setIme(null);
        customerDevice.setActivatePin(RandomStringUtils.randomNumeric(6));
        customerDevice.setStatus(Constants.INACTIVE_STATUS);
        customerDevice.setExpiredDate(DateUtils.after(new Date(), expiredDays));
        customerDevice.setUpdated(new Date());
        customerDevice.setUpdatedby(customerId);
        customerDeviceDao.save(customerDevice);

        return customerDevice;
    }

    /**
     * {@inheritDoc}
     */
    public ResultView deactivateService(Long customerId) {
        Customer customer = customerDao.get(customerId);
        if (customer == null) {
            throw new BusinessException("IB-1004");
        }
        
//        	unregisterUser(customerId);	//requested by pak arie & pak michael, tidak perlu registrasi ulang, cukup 

//        customerDeviceDao.removeAll(customerId);
        customer.setStatus(Constants.INACTIVE_STATUS);
        customer.setUpdated(new Date());
        customer.setUpdatedby(customerId);

        customerDao.save(customer);

        ResultView rv = new ResultView();
        rv.setReferenceNumber(ReferenceGenerator.generate());
        rv.setResponseCode(Constants.SUCCESS_CODE);
        return rv;
    }

    /**
     * Instead having otp client instance in this class, we used otp client
     * instance of descendant class.
     */
    public abstract void unregisterUser(Long customerId);

    /**
     * Instead having otp client instance in this class, we used otp client
     * instance of descendant class.
     */
    public abstract void unregisterDevice(Long customerId, String deviceId);
}
