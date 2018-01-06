package com.dwidasa.engine.service.impl;

import com.dwidasa.engine.dao.KioskPaperDao;
import com.dwidasa.engine.model.KioskPaper;
import com.dwidasa.engine.service.KioskPaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/6/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("kioskPaperService")
public class KioskPaperServiceImpl extends GenericServiceImpl<KioskPaper, Long> implements KioskPaperService {
    private final KioskPaperDao kioskPaperDao;
    @Autowired
    public KioskPaperServiceImpl(KioskPaperDao kioskPaperDao) {
        super(kioskPaperDao);
        this.kioskPaperDao = kioskPaperDao;
    }

    @Override
    public List<KioskPaper> getByPrinterType(int printerType) {
        return kioskPaperDao.getByPrinterType(printerType);
    }
}
