package com.dwidasa.admin.pages.product;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.GenericSelectModelFactory;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ProviderDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ProductDenominationService;
import com.dwidasa.engine.service.ProviderDenominationService;
import com.dwidasa.engine.service.ProviderService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 18/11/11
 * Time: 15:43
 */
@Import(library = "context:bprks/js/product/PurchaseProviderDetail.js")
@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class PurchaseProviderDetail {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Property
    private String transactionType;

    private String transactionTypeCode;

    @Persist
    @Property(write = false)
    private Long id;

    @Persist
    @Property(write = false)
    private Long productDenominationId;

    @Persist
    @Property
    private ProductDenomination productDenomination;

    @Inject
    private ProductDenominationService productDenominationService;

    @Inject
    private BillerProductService billerProductService;

    @Inject
    private BillerService billerService;

    @Persist
    @Property
    private BillerProduct billerProduct;

    @Persist
    @Property
    private Biller biller;

    @Inject
    private Messages messages;

    @InjectPage
    private PurchaseProviderList purchaseProviderList;

    @Property
    @Persist
    private ProviderDenomination providerDenomination;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel providerNameModel;

    @Property
    private String providerCode;

    @Inject
    private ProviderDenominationService providerDenominationService;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private BigDecimal denomination;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private ProviderDao providerDao;

    @Inject
    private ProviderService providerService;

    @Inject
    private CacheManager cacheManager;

    void onActivate(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductDenominationId(Long productDenominationId) {
        this.productDenominationId = productDenominationId;
    }

    void setupRender() {
        if (id != null) {
            providerDenomination = providerDenominationService.get(id);
            productDenomination = productDenominationService.get(providerDenomination.getProductDenominationId());
            denomination = new BigDecimal(productDenomination.getDenomination());
            billerProduct = billerProductService.get(productDenomination.getBillerProductId());
            biller = billerService.get(billerProduct.getBillerId());
            providerCode = providerService.get(providerDenomination.getProviderId()).getProviderCode();
        } else {
            productDenomination = productDenominationService.get(productDenominationId);
            denomination = new BigDecimal(productDenomination.getDenomination());
            billerProduct = billerProductService.get(productDenomination.getBillerProductId());
            biller = billerService.get(billerProduct.getBillerId());
            providerDenomination = new ProviderDenomination();
        }
        if (biller.getBillerName().equalsIgnoreCase("PLN")) {
            transactionType = messages.get("plnPurchase");
            transactionTypeCode = Constants.PLN_PURCHASE_CODE;
        } else {
            transactionType = messages.get("voucherPurchase");
            transactionTypeCode = Constants.VOUCHER_PURCHASE_CODE;
        }
        buildProviderNameModel();
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
    }

    public String getAction() {
        if (id != null) {
            return messages.get("edit");
        }

        return messages.get("add");
    }

    private void buildProviderNameModel() {
        List<Provider> lp = providerService.getAll();
        List<Provider> tempList = cacheManager.getProviders(transactionTypeCode, biller.getBillerCode(), billerProduct.getProductCode());

        for (Provider prov : tempList) {
            lp.remove(prov);
        }

        providerNameModel = genericSelectModelFactory.create(lp);
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        if (id == null) {
            purchaseProviderList.setId(productDenominationId);
        }
        return purchaseProviderList;
    }
    
    @Inject
    private VersionService versionService;

    @DiscardAfter
    Object onSelectedFromAdd() {
        if (providerDenomination.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            Provider provider = providerDao.get(providerCode);
            providerDenomination.setCreated(new Date());
            providerDenomination.setCreatedby(userId);
            providerDenomination.setUpdated(new Date());
            providerDenomination.setUpdatedby(userId);
            providerDenomination.setProviderId(provider.getId());
            providerDenomination.setProductDenominationId(productDenomination.getId());
        }
        versionService.versionedSave(providerDenomination, providerDenominationService);
        
        purchaseProviderList.setId(productDenomination.getId());
        return purchaseProviderList;
    }
}
