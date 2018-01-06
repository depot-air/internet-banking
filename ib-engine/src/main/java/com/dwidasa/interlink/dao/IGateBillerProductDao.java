package com.dwidasa.interlink.dao;

import com.dwidasa.interlink.model.MIGateBillerProduct;

import java.util.List;

public interface IGateBillerProductDao {
	public MIGateBillerProduct getIGateBillerProduct(String billerCode, String productCode);
    public List<MIGateBillerProduct> getAll();
}
