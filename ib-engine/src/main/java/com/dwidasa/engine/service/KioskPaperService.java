package com.dwidasa.engine.service;

import com.dwidasa.engine.model.KioskPaper;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/7/12
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public interface KioskPaperService extends GenericService<KioskPaper, Long> {

	public List<KioskPaper> getByPrinterType(int printerType);
}
