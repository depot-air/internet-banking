package com.dwidasa.engine.model.view;

import com.dwidasa.engine.model.CustomerRegister;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Base interface for all transactional view. This class will be used for
 * simplifying process on <code>BaseTransactionSercive</code> class implementation.
 *
 * @author rk
 */
public interface BaseView {
    /**
     * Get customer id
     * @return customer id
     */
    public Long getCustomerId();

    /**
     * Get customer account number
     * @return customerAccount string
     */
    public String getAccountNumber();

    /**
     * Get transaction date.
     * @return transaction date
     */
    public Date getTransactionDate();

    /**
     * Get transaction type
     * @return transactionType code string
     */
    public String getTransactionType();

    /**
     * Get transaction amount.
     * @return transaction amount
     */
    public BigDecimal getAmount();

    /**
     * Get response code.
     * @return response code
     */
    public String getResponseCode();

    /**
     * Get reference number.
     * @return reference number
     */
    public String getReferenceNumber();

    /**
     * Validating view object for non database validation.
     * @return true if validation succeed
     */
    public Boolean validate();

    /**
     * Transform this object into customer register.
     * @return customer register object
     */
    public CustomerRegister transform();
}
