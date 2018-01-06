package com.dwidasa.ib.services.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.CustomerDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.AppVersion;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.LocationType;
import com.dwidasa.engine.model.News;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.model.ResponseCode;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.model.Version;
import com.dwidasa.engine.model.view.CellularPrefixView;
import com.dwidasa.engine.service.AppVersionService;
import com.dwidasa.engine.service.NewsService;
import com.dwidasa.engine.service.ParameterService;
import com.dwidasa.engine.service.facade.SynchronizationService;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.SynchronizationResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 2:07 PM
 */
@PublicPage
public class SynchronizationResourceImpl implements SynchronizationResource {
	private static Logger logger = Logger.getLogger( SynchronizationResourceImpl.class );
    @Inject
    private SynchronizationService synchronizationService;

    @Inject
    private AppVersionService appVersionService;

    @Inject
    private NewsService newsService;

    @Inject
    private ParameterService parameterService;
    
    @Autowired
    private CustomerDao customerDao;

    public SynchronizationResourceImpl() {
    }

    public String getVersions(Long customerId, String sessionId) {
        List<Version> versions = synchronizationService.getVersions();
        return PojoJsonMapper.toJson(versions);
    }

    public String getBillers(Long customerId, String sessionId) {
    	List<Biller> billers = synchronizationService.getBillers();
        return PojoJsonMapper.toJson(billers);
    }

    public String getBillersNoSession() {
    	List<Biller> billers = synchronizationService.getBillers();
    	//kiosk pulsa hanya yg jaringan 3 dan jaringan 4 saja
    	for (int i = billers.size() - 1; i >= 0 ; i--) {
    		Biller biller = billers.get(i);
    		if (biller.getTransactionTypeId().intValue() == 66) {	
    			if (!biller.getBillerCode().equals(Constants.BILLER_CODE.AXIS_PRABAYAR) && !biller.getBillerCode().equals(Constants.BILLER_CODE.ESIA_AHA) &&
    					!biller.getBillerCode().equals(Constants.BILLER_CODE.SMARTFREN_PREPAID) && !biller.getBillerCode().equals(Constants.BILLER_CODE.THREE_PRABAYAR)) {
    				billers.remove(i);
    			}
    		}
		}    	
    	return PojoJsonMapper.toJson(billers);
    }

    public String getBillerProducts(Long customerId, String sessionId) {
        List<BillerProduct> billerProducts = synchronizationService.getBillerProducts();
        return PojoJsonMapper.toJson(billerProducts);
    }

    public String getBillerProductsNoSession() {
        List<BillerProduct> billerProducts = synchronizationService.getBillerProducts();
        return PojoJsonMapper.toJson(billerProducts);
    }

    public String getProductDenominations(Long customerId, String sessionId) {
    	List<ProductDenomination> productDenominations = synchronizationService.getProductDenominations();
        for (int i = productDenominations.size() - 1; i >= 0 ; i--) {
    		ProductDenomination pd = productDenominations.get(i);
    		if (pd.getDefaultProvider().getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
    			productDenominations.remove(i);
    		}    		
    	}   
        return PojoJsonMapper.toJson(productDenominations);
    }

    public String getProductDenominationsNoSession() {
    	List<ProductDenomination> productDenominations = synchronizationService.getProductDenominations();
        for (int i = productDenominations.size() - 1; i >= 0 ; i--) {
    		ProductDenomination pd = productDenominations.get(i);
    		//kiosk hanya ada jaringan 3 dan 4 saja (akses), dan jaringan 1 (GSP)
    		String providerCode = pd.getDefaultProvider().getProviderCode();
    		if (!providerCode.equals(Constants.PAYEE_ID.AKSES_NUSANTARA) && !providerCode.equals(Constants.PAYEE_ID.AKSES_CIPTA_SOLUSI) && !providerCode.equals(Constants.GSP.PROVIDER_CODE) ) {
    			productDenominations.remove(i);
    		}
    	}   
        return PojoJsonMapper.toJson(productDenominations);
    }

    
    public String getProviderProduct(Long customerId, String sessionId) {
        List<ProviderProduct> providerProducts = synchronizationService.getProviderProducts();
        return PojoJsonMapper.toJson(providerProducts);
    }

    public String getProviderProductNoSession() {
        List<ProviderProduct> providerProducts = synchronizationService.getProviderProducts();
        return PojoJsonMapper.toJson(providerProducts);
    }

    public String getProviderDenominations(Long customerId, String sessionId) {
    	List<ProviderDenomination> providerDenominations = synchronizationService.getProviderDenominations();
    	for (int i = providerDenominations.size() - 1; i >= 0; i--) {
    		ProviderDenomination pd = providerDenominations.get(i);
    		if (pd.getProvider().getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
    			providerDenominations.remove(i);
    		}
    	}
    	return PojoJsonMapper.toJson(providerDenominations);
    }

    public String getProviderDenominationsNoSession() {
    	List<ProviderDenomination> providerDenominations = synchronizationService.getProviderDenominations();
    	for (int i = providerDenominations.size() - 1; i >= 0; i--) {
    		ProviderDenomination pd = providerDenominations.get(i);
    		//kiosk hanya ada jaringan 3 dan 4 saja (akses), dan jaringan 1 (GSP)
    		String providerCode = pd.getProvider().getProviderCode();
    		if (!providerCode.equals(Constants.PAYEE_ID.AKSES_NUSANTARA) && !providerCode.equals(Constants.PAYEE_ID.AKSES_CIPTA_SOLUSI) && !providerCode.equals(Constants.GSP.PROVIDER_CODE) ) {
    			providerDenominations.remove(i);
    		}
    	}
    	return PojoJsonMapper.toJson(providerDenominations);
    }

    public String getProviders(Long customerId, String sessionId) {
        return getProviderNoSession();
    }

    public String getProviderNoSession() {
        List<Provider> providers = synchronizationService.getProviders();
        //membuang SYB
        for (int i = providers.size() - 1; i >= 0 ; i--) {
    		Provider provider = providers.get(i);
    		if (provider.getProviderCode().equals(Constants.SYB.PROVIDER_CODE)) {
    			providers.remove(i);
    		}
    	}   
        return PojoJsonMapper.toJson(providers);
    }

    public String getCurrencies(Long customerId, String sessionId) {
        List<Currency> currencies = synchronizationService.getCurrencies();
        return PojoJsonMapper.toJson(currencies);
    }

    public String getCurrenciesNoSession() {
        List<Currency> currencies = synchronizationService.getCurrencies();
        return PojoJsonMapper.toJson(currencies);
    }

    public String getTransactionTypes(Long customerId, String sessionId) {
        List<TransactionType> transactionTypes = synchronizationService.getTransactionTypes();
        return PojoJsonMapper.toJson(transactionTypes);
    }

    public String getTransactionTypesNoSession() {
        List<TransactionType> transactionTypes = synchronizationService.getTransactionTypes();
        return PojoJsonMapper.toJson(transactionTypes);
    }

    
    public String getLocationTypes(Long customerId, String sessionId) {
        List<LocationType> locationTypes = synchronizationService.getLocationTypes();
        return PojoJsonMapper.toJson(locationTypes);
    }

    public String getLocationTypesNoSession() {
        List<LocationType> locationTypes = synchronizationService.getLocationTypes();
        return PojoJsonMapper.toJson(locationTypes);
    }

    public String getResponseCodes(Long customerId, String sessionId) {
        List<ResponseCode> responseCodes = synchronizationService.getResponseCodes();
        return PojoJsonMapper.toJson(responseCodes);
    }

    public String getResponseCodesNoSession() {
        List<ResponseCode> responseCodes = synchronizationService.getResponseCodes();
        return PojoJsonMapper.toJson(responseCodes);
    }

    
    public String getCellularPrefixes(Long customerId, String sessionId) {

        List<CellularPrefixView> prefixes = synchronizationService.getCellularPrefixesView();
        return PojoJsonMapper.toJson(prefixes);
    }

    public String getCellularPrefixesNoSession() {
        List<CellularPrefixView> prefixes = synchronizationService.getCellularPrefixesView();
        return PojoJsonMapper.toJson(prefixes);
    }

    public String getLatestAppVersion(String deviceType, Long versionId) {
        AppVersion appVersion =  appVersionService.getLatestVersion(deviceType, versionId);
        return PojoJsonMapper.toJson(appVersion);

    }

    public String news() {
        List<News> newsList = newsService.getAll();
        return PojoJsonMapper.toJson(newsList);
    }

    public String parameters() {
        List<Parameter> parameterList = parameterService.getAll();
        return PojoJsonMapper.toJson(parameterList);
    }
}
