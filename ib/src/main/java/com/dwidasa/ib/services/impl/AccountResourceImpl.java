package com.dwidasa.ib.services.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.FormParam;

import com.dwidasa.ib.pages.util.DemoListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.codehaus.jackson.type.TypeReference;

import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.CustomerAccountDao;
import com.dwidasa.engine.dao.IbMerchantDao;
import com.dwidasa.engine.dao.ParameterDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.IbMerchant;
import com.dwidasa.engine.model.view.AccountSsppView;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.CustomerAccountView;
import com.dwidasa.engine.model.view.CustomerMerchant;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.model.view.PortfolioView;
import com.dwidasa.engine.service.BalanceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.IsoBitmapService;
import com.dwidasa.engine.service.MessageMailer;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.AccountResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 10:59 AM
 */
@PublicPage
public class AccountResourceImpl implements AccountResource {
    private static Logger logger = Logger.getLogger( AccountResourceImpl.class );
    @Inject
    private AccountService accountService;

    @Inject
    private BalanceService balanceService;

    @Inject
    private IsoBitmapService isoBitmapService;
    
    @Inject
    private CustomerAccountDao customerAccountDao;

    @Inject
    private MessageMailer messageMailer;
    
    @Inject
    private PaymentService paymentService;
    
    @Inject
    private CustomerService customerService;

    @Inject
    private RequestGlobals requestGlobals;
    
    @Inject
	private ParameterDao parameterDao;
    
    @Inject
    private IbMerchantDao ibMerchantDao;
    
    @Inject
    private ExtendedProperty extendedProperty;
    
    public AccountResourceImpl() {
    }

    public String getCustomerAccounts(Long customerId, String sessionId) {
        List<AccountView> accounts = accountService.getAccounts(customerId);
        return PojoJsonMapper.toJson(accounts);
    }

    public String getCustomerAccountsPost(Long customerId, String sessionId) {
        return getCustomerAccounts(customerId, sessionId);
    }
    
    public String getAccountBalance(Long customerId, String sessionId, String json) {
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        view = accountService.getAccountBalance(view);

        return PojoJsonMapper.toJson(view);
    }

    public String getAccountBalancePost(Long customerId, String sessionId, String json) {
        return getAccountBalance(customerId, sessionId, json);
    }

    public String getAccountBalances(Long customerId, String sessionId, String json){

        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        List<AccountView> accounts = accountService.getAccountBalances(view);

        return PojoJsonMapper.toJson(accounts);
    }

    public String getAccountBalancesPost(Long customerId, String sessionId, String json) {
        return getAccountBalances(customerId, sessionId, json);
    }

    public String getAccountStatement(Long customerId, String sessionId, String startDate, String endDate, String json) throws ParseException {

       SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");

        // check date
        Date start = DateUtils.truncate(sdf.parse(startDate));
        Date end = DateUtils.truncate(sdf.parse(endDate));

        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        view.setCustomerId(customerId);
        List<AccountStatementView> stats = accountService.getAccountStatement(view, start, end);
        recalculateRunningBalanceForMb(stats);
        return PojoJsonMapper.toJson(stats);
    }
    

    /**
     * Cara menghitung saldo akhir di mobile banking agak berbeda
     * menyesuaikan untuk MB agar tidak perlu perbaikan di native apps
     * @param asvs
     */
    private void recalculateRunningBalanceForMb(List<AccountStatementView> asvs){

        for (AccountStatementView asv: asvs){

            if ("D".equals(asv.getTransactionIndicator())){
                asv.setRunningBalance(asv.getRunningBalance().add(asv.getAmount()));
            }
            else {
                asv.setRunningBalance(asv.getRunningBalance().subtract(asv.getAmount()));
            }
        }

    }

    public String getAccountStatementPost(Long customerId, String sessionId, String startDate, String endDate, String json) throws ParseException {
    	return getAccountStatement(customerId, sessionId, startDate, endDate, json);
    }

    public String getAccountStatementApp(String startDate, String endDate, String json) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");

        // check date
        Date start = DateUtils.truncate(sdf.parse(startDate));
        Date end = DateUtils.truncate(sdf.parse(endDate));

        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        List<AccountStatementView> stats = accountService.getAccountStatement(view, start, end);
        recalculateRunningBalanceForMb(stats);
        return PojoJsonMapper.toJson(stats);
    }

    public String getAccountStatementAppPost(String startDate, String endDate, String json) throws ParseException {
        return getAccountStatementApp(startDate, endDate, json);

    }

    public String getAccountStatementDemoPost(Long customerId, String sessionId, String startDate, String endDate, String json) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        // check date
        Date start = DateUtils.truncate(sdf.parse(startDate));
        Date end = DateUtils.truncate(sdf.parse(endDate));
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        view.setCustomerId(customerId);
        List<AccountStatementView> stats = accountService.getAccountStatement(view, start, end);
        recalculateRunningBalanceForMb(stats);
        //bikin data sendiri
        List<AccountStatementView> datas = DemoListUtils.getAccountStatementViews();
        stats.addAll(datas);
        return PojoJsonMapper.toJson(stats);
    }


    public String getLastNStatementPost(Long customerId, String sessionId, Integer n, String json) throws ParseException {
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        List<AccountStatementView> stats = accountService.getLastNTransaction(view, n);
        return PojoJsonMapper.toJson(stats);
    }

    public String getTransactionStatus(Long customerId, String sessionId, String startDate,
                                       String endDate, String json) throws ParseException {
        //logger.info("startDate " + startDate + ", endDate " + endDate + ", json = " + json);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        List<AccountStatementView> stats = accountService.getTransactionStatus(view,
                com.dwidasa.engine.Constants.PENDING_STATUS, sdf.parse(startDate), sdf.parse(endDate));
        return PojoJsonMapper.toJson(stats);
    }

    public String checkTransactionStatus(Long customerId, String sessionId, String startDate,
                                       String endDate, String json) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        List<AccountStatementView> stats = accountService.checkTransactionStatus(view);
        return PojoJsonMapper.toJson(stats);
    }


    public String getTransactionStatusPost(Long customerId, String sessionId, String startDate, String endDate, String json) throws ParseException {
        return getTransactionStatus(customerId, sessionId, startDate, endDate, json);
    }

    public String getTransactionHistory(Long customerId, String sessionId, String startDate,
                                        String endDate, String json) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        List<AccountStatementView> stats = accountService.getTransactionHistory(view,
                sdf.parse(startDate), sdf.parse(endDate));
        return PojoJsonMapper.toJson(stats);
    }

    public String getTransactionHistoryPost(Long customerId, String sessionId, String startDate, String endDate, String json) throws ParseException {
        return getTransactionHistory(customerId, sessionId, startDate, endDate, json);
    }

    public String deactivateCard(Long customerId, String deviceId, String sessionId, String token, String json) {
        return deactiveCardExtract(json);
    }

    public String deactivateCard2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return deactiveCardExtract(json);
    }

    public String deactivateCardPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return deactiveCardExtract(json);
    }

    public String deactivateCardPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return deactiveCardExtract(json);
    }

    private String deactiveCardExtract(String json) {
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        accountService.deactivateCard(view);

        return Constants.OK;
    }
              
    public String inquiryAccount(Long customerId, String sessionId, String json) {
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        List<AccountView> avs = accountService.getCards(view);
        return PojoJsonMapper.toJson(avs);
    }

    public String inquiryAccountPost(Long customerId, String sessionId, String json) {
        return inquiryAccount(customerId, sessionId, json);
    }

    public String mailStatement(Long customerId, String sessionId, String json) throws Exception {

        List<AccountStatementView> stats = PojoJsonMapper.fromJson(json,
                new TypeReference<List<AccountStatementView>>() {});
        messageMailer.sendAccountMessage(customerId, stats);

        return Constants.OK;
    }

    public String mailStatementPost(Long customerId, String sessionId, String json) throws Exception {
        return mailStatement(customerId, sessionId, json);
    }

    public String lotteryViewPointsPost(Long customerId, String sessionId, String json) {
        return lotteryViewPointsExtract(json);
    }

    public String lotteryViewPointsPostPin(@FormParam("customerId") Long customerId, @FormParam("sessionId") String sessionId, @FormParam("json") String json) {
        return lotteryViewPointsExtract(json);
    }

    private String lotteryViewPointsExtract(String json) {
        LotteryView view = PojoJsonMapper.fromJson(json, LotteryView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        accountService.getLotteryViewPoint(view);
        return PojoJsonMapper.toJson(view);
    }

    public String accountSsppPost(String json) {
    	logger.info("json=" + json);
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        logger.info("view=" + view);
        List<AccountSsppView> sspps = accountService.getAccountSsppPerPage(view);
        return PojoJsonMapper.toJson(sspps);
    }

    public String getPortfolio( Long customerId, String sessionId, String json ) {    	
        AccountView view = PojoJsonMapper.fromJson( json, AccountView.class );
        List< PortfolioView > pvs = accountService.getPortfolioFromIGate(view, com.dwidasa.engine.Constants.PORTFOLIO.CASA);

        try {
            List< PortfolioView > pvsDeposito = accountService.getPortfolioFromIGate(view, com.dwidasa.engine.Constants.PORTFOLIO.DEPOSITO);
            if (pvsDeposito != null && pvsDeposito.size() > 0) {
        	    pvs.addAll(pvsDeposito);
            }
        }
        catch(Exception e){
            // just ignore and move to next porto if we can't get depositos
            e.printStackTrace();
        }

        try {
            List< PortfolioView > pvsLoan = accountService.getPortfolioFromIGate(view, com.dwidasa.engine.Constants.PORTFOLIO.LOAN);
            if (pvsLoan != null && pvsLoan.size() > 0) {
        	    pvs.addAll(pvsLoan);
            }
        }
        catch (Exception e){
            // just ignore if we can't get loans
            e.printStackTrace();
        }

        return PojoJsonMapper.toJson( pvs );
    }
    
    public String getPortfolioPost( Long customerId, String sessionId, String json ) {
        return getPortfolio( customerId, sessionId, json );
    }

	@Override
	public String getSsppTemenos(String json) {
		logger.info("json=" + json);
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        view.setTransactionType(com.dwidasa.engine.Constants.SSPP_TEMENOS);
        logger.info("view=" + view);
        
        //String message = messageLength + typeIdentifier + view.getAccountNumber() + time + stan.substring(stan.length() - 6) + view.getTerminalId() + rcInput;
        view = (AccountView) accountService.ssppTemenos(view);
        //bit 120 ditaruh di accountName, 122 ditaruh di productName
        logger.info("bit 48=" + view.getCustomerName() + " bit 120=" + view.getAccountName() + " bit 122=" + view.getProductName());
        /*
         * 	bit-48 = 000000000022683004051						
			bit-120 = 0501200620131KREC00000012059163900000012173797002200620132DEBD00000012173029200000000000767803201306301051C00000000000000600000000000768404201306305057D00000000000000100000000000768305201307141010C000000000015000000000000022683						
         */
        String bit48 = view.getCustomerName();
        String bit120 = view.getAccountName();
        String bit122 = view.getProductName();
        List<AccountSsppView> asvs = new ArrayList<AccountSsppView>();
        bundlingMessage(bit48, bit120, asvs);
        bundlingMessage(bit48, bit122, asvs);
//        logger.info("asvs.size()=" + asvs.size());
//        if (view != null && view.getProductId().intValue() > 0) {
//        	view.setTransactionType(com.dwidasa.engine.Constants.SSPP_TEMENOS_UPDATE);
//        	accountService.ssppTemenos(view);
//        }
        
        //set flag di item terakhir
        if (asvs.size() > 0) {
        	AccountSsppView last = asvs.get(asvs.size() - 1);
        	if (bit48.substring(bit48.length() - 1).equals("1")) 
        		last.setAgainFlag(false);
    		else
    			last.setAgainFlag(true);
        	logger.info("flag again=" + last.getAgainFlag());
        }
        
        String res = PojoJsonMapper.toJson(asvs);
        return res;
	}

	private void bundlingMessage(String bit48, String bitContent, List<AccountSsppView> asvs) {
		String trxs = bitContent.substring(2);
		logger.info("bit120/122=" + bitContent);
		logger.info("trxs=" + trxs);
		for (int i = 0; i < Integer.parseInt(bitContent.substring(0, 2)); i++) {
            AccountSsppView asv = new AccountSsppView();
            String oneTrx = trxs.substring(0 + (46 * i), 46 + (46 * i));
            int page = Integer.parseInt(bit48.substring(15, 18));   
            int row = Integer.parseInt(oneTrx.substring(0, 2));		//bit48.substring(18, 20)
            logger.info("oneTrx=" + oneTrx + " page=" + page + " row=" + row);
            asv.setPage(page);
            asv.setRow(row);
            asv.setSsppRow(row);
            /*
20111222MXAGD              30,000        9,232,643,495-ATMEDC    00704
20111222MXAGD              50,000-       9,232,693,495-ATMEDC    00705
01200620131KREC000000013183635000000018659163
02200620132DEBD000000017500000000000001159163
03201306251010D000000001000000000000000159163
04201306251010C000000000128000000000000287163
			menjadi
			_DataDummy[0] = "01   11/01/11    11            10,000,000.00                                    23,649,162.66    9999\n";
			_DataDummy[1] = "02   11/01/11    99                                     5,000,000.00            18,649,162.66    9999\n";
			
01   11/01/11    11            10,000,000.00                                    23,649,162.66    9999
02   11/01/11    99                                     5,000,000.00            18,649,162.66    9999
03   12/01/11    11            15,000,000.00                                    43,649,162.66    9999
04   13/01/11    54                                     1,250,000.00            42,399,162.66    25316
05   14/01/11    54                                     1,250,000.00            41,149,162.66    25316

format baru:
01   11.01.11    11              10.000.000                                       23,649,162    9999
02   11.01.11    99                                       5.000.000               18,649,162    9999

            */

            String content = "";
            content += "  ";
            content += oneTrx.substring(8, 10) + "." + oneTrx.substring(6, 8) + "." + oneTrx.substring(4, 6);
            content += "";
            content += StringUtils.leftPad(oneTrx.substring(10, 14).trim(), 4, " ");
            content += "  ";
            String debitCredit = formattedValue(oneTrx.substring(15, 30), 14);            
            content += oneTrx.substring(14, 15).equals("D") ? debitCredit + "                " : "                " + debitCredit;
            content += "    ";             
            content += formattedValue(oneTrx.substring(30, 45), 14);
            content += (oneTrx.substring(45).equals("-")) ? "-" : "";
            content += " ";
            content += "  ";	//oneTrx.substring(10, 14).trim(); operator Id tidak dikirim dari Core
            asv.setContent(content);
            logger.info("content=" + asv.getContent() );
            asvs.add(asv);
        }
	}

    static Integer demoIndex = 0;
    static Map bit48s;
    static Map bit120s;
    static Map ssppUpdates;

    @Override
    public String getSsppTemenosDemo(String json) {
        logger.info("inside getSsppTemenosDemo(), demoIndex=" + demoIndex);

        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
        view.setTransactionType(com.dwidasa.engine.Constants.SSPP_TEMENOS);

        //String message = messageLength + typeIdentifier + view.getAccountNumber() + time + stan.substring(stan.length() - 6) + view.getTerminalId() + rcInput;
        view = (AccountView) accountService.ssppTemenos(view);
        //bit 120 ditaruh di accountName, 122 ditaruh di productName
        //logger.info("bit 48=" + view.getCustomerName() + " bit 120=" + view.getAccountName() + " bit 122=" + view.getProductName());
        /*
         * 	bit-48 = 000000000022683004051
            bit-120 = 0501200620131KREC00000012059163900000012173797002200620132DEBD00000012173029200000000000767803201306301051C00000000000000600000000000768404201306305057D00000000000000100000000000768305201307141010C000000000015000000000000022683
         */

        if (demoIndex.intValue() == 0 || demoIndex.intValue() == 16) {
            bit48s = DemoListUtils.getBit48s();
            bit120s = DemoListUtils.getBit120s();
            ssppUpdates = DemoListUtils.getSsppUpdates();
            demoIndex = 0;
        }
        String bit48 = (String) bit48s.get(demoIndex);
        String bit120 = (String) bit120s.get(demoIndex);
        demoIndex += 1;
        //String bit122 = (String) ssppUpdates.get(demoIndex);
        logger.info("bit48=" + bit48 + "bit120=" + bit120);
        List<AccountSsppView> asvs = new ArrayList<AccountSsppView>();
        bundlingMessage(bit48, bit120, asvs);

        if (asvs.size() > 0) {
            AccountSsppView last = asvs.get(asvs.size() - 1);
            if (bit48.substring(bit48.length() - 1).equals("1"))
                last.setAgainFlag(false);
            else
                last.setAgainFlag(true);
            logger.info("flag again=" + last.getAgainFlag());
        }

        String res = PojoJsonMapper.toJson(asvs);
        return res;
    }

    @Override
   	public String ssppUpdateDemo(String json) {
   		logger.info("inside ssppUpdateDemo demoIndex-1=" + (demoIndex - 1));
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
       	view.setTransactionType(com.dwidasa.engine.Constants.SSPP_TEMENOS_UPDATE);
       	view = (AccountView) accountService.ssppTemenos(view);

        String update = (String) ssppUpdates.get(demoIndex - 1);
        logger.info("update=" + update);
        if (update.equals("0")) {
            return getSsppTemenosDemo(json);
        }
        return "OK";
   	}


	@Override
	public String ssppUpdate(String json) {
		logger.info("json=" + json);
        AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
    	view.setTransactionType(com.dwidasa.engine.Constants.SSPP_TEMENOS_UPDATE);
    	view = (AccountView) accountService.ssppTemenos(view);
    	logger.info("view.getGenerated()=" + view.getGenerated());	//if empty string then no again flag
    	logger.info("view.getResponseCode()=" + view.getResponseCode());
        if (!view.getGenerated().equals("") && view.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        	return getSsppTemenos(json);
        } 
        return "OK";
	}

	private static String formattedValue(String val, int columnLength) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.ENGLISH);
		numberFormat.setMinimumFractionDigits(0);
		numberFormat.setMaximumFractionDigits(0);
		String debitCredit = StringUtils.leftPad(numberFormat.format(Double.valueOf(val)), columnLength, " ");
		return debitCredit;
	}
	
	public static void main(String[] args) {
		System.out.println(formattedValue("000000017500000", 24));
		
	}
	
	@Override
	public String getJenisUndian(Long customerId,  String sessionId, String json) throws ParseException {
		logger.info("jenis undian customerId=" + customerId +" json=" + json);
		LotteryTransactionView lotteryView = PojoJsonMapper.fromJson(json, LotteryTransactionView.class);
		if (lotteryView.getCardData2() != null) lotteryView.setCardData2(EngineUtils.getEncryptedPin(lotteryView.getCardData2(), lotteryView.getCardNumber()));
		lotteryView.setTransactionType(com.dwidasa.engine.Constants.INQUIRY_LOTTERY);
//      
		lotteryView.setAccountNumber(getAllAccountNumber(customerId));
		lotteryView.setPosisiAwal(BigDecimal.ZERO);
        lotteryView.setTotalData(BigDecimal.ZERO);
        lotteryView.setNextAvailableFlag("");
		
        List<LotteryTransactionView> stats = accountService.getPortfolioFromIGateUndian(lotteryView, com.dwidasa.engine.Constants.TRANSLATION_LOTTERY);
        //lotteryView.setLotteryTransactionViews(stats);
        return PojoJsonMapper.toJson(stats);
	}
	
	
	public String getAllAccountNumber(Long customerId){
    	List<CustomerAccount> customerAccounts = customerAccountDao.getAllWithTypeAndCurrency(customerId);
    	String delimitter = "";
    	for(CustomerAccount account : customerAccounts){
    		if("".equals(delimitter)){
    			delimitter += account.getAccountNumber();
    		}else
    			delimitter += ","+account.getAccountNumber();
    	}
    	
    	return delimitter;
    }

	
	@Override
	public String getNomorUndian(Long customerId, String sessionId, String accountNumber, String kodeUndian,
			String json) throws ParseException {
		
		LotteryTransactionView lotteryView = PojoJsonMapper.fromJson(json, LotteryTransactionView.class);
		if (lotteryView.getCardData2() != null) lotteryView.setCardData2(EngineUtils.getEncryptedPin(lotteryView.getCardData2(), lotteryView.getCardNumber()));
		lotteryView.setTransactionType(com.dwidasa.engine.Constants.INQUIRY_NOMOR_UNDIAN);
//      
		lotteryView.setAccountNumber(accountNumber);
		lotteryView.setKodeUndian(kodeUndian);
		lotteryView.setPosisiAwal(BigDecimal.ZERO);
		
        List<LotteryTransactionView> stats = accountService.getPortfolioFromIGateNomorUndian(lotteryView, com.dwidasa.engine.Constants.TRANSLATION_LOTTERY);
        //lotteryView.setLotteryTransactionViews(stats);
        return PojoJsonMapper.toJson(stats);
	}
	
	public String getJenisUndianPost(Long customerId, String sessionId, String json) throws ParseException {
		return getJenisUndian(customerId, sessionId, json);
	}

	public String getNomorUndianPost(Long customerId, String sessionId, String accountNumber, String kodeUndian, String json) throws ParseException {
		LotteryTransactionView lotteryView = PojoJsonMapper.fromJson(json, LotteryTransactionView.class);
		return getNomorUndian(customerId, sessionId, lotteryView.getAccountNumber(), lotteryView.getKodeUndian(), json);
	}

	public String getJenisUndianPostPin(Long customerId, String sessionId, String json) throws ParseException {
		return getJenisUndian(customerId, sessionId, json);
	}

	public String getNomorUndianPostPin(Long customerId, String sessionId, String accountNumber, String kodeUndian, String json) throws ParseException {
		LotteryTransactionView lotteryView = PojoJsonMapper.fromJson(json, LotteryTransactionView.class);
		return getNomorUndian(customerId, sessionId, lotteryView.getAccountNumber(), lotteryView.getKodeUndian(), json);
	}

	
	public String getMultiRekening(Long customerId, String sessionId,
			String json) {
		
		
		AccountView view = PojoJsonMapper.fromJson(json, AccountView.class);
		System.out.println("TErminal ID "+view.getTerminalId());
		System.out.println("Extended Property "+extendedProperty.getDefaultTerminalId());
		boolean isMulti = false;
		if(view.getAccountNumber().startsWith(Constants.MERCHANT_EDC1) || view.getAccountNumber().startsWith(Constants.MERCHANT_EDC2) || 
				view.getAccountNumber().startsWith(Constants.HIPERWALLET) || view.getAccountNumber().equals("3000113122") ){
	    	
			isMulti = false;
	    	
	    }else if (isAccountPAC(view.getAccountNumber(), getMerchantPAC())){
		    
	    	isMulti = false;
		
	    } else if (view.getTerminalId().equals(extendedProperty.getDefaultTerminalId())) {
	    	
	    	isMulti = true;
	    	
	    }else{
	    	
	    	isMulti = false;
	    	
	    }
	   
		if(isMulti){
		
		CustomerAccountView accountView = new CustomerAccountView();
    	System.out.println("HOHOHOHO "+view.getAccountNumber());
    	accountView.setTransactionType(com.dwidasa.engine.Constants.MULTI_REKENING);
    	accountView.setAccountType("1");
    	accountView.setCardNumber(view.getCardNumber());
    	accountView.setToAccountType("00");
    	accountView.setDigitNoKartu(view.getAccountNumber());
    	accountView.setCurrencyCode(Constants.CURRENCY_CODE);
    	accountView.setProviderCode("A004");
    	accountView.setTerminalId(view.getTerminalId());
    	accountView.setMerchantType(view.getMerchantType());
    	accountView.setCustomerId(customerId); 
    	paymentService.inquiry(accountView);
    	
    	List<CustomerAccount> accounts = customerAccountDao.getAllNoDefault(customerId, view.getAccountNumber());
    	for(CustomerAccount account : accounts){
    		System.out.println("ID "+account.getId());
    		customerAccountDao.deleteCustomerAccountsPerId(account.getId());
    	}
    	//List<CustomerAccountView> accountViews = new ArrayList<CustomerAccountView>();
    	
    	for(int i=0; i<accountView.getAccountViews().size(); i++){
    		
    		if(!accountView.getAccountViews().get(i).getDigitRekening().equals(view.getAccountNumber()) && 
    	       !accountView.getAccountViews().get(i).getDigitNoKartu().equals("0000000000000000")){
    			
    			//CustomerAccountView accountView2 = new CustomerAccountView();
    			
    			CustomerAccount account = new CustomerAccount();
        		account.setAccountTypeId((long)1);
        		account.setCurrencyId((long)1);
        		account.setCustomerId(customerId);
        		account.setProductId((long)26);
        		account.setAccountNumber(accountView.getAccountViews().get(i).getDigitRekening());
        		//account.setCardNumber(EngineUtils.encrypt(com.dwidasa.engine.Constants.SERVER_SECRET_KEY, 
        		//									accountView.getAccountViews().get(i).getDigitNoKartu()));
        		account.setCardNumber(accountView.getAccountViews().get(i).getDigitNoKartu());
        		account.setIsDefault("N");
        		account.setStatus(1);
        		account.setCreated(new Date());
        		account.setCreatedby(customerId);
        		account.setUpdated(new Date());
        		account.setUpdatedby(customerId);
        		customerAccountDao.save(account);
        		
        		
    		}
    	}
    	
    	List<CustomerAccountView> customerAccountViews = new ArrayList<CustomerAccountView>();
    	List<CustomerAccount> accounts2 = customerAccountDao.getAllWithTypeAndCurrency(customerId);
        for(CustomerAccount account : accounts2){
        	
        	CustomerAccountView view2 = new CustomerAccountView();
        	view2.setDigitRekening(account.getAccountNumber());
        	view2.setDigitNoKartu(account.getCardNumber());
        	customerAccountViews.add(view2);
        }
        
        return PojoJsonMapper.toJson(customerAccountViews);
        
		}else{
			
			System.out.println("Merchant");
			List<CustomerAccountView> customerAccountViews = new ArrayList<CustomerAccountView>();
    		List<CustomerAccount> accounts = customerAccountDao.getAllRekMerchant(view.getCustomerId());
            for(CustomerAccount account : accounts){
            	CustomerAccountView view2 = new CustomerAccountView();
            	view2.setDigitRekening(account.getAccountNumber());
            	view2.setDigitNoKartu(account.getCardNumber());
            	customerAccountViews.add(view2);
            }
            
            return PojoJsonMapper.toJson(customerAccountViews);
			
		}
		
		
	}
	
	
	public String getMerchantPAC() {    	
    	com.dwidasa.engine.model.Parameter parameter = parameterDao.get("MERCHANT_PAC");
    	return parameter.getParameterValue();
    }
	
	public boolean isAccountPAC(String accountNumber, String accountNumberPac) {    	
    	//String firstFive = phoneInput.substring(0, 5);
    	String[] PAC = accountNumberPac.split(",");
    	boolean isPAC = false;
    	if (PAC.length > 0 ) {
    		for(int i = 0; i < PAC.length; i++) {
    			if (accountNumber.equals(PAC[i]))
    				isPAC = true;
    		}
    	}
    	return isPAC;
    }
	
	@Override
	public String getUnblock(String customerUsername, String TerminalID, int status, String userLog)
			throws Exception {

		Customer cus = null;
		IbMerchant ibMerchant = null;
		String xmlString = "";

		if (!customerUsername.equals("") && TerminalID.equals("")) {
			System.out.println("Terminal ID Kosong");

			cus = customerService.getByUsername(customerUsername.toUpperCase());


		} else if (customerUsername.equals("") && !TerminalID.equals("")) {
			System.out.println("Customer Username Kosong");

			cus = customerService.getByTID(TerminalID);
			//ibMerchant = ibMerchantDao.getByCustomerId(cus.getId());

		} else {

			System.out.println("Semuanya Tidak Kosong");
			cus = customerService.getByUsernameAndByTID(customerUsername.toUpperCase(), TerminalID);
			//ibMerchant = ibMerchantDao.getByCustomerId(cus.getId());

		}

		if(cus != null){
			String ipAddres = requestGlobals.getHTTPServletRequest().getRemoteAddr();
			logger.info("Customer Id = "+cus.getId()+" Ip Address "+ipAddres+" User Log "+userLog);

			ibMerchant = ibMerchantDao.getByCustomerId(cus.getId());
			customerService.updateStatusUnblockOrblock(cus.getId(), userLog, status);
			customerService.updateStatusUnblockOrblockDevice(cus.getId(), userLog, status);
			customerService.updateStatusUnblockOrblockIBMerchant(cus.getId(), userLog, status);
			customerService.updateStatusUnblockOrblockIBToken(cus.getId(), userLog, status);


			CustomerMerchant customerMerchant = new CustomerMerchant();
			customerMerchant.setCustomerUsername(cus.getCustomerUsername());
			customerMerchant.setTerminalId(ibMerchant.getTerminalId());
			customerMerchant.setStatus(status);
			customerMerchant.setException("");

			 xmlString = "<xml> " +
			 		"\n   <item> " +
			 		"\n      <username> "+customerMerchant.getCustomerUsername()+" </username> " +
			 		"\n      <tid> "+customerMerchant.getTerminalId()+" </tid> " +
			 		"\n      <status> "+customerMerchant.getStatus()+" </status> " +
			 		"\n      <exception> "+customerMerchant.getException()+" </exception>  " +
			 		"\n   </item> \n" +
			 		"</xml>";

			//if(status == 1){
		    return xmlString;
			//}else{
				//return "Customer "+cus.getCustomerName()+" Tidak Aktif. ";
			//}


		}else{


			CustomerMerchant customerMerchant = new CustomerMerchant();
			customerMerchant.setCustomerUsername(customerUsername);
			customerMerchant.setTerminalId(TerminalID);
			customerMerchant.setStatus(status);
			customerMerchant.setException("Customer Not Found");

			 xmlString = "<xml> " +
				 		"\n   <item> " +
				 		"\n      <username> "+customerMerchant.getCustomerUsername()+" </username> " +
				 		"\n      <tid> "+customerMerchant.getTerminalId()+" </tid> " +
				 		"\n      <status> "+customerMerchant.getStatus()+" </status> " +
				 		"\n      <exception> "+customerMerchant.getException()+" </exception>  " +
				 		"\n   </item> \n" +
				 		"</xml>";

				//if(status == 1){
			 return xmlString;
		}

	}
	
}

