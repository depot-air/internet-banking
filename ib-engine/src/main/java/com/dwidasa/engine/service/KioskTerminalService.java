package com.dwidasa.engine.service;

import com.dwidasa.engine.model.KioskTerminal;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
public interface KioskTerminalService extends GenericService<KioskTerminal, Long> {
    public KioskTerminal getByTerminalId(String terminalId);
}
