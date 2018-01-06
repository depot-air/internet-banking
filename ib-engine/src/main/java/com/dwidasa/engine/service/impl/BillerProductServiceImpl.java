package com.dwidasa.engine.service.impl;

import java.util.List;

import com.dwidasa.engine.dao.BillerProductDao;
import com.dwidasa.engine.model.BillerProduct;
import com.dwidasa.engine.service.BillerProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/11/11
 * Time: 11:28 AM
 */
@Service("billerProductService")
public class BillerProductServiceImpl extends GenericServiceImpl<BillerProduct, Long> implements BillerProductService {
	private BillerProductDao billerProductDao;
	
    @Autowired
    public BillerProductServiceImpl(BillerProductDao billerProductDao) {
        super(billerProductDao);
        this.billerProductDao = billerProductDao;
    }

	@Override
	public List<BillerProduct> getAllByTransactionType(String transactionType) {
		return billerProductDao.getAllByTransactionType(transactionType);
	}
}
