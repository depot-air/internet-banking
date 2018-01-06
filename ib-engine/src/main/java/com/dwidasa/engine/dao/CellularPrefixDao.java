package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.CellularPrefix;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:26 PM
 */
public interface CellularPrefixDao extends GenericDao<CellularPrefix, Long> {

	CellularPrefix getWithTransactionType(Long id);
    public List<CellularPrefix> getAllCellularPrefix();
    public List<CellularPrefix> getAllCellularPrefixBiller(Long mBillerId);
    public List<CellularPrefix> getByPrefix(String prefix);
}
