package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.CellularPrefixDao;
import com.dwidasa.engine.model.CellularPrefix;
import com.dwidasa.engine.service.CellularPrefixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:58 PM
 */
@Service("cellularPrefixService")
public class CellularPrefixServiceImpl extends GenericServiceImpl<CellularPrefix, Long>
        implements CellularPrefixService {
	private CellularPrefixDao cellularPrefixDao;
	
    @Autowired
    public CellularPrefixServiceImpl(CellularPrefixDao cellularPrefixDao) {
        super(cellularPrefixDao);
        this.cellularPrefixDao = cellularPrefixDao;
    }

	@Override
	public CellularPrefix getWithTransactionType(Long id) {
		return cellularPrefixDao.getWithTransactionType(id);
	}
}
