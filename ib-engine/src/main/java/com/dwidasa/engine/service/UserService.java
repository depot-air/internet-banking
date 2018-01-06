package com.dwidasa.engine.service;

import java.util.List;

import com.dwidasa.engine.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 9:11 PM
 */
public interface UserService extends GenericService<User, Long> {
    /**
     * Saves a user's information.
     * @param user the user's information
     * @return user the updated user object
     */
    User saveUser(User user);

    /**
     * Authenticate user credentials.
     * @param username username
     * @param password password
     * @return user object if authenticated otherwise BusinessException being throws.
     */
    User authenticate(String username, String password);
    
    User getByUsername(String username);
    List<User> getTreasuryUsers();    
}
