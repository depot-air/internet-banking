package com.dwidasa.admin.transform;

import java.util.ArrayList;
import java.util.List;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.view.UserView;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.RoleService;
import com.dwidasa.engine.service.ServiceLocator;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 12/20/11
 * Time: 00:44 am
 */
public class UserTransformer implements Transformer {
    private RoleService roleService;

    public UserTransformer() {
        roleService = (RoleService) ServiceLocator.getService("roleService");
    }

    public List transform(List entities) {
        List<UserView> result = new ArrayList<UserView>();

        for (Object e : entities) {
            User user = (User) e;
            UserView userView = new UserView();

            userView.setId(user.getId());
            userView.setUsername(user.getUsername());
            userView.setPassword(user.getPassword());            
            userView.setEmail(user.getEmail());
            userView.setFailedAuthAttempts(user.getFailedAuthAttempts());
            userView.setLastLogin(user.getLastLogin());
            userView.setPhone(user.getPhone());
            if(user.getStatus() == 1){
              userView.setStatus("Aktif");
            }
            else {
              userView.setStatus("Non Aktif");
            }
            userView.setName(user.getName());
            
            if (user.getRoleId().equals(Constants.Role.SUPERUSER)) {
            	userView.setRoleName(Constants.RoleName.SUPERUSER);
            } else if (user.getRoleId().equals(Constants.Role.ADMIN)) {
            	userView.setRoleName(Constants.RoleName.ADMIN);
            } else if (user.getRoleId().equals(Constants.Role.TREASURY)) {
            	userView.setRoleName(Constants.RoleName.TREASURY);
            } else if (user.getRoleId().equals(Constants.Role.DAY_ADMIN)) {
            	userView.setRoleName(Constants.RoleName.DAY_ADMIN);
            } else if (user.getRoleId().equals(Constants.Role.NIGHT_ADMIN)) {
            	userView.setRoleName(Constants.RoleName.NIGHT_ADMIN);
            }
            result.add(userView);
        }

        return result;
    }
}
