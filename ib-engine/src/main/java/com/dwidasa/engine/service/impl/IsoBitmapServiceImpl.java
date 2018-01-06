package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.IsoBitmapDao;
import com.dwidasa.engine.model.IsoBitmap;
import com.dwidasa.engine.service.IsoBitmapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/30/11
 * Time: 10:20 AM
 */
@Service("isoBitmapService")
public class IsoBitmapServiceImpl extends GenericServiceImpl<IsoBitmap, Long> implements IsoBitmapService {
    private final IsoBitmapDao isoBitmapDao;

    @Autowired
    public IsoBitmapServiceImpl(IsoBitmapDao isoBitmapDao) {
        super(isoBitmapDao);
        this.isoBitmapDao = isoBitmapDao;
    }

    public List<IsoBitmap> getAllWithTransactionType() {
        return isoBitmapDao.getAllWithTransactionType();
    }

	public IsoBitmap getByTransactionType(String transactionType) {
		return isoBitmapDao.getByTransactionType(transactionType);
	}
}
