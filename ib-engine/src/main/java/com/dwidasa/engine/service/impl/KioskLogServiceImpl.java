package com.dwidasa.engine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.dao.KioskLogDao;
import com.dwidasa.engine.dao.KioskTerminalDao;
import com.dwidasa.engine.model.KioskLog;
import com.dwidasa.engine.service.KioskLogService;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 4:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("kioskLogService")
public class KioskLogServiceImpl extends GenericServiceImpl<KioskLog, Long> implements KioskLogService {

	@Autowired
	private KioskLogDao kioskPrinterDao;

	@Autowired
	private KioskTerminalDao kioskTerminalDao;

    @Autowired
    public KioskLogServiceImpl(KioskLogDao kioskLogDao) {
        super(kioskLogDao);
    }
}
