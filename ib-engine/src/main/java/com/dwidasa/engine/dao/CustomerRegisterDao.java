package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.CustomerRegister;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/5/11
 * Time: 2:58 PM
 */
public interface CustomerRegisterDao extends GenericDao<CustomerRegister, Long> {
    /**
     * Get all registered list of a customer and transaction type
     * @param customerId customer id
     * @param transactionType transaction type
     * @param billerCode biller code
     * @return list of customerRegister
     */
    public List<CustomerRegister> getAll(Long customerId, String transactionType, String billerCode);
    public List<CustomerRegister> getAllOrderBy(Long customerId, String transactionType, String billerCode, String orderBy);
    
    public List<CustomerRegister> getAirportsFrom();
    public List<CustomerRegister> getAirportsTo();
    public List<CustomerRegister> getAirportsFromTo(String customerId, String transactionType, String billerCode, String fromTo);
    
    /**
     * Get all registered list of a customer and transaction type
     * @param customerId customer id
     * @param transactionType transaction type
     * @return list of customerRegister
     */
    public List<CustomerRegister> getByTransactionType(Long customerId, String transactionType);
    /**
     * Get customer register with transaction type and customer reference specified.
     * @param customerId customer id
     * @param transactionType transaction type
     * @param customerReference customer reference
     * @return customer register object
     */
    public CustomerRegister get(Long customerId, String transactionType, String customerReference);

    /**
	 * Get register list for TransferDestinationList, PaymentDestinationList, PurchaseDestinationList page
	 * @param restrictions
	 * @param orders
	 * @return list of customer register
	 */
	public List<CustomerRegister> getRegisterList(List<String> restrictions, List<String> orders);

    public int deleteCustomerRegisters(Long customerId);
}
