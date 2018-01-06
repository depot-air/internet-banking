package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.KioskPrinterDao;
import com.dwidasa.engine.dao.KioskTerminalDao;
import com.dwidasa.engine.model.KioskPrinter;
import com.dwidasa.engine.model.KioskTerminal;
import com.dwidasa.engine.service.KioskPrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("kioskPrinterService")
public class KioskPrinterServiceImpl extends GenericServiceImpl<KioskPrinter, Long> implements KioskPrinterService {

	@Autowired
	private KioskPrinterDao kioskPrinterDao;

	@Autowired
	private KioskTerminalDao kioskTerminalDao;

    @Autowired
    public KioskPrinterServiceImpl(KioskPrinterDao kioskPrinterDao) {
        super(kioskPrinterDao);
    }

    @Override
    public Boolean incrementPrinter(String terminalId, Integer counterType, Double incr) {
        KioskTerminal kioskTerminal = kioskTerminalDao.getByTerminalId(terminalId);
        kioskPrinterDao.incrementPrinting(kioskTerminal.getId(), counterType, incr);
        return true;
    }
}
