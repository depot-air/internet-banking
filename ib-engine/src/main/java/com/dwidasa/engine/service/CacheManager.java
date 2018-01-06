package com.dwidasa.engine.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.AccountExceptionDao;
import com.dwidasa.engine.dao.AirlineDao;
import com.dwidasa.engine.dao.AirportDao;
import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.dao.BillerProductDao;
import com.dwidasa.engine.dao.CellularPrefixDao;
import com.dwidasa.engine.dao.CityDao;
import com.dwidasa.engine.dao.CurrencyDao;
import com.dwidasa.engine.dao.CustomerRegisterDao;
import com.dwidasa.engine.dao.FormConfigDao;
import com.dwidasa.engine.dao.MenuDao;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.dao.ProductDenominationDao;
import com.dwidasa.engine.dao.ProviderDenominationDao;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.dao.TrainStationDao;
import com.dwidasa.engine.model.AccountException;
import com.dwidasa.engine.model.Airline;
import com.dwidasa.engine.model.Airport;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.model.City;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.Menu;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.model.ProductDenomination;
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.ProviderProduct;
import com.dwidasa.engine.model.TrainStation;
import com.dwidasa.interlink.dao.FeatureDao;
import com.dwidasa.interlink.dao.IGateBillerProductDao;
import com.dwidasa.interlink.model.MFeature;
import com.dwidasa.interlink.model.MIGateBillerProduct;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 9:23 AM
 */
@Service("cacheManager")
@SuppressWarnings("unchecked")
public class CacheManager implements ServletContextAware, ApplicationContextAware {

//    private static Logger logger = Logger.getLogger( CacheManager.class );
    private ServletContext context;
    private ApplicationContext ctx;

    public CacheManager() {
    }

    public void setServletContext(ServletContext servletContext) {
        context = servletContext;
    }

    public void setApplicationContext(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Initialize cache to store common lookup for entire application.
     */
    public void initialiazeCache() {
        setupBiller(ctx, context);
        setupBillerIsMerchant(ctx, context);
        setupProduct(ctx, context);
        setupProductDenomination(ctx, context);
        setupProviderDenomination(ctx, context);
        setupProviderProduct(ctx, context);
        setupFormConfig(ctx, context);
        setupParameter(ctx, context);
        setupMenu(ctx, context);
        setupMenuMerchantEdcAndHiperwallet(ctx, context);
        setupMenuMerchant(ctx, context);
        setupMenuMerchantPac(ctx, context);
        setupCurrency(ctx, context);
        setupCellularPrefix(ctx, context);
        setupAccountException(ctx, context);
        setupSmsTokenPrefix(ctx, context);
        setupMobileRegistrationPrefix(ctx, context);
        setupFeatureCode(ctx, context);
        setupIGateBillerProduct(ctx, context);
//        setupCity(ctx, context);
        setupAirport(ctx, context);
        setupAirline(ctx, context);
        setupCRAirport(ctx, context);
        setupKai(ctx, context);
    }
    
    /**
     * refresh cache parameter with values from database
     */
    public void refreshCacheParameter() {
    	setupParameter(ctx, context);
    }

    /**
     * Setup map to cache biller object by transaction type.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupBiller(ApplicationContext ctx, ServletContext context) {
        BillerDao billerDao = (BillerDao) ctx.getBean("billerDao");
        List<Biller> billers =  billerDao.getAllWithTransactionType();
        Map<String, List<Biller>> billerMap = new HashMap<String, List<Biller>>();
        for (Biller biller : billers) {
            if (billerMap.containsKey(biller.getTransactionType().getTransactionType())) {
                List<Biller> billerList = billerMap.get(biller.getTransactionType().getTransactionType());
                billerList.add(biller);
            }
            else {
                List<Biller>  billerList = new ArrayList<Biller>();
                billerList.add(biller);
                billerMap.put(biller.getTransactionType().getTransactionType(), billerList);
            }
            biller.setTransactionType(null);
        }

        context.setAttribute(Constants.BILLER_BY_TRANSACTION_TYPE, billerMap);
    }
    
    
    private void setupBillerIsMerchant(ApplicationContext ctx, ServletContext context) {
        BillerDao billerDao = (BillerDao) ctx.getBean("billerDao");
        List<Biller> billers =  billerDao.getAllWithTransactionTypeIsMerchant();
        Map<String, List<Biller>> billerMap = new HashMap<String, List<Biller>>();
        for (Biller biller : billers) {
            if (billerMap.containsKey(biller.getTransactionType().getTransactionType())) {
                List<Biller> billerList = billerMap.get(biller.getTransactionType().getTransactionType());
                billerList.add(biller);
            }
            else {
                List<Biller>  billerList = new ArrayList<Biller>();
                billerList.add(biller);
                billerMap.put(biller.getTransactionType().getTransactionType(), billerList);
            }
            biller.setTransactionType(null);
        }

        context.setAttribute(Constants.BILLER_BY_TRANSACTION_TYPE_MERCHANT, billerMap);
    }

    /**
     * Setup map to cache billerProduct by transaction type and biller code.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupProduct(ApplicationContext ctx, ServletContext context) {
        BillerProductDao billerProductDao = (BillerProductDao) ctx.getBean("billerProductDao");
        List<BillerProduct> billerProducts = billerProductDao.getAllWithTransactionType();
        Map<String, List<BillerProduct>> billerProductMap = new HashMap<String, List<BillerProduct>>();
        for (BillerProduct billerProduct : billerProducts) {
            String key = billerProduct.getBiller().getTransactionType().getTransactionType() +
                    billerProduct.getBiller().getBillerCode();
            if (billerProductMap.containsKey(key)) {
                List<BillerProduct> billerProductList = billerProductMap.get(key);
                billerProductList.add(billerProduct);
            }
            else {
                List<BillerProduct> billerProductList = new ArrayList<BillerProduct>();
                billerProductList.add(billerProduct);
                billerProductMap.put(key, billerProductList);
            }

            billerProduct.setBiller(null);
        }

        context.setAttribute(Constants.PRODUCT_BY_BILLER, billerProductMap);
    }

    /**
     * Setup map to cache productDenomination by transaction type, biller code and product code.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupProductDenomination(ApplicationContext ctx, ServletContext context) {
        ProductDenominationDao productDenominationDao = (ProductDenominationDao) ctx.getBean("productDenominationDao");
        List<ProductDenomination> productDenominations = productDenominationDao.getAllWithTransactionType();
        Map<String, List<ProductDenomination>> productDenominationMap =
                new HashMap<String, List<ProductDenomination>>();
        for (ProductDenomination productDenomination : productDenominations) {
            String key = productDenomination.getBillerProduct().getBiller().getTransactionType().getTransactionType() +
                    productDenomination.getBillerProduct().getBiller().getBillerCode() +
                    productDenomination.getBillerProduct().getProductCode();

            if (productDenominationMap.containsKey(key)) {
                List<ProductDenomination> productDenominationList = productDenominationMap.get(key);
                productDenominationList.add(productDenomination);
            }
            else {
                List<ProductDenomination> productDenominationList = new ArrayList<ProductDenomination>();
                productDenominationList.add(productDenomination);
                productDenominationMap.put(key, productDenominationList);
            }

            productDenomination.setBillerProduct(null);
        }

        context.setAttribute(Constants.DENOMINATION_BY_PRODUCT, productDenominationMap);
    }

    /**
     * Setup map to cache providerDenomination by transaction type, biller code,
     * product code and, denomination.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupProviderDenomination(ApplicationContext ctx, ServletContext context) {
        ProviderDenominationDao providerDenominationDao = (ProviderDenominationDao)
                ctx.getBean("providerDenominationDao");
        List<ProviderDenomination> providerDenominations = providerDenominationDao.
                getAllWithTransactionTypeAndProvider();
        Map<String, List<ProviderDenomination>> providerDenominationMap =
                new HashMap<String, List<ProviderDenomination>>();

        for (ProviderDenomination denomination : providerDenominations) {
            String key = denomination.getProductDenomination().getBillerProduct().getBiller().
                    getTransactionType().getTransactionType() +
                    denomination.getProductDenomination().getBillerProduct().getBiller().getBillerCode() +
                    denomination.getProductDenomination().getBillerProduct().getProductCode() +
                    denomination.getProductDenomination().getDenomination();

            if (providerDenominationMap.containsKey(key)) {
                List<ProviderDenomination> providerDenominationList = providerDenominationMap.get(key);
                providerDenominationList.add(denomination);
            }
            else {
                List<ProviderDenomination> providerDenominationList = new ArrayList<ProviderDenomination>();
                providerDenominationList.add(denomination);
                providerDenominationMap.put(key, providerDenominationList);
            }

            denomination.setProductDenomination(null);
        }

        context.setAttribute(Constants.PROVIDER_BY_DENOMINATION, providerDenominationMap);
    }

    /**
     * Setup map to cache providerProduct by transaction type, biller code,
     * and product code.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupProviderProduct(ApplicationContext ctx, ServletContext context) {
        ProviderProductDao providerProductDao = (ProviderProductDao)
                ctx.getBean("providerProductDao");
        List<ProviderProduct> providerProducts = providerProductDao.getAllWithTransactionTypeAndProvider();
        Map<String, List<ProviderProduct>> providerProductMap = new HashMap<String, List<ProviderProduct>>();

        for (ProviderProduct providerProduct : providerProducts) {
            String key = providerProduct.getBillerProduct().getBiller().getTransactionType().getTransactionType() +
                    providerProduct.getBillerProduct().getBiller().getBillerCode() +
                    providerProduct.getBillerProduct().getProductCode();

            if (providerProductMap.containsKey(key)) {
                List<ProviderProduct> ppList = providerProductMap.get(key);
                ppList.add(providerProduct);
            }
            else {
                List<ProviderProduct> ppList = new ArrayList<ProviderProduct>();
                ppList.add(providerProduct);
                providerProductMap.put(key, ppList);
            }

            providerProduct.setBillerProduct(null);
        }

        context.setAttribute(Constants.PROVIDER_BY_PRODUCT, providerProductMap);
    }

    private void setupAirport(ApplicationContext ctx, ServletContext context) {
        AirportDao airportDao = (AirportDao) ctx.getBean("airportDao");
        List<Airport> airports = airportDao.getAll();
        Map<Long, List<Airport>> airportMap = new HashMap<Long, List<Airport>>();

        airportMap.put(0L, airports);
        context.setAttribute(AirConstants.AEROTICKETING.AIRPORT, airportMap);
    }
    
    private void setupCRAirport(ApplicationContext ctx, ServletContext context) {
        CustomerRegisterDao customerRegisterDao = (CustomerRegisterDao) ctx.getBean("customerRegisterDao");
        List<CustomerRegister> customerRegisters = customerRegisterDao.getAirportsFrom();	//sudah urut
        Map<String, List<CustomerRegister>> crMap = new LinkedHashMap<String, List<CustomerRegister>>();	//That is, when iterating a LinkedHashMap, the elements will be returned in the order in which they were inserted.
        for (int i = customerRegisters.size() - 1; i >= 0; i--) {
        	CustomerRegister customerRegister = customerRegisters.get(i);
        	String key = "" + customerRegister.getCustomerId() + customerRegister.getTransactionType() + customerRegister.getData1();
            if (crMap.containsKey(key)) {
                List<CustomerRegister> crs = crMap.get(key);
                crs.add(customerRegister);
            }
            else {
                List<CustomerRegister> crs = new ArrayList<CustomerRegister>();
                crs.add(customerRegister);
                crMap.put(key, crs);
            }
        }
        
        context.setAttribute(Constants.SELECTED_CR_AIRPORT, crMap);
    }

    public List<Airport> getCRAirport(String customerId, String transactionType, String billerCode, String fromTo) {    	
		Map<String, List<CustomerRegister>> crsMap = (Map<String, List<CustomerRegister>>) context.getAttribute(Constants.SELECTED_CR_AIRPORT);
		String key = customerId + transactionType + billerCode;
		
		List<CustomerRegister> crs = crsMap.get(key);
		if (crs != null) {
			Collections.sort(crs, new Comparator<CustomerRegister>() {
	            @Override
	            public int compare(CustomerRegister cr1, CustomerRegister cr2) {
	                return cr1.getCustomerReference().compareTo(cr2.getCustomerReference());
	            }
	        });
			List<Airport> airports = new ArrayList<Airport>();
			for (CustomerRegister cr : crs) {
				Airport airport = (fromTo.equals("FROM")) ? getAirport(cr.getData2()) : getAirport(cr.getData3());
				airports.add(airport);
			}
	
			HashSet<Airport> hs = new HashSet<Airport>();	//buang airport yg sama
	        hs.addAll(airports);
	        airports.clear();
	        airports.addAll(hs);
	        
	        if (airports.size() <= 5) {
	        	return airports;	
	        } else {
	        	List<Airport> fiveAirports = new ArrayList<Airport>();
	        	for (int i = 0; i < 5; i++) {
	        		fiveAirports.add(airports.get(i));
	        	}
	        	return fiveAirports;
	        }         
		}
		return null;
	}

    public List<CustomerRegister> getCRForAero(String customerId, String transactionType, String billerCode) {    	
		Map<String, List<CustomerRegister>> crsMap = (Map<String, List<CustomerRegister>>) context.getAttribute(Constants.SELECTED_CR_AIRPORT);
		String key = customerId + transactionType + billerCode;
		List<CustomerRegister> crs = crsMap.get(key);
		if (crs != null) {
			Collections.sort(crs, new Comparator<CustomerRegister>() {
	            @Override
	            public int compare(CustomerRegister cr1, CustomerRegister cr2) {
	                return cr1.getCustomerReference().compareTo(cr2.getCustomerReference());
	            }
	        });
		}
        return crs;   
	}
    
    private void setupAirline(ApplicationContext ctx, ServletContext context) {
        AirlineDao airlineDao = (AirlineDao) ctx.getBean("airlineDao");
        List<Airline> airlines = airlineDao.getAll();
        Map<Long, List<Airline>> airlineMap = new HashMap<Long, List<Airline>>();

        airlineMap.put(0L, airlines);
        context.setAttribute(AirConstants.AEROTICKETING.AIRLINE, airlineMap);
    }
    
    private void setupKai(ApplicationContext ctx, ServletContext context) {
        TrainStationDao trainStationDao = (TrainStationDao) ctx.getBean("trainStationDao");
        List<TrainStation> stationList = trainStationDao.getAll();
        context.setAttribute(Constants.TRAIN_STATION, stationList);
    }

    
    /**
     * Setup map to cache formConfig by formName
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupFormConfig(ApplicationContext ctx, ServletContext context) {
        FormConfigDao formConfigDao = (FormConfigDao) ctx.getBean("formConfigDao");
        List<FormConfig> formConfigs = formConfigDao.getAll();
        Map<String, FormConfig> formConfigMap = new HashMap<String, FormConfig>();
        for (FormConfig formConfig : formConfigs) {
            formConfigMap.put(formConfig.getFormName(), formConfig);
        }

        context.setAttribute(Constants.FORM_CONFIG_BY_FORM, formConfigMap);
    }

    /**
     * Setup map to cache paramter by parameterName
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupParameter(ApplicationContext ctx, ServletContext context) {
        ParameterDao parameterDao = (ParameterDao) ctx.getBean("parameterDao");
        List<Parameter> parameters = parameterDao.getAll();
        Map<String, Parameter> parameterMap = new HashMap<String, Parameter>();
        for (Parameter parameter : parameters) {
            parameterMap.put(parameter.getParameterName(), parameter);
        }

        context.setAttribute(Constants.PARAMETER_BY_NAME, parameterMap);
    }

    /**
     * Setup map to cache all menu by id, bucket #0 is special one, being used to host all root node.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupMenu(ApplicationContext ctx, ServletContext context) {
        MenuDao menuDao = (MenuDao) ctx.getBean("menuDao");
        List<Menu> menus = menuDao.getAllByHierarchy();

        List<Menu> roots = new ArrayList<Menu>();
        Map<Long, Object> menuMap = new HashMap<Long, Object>();

        Menu parent;
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);

            if (menu.getParentId() == 0) {
                roots.add(menu);
                continue;
            }

            parent = (Menu) menuMap.get(menu.getParentId());
            parent.getChildrens().add(menu);
            menu.setParent(parent);
        }

        menuMap.put(0L, roots);
        context.setAttribute(Constants.MENU_MAP_BY_ID, menuMap);
    }

    /**
     * Setup map to cache all menu by id, bucket #0 is special one, being used to host all root node.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupMenuMerchantEdcAndHiperwallet(ApplicationContext ctx, ServletContext context) {
        MenuDao menuDao = (MenuDao) ctx.getBean("menuDao");
        List<Menu> menus = menuDao.getMerchantEdcAndHiperwalletMenu();

        List<Menu> roots = new ArrayList<Menu>();
        Map<Long, Object> menuMap = new HashMap<Long, Object>();

        Menu parent;
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);

            if (menu.getParentId() == 0) {
                roots.add(menu);
                continue;
            }

            parent = (Menu) menuMap.get(menu.getParentId());
            parent.getChildrens().add(menu);
            menu.setParent(parent);
        }

        menuMap.put(0L, roots);
        context.setAttribute(Constants.MENU_MAP_BY_ID2, menuMap);
    }
    
    private void setupMenuMerchant(ApplicationContext ctx, ServletContext context) {
        MenuDao menuDao = (MenuDao) ctx.getBean("menuDao");
        List<Menu> menus = menuDao.getMerchantMenu();

        List<Menu> roots = new ArrayList<Menu>();
        Map<Long, Object> menuMap = new HashMap<Long, Object>();

        Menu parent;
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);

            if (menu.getParentId() == 0) {
                roots.add(menu);
                continue;
            }

            parent = (Menu) menuMap.get(menu.getParentId());
            parent.getChildrens().add(menu);
            menu.setParent(parent);
        }

        menuMap.put(0L, roots);
        context.setAttribute(Constants.MENU_MAP_BY_ID_MERCHANT, menuMap);
    }
    
    
    private void setupMenuMerchantPac(ApplicationContext ctx, ServletContext context) {
        MenuDao menuDao = (MenuDao) ctx.getBean("menuDao");
        List<Menu> menus = menuDao.getMerchantMenuPac();

        List<Menu> roots = new ArrayList<Menu>();
        Map<Long, Object> menuMap = new HashMap<Long, Object>();

        Menu parent;
        for (Menu menu : menus) {
            menuMap.put(menu.getId(), menu);

            if (menu.getParentId() == 0) {
                roots.add(menu);
                continue;
            }

            parent = (Menu) menuMap.get(menu.getParentId());
            parent.getChildrens().add(menu);
            menu.setParent(parent);
        }

        menuMap.put(0L, roots);
        context.setAttribute(Constants.MENU_MAP_BY_ID_PAC, menuMap);
    }
    
    /**
     * Setup map to cache all currency by swift code.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupCurrency(ApplicationContext ctx, ServletContext context) {
        CurrencyDao currencyDao = (CurrencyDao) ctx.getBean("currencyDao");
        List<Currency> currencies = currencyDao.getAll();
        Map<String, Currency> currencyMap = new HashMap<String, Currency>();

        for (Currency currency : currencies) {
            currencyMap.put(currency.getSwiftCode(), currency);
        }

        context.setAttribute(Constants.CURRENCY_MAP_BY_CODE, currencyMap);
    }
    
    /**
     * Setup map to cache AccountException object by transaction type.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupAccountException(ApplicationContext ctx, ServletContext context) {
    	 AccountExceptionDao accountExceptionDao = (AccountExceptionDao) ctx.getBean("accountExceptionDao");
         List<AccountException> accountExceptions = accountExceptionDao.getAll();
         Map<String, AccountException> accountExceptionMap = new HashMap<String, AccountException>();

         for (AccountException accountExeption : accountExceptions) {
             accountExceptionMap.put(accountExeption.getAccountNumber(), accountExeption);
         }

         context.setAttribute(Constants.ACCOUNT_EXCEPTION_MAP_BY_ACCOUNT_NUMBER, accountExceptionMap);
    }

    /**
     * Setup list to cache SMS Token prefix.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupSmsTokenPrefix(ApplicationContext ctx, ServletContext context) {

        Set<String> smsTokenPrefixes = new HashSet<String>();
        String strSmsTokenPrefixes = this.getParameter("SMS_TOKEN_PREFIX").getParameterValue();

        StringTokenizer tokenPrefixes = new StringTokenizer(strSmsTokenPrefixes,",");
        while (tokenPrefixes.hasMoreTokens()){

            String tokenPrefix = tokenPrefixes.nextToken();
            smsTokenPrefixes.add(tokenPrefix);
        }

        context.setAttribute(Constants.LIST_SMS_TOKEN_PREFIX, smsTokenPrefixes);
    }
    
    private void setupMobileRegistrationPrefix(ApplicationContext ctx, ServletContext context) {

        Set<String> smsTokenPrefixes = new HashSet<String>();
        String strSmsTokenPrefixes = this.getParameter("MOBILE_REGISTRATION_PREFIX").getParameterValue();

        StringTokenizer tokenPrefixes = new StringTokenizer(strSmsTokenPrefixes,",");
        while (tokenPrefixes.hasMoreTokens()){

            String tokenPrefix = tokenPrefixes.nextToken();
            smsTokenPrefixes.add(tokenPrefix);
        }

        context.setAttribute(Constants.LIST_MOBILE_REGISTRATION_PREFIX, smsTokenPrefixes);
    }

    /**
     * Setup map of feature code.
     * @param ctx ApplicationContext object
     * @param context ServletContext object
     */
    private void setupFeatureCode(ApplicationContext ctx, ServletContext context) {

        Map<String,MFeature> features = new HashMap<String,MFeature>();
        FeatureDao featureDao = (FeatureDao) ctx.getBean("featureDao");

        List<MFeature> fs = featureDao.getAll();

        for (MFeature f: fs){
            features.put(f.getTransactionType() + "|" + f.getProviderCode(), f);
        }

        context.setAttribute(Constants.FEATURE_CODE_MAP, features);
    }

    /**
     *
     * @param ctx
     * @param context
     */
    private void setupIGateBillerProduct(ApplicationContext ctx, ServletContext context) {

        Map<String,MIGateBillerProduct> map = new HashMap<String,MIGateBillerProduct>();
        IGateBillerProductDao iGateBillerProductDao = (IGateBillerProductDao) ctx.getBean("iGateBillerProductDao");

        List<MIGateBillerProduct> is = iGateBillerProductDao.getAll();

        for (MIGateBillerProduct i: is){
            map.put(i.getBillerCode() + "|" + i.getProductCode(), i);
        }

        context.setAttribute(Constants.IGATE_BILLER_PRODUCT_MAP, map);
    }

    /**
     * Get all biller by transaction type.
     * @param transactionType transaction type
     * @return list of biller
     */
    public List<Biller> getBillers(String transactionType) {
        Map<String, List<Biller>> billerMap = (Map<String, List<Biller>>)
                context.getAttribute(Constants.BILLER_BY_TRANSACTION_TYPE);
        List<Biller> billers = billerMap.get(transactionType);
        Collections.sort(billers);
        return billers;
    }
    
    
    public List<Biller> getBillersMerchant(String transactionType) {
        Map<String, List<Biller>> billerMap = (Map<String, List<Biller>>)
                context.getAttribute(Constants.BILLER_BY_TRANSACTION_TYPE_MERCHANT);
        List<Biller> billers = billerMap.get(transactionType);
        Collections.sort(billers);
        return billers;
    }

    /**
     * Get all biller by transaction type and biller code.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @return list of billerProduct
     */
    public List<BillerProduct> getBillerProducts(String transactionType, String billerCode) {
        Map<String, List<BillerProduct>> billerProductMap = (Map<String, List<BillerProduct>>)
                context.getAttribute(Constants.PRODUCT_BY_BILLER);
        String key = transactionType + billerCode;
        List<BillerProduct> products = billerProductMap.get(key);
        Collections.sort(products);
        return products;
    }

    /**
     * Get all product denomination by transaction type, biller code, and product code.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @param productCode biller product code
     * @return list of productDenomination
     */
    public List<ProductDenomination> getProductDenominations(String transactionType, String billerCode,
                                                             String productCode) {
        Map<String, List<ProductDenomination>> productDenominationMap = (Map<String, List<ProductDenomination>>)
                context.getAttribute(Constants.DENOMINATION_BY_PRODUCT);
        String key = transactionType + billerCode + productCode;
        return productDenominationMap.get(key);
    }

    /**
     * Get all provider denomination by transaction type, biller code, product code, and
     * denomination.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @param productCode biller product code
     * @param denomination denomination
     * @return list of providerDenomination
     */
    public List<ProviderDenomination> getProviderDenominations(String transactionType, String billerCode,
                                                               String productCode, String denomination) {
        Map<String, List<ProviderDenomination>> providerDenominationMap = (Map<String, List<ProviderDenomination>>)
                context.getAttribute(Constants.PROVIDER_BY_DENOMINATION);
        String key = transactionType + billerCode + productCode + denomination;
        List<ProviderDenomination> prodDenoms = providerDenominationMap.get(key);
        Collections.sort(prodDenoms, new ProviderDenomination());
        return prodDenoms;         
    }

    /**
     * Get all provider by transaction type, biller code, product code and denomination.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @param productCode biller product code
     * @param denomination denomination
     * @return list of provider
     */
    public List<Provider> getProviders(String transactionType, String billerCode, String productCode,
                                       String denomination) {
        List<Provider> result = new ArrayList<Provider>();
        List<ProviderDenomination> pds = getProviderDenominations(transactionType, billerCode, productCode,
                denomination);

        if (pds != null) {
            for (ProviderDenomination pd : pds) {
                result.add(pd.getProvider());
            }
        }


        return result;
    }
	public Boolean isAccountException(String accountNumber){
		Boolean result = false;
		try{
			Map<String, List<AccountException>> accountExceptionMap = (Map<String, List<AccountException>>)
	                context.getAttribute(Constants.ACCOUNT_EXCEPTION_MAP_BY_ACCOUNT_NUMBER);
	        String key = accountNumber;
//	        List<AccountException> aex = accountExceptionMap.get(key);
//	        Collections.sort(aex, new AccountException());
	        if(accountExceptionMap.get(key) != null){
	        	result = true;
	        }
		}catch (DataAccessException e) {
			System.out.println("error :" +e.getMessage());
		}		
		System.out.println("result : "+result);
		return result;
	}
	
    /**
     * Get all provider by transaction type, biller code, and product code.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @param productCode biller product code
     * @return list of provider
     */
    public List<Provider> getProviders(String transactionType, String billerCode, String productCode) {
        Map<String, List<ProviderProduct>> providerProductMap = (Map<String, List<ProviderProduct>>)
                context.getAttribute(Constants.PROVIDER_BY_PRODUCT);
        String key = transactionType + billerCode + productCode;

        List<Provider> ps = new ArrayList<Provider>();
        List<ProviderProduct> pps = providerProductMap.get(key);

        if (pps != null) {
            for (ProviderProduct pp : pps) {
                ps.add(pp.getProvider());
            }
        }

        return ps;
    }
    public List<City> getCities() {
    	Map<Long, Object> cityMap = (Map<Long, Object>) context.getAttribute(AirConstants.AEROTICKETING.CITY);
        return (List<City>) cityMap.get(0L);
    }
    public List<Airport> getAirports() {
    	Map<Long, Object> airportMap = (Map<Long, Object>) context.getAttribute(AirConstants.AEROTICKETING.AIRPORT);
        return (List<Airport>) airportMap.get(0L);
    }
    public List<Airline> getAirlines() {
    	Map<Long, Object> airlineMap = (Map<Long, Object>) context.getAttribute(AirConstants.AEROTICKETING.AIRLINE);
        return (List<Airline>) airlineMap.get(0L);
    }
    public Airline getAirlineByCode(String code) {
    	List<Airline> airlines = getAirlines();
    	for (Airline airline : airlines) {
			if (code.equals(airline.getAirlineCode())) {
				return airline;
			}
		}
    	return null;
    }
    public Airline getAirlineById(String id) {
    	List<Airline> airlines = getAirlines();
    	for (Airline airline : airlines) {
			if (id.equals(airline.getAirlineId())) {
				return airline;
			}
		}
    	return null;
    }
    public Airport getAirport(String airportCode) {
    	List<Airport> airports = getAirports();
    	for (Airport airport : airports) {
			if (airport.getAirportCode().equals(airportCode)) return airport;
		}
        return null;
    }
    /**
     * Get all root menu.
     * @return list of root menu
     */
    public List<Menu> getMenuRoots() {
        Map<Long, Object> menuMap = (Map<Long, Object>) context.getAttribute(Constants.MENU_MAP_BY_ID);
        return (List<Menu>) menuMap.get(0L);
    }
    public List<Menu> getMenuRootsForMerchant() {
        Map<Long, Object> menuMap = (Map<Long, Object>) context.getAttribute(Constants.MENU_MAP_BY_ID_MERCHANT);
        return (List<Menu>) menuMap.get(0L);
    }
    public List<Menu> getMenuRootsForMerchantPac() {
        Map<Long, Object> menuMap = (Map<Long, Object>) context.getAttribute(Constants.MENU_MAP_BY_ID_PAC);
        return (List<Menu>) menuMap.get(0L);
    }
    /**
     * Get all root menu.
     * @return list of root menu
     */
    public List<Menu> getMenuRootsForMerchantEdc() {
        Map<Long, Object> menuMap = (Map<Long, Object>) context.getAttribute(Constants.MENU_MAP_BY_ID2);
        return (List<Menu>) menuMap.get(0L);
    }
    
    /**
     * Get specific provider denomination by transaction type, biller code, product code,
     * denomination and provider denomination id.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @param productCode biller product code
     * @param denomination product denomination
     * @param providerCode provider code
     * @return providerDenomination object
     */
    public ProviderDenomination getProviderDenomination(String transactionType, String billerCode,
                                                        String productCode, String denomination,
                                                        String providerCode) {
        Map<String, List<ProviderDenomination>> providerDenominationMap =
                (HashMap<String, List<ProviderDenomination>>)
                context.getAttribute(Constants.PROVIDER_BY_DENOMINATION);
        String key = transactionType + billerCode + productCode + denomination;
        List<ProviderDenomination> providerDenominations = providerDenominationMap.get(key);

        ProviderDenomination result = null;
        for (ProviderDenomination providerDenomination : providerDenominations) {
            if (providerDenomination.getProvider().getProviderCode().equals(providerCode)) {
                result = providerDenomination;
                break;
            }
        }

        return result;
    }

    /**
     * Get specific biller from transaction type and biller code.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @return biller object
     */
    public Biller getBiller(String transactionType, String billerCode) {
        Map<String, List<Biller>> billerMap = (Map<String, List<Biller>>)
                context.getAttribute(Constants.BILLER_BY_TRANSACTION_TYPE);
        List<Biller> billers = billerMap.get(transactionType);
        Biller result = null;
        for (Biller biller : billers) {
            if (biller.getBillerCode().equals(billerCode)) {
                result = biller;
                break;
            }
        }

        return result;
    }

    /**
     * Get specific biller product from transaction type, biller code, and product code.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @param productCode product code
     * @return billerProduct object
     */
    public BillerProduct getBillerProduct(String transactionType, String billerCode, String productCode) {
        Map<String, List<BillerProduct>> billerProductMap = (Map<String, List<BillerProduct>>)
                context.getAttribute(Constants.PRODUCT_BY_BILLER);
        String key = transactionType + billerCode;
        List<BillerProduct> products = billerProductMap.get(key);
        BillerProduct result = null;
        for (BillerProduct product : products) {
            if (product.getProductCode().equals(productCode)) {
                result = product;
                break;
            }
        }

        return result;
    }

    /**
     * Get specific provider product from transaction type, biller code, product code, and provider code.
     * @param transactionType transaction type
     * @param billerCode biller code
     * @param productCode biller product code
     * @param providerCode provider code
     * @return provider object
     */
    public ProviderProduct getProviderProduct(String transactionType, String billerCode, String productCode,
                                String providerCode) {
        Map<String, List<ProviderProduct>> providerProductMap = (Map<String, List<ProviderProduct>>)
                context.getAttribute(Constants.PROVIDER_BY_PRODUCT);
        String key = transactionType + billerCode + productCode;
        List<ProviderProduct> pps = providerProductMap.get(key);
        ProviderProduct result = null;
        for (ProviderProduct pp : pps) {
            if (pp.getProvider().getProviderCode().equals(providerCode)) {
                result = pp;
                break;
            }
        }

        return result;
    }

    /**
     * Get form config by form name.
     * @param formName form name
     * @return form config object
     */
    public FormConfig getFormConfig(String formName) {
        Map<String, FormConfig> formConfigMap = (Map<String, FormConfig>)
                context.getAttribute(Constants.FORM_CONFIG_BY_FORM);

        return formConfigMap.get(formName);
    }

    /**
     * Get parameter by parameter name.
     * @param parameterName parameter name
     * @return parameter object
     */
    public Parameter getParameter(String parameterName) {
        Map<String, Parameter> parameterMap = (Map<String, Parameter>)
                context.getAttribute(Constants.PARAMETER_BY_NAME);

        return parameterMap.get(parameterName);
    }

    /**
     * Get menu by menu id.
     * @param menuId menu id
     * @return menu object
     */
    public Menu getMenu(Long menuId) {
        Map<Long, Object> menuMap = (Map<Long, Object>) context.getAttribute(Constants.MENU_MAP_BY_ID);
        return (Menu) menuMap.get(menuId);
    }

    /**
     * Get currency by currency code.
     * @param swiftCode swiftCode code
     * @return currency object
     */
    public Currency getCurrency(String swiftCode) {
        Map<String, Currency> currencyMap = (Map<String, Currency>) context.
                getAttribute(Constants.CURRENCY_MAP_BY_CODE);

        return currencyMap.get(swiftCode);
    }

    private void setupCellularPrefix(ApplicationContext ctx, ServletContext context)
    {
        CellularPrefixDao cellularPrefixDao = (CellularPrefixDao) ctx.getBean("cellularPrefixDao");
        List<CellularPrefix> cellularPrefixs = cellularPrefixDao.getAllCellularPrefix();
        Map<String, List<CellularPrefix>> mapResult = new HashMap<String, List<CellularPrefix>>();
        for (CellularPrefix cellularPrefix : cellularPrefixs)
        {
            String key = cellularPrefix.getBillerProductId() + "";
            if(mapResult.containsKey(key))
                mapResult.get(key).add(cellularPrefix);
            else
            {
                ArrayList<CellularPrefix> alCellular = new ArrayList<CellularPrefix>();
                alCellular.add(cellularPrefix);
                mapResult.put(key,alCellular);
            }
        }
        context.setAttribute(Constants.CELLULAR_PREFIX_BY_ID, mapResult);
    }

    public List<CellularPrefix> getCellularPrefixListByBillerProductId(String billerProductId)
    {
        return ((Map<String, List<CellularPrefix>>) context.getAttribute(Constants.CELLULAR_PREFIX_BY_ID)).get(billerProductId);
    }

    /**
     * validate if given phone number has valid prefix
     * @param phoneNumber
     * @return
     */
    public Boolean isValidSmsTokenPrefix(String phoneNumber){

        if (phoneNumber == null || phoneNumber.length() < 5) return Boolean.FALSE;

        Set<String> smsTokenPrefixes = (Set<String>)context.getAttribute(Constants.LIST_SMS_TOKEN_PREFIX);
        return (smsTokenPrefixes.contains(phoneNumber.substring(0,4)) ||
                smsTokenPrefixes.contains(phoneNumber.substring(0,5)));
    }

    public Boolean isValidMobileRegistrationPrefix(String phoneNumber){

        if (phoneNumber == null || phoneNumber.length() < 5) return Boolean.FALSE;

        Set<String> smsTokenPrefixes = (Set<String>)context.getAttribute(Constants.LIST_MOBILE_REGISTRATION_PREFIX);
        return (smsTokenPrefixes.contains(phoneNumber.substring(0,4)) ||
                smsTokenPrefixes.contains(phoneNumber.substring(0,5)));

    }
    
    /**
     * Validate and reconstruct a valid SMS Token Phone Number
     * @param phoneNumber
     * @return valid phone number or null if sms token phone number is not valid
     */
    public String validateSmsTokenPhoneNumber(String phoneNumber){

        if (phoneNumber == null || phoneNumber.trim().length() == 0) return null;

        if (phoneNumber.startsWith("0")){
            phoneNumber = "62" + phoneNumber.substring(1);
        }

        if (isValidSmsTokenPrefix(phoneNumber)){
            return phoneNumber;
        }

        return null;
    }

    public String validateMobileRegistrationPhoneNumber(String phoneNumber){

        if (phoneNumber == null || phoneNumber.trim().length() == 0) return null;

        if (phoneNumber.startsWith("0")){
            phoneNumber = "62" + phoneNumber.substring(1);
        }

        if (isValidMobileRegistrationPrefix(phoneNumber)){
            return phoneNumber;
        }

        return null;
    }

    /**
     * Get feature code by transactionType and providerCode
     * @param transactionType
     * @param providerCode
     * @return
     */
    public MFeature getFeatureCode(String transactionType, String providerCode){

        Map<String,MFeature> map = (Map<String,MFeature>) context.getAttribute(Constants.FEATURE_CODE_MAP);
        MFeature f = map.get(transactionType + "|" + providerCode);

//        if (f == null) {
//            logger.info("Get MFeature for tt="+ transactionType + " providerCode=" + providerCode + " return null");
//        }
//        else {
//            logger.info("Get MFeature for tt="+ transactionType + " providerCode=" + providerCode + " return " + f.getFeatureName());
//        }

        return f;
    }

    /**
     *
     * @param billerCode
     * @param productCode
     * @return
     */
    public MIGateBillerProduct getIGateBillerProduct(String billerCode, String productCode){

        Map<String,MIGateBillerProduct> map = (Map<String,MIGateBillerProduct>) context.getAttribute(Constants.IGATE_BILLER_PRODUCT_MAP);
        MIGateBillerProduct i = map.get(billerCode + "|" + productCode);

//        if (i == null) {
//            logger.info("Get MIGateBillerProduct for biller="+ billerCode + " product=" + productCode + " return null");
//        }
//        else {
//            logger.info("Get MIGateBillerProduct for biller="+ billerCode + " product=" + productCode + " return " + i.getIndustrialCode()+i.getiGateBillerCode()+","+i.getiGateProductCode());
//        }

        return i;
    }
    
    public List<TrainStation> getTrainStation() {
    	return (List<TrainStation>) context.getAttribute(Constants.TRAIN_STATION);
    }
    

}
