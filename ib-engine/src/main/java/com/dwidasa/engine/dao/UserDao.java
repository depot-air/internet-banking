package com.dwidasa.engine.dao;

import java.util.List;

import com.dwidasa.engine.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:25 PM
 */
public interface UserDao extends GenericDao<User, Long> {
    /**
     * Get user by its username.
     * @param username username
     * @return user object
     */
    User get(String username);
    List<User> getTreasuryUsers();
}
