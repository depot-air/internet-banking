package com.dwidasa.engine.dao.mapper;

import com.dwidasa.engine.model.User;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:26 PM
 */
@Component("userMapper")
public class UserMapper extends ChainedRowMapper<User> implements ParameterizedRowMapper<User> {
    public UserMapper() {
    }

    public User chainRow(ResultSet rs, int index) throws SQLException {
        User user = new User();

        user.setId(rs.getLong(++index));
        user.setRoleId(rs.getLong(++index));
        user.setUsername(rs.getString(++index));
        user.setPassword(rs.getString(++index));
        user.setName(rs.getString(++index));
        user.setPhone(rs.getString(++index));
        user.setEmail(rs.getString(++index));
        user.setFailedAuthAttempts(rs.getInt(++index));
        user.setLastLogin(rs.getTimestamp(++index));
        user.setStatus(rs.getInt(++index));
        user.setCreated(rs.getTimestamp(++index));
        user.setCreatedby(rs.getLong(++index));
        user.setUpdated(rs.getTimestamp(++index));
        user.setUpdatedby(rs.getLong(++index));

        return user;
    }
}
