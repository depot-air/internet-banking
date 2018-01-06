package com.dwidasa.admin.transform;

import com.dwidasa.admin.view.ProductDenominationView;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.ServiceLocator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 18/11/11
 * Time: 16:21
 */
public class ProductDenominationTransformer implements Transformer{
    private BillerProductService billerProductService;

    public ProductDenominationTransformer() {
        billerProductService = (BillerProductService) ServiceLocator.getService("billerProductService");
    }

    public List transform(List entities) {
        List<ProductDenominationView> result = new ArrayList<ProductDenominationView>();
        for (Object e : entities) {
            ProductDenomination pd = (ProductDenomination) e;
            ProductDenominationView pdv = new ProductDenominationView();
            try {
            	pdv.setDenomination(new BigDecimal(pd.getDenomination()));
			} catch (Exception e2) {
				pdv.setDenomination(new BigDecimal(pd.getDenomination()));// TODO: handle exception
			}
            BillerProduct billerProduct = billerProductService.get(pd.getBillerProductId());
            pdv.setId(pd.getId());
            pdv.setProductCode(billerProduct.getProductCode());
            pdv.setProductName(pd.getBillerProduct().getProductName());
            pdv.setIsActive(pd.getIsActive());
            result.add(pdv);
        }

        return result;
    }
}
