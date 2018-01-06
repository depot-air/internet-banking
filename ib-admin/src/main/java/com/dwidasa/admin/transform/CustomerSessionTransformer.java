package com.dwidasa.admin.transform;

import java.util.ArrayList;
import java.util.List;

import com.dwidasa.admin.view.CustomerSessionView;
import com.dwidasa.engine.model.CustomerSession;

public class CustomerSessionTransformer implements Transformer {

    public CustomerSessionTransformer() {
    }

    public List transform(List entities) {
        List<CustomerSessionView> result = new ArrayList<CustomerSessionView>();
        for (Object e : entities) {
            CustomerSession cs = (CustomerSession) e;
            CustomerSessionView csv = new CustomerSessionView();
            csv.setId(cs.getId());
            csv.setDeviceId(cs.getDeviceId());
            csv.setSessionId(cs.getSessionId());
            csv.setUsername(cs.getCustomer().getCustomerUsername());
            csv.setUpdated(cs.getUpdated());
            result.add(csv);
        }

        return result;
    }
}
