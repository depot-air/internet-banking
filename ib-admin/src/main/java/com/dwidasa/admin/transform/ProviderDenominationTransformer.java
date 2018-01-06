package com.dwidasa.admin.transform;

import java.util.ArrayList;
import java.util.List;

import com.dwidasa.admin.view.ProviderDenominationView;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.service.ProviderService;
import com.dwidasa.engine.service.ServiceLocator;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 20/11/11
 * Time: 21:11
 */
public class ProviderDenominationTransformer implements Transformer {
    private ProviderService providerService;
    public ProviderDenominationTransformer() {
        providerService = (ProviderService) ServiceLocator.getService("providerService");
    }

    public List transform(List entities) {
        List<ProviderDenominationView> result = new ArrayList<ProviderDenominationView>();

        for (Object e : entities) {
            ProviderDenomination pd = (ProviderDenomination) e;
            ProviderDenominationView pdv = new ProviderDenominationView();
            pdv.setFee(pd.getFee());
            pdv.setId(pd.getId());
            pdv.setProviderCode(providerService.get(pd.getProviderId()).getProviderCode());
            pdv.setPrice(pd.getPrice());
            pdv.setProviderName(pd.getProvider().getProviderName());
            result.add(pdv);
        }
        return result;
    }
}