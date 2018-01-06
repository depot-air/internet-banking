package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Biller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:00 PM
 */
public interface BillerDao extends GenericDao<Biller, Long> {
    /**
     * Get biller by biller's code
     * @param code biller's code
     * @return biller object
     */
    public Biller get(String code);
    
    public Biller getBillerName(String code);
    
    public Biller getTransactionType(long code);
    
    public void updateStatusBiller(Long id, boolean status, Date createdBy, Long created, Date UpdateBy, Long updated);

    /**
     * Get all biller with relation to transactionType object is initialized
     * @return list of biller
     */
    @Transactional
    public List<Biller> getAllWithTransactionType();
    
    @Transactional
    public List<Biller> getAllWithTransactionType(String transactionType);
    
    @Transactional
    public List<Biller> getAllWithTransactionTypeIsMerchant();
    
    @Transactional
    public List<Biller> getAllWithTransactionTypeIsMerchant(String transactionType);
    
    @Transactional
    public List<Biller> getAllWithTransactionTypeTransfer(String transactionAtmb);
    
    @Transactional
    public List<Biller> getAllData1WithTransactionTypeTransfer(String transactionAlto, String transactionAtmb, String billerCole);

    @Transactional
    public List<Biller> getAllTransactionTypeTransfer(long idBiller, long transactionTypeId, String transactionType);
    /**
     * Get all biller for specified transaction type id
     * @param transactionTypeId transaction type id
     * @return list of biller
     */
    public List<Biller> getAll(Long transactionTypeId);
    
    public List<Biller> getAll();
    
    public List<Biller> getAllNoActive();

}
