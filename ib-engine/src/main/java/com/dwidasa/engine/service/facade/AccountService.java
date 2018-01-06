package com.dwidasa.engine.service.facade;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountSsppView;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.LotteryTransactionView;
import com.dwidasa.engine.model.view.LotteryView;
import com.dwidasa.engine.model.view.PortfolioView;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;

/**
 * Class that hold all required features related to Account.
 *
 * @author rk
 */
public interface AccountService {
    /**
     * In this method we do the following things.
     * 1. Generate username, with customer name and card number provided.
     * 2. Generate activation code and register to OTP Server.
     * 3. Create a record for m_customer, m_customer_account and m_customer_device
     * with active status on all table. But check for pre-existing account with same
     * card number, and if exist, return don't create another data instead used pre-registered
     * data except the activation code.
     *
     * The signature of this method might be change based on provided message from MX.
     * @param t transaction object
     */
    public void registerEBanking(final Transaction t);
    public void registerEBankingIGate(final Transaction t);

    /**
     * Get all customer accounts for specified customer id. Only active account will be returned.
     * @param customerId customer id
     * @return all available customer account information in AccountView class.
     */
    public List<AccountView> getAccounts(Long customerId);

    /**
     * Get all registered customer account, whether active or inactive account will be returned.
     * @param customerId customer id
     * @return list of customer account
     */
    public List<CustomerAccount> getRegisteredAccounts(Long customerId);

    /**
     * Get customer default account.
     * @param customerId customer id
     * @return customer default account in AccountView class
     */
    public AccountView getDefaultAccount(Long customerId);

    /**
     * Get account balance for specified customer account.
     * @param view inside view object, these fields are mandatory, customerId, cardNumber, accountNumber,
     * accountType, currencyCode, and merchant type
     * @return account balance for specified customer account
     */
    public AccountView getAccountBalance(AccountView view);

    /**
     * Get account balances for all account of a customer that was registered in IB systems.
     * @param view inside view object, these fields are mandatory, customerId, currencyCode,
     * and merchant type
     * @return list of account view
     */
    public List<AccountView> getAccountBalances(AccountView view);

    /**
     * Get account statement for specified account.
     * @param view inside view object, these fields are mandatory, customerId, cardNumber, accountNumber,
     * accountType, currencyCode, and merchant type
     * @param startDate start date
     * @param endDate end date
     * @return account statements for specified account
     */
    public List<AccountStatementView> getAccountStatement(AccountView view, Date startDate, Date endDate);
    public List<LotteryTransactionView> getAccountJenisUndian(LotteryTransactionView view);
    public List<AccountStatementView> getAccountStatementFromCore(AccountView view);
    /**
     * Refresh pending transaction status from core for today transaction (purchase voucher only).
     * @param view inside view object, these fields are mandatory, customerId, accountNumber, cardNumber,
     * and merchant type
     * @param status status of transaction, eg. pending, canceled or all
     * @param startDate start date
     * @param endDate end date
     * @return list of Transaction in account statement format
     */
    public List<AccountStatementView> getTransactionStatus(AccountView view, String status, Date startDate,
                                                           Date endDate);

    /**
     * List of all transaction that was happen in our internet banking system.
     * @param view inside view object, these fields are mandatory, customerId, accountNumber,
     * and transactionType.
     * @param startDate start date
     * @param endDate end date
     * @return list of account statment view
     */
    public List<AccountStatementView> getTransactionHistory(AccountView view, Date startDate, Date endDate);

    /**
     * Get N last transactions specified by account number.
     * @param view inside view object, these fields are mandatory, customerId, accountNumber, cardNumber,
     * accountType, currencyCode, and merchant type
     * @param n number of last transaction to be retrieved
     * @return list of Transaction in account statement format
     */
    public List<AccountStatementView> getLastNTransaction(AccountView view, Integer n);

    /**
     * Get all customer user device.
     * @param customerId customer id
     * @return list of customer device
     */
    public List<CustomerDevice> getDevices(Long customerId);

    /**
     * Inquiry card to be deactivated.
     * @param view inside view object, these fields are mandatory, customerId, accountType, currencyCode,
     * cardNumber and merchantType
     * @return list of account view object
     */
    public List<AccountView> getCards(AccountView view);

    /**
     * Deactivate card number as specified in customer account number.
     * @param view inside view object, these fields are mandatory, customerId, accountType, currencyCode,
     * cardNumber, merchant type and generatedMessage (will contain pairs of card number and accont number)
     * @return account view object
     */
    public AccountView deactivateCard(AccountView view);

    /**
     * Register a new card number to existing IB Account.
     * @param view inside view object, these fields are mandatory , customerId, cardNumber,
     * accountType, currencyCode, and merchant type
     * @return account view object
     */
    public AccountView registerCard(AccountView view);

    /**
     * Register an account to existing IB Account.
     * @param view inside view object, these fields are mandatory , customerId, cardNumber,
     * accountType, currencyCode, and merchant type
     * @return account view object
     */
    public AccountView registerAccount(AccountView view);

    /**
     * Change account status, to be activated or deactivated.
     * @param customerId customer id
     * @param accountNumbers map containing account number and status.
     * @return result view object
     */
    public ResultView changeAccountStatus(Long customerId, Map<String, Integer> accountNumbers);

    /**
     * Register an account to existing IB Account.
     * @param view of LotteryView
     * @return view of LotteryView
     */
    public LotteryView getLotteryViewPoint(LotteryView view);

    public List<AccountSsppView> getAccountSsppPerPage(AccountView view);
    
    /**
     * get all customer's portfolios
     * @param view request data
     * @return all customer's portfolios
     */
    //
    public List<PortfolioView> getPortfolioFromIGate(AccountView view, String portfolioType);

    public List<LotteryTransactionView> getPortfolioFromIGateUndian(LotteryTransactionView view, String portfolioType);
    
    public List<LotteryTransactionView> getPortfolioFromIGateNomorUndian(LotteryTransactionView view, String portfolioType);
    /**
     * Check today transaction status for account
     * @param view
     * @return
     */
    public List<AccountStatementView> checkTransactionStatus(AccountView view);

    public Customer registerCustomerSilent(Customer customer, AccountView accountView);

    public void registerAccountSilent(Customer customer, AccountView accountView, String defaultAccount);

    // bsueb cek transfer atmb status
    public Transaction getTransferViewForTransferAMTBStatus(TransferView tv);

    // bsueb check provider denom di hari yg sama
    public Boolean checkProviderDenomAtSameDay(VoucherPurchaseView vv);

    public BaseView ssppTemenos(BaseView view);
    
    public InputStream getAccountStatementCsvStream(AccountView view, List<AccountStatementView> asvList, Date startDate, Date endDate, BigDecimal endingBalance);
        

}
