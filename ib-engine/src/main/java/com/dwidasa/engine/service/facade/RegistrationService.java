package com.dwidasa.engine.service.facade;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 6/27/13
 * Time: 4:00 PM
 */

import com.dwidasa.engine.model.Transaction;

/**
 * Service object to handle registration
 */
public interface RegistrationService {

    public void registerEBanking(final Transaction t);
}
