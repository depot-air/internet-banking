package com.dwidasa.admin.pages.user;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.admin.view.UserView;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.UserService;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/10/11
 * Time: 00:29 am
 */
@Restricted(groups={Constants.RoleName.SUPERUSER})
public class MaintenanceUserList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private UserView row;

    @Property
    @Persist(value = PersistenceConstants.FLASH)
    private String userName;

    @InjectPage
    private MaintenanceUserDetail maintenanceUserDetail;

    @Inject
    private UserService userService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (userName != null && !userName.equals("")) {
            restrictions.add("userName like '%' || ? || '%'");
            values.add(userName);


        }
        Transformer t = TransformerFactory.getTransformer(User.class.getSimpleName());
        dataSource = new BaseDataSource(UserView.class, Constants.PAGE_SIZE, restrictions, values, t);
    }

    void onSelectedFromReset() {
        userName = null;
    }

    Object onSelectedFromAdd() {
        maintenanceUserDetail.setId(null);
        return maintenanceUserDetail;
    }
    
    @Inject
    private SessionManager sessionManager;

    void onActionFromDelete(Long id) {
    	Long userId = sessionManager.getLoggedUser().getId();
        userService.remove(id, userId);
    }
}
