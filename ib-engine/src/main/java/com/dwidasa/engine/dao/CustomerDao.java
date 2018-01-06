package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Customer;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/20/11
 * Time: 2:46 PM
 */
public interface CustomerDao extends GenericDao<Customer, Long> {
    /**
     * Get customer by its id, and populated with default account respectively
     * @param id customer id
     * @return customer object with default account already populated
     */
    public Customer getWithDefaultAccount(Long id);

    /**
     * Get customer by its username, and populated with default account respectively
     * @param username customer's username
     * @return customer object with default account already populated
     */
    public Customer getWithDefaultAccount(String username);

    /**
     * Get customer by its username, and populated with default account respectively
     * @param username customer's username
     * @return customer object with default account already populated
     */
    public Customer getByUsernameCardNo(String username, String cardNo);

    /**
     * Check whether specified customer id still active
     * @param id customer id
     * @return true if specified customer still active (status = 1)
     */
    public Boolean isActive(Long id);

    /**
     * Authenticate customer credentials
     * @param username customer user name
     * @param pin pin / password of customer
     * @return true if creadential is authenticated
     */
    public Boolean authenticate(String username, String pin);
    
    public Boolean authenticateSHA(String username, String pin);

    /**
     * Get customer object from customer username
     * (customer user id here is string inputted through login box as one of user credential)
     * @param username customer username
     * @return customer object or return null if customer object could not be found
     */
    public Customer get(String username);
    public Customer getByTID(String TerminalID);
    public Customer getByUsernameAndByTID(String username, String TerminalID);

    public Customer getByCifCardNo(String cif, String cardNo);

    /**
     * Check for customer status.
     * Function will return:
     * -1 if customer has not been registered (no record found in database)
     * 0 if customer has been registered but status is inactive (probably blocked by admin or authentication failed)
     * 1 id customer has been registered and active
     * @param id customer id
     * @return customer status
     */
    public int checkCustomerStatus(Long id);

    /**
	 * update field status
	 * @param id primary key of m_customer
	 * @param status (1=active, 0=block)
	 */
	public void updateStatus(Long id, int status);
	public void updateStatusUnblockOrblock(Long id, String tin, int status);
	public void updateStatusUnblockOrblockCustomerDevice(Long id, String tin, int status);
	public void updateStatusUnblockOrblockIbMerchant(Long id, String tin, int status);
	public void updateStatusUnblockOrblockIbToken(Long id, String tin, int status);

    public void insertCustomerRegisterSilent(Customer customer);
    
    public Customer save(Customer customer);

    /**
     * Get list of customer by its default account number
     * @param accountNumber
     * @return
     */
    public List<Customer> getByDefaultAccountNumber(String accountNumber);
    
    public List<Customer> getAllActiveCustomer();

    public int remove(Long customerId);
    
    public Boolean isMerchant(Long customerId);
    
    public Long getCustomerIdByUsername(String username);

	List<Customer> getByAccountNumber(String accountNumber);
	
	List<Customer> getByCustomerIsMerchant();
	
	List<Customer> getByCustomerIsIndividual();
	
	List<Customer> getByCustomerByCustomerName(String customerName, String customerUserName);
	
	List<Customer> getByCustomerIsMerchantByName(String customerName, String customerUserName);
	
	List<Customer> getByCustomerIsIndividualByName(String customerName, String customerUserName);
}
