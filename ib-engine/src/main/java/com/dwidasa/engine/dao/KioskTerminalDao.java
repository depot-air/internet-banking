package com.dwidasa.engine.dao;

import com.dwidasa.engine.model.KioskPrinter;
import com.dwidasa.engine.model.KioskTerminal;
import com.dwidasa.engine.model.Location;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public interface KioskTerminalDao extends GenericDao<KioskTerminal, Long> {
    public KioskTerminal getByTerminalId(String terminalId);
}
