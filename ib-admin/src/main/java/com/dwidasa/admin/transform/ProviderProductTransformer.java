package com.dwidasa.admin.transform;

import com.dwidasa.admin.view.ProviderProductView;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.ProviderProductService;
import com.dwidasa.engine.service.ProviderService;
import com.dwidasa.engine.service.ServiceLocator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/24/11
 * Time: 00:15 am
 */
public class ProviderProductTransformer implements Transformer {
     private BillerProductService billerProductService;
     private ProviderService providerService ;
    public ProviderProductTransformer() {
        billerProductService = (BillerProductService) ServiceLocator.getService("billerProductService");
        providerService = (ProviderService) ServiceLocator.getService("providerService");
    }

    public List transform(List entities) {
        List<ProviderProductView> result = new ArrayList<ProviderProductView>();
        for (Object e : entities) {
            ProviderProduct pp = (ProviderProduct) e;
            ProviderProductView ppv = new ProviderProductView();
            ppv.setFee(pp.getFee());

            Provider provider = providerService.get(pp.getProviderId());
            ppv.setProviderName(pp.getProvider().getProviderName());
            BillerProduct billerProduct = billerProductService.get(pp.getBillerProductId());
            ppv.setId(pp.getId());
            ppv.setProductCode(billerProduct.getProductCode());
            ppv.setProductName(pp.getBillerProduct().getProductName());
            ppv.setIsActive(pp.getIsActive());
            result.add(ppv);
        }

        return result;
    }
}
