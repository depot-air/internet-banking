package com.dwidasa.engine.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.CustomerRegisterDao;
import com.dwidasa.engine.model.AeroPassenger;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.service.CustomerRegisterService;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/18/11
 * Time: 10:21 PM
 */
@Service("customerRegisterService")
public class CustomerRegisterServiceImpl extends GenericServiceImpl<CustomerRegister, Long>
        implements CustomerRegisterService {
	private CustomerRegisterDao dao;
	
    @Autowired
    public CustomerRegisterServiceImpl(CustomerRegisterDao customerRegisterDao) {
        super(customerRegisterDao);
        this.dao = customerRegisterDao;
        
    }

    @Override
    public List<CustomerRegister> getByTransactionType(Long customerId, String transactionType) {
        return dao.getByTransactionType(customerId, transactionType);
    }
    
    @Override
    public List<CustomerRegister> getAeroPassangers(Long customerId, String transactionType, String contactOrPass) {
    	List<CustomerRegister> customerRegisters = getByTransactionType(customerId, transactionType);
    	
		for (int i = customerRegisters.size() - 1; i >=0; i--) {
			CustomerRegister cr = customerRegisters.get(i);
			if (!cr.getData2().equals(contactOrPass)) {
				customerRegisters.remove(i);
			}
		}
    	
    	Collections.sort(customerRegisters, new Comparator<CustomerRegister>() {
            @Override
            public int compare(CustomerRegister cr1, CustomerRegister cr2) {
                return cr2.getData4().compareTo(cr1.getData4());	//dibalik, index terkecil ditaruh diakhir
            }
        });
        return customerRegisters;
    }

    @Override
	public List<CustomerRegister> getRegisterList(List<String> restrictions,
			List<String> orders) {
		return dao.getRegisterList(restrictions, orders);
	}

	@Override
	public void remove(Long registerId, Long customerId) {
		dao.remove(registerId, customerId);
	}

	@Override
	public CustomerRegister get(Long customerId, String transactionType, String customerReference) {
		return dao.get(customerId, transactionType, customerReference);
	}
	
	@Override
	public void saveOrUpdate(Long customerId, String transactionType, CustomerRegister customerRegister) {
		customerRegister.setCustomerReference(customerRegister.getCustomerReference().toUpperCase());
		dao.save(customerRegister);
	}
	
	@Override
	public List<CustomerRegister> getAirportsFromTo(String customerId, String transactionType, String billerCode, String fromTo) {
		return dao.getAirportsFromTo(customerId, transactionType, billerCode, fromTo);
	}
}
