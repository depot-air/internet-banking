package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.Menu;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 10:38 AM
 */
public interface MenuDao extends GenericDao<Menu, Long> {
    /**
     * Get all menu sorted by hierarchy.
     * @return list of menu
     */
    public List<Menu> getAllByHierarchy();
    public List<Menu> getMerchantEdcAndHiperwalletMenu();
    public List<Menu> getMerchantMenu();
    public List<Menu> getMerchantMenuPac();
}
