package com.dwidasa.admin.transform;

import com.dwidasa.admin.view.CellularPrefixView;
import com.dwidasa.engine.model.CellularPrefix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/13/11
 * Time: 6:56 PM
 */
public class CellularPrefixTransformer implements Transformer {
    public CellularPrefixTransformer() {
    }

    public List transform(List entities) {
        List<CellularPrefixView> result = new ArrayList<CellularPrefixView>();

        for (Object e : entities) {
            CellularPrefix cp = (CellularPrefix) e;
            CellularPrefixView cpv = new CellularPrefixView();

            cpv.setId(cp.getId());
            cpv.setPrefix(cp.getPrefix());
            cpv.setProductName(cp.getBillerProduct().getProductName());
            cpv.setStatus(cp.getStatus());

            result.add(cpv);
        }

        return result;
    }
}
