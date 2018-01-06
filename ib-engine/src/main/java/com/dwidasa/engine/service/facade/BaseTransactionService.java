package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.ResultView;

import java.util.List;

/**
 * Base transaction interface containing common functionality to all
 * financial transaction.
 *
 * @author rk
 */
public interface BaseTransactionService {
    /**
     * Inquiry required information to core
     *
     * @param view view object
     * @return view object populated with required information
     * @throws BusinessException receive response code error from core
     */
    public BaseView inquiry(BaseView view);

    /**
     * Phase to do modification to bussines object before execute phase
     *
     * @param view view object
     */
    public void confirm(BaseView view);

    /**
     * Execute purchase transaction after confirmation.
     *
     * @param view view object
     * @return an object model subject to specific transaction
     * @throws BusinessException business error when processing transaction
     */
    public BaseView execute(BaseView view);

    /**
     * Reprint a transaction.
     *
     * @param view view object
     * @param transactionId orginal transction id
     * @return an object model subject to specific transaction
     */
    public BaseView reprint(BaseView view, Long transactionId);

    /**
     * Register an account to saved list
     * @param register customer register object
     * @return result view
     */
    public ResultView register(CustomerRegister register);

    /**
     * Unregister already registered data
     * @param customerRegisterId customer register id
     * @return result view
     */
    public ResultView unregister(Long customerRegisterId, Long customerId);

    /**
     * Return customer register specifics to customer id and transaction type
     * @param customerId customer id
     * @param transactionType transaction type code
     * @param billerCode biller code
     * @return list of customer register
     */
    public List<CustomerRegister> getRegisters(Long customerId, String transactionType, String billerCode);
    //order By customer registers
    public List<CustomerRegister> getRegistersOrderBy(Long customerId, String transactionType, String billerCode, String orderBy);
}
