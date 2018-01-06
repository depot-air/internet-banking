package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.CustomerAccount;

import java.util.List;

/**
 * DAO for customer account. Only account with active status will be returned through accessing
 * methods in this interface, except otherwise, stated clearly at method's java docs.
 *
 * @author rk
 */
public interface CustomerAccountDao extends GenericDao<CustomerAccount, Long> {
    /**
     * Get customer account from customer id and account number.
     * WARNING! This method will return customer account object even the status of that account
     * is INACTIVE.
     * @param customerId customer id
     * @param accountNumber account number
     * @return customer account object
     */
    public CustomerAccount get(Long customerId, String accountNumber);

    /**
     * Get customer account from card number.
     * @param cardNumber card number
     * @return list customer account object
     */
    public List<CustomerAccount> get(String cardNumber);

    /**
     * Get default customer account from customer id with relation to account type and
     * currency is initialized.
     * @param customerId customer id
     * @return default customer account object for specified customer id
     */
    public CustomerAccount getDefaultWithType(Long customerId);

    /**
     * Get customer account from customer id and account number, populated with account type object.
     * @param customerId customer id
     * @param accountNumber account number
     * @return customer account object
     */
    public CustomerAccount getWithTypeAndProduct(Long customerId, String accountNumber);

    /**
     * Get all customer accounts for specified customer id.
     * @param customerId customer id
     * @return list of customerAccount
     */
    public List<CustomerAccount> getAll(Long customerId);
    
    public List<CustomerAccount> getAllCardNumberCustomer();
    public void updateEncriptCardNumber(Long id, String cardNew);

    /**
     * Get all registered customer accounts for specified customer id.
     * WARNING! This method will return customer account object even the status of that account
     * is INACTIVE.
     * @param customerId customer id
     * @return list of customerAccount
     */
    public List<CustomerAccount> getAllRegistered(Long customerId);

    /**
     * Get all customer account by customer id and card number.
     * @param customerId customer id
     * @param cardNumber card number
     * @return list of customerAccount
     */
    public List<CustomerAccount> getAll(Long customerId, String cardNumber);
    public List<CustomerAccount> getAllNoDefault(Long customerId, String cardNumber);
    /**
     * Get all customer accounts for specified customer id. AccountType object
     * inside CustomerAccount is eagerly featched.
     * @param customerId customer id
     * @return list of customerAccount
     */
    public List<CustomerAccount> getAllWithTypeAndCurrency(Long customerId);

    /**
     * Get all customer accounts for specified customer id. AccountType and Product object
     * inside CustomerAccount is eagerly featched.
     * @param customerId customer id
     * @return list of customerAccount
     */
    public List<CustomerAccount> getAllWithTypeAndProduct(Long customerId);
    public List<CustomerAccount> getAllRekMerchant(Long customerId);

    /**
     * delete all customer accounts
     * @param customerId
     * @return -1 if failed
     */
    public int deleteCustomerAccounts(Long customerId);
    public int deleteCustomerAccountsPerId(Long id);
    public int deleteCustomerAccountsCartNumber(Long customerId, String accountNumber);
}
