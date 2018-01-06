package com.dwidasa.admin.pages.master;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.service.CurrencyService;
import com.dwidasa.engine.service.VersionService;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/11/11
 * Time: 1:11
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class CurrencyList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private Currency row;

    @Property
    @Persist
    private String currencyCode;

    @Property
    @Persist
    private String swiftCode;

    @InjectPage
    private CurrencyDetail currencyDetail;

    @Inject
    private CurrencyService currencyService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (currencyCode != null && !currencyCode.equals("")) {
            restrictions.add("upper(currency_code) like '%' || ? || '%'");
            values.add(currencyCode.toUpperCase());
        }
        if(swiftCode != null && !swiftCode.equals("")){
            restrictions.add("upper(swift_code) like '%' || ? || '%'");
            values.add(swiftCode.toUpperCase());
        }

        dataSource = new BaseDataSource(Currency.class, Constants.PAGE_SIZE, restrictions, values);
    }

    @DiscardAfter
    void onSelectedFromReset() {
        currencyCode = null;
        swiftCode = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        currencyDetail.setId(null);
        return currencyDetail;
    }

    @Inject
    private VersionService versionService;
    
    @Inject
    private SessionManager sessionManager;

    @DiscardAfter
    void onActionFromDelete(Long id) {
    	Currency tt = new Currency();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, currencyService, userId);
    }
}
