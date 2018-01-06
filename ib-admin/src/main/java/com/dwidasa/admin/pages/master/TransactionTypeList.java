package com.dwidasa.admin.pages.master;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/10/11
 * Time: 00:30 am
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class TransactionTypeList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private TransactionType row;

    @Property
    @Persist(value = PersistenceConstants.FLASH)
    private String transactionType;

    @InjectPage
    private TransactionTypeDetail transactionTypeDetail;

    @Inject
    private TransactionTypeService transactionTypeService;

    @Inject
    private VersionService versionService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (transactionType != null && !transactionType.equals("")) {
            restrictions.add("upper(transaction_type) like '%' || ? || '%'");
            values.add(transactionType.toUpperCase());
        }

        dataSource = new BaseDataSource(TransactionType.class, Constants.PAGE_SIZE, restrictions, values);
    }

    void onSelectedFromReset() {
        transactionType = null;
    }

    Object onSelectedFromAdd() {
        transactionTypeDetail.setId(null);
        return transactionTypeDetail;
    }
    
    @Inject
    private SessionManager sessionManager;

    void onActionFromDelete(Long id) {
        TransactionType tt = new TransactionType();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, transactionTypeService, userId);
    }
}
