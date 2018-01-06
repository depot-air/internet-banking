package com.dwidasa.engine.service.transform;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;

/**
 * Base interface for all transform implementations.
 * This interface define the common methods to do transformation
 * between view object to transaction's POJO
 *
 * @author prayugo
 */
public interface Transformer {
    /**
	 * do the transformation from view object to <code>Transaction</code>
	 * @param view view object
	 * @param transaction empty transaction object
	 * @return transaction object after transformation
	 */
    public Transaction transformTo(BaseView view, Transaction transaction);
}
