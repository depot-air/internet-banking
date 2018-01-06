package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.dao.ProductDenominationDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CellularPrefixService;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.AccountResource;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Page class for handling input of voucher puchase template.
 *
 * @author rk
 */
public class VoucherPurchaseInput {
    @Property
    @Persist
    private VoucherPurchaseView voucherPurchaseView;

    @Property
    private SelectModel billerModel;

    @Property
    private SelectModel billerMerchantModel;

    @Property
    private int tokenType;

    @Property
    private SelectModel billerProductModel;

    @Property
    private SelectModel billerProductMerchantModel;
    
    @Property
    private SelectModel productDenominationModel;

    @Property
    private SelectModel productDenominationMerchantModel;
    
    @Property
    private List<ProviderDenomination> providerDenominations;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private PurchaseService purchaseService;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private CacheManager cacheManager;

    @InjectComponent
    private Zone billerProductZone;

    @InjectComponent
    private Zone billerProductMerchantZone;
    
    @InjectComponent
    private Zone productDenominationZone;
    
    @InjectComponent
    private Zone productDenominationMerchantZone;

    @InjectComponent
    private Zone providerDenominationZone;

    @InjectComponent
    private Form form;

    @Component(id = "phoneNumber")
    private TextField phoneNumber;

    @Inject
    private Messages message;

    @Inject
    private OtpManager otpManager;

    @Property
    private SelectModel providerDenominationModel;

    @InjectPage
    private VoucherPurchaseConfirm voucherPurchaseConfirm;

    @InjectPage
    private VoucherPurchaseReceipt voucherPurchaseReceipt;

    @Inject
    private CellularPrefixService cellularPrefixService;

    @Inject
    private AccountService accountService;

    @Inject
	private ParameterDao parameterDao;

    @Inject
	private ProductDenominationDao productDenominationDao;
    
    @Inject
    private ExtendedProperty extendedProperty;
    
    @Inject
    private AccountResource accountResource;
    
    @Inject
    private RequestGlobals requestGlobals;

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}    	
    	return null;
    }

    private void buildBillerModel() {
        billerModel = genericSelectModelFactory.create(cacheManager.getBillers(
                com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE));
        if(voucherPurchaseView.getBillerCode() == null)
        if (billerModel.getOptions().size() > 0) {
            voucherPurchaseView.setBillerCode(billerModel.getOptions().get(0).getValue().toString());
        }
    }

    private void buildBillerMerchantModel() {
    	 billerMerchantModel = genericSelectModelFactory.create(cacheManager.getBillersMerchant(
                com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE));
        if(voucherPurchaseView.getBillerCode() == null)
        if (billerMerchantModel.getOptions().size() > 0) {
            voucherPurchaseView.setBillerCode(billerMerchantModel.getOptions().get(0).getValue().toString());
        }
    }

    private void buildBillerProductModel(String billerCode) {
        if (billerCode != null) {
            billerProductModel = genericSelectModelFactory.create(
                    cacheManager.getBillerProducts(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, billerCode));
            if(form.getHasErrors())
                return;
            if (billerProductModel.getOptions().size() > 0) {
                voucherPurchaseView.setProductCode(billerProductModel.getOptions().get(0).getValue().toString());
            }
            return;
        }

        List<BillerProduct> billerProducts = new ArrayList<BillerProduct>();
        billerProductModel = genericSelectModelFactory.create(billerProducts);
    }

    private void buildBillerProductMerchantModel(String billerCode) {
        if (billerCode != null) {
            billerProductMerchantModel = genericSelectModelFactory.create(
                    cacheManager.getBillerProducts(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, billerCode));
            if(form.getHasErrors())
                return;
            if (billerProductMerchantModel.getOptions().size() > 0) {
                voucherPurchaseView.setProductCode(billerProductMerchantModel.getOptions().get(0).getValue().toString());
            }
            return;
        }

        List<BillerProduct> billerProducts = new ArrayList<BillerProduct>();
        billerProductMerchantModel = genericSelectModelFactory.create(billerProducts);
    }

    private void buildProductDenominationModel(String productCode) {
        if (productCode != null) {
        	List<ProductDenomination> pds = cacheManager.getProductDenominations( com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE,
                    voucherPurchaseView.getBillerCode(), productCode );
        	List<ProductDenomination> prodDenoms = new ArrayList<ProductDenomination>();
        	if (pds != null) {
        		prodDenoms.addAll(pds);
            	Parameter minVoucher = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.MINIMAL_VOUCHER_INDIVIDUAL);
            	for(int i = prodDenoms.size() - 1; i >= 0 ; i--) {
        			ProductDenomination pd = prodDenoms.get(i);
        			if (Double.parseDouble(pd.getDenomination()) < Double.parseDouble(minVoucher.getParameterValue())) {
        				if (!pd.getBillerProductCode().equals(com.dwidasa.engine.Constants.PREPAID_PRODCODE.FINNET_TELKOMSEL)) {
        					prodDenoms.remove(i);
        				}
        			}
        		}                
        	}
        	productDenominationModel = genericSelectModelFactory.create(prodDenoms);
            if(form.getHasErrors())
                return;
            if (productDenominationModel.getOptions().size() > 0) {
                voucherPurchaseView.setDenomination(productDenominationModel.getOptions().get(0).getValue().toString());
            }
            return;
        }

        List<ProductDenomination> productDenominations = new ArrayList<ProductDenomination>();
        productDenominationModel = genericSelectModelFactory.create(productDenominations);
    }

    private void buildProductDenominationMerchantModel(String productCode) {
        if (productCode != null) {
        	List<ProductDenomination> pds = cacheManager.getProductDenominations( com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE,
                    voucherPurchaseView.getBillerCode(), productCode );
        	
        	List<ProductDenomination> prodDenoms = new ArrayList<ProductDenomination>();
        	if (pds != null) {
        		prodDenoms.addAll(pds);
/*
            	for(int i = prodDenoms.size() - 1; i >= 0 ; i--) {
        			ProductDenomination pd = prodDenoms.get(i);
    				if (pd.getBillerProductCode().equals(com.dwidasa.engine.Constants.PREPAID_PRODCODE.XL_V_REGULAR_PRODCODE) &&
    						(pd.getDefaultProvider().getProviderCode().equals(com.dwidasa.engine.Constants.PAYEE_ID.SERA))) {
    					providerDenominations = cacheManager.getProviderDenominations( com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE,
    		                    voucherPurchaseView.getBillerCode(), productCode, pd.getDenomination());
    					//jika ada provider selain Sera maka OK, jika hanya sera maka delete
    					boolean isExistNonSera = false;
    					for(int j = providerDenominations.size() - 1; j >= 0 ; j--) {
    	        			ProviderDenomination vd = providerDenominations.get(j);
    	    				if (!vd.getProvider().getProviderCode().equals(com.dwidasa.engine.Constants.PAYEE_ID.SERA)) {
    	    					isExistNonSera = true;
    	    				}
    	        		}
    					if (!isExistNonSera) {
    						prodDenoms.remove(i);
    					}
    				}
        		}
*/
        	}
        	
        	productDenominationMerchantModel = genericSelectModelFactory.create(prodDenoms);
            if(form.getHasErrors())
                return;
            if (productDenominationMerchantModel.getOptions().size() > 0) {
                voucherPurchaseView.setDenomination(productDenominationMerchantModel.getOptions().get(0).getValue().toString());
            }        	
            return;
        }

        List<ProductDenomination> productDenominations = new ArrayList<ProductDenomination>();
        productDenominationMerchantModel = genericSelectModelFactory.create(productDenominations);
    }

    private void buildProviderDenominationModel(String denomination) {
        if (denomination != null) {
            providerDenominations = cacheManager.getProviderDenominations(
                    com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE,
                    voucherPurchaseView.getBillerCode(), voucherPurchaseView.getProductCode(), denomination);
        	
            List<ProviderDenomination> vDenoms = new ArrayList<ProviderDenomination>();
            if (providerDenominations != null) {
        		vDenoms.addAll(providerDenominations);
/*
        		for(int i = vDenoms.size() - 1; i >= 0 ; i--) {
        			ProviderDenomination vd = vDenoms.get(i);
    				if (vd.getProvider().getProviderCode().equals(com.dwidasa.engine.Constants.PAYEE_ID.SERA)) {
    					vDenoms.remove(i);
    				}
        		}
*/ 
        	}
            
            providerDenominationModel = genericSelectModelFactory.create(vDenoms);
            if(form.getHasErrors())
                return;
            if (null != providerDenominations) {
                voucherPurchaseView.setProviderCode(providerDenominations.get(0).getProvider().getProviderCode());
            }
            return;
        }
        providerDenominations = new ArrayList<ProviderDenomination>();
        providerDenominationModel = genericSelectModelFactory.create(providerDenominations);
    }

    private void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public void setupRender() {
    	if (!sessionManager.getSessionLastPage().equals(VoucherPurchaseConfirm.class.toString()) ) {
    		voucherPurchaseView = null;
    	}
    	sessionManager.setSessionLastPage(VoucherPurchaseInput.class.toString());
        if (voucherPurchaseView == null) {
            voucherPurchaseView = new VoucherPurchaseView();
        }

        setTokenType();

        if (isMerchant()) {
            buildBillerMerchantModel();
        	buildBillerProductMerchantModel(voucherPurchaseView.getBillerCode());
        	buildProductDenominationMerchantModel(voucherPurchaseView.getProductCode());
        	buildProviderDenominationModel(voucherPurchaseView.getDenomination());
        } else {
            buildBillerModel();
        	buildBillerProductModel(voucherPurchaseView.getBillerCode());
        	buildProductDenominationModel(voucherPurchaseView.getProductCode());
        }
    }
    
    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    void onValueChangedFromBiller(String billerCode) {
        voucherPurchaseView.setBillerCode(billerCode);
        
    	buildBillerProductModel(billerCode);
    	buildProductDenominationModel(voucherPurchaseView.getProductCode());
    	ajaxResponseRenderer.addRender(billerProductZone).addRender(productDenominationZone);        
    }

    void onValueChangedFromBillerMerchant(String billerCode) {
        voucherPurchaseView.setBillerCode(billerCode);
        
    	buildBillerProductMerchantModel(billerCode);
    	buildProductDenominationMerchantModel(voucherPurchaseView.getProductCode());
        buildProviderDenominationModel(voucherPurchaseView.getDenomination());
    	ajaxResponseRenderer.addRender(billerProductMerchantZone).addRender(productDenominationMerchantZone).addRender(providerDenominationZone);
    }

    void onValueChangedFromBillerProduct(String productCode) {
        voucherPurchaseView.setProductCode(productCode);

        buildProductDenominationModel(productCode);
        ajaxResponseRenderer.addRender(productDenominationZone);     	
    }

    void onValueChangedFromBillerProductMerchant(String productCode) {
        voucherPurchaseView.setProductCode(productCode);
    
    	buildProductDenominationMerchantModel(productCode);
        buildProviderDenominationModel(voucherPurchaseView.getDenomination());
    	ajaxResponseRenderer.addRender(productDenominationMerchantZone).addRender(providerDenominationZone);
    }
/*
    public Object onValueChangedFromProductDenomination(String denomination) {        
        return null;
    }
*/
    public Object onValueChangedFromProductDenominationMerchant(String denomination) {
    	buildProviderDenominationModel(denomination);
    	return providerDenominationZone.getBody();
    }
    private boolean validatePrefix()
    {
        boolean valid = false;
        List<BillerProduct> billerProducts =
                cacheManager.getBillerProducts
                        (com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, voucherPurchaseView.getBillerCode());
        String billerProductId = "";
        for(BillerProduct billerProduct : billerProducts)
        {
            if(billerProduct.getProductCode().equals(voucherPurchaseView.getProductCode()))
            {
                billerProductId = billerProduct.getId() + "";
                break;
            }
        }
        List<CellularPrefix> cellularPrefixes =
                cacheManager.getCellularPrefixListByBillerProductId(billerProductId);
        if(cellularPrefixes == null)
            form.recordError(message.get("phoneNumberPrefixError"));
        else
        {
            for(CellularPrefix cellularPrefix : cellularPrefixes)
            {
                if(voucherPurchaseView.getCustomerReference().indexOf(cellularPrefix.getPrefix())==0)
                {
                    valid = true;
                    break;
                }
            }
            if(!valid)
                form.recordError(message.get("phoneNumberPrefixError"));
        }
        return valid;
    }

    public void onValidateFromForm() {
        try {
            //check prefix   productDenominationModel
    		if(!validatePrefix())
                return;
            setVoucherPurchaseViewData();            
            /*
            if(accountService.checkProviderDenomAtSameDay(voucherPurchaseView))
            {
                form.recordError(message.get("providerDenomError"));
                return;
            } */
            
            purchaseService.confirm(voucherPurchaseView);
            
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
        }
    }

    private void setVoucherPurchaseViewData() {
        //-- populate placeholder fields
        Biller biller = cacheManager.getBiller(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE,
                voucherPurchaseView.getBillerCode());
        voucherPurchaseView.setBillerName(biller.getBillerName());

        BillerProduct billerProduct = cacheManager.getBillerProduct(
                com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE,
                voucherPurchaseView.getBillerCode(), voucherPurchaseView.getProductCode());
        voucherPurchaseView.setProductName(billerProduct.getProductName());
        voucherPurchaseView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        
        voucherPurchaseView.setFeeIndicator("C");

        //-- populate another mandatory fields
        voucherPurchaseView.setAccountType(sessionManager.getAccountType(voucherPurchaseView.getAccountNumber()));
        voucherPurchaseView.setTransactionType(com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE);
        voucherPurchaseView.setMerchantType(sessionManager.getDefaultMerchantType());
        voucherPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        voucherPurchaseView.setCurrencyCode(Constants.CURRENCY_CODE);
        voucherPurchaseView.setTransactionDate(new Date());
        
        if (!isMerchant()) {
        	ProductDenomination prodDenom = productDenominationDao.getByProductCodeDenom(voucherPurchaseView.getProductCode(), voucherPurchaseView.getDenomination());
        	Provider defaultProvider = prodDenom.getDefaultProvider();
        	voucherPurchaseView.setFee(BigDecimal.ZERO);
            voucherPurchaseView.setProviderCode(defaultProvider.getProviderCode());
        	voucherPurchaseView.setProviderName(defaultProvider.getProviderName());

        	Parameter minVoucher = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.MINIMAL_VOUCHER_INDIVIDUAL);
        	if (Double.parseDouble(voucherPurchaseView.getDenomination()) >= Double.parseDouble(minVoucher.getParameterValue())) {
        		voucherPurchaseView.setAmount(BigDecimal.valueOf(Double.valueOf(voucherPurchaseView.getDenomination())));
			} else {
				ProviderDenomination pd = cacheManager.getProviderDenomination(
	                    com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, voucherPurchaseView.getBillerCode(),
	                    voucherPurchaseView.getProductCode(), voucherPurchaseView.getDenomination(),
	                    voucherPurchaseView.getProviderCode());
				if (pd != null )
					voucherPurchaseView.setAmount(pd.getPrice());
			}
        	voucherPurchaseView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        	voucherPurchaseView.setIp(requestGlobals.getHTTPServletRequest().getRemoteAddr());
        	
        } else {
        	ProviderDenomination pd = cacheManager.getProviderDenomination(
                    com.dwidasa.engine.Constants.VOUCHER_PURCHASE_CODE, voucherPurchaseView.getBillerCode(),
                    voucherPurchaseView.getProductCode(), voucherPurchaseView.getDenomination(),
                    voucherPurchaseView.getProviderCode());
            voucherPurchaseView.setAmount(pd.getPrice());
            Provider provider = pd.getProvider();
            voucherPurchaseView.setFee(pd.getFee());
            
            voucherPurchaseView.setProviderName(provider.getProviderName());
            
            if (voucherPurchaseView.getAccountNumber().equals("3000000182"))
            	voucherPurchaseView.setTerminalId("G1501832");
            else if (voucherPurchaseView.getAccountNumber().equals("3000000317"))
            	voucherPurchaseView.setTerminalId("G1501833");
        }
        
    }

    public Object onSuccess() {
        voucherPurchaseConfirm.setVoucherPurchaseView(voucherPurchaseView);
        return voucherPurchaseConfirm;
    }

    public void pageReset() {
//        voucherPurchaseView = null;
//        voucherPurchaseConfirm.setVoucherPurchaseView(null);
//        voucherPurchaseReceipt.setVoucherPurchaseView(null);
    }

    public boolean isMerchant() {    	
//    	String firstFour = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
//    	Parameter ip = parameterDao.get(com.dwidasa.engine.Constants.MPARAMETER_NAME.PREFIX_MERCHANT_IB);
//    	String[] tokens = ip.getParameterValue().split(",");
//    	boolean isMerch = false;
//    	if (tokens.length > 0 ) {
//    		for(int i = 0; i < tokens.length; i++) {
//    			if (firstFour.equals(tokens[i]))
//    				isMerch = true;
//    		}
//    	}
    	return sessionManager.isMerchant();
    }
}
