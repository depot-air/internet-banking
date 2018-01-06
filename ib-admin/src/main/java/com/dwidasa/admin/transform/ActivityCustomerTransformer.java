package com.dwidasa.admin.transform;

import java.util.ArrayList;
import java.util.List;

import com.dwidasa.engine.model.ActivityCustomer;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActivityCustomerTransformer implements Transformer {

    public ActivityCustomerTransformer() {
    }

    public List transform(List entities) {
        List<ActivityCustomer> result = new ArrayList<ActivityCustomer>();
        for (Object e : entities) {
        	ActivityCustomer activityCustomer = (ActivityCustomer) e;

            result.add(activityCustomer);
        }

        return result;
    }
}
