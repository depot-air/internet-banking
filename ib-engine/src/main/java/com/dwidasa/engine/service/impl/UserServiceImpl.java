package com.dwidasa.engine.service.impl;

import java.util.List;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.UserDao;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.security.PasswordEncoder;
import com.dwidasa.engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/14/11
 * Time: 10:00 PM
 */
@Service("userService")
public class UserServiceImpl extends GenericServiceImpl<User, Long> implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        super(userDao);
        this.userDao = userDao;
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) {
        if (user.getId() == null) {
            user.setUsername(user.getUsername().toUpperCase());
        }

        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            if (user.getId() == null) {
                passwordChanged = true;
            } else {
                String currentPassword = userDao.get(user.getId()).getPassword();
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            if (passwordChanged) {
                user.setPassword(passwordEncoder.encodePassword(user.getPassword()));
            }
        }

        return userDao.save(user);
    }

    /**
     * {@inheritDoc}
     */
    public User authenticate(String username, String password) {
        User user = userDao.get(username);

        if (user != null) {
            if (user.getStatus().equals(Constants.INACTIVE_STATUS)) {
                throw new BusinessException("IB-1002");
            }

            String encodedPass = user.getPassword();
            if (passwordEncoder.isPasswordValid(encodedPass, password)) {
                return user;
            }
        }

        throw new BusinessException("IB-1000");
    }

	@Override
	public User getByUsername(String username) {
		return userDao.get(username);
	}

	@Override
	public List<User> getTreasuryUsers() {
		return userDao.getTreasuryUsers();
	}
}
