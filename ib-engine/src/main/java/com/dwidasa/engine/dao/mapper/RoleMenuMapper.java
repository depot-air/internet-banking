package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.RoleMenu;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/28/11
 * Time: 10:42 AM
 */
@Component("roleMenuMapper")
public class RoleMenuMapper extends ChainedRowMapper<RoleMenu> implements ParameterizedRowMapper<RoleMenu> {
    public RoleMenuMapper() {
    }

    public RoleMenu chainRow(ResultSet rs, int index) throws SQLException {
        RoleMenu roleMenu = new RoleMenu();

        roleMenu.setId(rs.getLong(++index));
        roleMenu.setRoleId(rs.getLong(++index));
        roleMenu.setMenuId(rs.getLong(++index));
        roleMenu.setCreated(rs.getTimestamp(++index));
        roleMenu.setCreatedby(rs.getLong(++index));
        roleMenu.setUpdated(rs.getTimestamp(++index));
        roleMenu.setUpdatedby(rs.getLong(++index));

        return roleMenu;
    }
}
