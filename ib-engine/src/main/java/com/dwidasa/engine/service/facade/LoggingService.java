package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;

/**
 * Logging customer when authentication failed. Method contained here is executed using
 * new transaction i.e. AUTONOMOUS_TRANSACTION
 *
 * @author rk
 */
public interface LoggingService {
    /**
     * Increment failed authentication counter for a customer.
     * Separating this class to another service has a purpose, for spring aop to work correctly.
     *
     * @param customer customer object
     * @return true if counter still less than FAILED_AUTHENTICATION_ATTEMPTS stored in M_PARAMETER
     * @exception BusinessException if counter equal to FAILED_AUTHENTICATION_ATTEMPTS
     * stored in M_PARAMETER
     */
    public Boolean incFailedAuthAttempts(Customer customer);

    public Boolean incFailedTokenAttempts(Customer customer);
    
    /**
     * Log transaction even BusinessException was thrown.
     * @param transaction transaction object
     * @param view view object
     */
    public void logFailedTransaction(Transaction transaction, BaseView view);
    
    /**
     * Log non financial customer activity
     */    
    public void logActivity(Long customerId, String activityType, String activityData, String referenceNumber, String merchantType, String terminalId);

}
