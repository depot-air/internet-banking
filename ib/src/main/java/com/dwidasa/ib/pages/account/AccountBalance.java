package com.dwidasa.ib.pages.account;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.dwidasa.engine.model.view.PortfolioView;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.AccountInfo;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: Fanny
 * Date: 15/09/11
 * Time: 10:20
 */
public class AccountBalance {
    @Property
    private AccountView accountView;

    @Persist
    @Property
    private List<AccountView> accountViewList;

    @Inject
    private AccountService accountService;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @InjectPage
    private AccountSummary accountSummary;

    @InjectPage
    private LastTenTransaction lastTenTransaction;

    @Property
    private EvenOdd evenOdd;
    
    @Property
    private String errorFlag;
    
    @InjectComponent
    private Form form;

    @Autowired
    private ExtendedProperty extendedProperty;
    
    void beginRender() {
        evenOdd = new EvenOdd();
        evenOdd.setEven(true);

        accountView = new AccountView();
        accountView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
        accountView.setCurrencyCode(Constants.CURRENCY_CODE);
        accountView.setMerchantType(sessionManager.getDefaultMerchantType());
        accountView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
        accountView.setAccountNumber(sessionManager.getLoggedCustomerView().getAccountNumber());
        accountView.setCardNumber(sessionManager.getLoggedCustomerView().getCardNumber());
        accountView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());

        try {
//            List<PortfolioView> pvs = accountService.getPortfolioFromIGate(accountView, com.dwidasa.engine.Constants.PORTFOLIO.CASA);
//            accountViewList = new ArrayList<AccountView>();
//            int i = 0;
//            for (PortfolioView pv : pvs){
//                AccountView av = new AccountView();
//                av.setCustomerId(sessionManager.getLoggedCustomerView().getId());
//                av.setMerchantType(sessionManager.getDefaultMerchantType());
//                av.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
//                av.setAccountNumber(pv.getAccountNumber());
//                av.setAccountName(pv.getAccountName());
//                av.setAccountStatus(pv.getAccountStatus());
//                av.setAccountType(pv.getAccountType());
//                av.setCustomerName(pv.getAccountName());
//                av.setProductCode(pv.getProductCode());
//                av.setProductName(pv.getProductName());
//                av.setAvailableBalance(pv.getAvailableBalance());
//                av.setBlockedBalance(pv.getBlockedBalance());
//                av.setGenerated(String.valueOf(++i));
//        	    av.setCurrencyCode(cacheManager.getCurrency(accountView.getCurrencyCode()).getCurrencyCode());
//                accountViewList.add(av);
//            }
//            errorFlag = "noError";
        	List< PortfolioView > pvs = accountService.getPortfolioFromIGate(accountView, com.dwidasa.engine.Constants.PORTFOLIO.CASA);
        	accountViewList = new ArrayList<AccountView>();
            try {
                List< PortfolioView > pvsDeposito = accountService.getPortfolioFromIGate(accountView, com.dwidasa.engine.Constants.PORTFOLIO.DEPOSITO);
                if (pvsDeposito != null && pvsDeposito.size() > 0) {
            	    pvs.addAll(pvsDeposito);
                }
            }
            catch(Exception e){
                // just ignore and move to next porto if we can't get depositos
                e.printStackTrace();
            }

            try {
                List< PortfolioView > pvsLoan = accountService.getPortfolioFromIGate(accountView, com.dwidasa.engine.Constants.PORTFOLIO.LOAN);
                if (pvsLoan != null && pvsLoan.size() > 0) {
            	    pvs.addAll(pvsLoan);
                }
            }
            catch (Exception e){
                // just ignore if we can't get loans
                e.printStackTrace();
            }
            
          int i = 0;
          for (PortfolioView pv : pvs){
              AccountView av = new AccountView();
              av.setCustomerId(sessionManager.getLoggedCustomerView().getId());
              av.setMerchantType(sessionManager.getDefaultMerchantType());
              av.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
              
              if(com.dwidasa.engine.Constants.PORTFOLIO.CASA.equals(pv.getTransactionStatus())){
            	  av.setAccountNumber(pv.getAccountNumber());
            	  av.setProductName(pv.getProductName());
            	  av.setAvailableBalance(pv.getAvailableBalance());
              }
              
              else if(com.dwidasa.engine.Constants.PORTFOLIO.DEPOSITO.equals(pv.getTransactionStatus())){
            	  av.setAccountNumber(pv.getDepositoNumber());
            	  av.setProductName(pv.getTransactionStatus());
            	  av.setAvailableBalance(pv.getNominalDeposito());
            	  if ("D".equals(pv.getSatuanWaktu())) {
            		  av.setJangkaWaktu(pv.getJangkaWaktu()+" Hari");
            	  } else {
            		  av.setJangkaWaktu(pv.getJangkaWaktu()+" Bulan");
            	  }
            	  av.setTglEfektif(pv.getEffectiveDate());
            	  av.setJatuhTempo(pv.getDueDate());
            	  double annualRate = Double.parseDouble(pv.getAnnualRate());
            	  double sukuBunga = annualRate / 10000;
            	  av.setAnnualRate(sukuBunga);
              }
              
              else if(com.dwidasa.engine.Constants.PORTFOLIO.LOAN.equals(pv.getTransactionStatus())){
            	  
            	  av.setAccountNumber(pv.getLoanNumber());
            	  av.setProductName(pv.getTransactionStatus());
            	  av.setPokokPinjaman(pv.getPokokPinjaman());
            	  av.setBungaPinjaman(pv.getBungaPinjaman());
            	  av.setTenor(pv.getTenor());
            	  av.setJumlahAngsuran(pv.getJumlahAngsuran());
            	  av.setSisaPinjaman(pv.getPokokPinjaman().subtract(pv.getJumlahAngsuran()));
            	  av.setEffectiveDate(pv.getEffectiveDate());
            	  av.setDueDate(pv.getDueDate());
            	  av.setTunggakanPokok(pv.getTunggakanPokok());
            	  av.setTunggakanBunga(pv.getTunggakanBunga());
            	  av.setTanggalBayarPokok(pv.getTanggalBayarPokok());
            	  av.setTanggalBayarBunga(pv.getTanggalBayarBunga());
            	  av.setAvailableBalance(pv.getPokokPinjaman().subtract(pv.getJumlahAngsuran()));
              }
              
              av.setAccountName(pv.getAccountName());
              av.setAccountStatus(pv.getAccountStatus());
              av.setAccountType(pv.getAccountType());
              av.setCustomerName(pv.getAccountName());
              av.setProductCode(pv.getProductCode());
             
              av.setBlockedBalance(pv.getBlockedBalance());
              av.setGenerated(String.valueOf(++i));
      	      av.setCurrencyCode(cacheManager.getCurrency(accountView.getCurrencyCode()).getCurrencyCode());
              accountViewList.add(av);
          }
          errorFlag = "noError";
        }
        catch (BusinessException e) {
            form.clearErrors();
            form.recordError(e.getFullMessage());
            errorFlag = null;
            e.printStackTrace();
        }

    }

    //@DiscardAfter
    Object onViewStatement(String mode, String generated) {
        AccountView av = null;
        for (AccountView view : accountViewList) {
            if (view.getGenerated().equals(generated)) {
                av = view;
                break;
            }
        }

//          AccountInfo ai = sessionManager.getAccountInfo(av.getAccountNumber());
//
//          av.setCurrencyCode(ai.getCurrencyCode());
//        av.setAccountType(ai.getAccountType());
//        av.setAccountNumber(ai.getAccountNumber());
//        av.setCardNumber(ai.getCardNumber());
//
//        av.setMerchantType(sessionManager.getDefaultMerchantType());
//        av.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
//        av.setCustomerId(sessionManager.getLoggedCustomerView().getId());
//
//        if (mode.equals("1")) {
            accountSummary.setAccountView(av);
            return accountSummary;
        //}

        //10 transaksi terakhir
//        try {
//            List<AccountStatementView> asvList = accountService.getLastNTransaction(av, 10);
//            System.out.println("mini statement result # of rows : " + asvList.size());
//
//            lastTenTransaction.setAccountView(av);
//            lastTenTransaction.setReferer(this.getClass().getSimpleName());
//            lastTenTransaction.setAsvList(asvList);
//            return lastTenTransaction;
//        }
//        catch (BusinessException be){
//
//            be.printStackTrace();
//            form.clearErrors();
//            form.recordError(be.getFullMessage());
//           return this;
//        }


    }

    void pageReset() {
        accountViewList = null;
    }
}
