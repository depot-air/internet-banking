package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.CustomerRegister;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/18/11
 * Time: 10:21 PM
 */
public interface CustomerRegisterService extends GenericService<CustomerRegister, Long> {
    /**
     * Get all registered list of a customer and transaction type
     * @param customerId customer id
     * @param transactionType transaction type
     * @return list of customerRegister
     */
	public List<CustomerRegister> getByTransactionType(Long customerId, String transactionType);
	public List<CustomerRegister> getAeroPassangers(Long customerId, String transactionType, String contactOrPass);
	/**
	 * Get register list for TransferDestinationList, PaymentDestinationList, PurchaseDestinationList page
	 * @param restrictions
	 * @param orders
	 * @return list of customer register
	 */
	public List<CustomerRegister> getRegisterList(List<String> restrictions, List<String> orders);

	/**
	 * Delete destination registerId that belongs to customerId	 
	 * @param registerId
	 * @param customerId
	 */
	public void remove(Long registerId, Long customerId);
	
	public CustomerRegister get(Long customerId, String transactionType, String customerReference);
	//untuk menyimpan penumpang tiket pesawat
	public void saveOrUpdate(Long customerId, String transactionType, CustomerRegister customerRegister);
	public List<CustomerRegister> getAirportsFromTo(String customerId, String transactionType, String billerCode, String fromTo);
}
