package com.dwidasa.engine.dao.impl;

import java.util.List;

import com.dwidasa.engine.dao.UserDao;
import com.dwidasa.engine.dao.mapper.UserMapper;
import com.dwidasa.engine.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:38 PM
 */
@Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {
    @Autowired
    public UserDaoImpl(DataSource dataSource, UserMapper userMapper) {
        super("m_user", dataSource);
        defaultMapper = userMapper;

        insertSql = new StringBuilder()
            .append("insert into m_user (")
            .append("   m_role_id, username, password, name, phone, email, failed_auth_attempts, last_login, ")
            .append("   status, created, createdby, updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   :roleId, :username, :password, :name, :phone, :email, :failedAuthAttempts, :lastLogin, ")
            .append("   :status, :created, :createdby, :updated, :updatedby ")
            .append(") ");

        updateSql = new StringBuilder()
            .append("update m_user ")
            .append("set ")
            .append("   m_role_id = :roleId, username = :username, password = :password, name = :name, ")
            .append("   phone = :phone, email = :email, failed_auth_attempts = :failedAuthAttempts, ")
            .append("   last_login = :lastLogin, status = :status, created = :created, createdby = :createdby, ")
            .append("   updated = :updated, updatedby = :updatedby ")
            .append("where id = :id ");
    }

    /**
     * {@inheritDoc}
     */
    public User get(String username) {
        StringBuilder sql = new StringBuilder()
                .append("select mu.* ")
                .append("from m_user mu ")
                .append("where mu.username = ? ");

        User result = null;

        try {
            result = getSimpleJdbcTemplate().queryForObject(sql.toString(), defaultMapper, username);
        } catch (DataAccessException e) {
        }

        return result;
    }

	@Override
	public List<User> getTreasuryUsers() {
        StringBuilder sql = new StringBuilder()
	        .append("select mu.* ")
	        .append("from m_user mu ")
	        .append("where mu.m_role_id = ? ");

        return getSimpleJdbcTemplate().query(sql.toString(), defaultMapper, 3);	//3 = role treasury
	}
}
