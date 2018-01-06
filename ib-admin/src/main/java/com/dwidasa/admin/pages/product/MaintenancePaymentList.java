package com.dwidasa.admin.pages.product;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
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
import com.dwidasa.admin.view.ProviderProductView;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.service.BillerProductService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ProviderProductService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/15/11
 * Time: 00:37 am
 */
@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class MaintenancePaymentList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private ProviderProductView row;

    @Property
    @Persist
    private Long productId;

    @Property
    @Persist
    private String transactionType;

    @InjectPage
    private MaintenancePaymentDetail maintenancePaymentDetail;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Property
    private SelectModel productNameModel;

    @Property
    private SelectModel transactionTypeModel;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private Messages messages;

    @InjectComponent
    private Zone productNameZone;

    @Inject
    private ProviderProductService providerProductService;

    @Inject
    private ThreadLocale threadLocale;
    
    @Inject
    private BillerProductService billerProductService;
    
    @Inject
    private ProviderProductDao productDao;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @SuppressWarnings("unchecked")
    void setupRender() {
        if (transactionType == null) {
            transactionType = com.dwidasa.engine.Constants.PLN_PAYMENT_CODE;            
        }
        buildBillerProductNameModel(transactionType);
        buildTransactionTypeModel();
        pageSize = Constants.PAGE_SIZE;
        
        if (productId == null && productNameModel.getOptions().size() > 0) {
        	productId = (Long) productNameModel.getOptions().get(0).getValue();
        }

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (productId != null) {
            restrictions.add("m_biller_product_id =?");
            values.add(productId);
        } 

        Transformer t = TransformerFactory.getTransformer(ProviderProduct.class.getSimpleName());
        dataSource = new BaseDataSource(ProviderProductView.class, Constants.PAGE_SIZE, restrictions, values, t);
    }

    public Object onValueChangedFromTransactionType(String transactionType) {
        buildBillerProductNameModel(transactionType);
        return productNameZone.getBody();
    }

    private void buildTransactionTypeModel() {
    	List<OptionModel> optionModelList = new ArrayList<OptionModel>();
    	optionModelList.add(new OptionModelImpl(messages.get("transfer"), com.dwidasa.engine.Constants.TRANSFER_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("transferOther"), com.dwidasa.engine.Constants.TRANSFER_TREASURY_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentPln"), com.dwidasa.engine.Constants.PLN_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentTelkom"), com.dwidasa.engine.Constants.TELKOM_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentInternet"), com.dwidasa.engine.Constants.INTERNET_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentCellular"), com.dwidasa.engine.Constants.TELCO_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentTv"), com.dwidasa.engine.Constants.ENTERTAINMENT_PAYMENT_CODE));
    	optionModelList.add(new OptionModelImpl(messages.get("paymentTrain"), com.dwidasa.engine.Constants.TRANSPORTATION_PAYMENT_CODE));
    	transactionTypeModel = new SelectModelImpl(null, optionModelList);
    }

    private void buildBillerProductNameModel(String transactionType) {
    	if (transactionType == null || transactionType.equals("")) {
    		productNameModel = new SelectModelImpl(null, new ArrayList<OptionModel>());
    		return;
    	}
    	List<BillerProduct> bpList = billerProductService.getAllByTransactionType(transactionType);
    	List<OptionModel> optionModelList = new ArrayList<OptionModel>();
	    for (BillerProduct bp : bpList) {
	    	optionModelList.add(new OptionModelImpl(bp.getProductName(), bp.getId()));
	    }
	    productNameModel = new SelectModelImpl(null, optionModelList);    	
    }

    @DiscardAfter
    void onSelectedFromReset() {
        transactionType = null;
        productId = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        return MaintenancePaymentDetail.class;
    }
    
    @Inject
    private VersionService versionService;
    
    @Inject
    private SessionManager sessionManager;

    @DiscardAfter
    void onActionFromDelete(Long id) {
        ProviderProduct tt = new ProviderProduct();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, providerProductService, userId);
    }
    
    @DiscardAfter
    void onActionFromUpdate(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	productDao.updateStatusProviderProduct(id, active, new Date(), userId, new Date(), userId);
    }
    
    @DiscardAfter
    void onActionFromUpdateActive(Long id, boolean active) {
    	System.out.println("Id "+id);
    	System.out.println("Status "+active);
    	Long userId = sessionManager.getLoggedUser().getId();
    	
    	System.out.println("Status Dari DB "+active );
    	productDao.updateStatusProviderProduct(id, active, new Date(), userId, new Date(), userId);
    }
}
