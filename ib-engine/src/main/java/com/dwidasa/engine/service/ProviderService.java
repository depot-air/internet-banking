package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.Provider;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/16/11
 * Time: 12:20 PM
 */
public interface ProviderService extends GenericService<Provider, Long> {

	List<Provider> getAllWithOrder();
}
