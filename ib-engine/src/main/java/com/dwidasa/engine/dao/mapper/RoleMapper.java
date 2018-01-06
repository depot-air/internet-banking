package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.Role;
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
@Component("roleMapper")
public class RoleMapper extends ChainedRowMapper<Role> implements ParameterizedRowMapper<Role> {
    public RoleMapper() {
    }

    public Role chainRow(ResultSet rs, int index) throws SQLException {
        Role role = new Role();

        role.setId(rs.getLong(++index));
        role.setRoleName(rs.getString(++index));
        role.setDescription(rs.getString(++index));
        role.setCreated(rs.getTimestamp(++index));
        role.setCreatedby(rs.getLong(++index));
        role.setUpdated(rs.getTimestamp(++index));
        role.setUpdatedby(rs.getLong(++index));

        return role;
    }
}
