package com.dwidasa.engine.service.facade;

import com.dwidasa.engine.model.view.BaseView;

/**
 * 
 * @author gandos
 *
 */
public interface KioskReprintService {
	/**
     * Reprint a transaction for kiosk, it fetch a transaction detail from mx
     * while ib reprint feature fetch the detail from local database 
     *
     * @param view view object
     * @return an object model subject to specific transaction
     */
    public BaseView reprint(BaseView view);
}
