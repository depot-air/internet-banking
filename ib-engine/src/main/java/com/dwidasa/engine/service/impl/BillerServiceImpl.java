package com.dwidasa.engine.service.impl;

import java.util.List;

import com.dwidasa.engine.dao.BillerDao;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.service.BillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/19/11
 * Time: 4:05 PM
 */
@Service("billerService")
public class BillerServiceImpl extends GenericServiceImpl<Biller, Long> implements BillerService {
	private BillerDao billerDao;
	
    @Autowired
    public BillerServiceImpl(BillerDao billerDao) {
        super(billerDao);
        this.billerDao = billerDao;
    }

	public List<Biller> getByTransactionTypeId(Long transactionTypeId) {
		return billerDao.getAll(transactionTypeId);
	}
}
