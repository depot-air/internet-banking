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
import com.dwidasa.engine.model.Parameter;
import com.dwidasa.engine.service.ParameterService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/18/11
 * Time: 10:49 AM
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class ParameterList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private Parameter row;

    @Property
    @Persist
    private String parameterName;

    @InjectPage
    private ParameterDetail parameterDetail;

    @Inject
    private ParameterService parameterService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (parameterName != null && !parameterName.equals("")) {
            restrictions.add("upper(parameter_name) like '%' || ? || '%'");
            values.add(parameterName.toUpperCase());
        }
        List<String> orders = new ArrayList<String>();
        orders.add("parameter_name");
        dataSource = new BaseDataSource(Parameter.class, Constants.PAGE_SIZE, restrictions, values, null, orders);
    }

    @DiscardAfter
    void onSelectedFromReset() {
        parameterName = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        parameterDetail.setId(null);
        return parameterDetail;
    }
    
    @Inject
    private VersionService versionService;

    @Inject
    private SessionManager sessionManager;
    
    @DiscardAfter
    void onActionFromDelete(Long id) {
    	Parameter tt = new Parameter();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, parameterService, userId);
    }
}
