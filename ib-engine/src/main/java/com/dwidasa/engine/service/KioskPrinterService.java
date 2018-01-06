package com.dwidasa.engine.service;

import com.dwidasa.engine.model.KioskPrinter;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KioskPrinterService extends GenericService<KioskPrinter, Long> {
     public Boolean incrementPrinter(String terminalId, Integer counterType, Double incr);
}
