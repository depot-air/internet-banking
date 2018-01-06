package com.dwidasa.engine.service;

import com.dwidasa.engine.model.CellularPrefix;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:10 PM
 */
public interface CellularPrefixService extends GenericService<CellularPrefix, Long> {

	CellularPrefix getWithTransactionType(Long id);
}
