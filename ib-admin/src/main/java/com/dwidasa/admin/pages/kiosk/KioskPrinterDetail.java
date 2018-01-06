package com.dwidasa.admin.pages.kiosk;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.KioskPaper;
import com.dwidasa.engine.model.KioskPrinter;
import com.dwidasa.engine.model.KioskTerminal;
import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.service.*;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 4:51 PM
 * To change this template use File | Settings | File Templates.
 */
//@Import(library = "context:bprks/js/master/CurrencyDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class KioskPrinterDetail {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Property
    private KioskPrinter kioskPrinter;

    @Inject
    private KioskPrinterService kioskPrinterService;

    @Inject
    private KioskPaperService kioskPaperService;

    @Inject
    private KioskTerminalService kioskTerminalService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Property
    private SelectModel mKioskTerminalIdModel;

    @Property
    private SelectModel strukMaxIdModel;

    @Property
    private SelectModel a4MaxIdModel;

    @Property
    private SelectModel ssppMaxIdModel;

    @Inject
    private SessionManager sessionManager;

    void setupRender() {
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
        buildmKioskTerminalIdModel();
        buildStrukMaxIdModel();
        buildA4MaxIdModel();
        buildSsppMaxIdModel();
    }

    private void buildmKioskTerminalIdModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<KioskTerminal> kioskTerminalList = kioskTerminalService.getAll();
    	for (KioskTerminal kioskTerminal : kioskTerminalList) {
    		optionList.add(new OptionModelImpl(kioskTerminal.getDescription() , kioskTerminal.getId()));
    	}
    	mKioskTerminalIdModel = new SelectModelImpl(null, optionList);
	}

    private void buildStrukMaxIdModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<KioskPaper> kioskPaperList = kioskPaperService.getByPrinterType(KioskPaper.PRINTER_TYPE.STRUK);
    	for (KioskPaper kioskPaper : kioskPaperList) {
    		optionList.add(new OptionModelImpl(kioskPaper.getDescription() + " - " +  kioskPaper.getMaxPaper() + "x Cetak", kioskPaper.getId()));
    	}
    	strukMaxIdModel = new SelectModelImpl(null, optionList);
	}

    private void buildA4MaxIdModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<KioskPaper> kioskPaperList = kioskPaperService.getByPrinterType(KioskPaper.PRINTER_TYPE.MUTASI);
    	for (KioskPaper kioskPaper : kioskPaperList) {
    		optionList.add(new OptionModelImpl(kioskPaper.getDescription() + " - " +  kioskPaper.getMaxPaper() + "x Cetak - " + kioskPaper.getMaxTape() + "x Pita", kioskPaper.getId()));
    	}
    	a4MaxIdModel = new SelectModelImpl(null, optionList);
	}

    private void buildSsppMaxIdModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<KioskPaper> kioskPaperList = kioskPaperService.getByPrinterType(KioskPaper.PRINTER_TYPE.SSPP);
    	for (KioskPaper kioskPaper : kioskPaperList) {
    		optionList.add(new OptionModelImpl(kioskPaper.getDescription() + " - " +  kioskPaper.getMaxTape() + "x Pita", kioskPaper.getId()));
    	}
    	ssppMaxIdModel = new SelectModelImpl(null, optionList);
	}

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            kioskPrinter = kioskPrinterService.get(id);
            KioskTerminal terminal = kioskTerminalService.get(kioskPrinter.getmKioskTerminalId());
            kioskPrinter.setKioskTerminal(terminal);
            kioskPrinter.setKioskTerminalDesc(terminal.getDescription());
        } else {
            kioskPrinter = new KioskPrinter();
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubtitle() {
        if (id != null) {
            return messages.get("subtitle-edit");
        }

        return messages.get("subtitle-add");
    }

    public String getAction() {
        if (id != null) {
            return messages.get("edit");
        }

        return messages.get("add");
    }

    @Inject
    private VersionService versionService;

    @DiscardAfter
    Object onSelectedFromAdd() {
        if (kioskPrinter.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            kioskPrinter.setCreated(new Date());
            kioskPrinter.setCreatedby(userId);
            kioskPrinter.setUpdated(new Date());
            kioskPrinter.setUpdatedby(userId);
        }
        versionService.versionedSave(kioskPrinter, kioskPrinterService);
        return KioskPrinterList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return KioskPrinterList.class;
    }

    @DiscardAfter
    Object onSelectedFromStrukReset() {
        kioskPrinter.setStrukCounter(Double.parseDouble("0"));
        kioskPrinterService.save(kioskPrinter);
        return KioskPrinterList.class;
    }

    @DiscardAfter
    Object onSelectedFromA4Reset() {
        kioskPrinter.setA4Counter(0L);
        kioskPrinterService.save(kioskPrinter);
        return KioskPrinterList.class;
    }

    @DiscardAfter
    Object onSelectedFromA4TapeReset() {
        kioskPrinter.setA4CounterTape(0L);
        kioskPrinterService.save(kioskPrinter);
        return KioskPrinterList.class;
    }

    @DiscardAfter
    Object onSelectedFromSsppReset() {
        kioskPrinter.setSsppCounterTape(0L);
        kioskPrinterService.save(kioskPrinter);
        return KioskPrinterList.class;
    }

}
