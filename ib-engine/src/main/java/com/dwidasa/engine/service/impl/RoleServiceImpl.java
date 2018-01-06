package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.RoleDao;
import com.dwidasa.engine.model.Role;
import com.dwidasa.engine.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 12/20/11
 * Time: 4:22 PM
 */
@Service("roleService")
public class RoleServiceImpl extends GenericServiceImpl<Role, Long> implements RoleService {
    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        super(roleDao);
    }
}
