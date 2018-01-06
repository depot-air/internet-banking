package com.dwidasa.engine.service.view;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;

/**
 * An interface used for compose or decompose specific message to be sent to core.
 *
 * @author rk
 */
public interface MessageCustomizer {
    /**
     * Compose custom data message from view object. No return value is required,
     * because this operation include applying custom data to transaction object itself.
     * @param view view object
     * @param transaction transaction object
     */
    public void compose(BaseView view, Transaction transaction);

    /**
     * Decompose custom data feedback and apply it to view object. Basically this view parameter
     * can be BaseView object or list of BaseView, its depends to the enclosing transaction.
     * @param view BaseView object or list of BaseView
     * @param transaction source data
     * @return if true then this decompose method calling is already answer the question
     */
    public Boolean decompose(Object view, Transaction transaction);
}
