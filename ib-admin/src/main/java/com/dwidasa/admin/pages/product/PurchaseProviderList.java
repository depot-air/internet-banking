package com.dwidasa.admin.pages.product;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.GenericSelectModelFactory;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.admin.view.ProviderDenominationView;
import com.dwidasa.engine.dao.ProviderDenominationDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.engine.service.ProductDenominationService;
import com.dwidasa.engine.service.ProviderDenominationService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 18/11/11
 * Time: 15:42
 */
@Import(library = "context:bprks/js/product/PurchaseProviderList.js")
@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class PurchaseProviderList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private ProviderDenominationView row;

    @Persist
    @Property(write = false)
    private String transactionType;

    @Persist
    @Property(write = false)
    private Long id;

    @Property
    private ProductDenomination productDenomination;

    @Inject
    private ProductDenominationService productDenominationService;

    @Inject
    private BillerProductService billerProductService;

    @Inject
    private BillerService billerService;

    @Property
    private BillerProduct billerProduct;

    @Property
    private Biller biller;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private BigDecimal denomination;

    @InjectPage
    private PurchaseProviderDetail purchaseProviderDetail;

    @InjectPage
    private PurchaseProductList purchaseProductList;
    
    @Inject
    private ProviderDenominationService providerDenominationService;

    @Inject
    private ProviderDenominationDao providerDenominationDao;
    
    @Persist
    private List<ProviderDenomination> providerDenominations;

    @Property
    private String defaultProviderName;

    @Property
    private SelectModel providerListModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    
    void onActivate(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private void buildProviderListModel() {
    	List<Provider> providers = new ArrayList<Provider>();
    	for (ProviderDenomination vd : providerDenominations) {
			Provider prov = vd.getProvider();
			providers.add(prov);
			if (vd.getProductDenomination().getDefaultProviderId().intValue() == vd.getProviderId().intValue()) {
            	defaultProviderName = vd.getProvider().getProviderCode();
            }
		}
    	
    	providerListModel = genericSelectModelFactory.create(providers);
    	
    	
    }

    void setupRender() {
        pageSize = Constants.PAGE_SIZE;
        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        if (id != null && !id.equals("")) {
            restrictions.add("m_product_denomination_id =?");
            values.add(id);
        }
        Transformer t = TransformerFactory.getTransformer(ProviderDenomination.class.getSimpleName());
        dataSource = new BaseDataSource(ProviderDenominationView.class, Constants.PAGE_SIZE, restrictions, values, t);
        
        providerDenominations = providerDenominationDao.getCurrentPageRows(0, pageSize, restrictions, null, values.toArray());
        buildProviderListModel();
    }

    void onPrepare() {
        if (id != null) {
            productDenomination = productDenominationService.get(id);
            denomination = new BigDecimal(productDenomination.getDenomination());
            billerProduct = billerProductService.get(productDenomination.getBillerProductId());
            biller = billerService.get(billerProduct.getBillerId());
        } else {
            productDenomination = new ProductDenomination();
            billerProduct = new BillerProduct();
        }
        if (biller != null && biller.getBillerName() != null && biller.getBillerName().equalsIgnoreCase("PLN")) {
            transactionType = messages.get("plnPurchase");
        } else {
            transactionType = messages.get("voucherPurchase");
        }
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        purchaseProviderDetail.setProductDenominationId(id);
        return purchaseProviderDetail;
    }
    
    @Inject
    private VersionService versionService;
    
    @Inject
    private SessionManager sessionManager;

    void onActionFromDelete(Long id) {
        ProviderDenomination tt = new ProviderDenomination();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, providerDenominationService, userId);
    }

    @DiscardAfter
    Object onSelectedFromSave() {
        for (ProviderDenomination vd : providerDenominations) {
			if (defaultProviderName.equals(vd.getProvider().getProviderCode())) {
				productDenomination.setDefaultProvider(vd.getProvider());
				productDenomination.setDefaultProviderId(vd.getProvider().getId());
				productDenominationService.save(productDenomination);
				return purchaseProductList;
            }
		}
        return purchaseProductList;
    }
}
