package com.dwidasa.engine.dao;

import java.util.Date;
import java.util.List;

import com.dwidasa.engine.model.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 6/22/11
 * Time: 6:20 PM
 */
public interface ProviderDao extends GenericDao<Provider, Long> {
    /**
     * Get biller by provider's code
     * @param code provider's code
     * @return provider object
     */
    public Provider get(String code);

    /**
     * Get All Provider order by provider name
     * @return
     */
	public List<Provider> getAllWithOrder();
	
	public void updateStatusBiller(Long id, boolean status, Date createdBy, Long created, Date UpdateBy, Long updated);
}
