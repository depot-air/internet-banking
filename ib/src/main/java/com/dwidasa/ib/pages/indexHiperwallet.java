package com.dwidasa.ib.pages;

import com.dwidasa.engine.model.Menu;
import com.dwidasa.engine.service.CacheManager;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * Start page of application ib.
 */
public class indexHiperwallet {
//    @Component(parameters = "")
//    private Tree tree;

    @Inject
    private CacheManager cacheManager;

    private List<Menu> roots;

    public List<Menu> getRoots() {
        return roots;
    }

    void setupRender() {
        //roots = cacheManager.getMenuRoots();
    	roots = cacheManager.getMenuRootsForMerchantEdc();
    }
}
