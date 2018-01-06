package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.KioskTerminalDao;
import com.dwidasa.engine.dao.LocationDao;
import com.dwidasa.engine.model.KioskPaper;
import com.dwidasa.engine.model.KioskTerminal;
import com.dwidasa.engine.service.KioskTerminalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("kioskTerminalService")
public class KioskTerminalServiceImpl extends GenericServiceImpl<KioskTerminal, Long> implements KioskTerminalService {
    @Autowired
    private KioskTerminalDao kioskTerminalDao;

    @Autowired
    public KioskTerminalServiceImpl(KioskTerminalDao kioskTerminalDao) {
        super(kioskTerminalDao);
    }

    @Override
    public KioskTerminal getByTerminalId(String terminalId) {
        return kioskTerminalDao.getByTerminalId(terminalId);
    }
}
