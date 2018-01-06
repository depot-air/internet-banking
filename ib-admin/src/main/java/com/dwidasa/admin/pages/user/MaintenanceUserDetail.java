package com.dwidasa.admin.pages.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Role;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.UserService;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/10/11
 * Time: 00:25 am
 */
@Import(library = "context:bprks/js/user/MaintenanceUserDetail.js")
@Restricted(groups={Constants.RoleName.SUPERUSER})
public class MaintenanceUserDetail {
    @Property
    private User user;

    @Property
    private String confirmPassword;

    @Property
    private SelectModel roleListModel;

    @Property
    private List<String> statusListModel;

    @Property
    private String statusList;

    @Property
    private Long roleList;

    @Property
    private Role role;

    @Inject
    private UserService userService;

    @Inject
    private Messages messages;

    @InjectComponent
    private Form form;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    public void buildRoleList() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	optionList.add(new OptionModelImpl(com.dwidasa.admin.Constants.RoleName.SUPERUSER, com.dwidasa.admin.Constants.Role.SUPERUSER));
    	optionList.add(new OptionModelImpl(com.dwidasa.admin.Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.Role.ADMIN));
    	optionList.add(new OptionModelImpl(com.dwidasa.admin.Constants.RoleName.TREASURY, com.dwidasa.admin.Constants.Role.TREASURY));
    	optionList.add(new OptionModelImpl(com.dwidasa.admin.Constants.RoleName.DAY_ADMIN, com.dwidasa.admin.Constants.Role.DAY_ADMIN));
    	optionList.add(new OptionModelImpl(com.dwidasa.admin.Constants.RoleName.NIGHT_ADMIN, com.dwidasa.admin.Constants.Role.NIGHT_ADMIN));
    	roleListModel = new SelectModelImpl(null, optionList);
    }

    public void buildStatusList() {
        statusListModel = new ArrayList<String>();
        statusListModel.add(messages.get("aktif"));
        statusListModel.add(messages.get("nonAktif"));
    }

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            user = userService.get(id);
        } else {
            user = new User();
            role = new Role();
        }
    }

    void setupRender(){
        buildRoleList();
        buildStatusList();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubtitle() {
        if (id != null) {
            return messages.get("subtitle-edit");

        }

        return messages.get("subtitle-add");
    }

    public String getAction() {
        if (id != null) {
            return messages.get("edit");
        }

        return messages.get("add");
    }

    public void onValidateFromForm(){
        if(!user.getPassword().equals(confirmPassword)){
            form.recordError(messages.get("passwordError"));
        }
        if (id == null) {
            User userFromDB = userService.getByUsername(user.getUsername().toUpperCase());
            if(userFromDB != null){
                form.recordError(messages.get("userExist"));
            }
        }
    }

    @DiscardAfter
    Object onSuccess() {
        if (user.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            user.setCreated(new Date());
            user.setCreatedby(userId);
            user.setUpdated(new Date());
            user.setUpdatedby(userId);
        }

        if (statusList.equalsIgnoreCase(messages.get("aktif"))) {
            user.setStatus(1);
        } else {
            user.setStatus(0);
        }

        userService.saveUser(user);
        return MaintenanceUserList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return MaintenanceUserList.class;
    }
}
