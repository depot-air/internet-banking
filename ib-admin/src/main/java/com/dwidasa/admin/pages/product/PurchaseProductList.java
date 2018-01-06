package com.dwidasa.admin.pages.product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
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
import com.dwidasa.admin.view.ProductDenominationView;
import com.dwidasa.engine.dao.ProductDenominationDao;
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
 * Time: 15:41
 */
@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class PurchaseProductList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private ProductDenominationView row;

    @Property
    @Persist
    private String productCode;

    @Property
    @Persist
    private String transactionType;

    @InjectPage
    private PurchaseProductDetail purchaseProductDetail;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel productNameModel;

    @Property
    private List<String> transactionTypeModel;

    @Inject
    private CacheManager cacheManager;

    private HashMap billerProductMap;

    @Inject
    private Messages messages;

    @InjectComponent
    private Zone productNameZone;

    @Inject
    private ProductDenominationService productDenominationService;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @SuppressWarnings("unchecked")
    void setupRender() {
        if (transactionType == null) {
            transactionType = messages.get("voucherPurchase");
        }
        buildBillerProductNameModel(transactionType);
        buildTransactionTypeModel();
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (productCode != null && !productCode.equals("")) {
            restrictions.add("m_biller_product_id =?");
            Object key = billerProductMap.get(productCode);
            values.add(key);
        }

        Transformer t = TransformerFactory.getTransformer(ProductDenomination.class.getSimpleName());
        dataSource = new BaseDataSource(ProductDenominationView.class, Constants.PAGE_SIZE, restrictions, values, t);
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

    @DiscardAfter
    void onSelectedFromReset() {
        productCode = null;
        transactionType = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        return purchaseProductDetail;
    }
    
    @Inject
    private VersionService versionService;
    
    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private ProductDenominationDao denominationDao;

    @DiscardAfter
    void onActionFromDelete(Long id) {
        ProductDenomination tt = new ProductDenomination();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, productDenominationService, userId);
    }
    
    
    @DiscardAfter
    void onActionFromUpdate(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	denominationDao.updateStatusBiller(id, active, new Date(), userId, new Date(), userId);
    }
    
    @DiscardAfter
    void onActionFromUpdateActive(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	denominationDao.updateStatusBiller(id, active, new Date(), userId, new Date(), userId);
    }
    
    
}
