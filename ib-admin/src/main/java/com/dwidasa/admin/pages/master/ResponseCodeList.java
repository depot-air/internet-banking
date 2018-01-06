package com.dwidasa.admin.pages.master;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.PersistenceConstants;
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
import com.dwidasa.engine.model.ResponseCode;
import com.dwidasa.engine.service.ResponseCodeService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/10/11
 * Time: 00:14 am
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class ResponseCodeList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private ResponseCode row;

    @Property
    @Persist(value = PersistenceConstants.FLASH)
    private String responseCode;

    @InjectPage
    private ResponseCodeDetail responseCodeDetail;

    @Inject
    private ResponseCodeService responseCodeService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (responseCode != null && !responseCode.equals("")) {
            restrictions.add("upper(response_code) like '%' || ? || '%'");
            values.add(responseCode.toUpperCase());
        }

        dataSource = new BaseDataSource(ResponseCode.class, Constants.PAGE_SIZE, restrictions, values);
    }

    void onSelectedFromReset() {
        responseCode = null;
    }

    Object onSelectedFromAdd() {
        responseCodeDetail.setId(null);
        return responseCodeDetail;
    }
    
    @Inject
    private VersionService versionService;

    @Inject
    private SessionManager sessionManager;
    
    @DiscardAfter
    void onActionFromDelete(Long id) {
    	ResponseCode rc = new ResponseCode();
        rc.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(rc, responseCodeService, userId);
    }

}
