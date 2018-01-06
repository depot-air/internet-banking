package com.dwidasa.engine.service.view;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;

/**
 * Base interface for all transactional view object.
 *
 * @author rk
 */
public interface BaseViewService {
    /**
     * Pre processing on view upon confirmation / inquiry phase
     * @param view view object to process
     */
    public void preProcess(BaseView view);

    /**
     * Defining specific validation to each view object
     * @param view view object to validate
     * @return true if validation succeed else otherwise
     */
    public Boolean validate(BaseView view);

    /**
     * Composing custom data field for inquiry phase
     * @param view source view object
     * @param transaction transaction object
     */
    public void composeInquiry(BaseView view, Transaction transaction);

    /**
     * Decompose transaction's custom data feedback of inquiry phase and then
     * apply it to view object specified.
     * @param view source view object
     * @param transaction transaction object
     * @return if true then this decompose method calling is already answer the question
     */
    public Boolean decomposeInquiry(BaseView view, Transaction transaction);

    /**
     * Composing custom data field for execute phase
     * @param view source view object
     * @param transaction transaction object
     */
    public void composeTransaction(BaseView view, Transaction transaction);

    /**
     * Decompose transaction's custom data feedback of execute phase and then
     * apply it to view object specified.
     * @param view source view object
     * @param transaction source data
     * @return if true then this decompose method calling is already answer the question
     */
    public Boolean decomposeTransaction(BaseView view, Transaction transaction);

    /**
     * Composing custom data field for reprint phase
     * @param view source view object
     * @param transaction transaction object
     */
    public void composeReprint(BaseView view, Transaction transaction);

    /**
     * Decompose transaction's custom data feedback of reprint phase and then
     * apply it to view object specified.
     * @param view source view object
     * @param transaction source data
     * @return if true then this decompose method calling is already answer the question
     */
    public Boolean decomposeReprint(BaseView view, Transaction transaction);
}
