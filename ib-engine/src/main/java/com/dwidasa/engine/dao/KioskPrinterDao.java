package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.KioskPrinter;
import com.dwidasa.engine.model.Location;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public interface KioskPrinterDao extends GenericDao<KioskPrinter, Long> {
    public KioskPrinter getByKioskTerminalId(Long kioskTerminalId);
    public Boolean incrementPrinting(Long terminalId, Integer counterType, Double incr);

}
