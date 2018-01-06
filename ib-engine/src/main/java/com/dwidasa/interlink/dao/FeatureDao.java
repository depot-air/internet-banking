package com.dwidasa.interlink.dao;

import com.dwidasa.interlink.model.MFeature;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 10/24/12
 */
public interface FeatureDao {
    public MFeature getFeature(String transactionType, String providerCode);
    public List<MFeature> getAll();

}
