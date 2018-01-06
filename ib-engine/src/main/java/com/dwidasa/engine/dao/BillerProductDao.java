package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.BillerProduct;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:03 PM
 */
public interface BillerProductDao extends GenericDao<BillerProduct, Long> {
    /**
     * Get biller by biller product's code
     * @param code biller product's code
     * @return billerProduct object
     */
    public BillerProduct get(String code);

    /**
     * Get all biller's product with indirect relation to transaction type is initialized.
     * Indirect here means, BillerProduct get relation to TransactionType object through Biller,
     * therefore biller object is initialized as consequence.
     * @return list of billerProduct
     */
    public List<BillerProduct> getAllWithTransactionType();

    /**
     * Get all biller's product for specifed biller id
     * @param billerCode biller product's code
     * @return list of billerProduct
     */
    public List<BillerProduct> getAll(String billerCode);

    /**
     * Get all biller's product which belongs to specified transaction type
     * @param transactionType transaction type
     * @return list of billerProduct
     */
	public List<BillerProduct> getAllByTransactionType(String transactionType);
	
	//digunakan untuk BPR KS Migration
	public BillerProduct getByBillerCodeProductCode(String billerCode, String productCode);
}
