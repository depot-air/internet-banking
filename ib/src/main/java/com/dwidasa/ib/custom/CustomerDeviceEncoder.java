package com.dwidasa.ib.custom;

import org.apache.tapestry5.ValueEncoder;

import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.ib.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 9/19/11
 * Time: 8:38 PM
 */
public class CustomerDeviceEncoder implements ValueEncoder<CustomerDevice> {
    public CustomerDeviceEncoder() {
    }

    public String toClient(CustomerDevice value) {
        //-- serialized only what we need, no less no more !!
        return String.valueOf(value.getId()) + Constants.SEPARATOR + value.getIme() +
                Constants.SEPARATOR + value.getDeviceId();
    }

    public CustomerDevice toValue(String clientValue) {
        CustomerDevice customerDevice = new CustomerDevice();
        String items[] = clientValue.split(Constants.SEPARATOR);

        customerDevice.setId(Long.valueOf(items[0]));
        customerDevice.setIme(items[1]);
        customerDevice.setDeviceId(items[2]);

        return customerDevice;
    }
}
