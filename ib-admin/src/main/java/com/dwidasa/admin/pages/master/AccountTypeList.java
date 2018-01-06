package com.dwidasa.admin.pages.master;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.service.AccountTypeService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/11/11
 * Time: 18:12
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class AccountTypeList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private AccountType row;

    @Property
    @Persist
    private String accountType;

    @InjectPage
    private AccountTypeDetail accountTypeDetail;

    @Inject
    private AccountTypeService accountTypeService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (accountType != null && !accountType.equals("")) {
            restrictions.add("upper(account_type) like '%' || ? || '%'");
            values.add(accountType.toUpperCase());
        }

        dataSource = new BaseDataSource(AccountType.class, Constants.PAGE_SIZE, restrictions, values);
    }

    @DiscardAfter
    void onSelectedFromReset() {
        accountType = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        accountTypeDetail.setId(null);
        return accountTypeDetail;
    }
    
    @Inject
    private VersionService versionService;

    @Inject
    private SessionManager sessionManager;
    
    @DiscardAfter
    void onActionFromDelete(Long id) {
    	AccountType tt = new AccountType();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, accountTypeService, userId);
    }
}
