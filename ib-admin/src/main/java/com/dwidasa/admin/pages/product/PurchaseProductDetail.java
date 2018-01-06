package com.dwidasa.admin.pages.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.GenericSelectModelFactory;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ProductDenominationService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 18/11/11
 * Time: 15:42
 */
@Import(library = "context:bprks/js/product/PurchaseProductDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class PurchaseProductDetail {
    @Property
    private String transactionType;

    @InjectComponent
    private Zone productNameZone;

    @Property
    private String productCode;

    @Property
    private ProductDenomination productDenomination;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel productNameModel;

    @Property
    private List<String> transactionTypeModel;

    @Inject
    private CacheManager cacheManager;

    @Persist
    private HashMap billerProductMap;

    @Inject
    private Messages messages;

    @Inject
    private ProductDenominationService productDenominationService;

    @Inject
    private SessionManager sessionManager;

    void onPrepare() {
        if (productDenomination == null) {
            productDenomination = new ProductDenomination();
        }
    }

    void setupRender() {
        if (transactionType == null) {
            transactionType = messages.get("voucherPurchase");
        }
        buildBillerProductNameModel(transactionType);
        buildTransactionTypeModel();
    }

    public Object onValueChangedFromTransactionType(String transactionType) {
        buildBillerProductNameModel(transactionType);
        return productNameZone.getBody();
    }

    private void buildTransactionTypeModel() {
        transactionTypeModel = new ArrayList<String>();
        transactionTypeModel.add(messages.get("voucherPurchase"));
        transactionTypeModel.add(messages.get("plnPurchase"));
    }

    private void buildBillerProductNameModel(String transactionType) {
        if (transactionType.equalsIgnoreCase(messages.get("voucherPurchase"))) {
            billerProductMap = new HashMap();
            List<Biller> billerList = cacheManager.getBillers(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE);
            List<BillerProduct> allBpList = new ArrayList<BillerProduct>();
            for (Biller biller : billerList) {
                List<BillerProduct> bpList = cacheManager.getBillerProducts(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, biller.getBillerCode());
                for (BillerProduct bp : bpList) {
                    billerProductMap.put(bp.getProductCode(), bp.getId());
                    allBpList.add(bp);
                }
            }
            productNameModel = genericSelectModelFactory.create(allBpList);
        } else if (transactionType.equalsIgnoreCase(messages.get("plnPurchase"))) {
            billerProductMap = new HashMap();
            List<Biller> billerList = cacheManager.getBillers(com.dwidasa.engine.Constants.PLN_PURCHASE_CODE);
            List<BillerProduct> allBpList = new ArrayList<BillerProduct>();
            for (Biller biller : billerList) {
                List<BillerProduct> bpList = cacheManager.getBillerProducts(com.dwidasa.engine.Constants.PLN_PURCHASE_CODE, biller.getBillerCode());
                for (BillerProduct bp : bpList) {
                    billerProductMap.put(bp.getProductCode(), bp.getId());
                    allBpList.add(bp);
                }
            }
            productNameModel = genericSelectModelFactory.create(allBpList);
        }
    }

    @Inject
    private VersionService versionService;
    
    
    @DiscardAfter
    Object onSelectedFromAdd() {
        Long userId = sessionManager.getLoggedUser().getId();
        productDenomination.setCreated(new Date());
        productDenomination.setCreatedby(userId);
        productDenomination.setUpdated(new Date());
        productDenomination.setUpdatedby(userId);
        productDenomination.setBillerProductId(Long.parseLong(billerProductMap.get(productCode).toString()));
        
        versionService.versionedSave(productDenomination, productDenominationService);
        return PurchaseProductList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return PurchaseProductList.class;
    }
}
