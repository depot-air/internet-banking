package com.dwidasa.admin.transform;

import com.dwidasa.engine.model.*;
import com.dwidasa.engine.service.KioskPaperService;
import com.dwidasa.engine.service.KioskTerminalService;
import com.dwidasa.engine.service.LocationService;
import com.dwidasa.engine.service.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/5/12
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class KioskPrinterTransformer implements Transformer {
    public static final String SIAP_SIAP_GANTI = "Siap-siap Ganti";
    public static final String WAKTUNYA_GANTI = "Waktunya Ganti";

    private KioskTerminalService kioskTerminalService;
    private KioskPaperService kioskPaperService;

    public KioskPrinterTransformer() {
        kioskTerminalService = (KioskTerminalService) ServiceLocator.getService("kioskTerminalService");
        kioskPaperService = (KioskPaperService) ServiceLocator.getService("kioskPaperService");
    }

    public List transform(List entities) {
        List<KioskPrinter> result = new ArrayList<KioskPrinter>();
        for (Object e : entities) {
            KioskPrinter kp = (KioskPrinter) e;

            KioskTerminal kioskTerminal = kioskTerminalService.get(kp.getmKioskTerminalId());
            kp.setKioskTerminal(kioskTerminal);
            kp.setKioskTerminalDesc(kioskTerminal.getDescription());

            KioskPaper paper = kioskPaperService.get(kp.getStrukMaxId());
            String n = (int)kp.getStrukCounter().doubleValue()  + "x Cetak";
            String paperDesc = (kp.getStrukCounter() < paper.getMaxPaper() - 25) ? n : (kp.getStrukCounter() < paper.getMaxPaper()) ? SIAP_SIAP_GANTI : WAKTUNYA_GANTI;
            kp.setStrukDesc(paperDesc);

            paper = kioskPaperService.get(kp.getA4MaxId());
            n = (int)kp.getA4Counter().doubleValue()  + "x Cetak";
            paperDesc = (kp.getA4Counter() < paper.getMaxPaper() - 25) ? n : (kp.getA4Counter() < paper.getMaxPaper()) ? SIAP_SIAP_GANTI : WAKTUNYA_GANTI;
            kp.setA4Desc(paperDesc);

            n = (int)kp.getA4CounterTape().doubleValue()  + "x Cetak";
            paperDesc = (kp.getA4CounterTape() < paper.getMaxTape() - 25) ? n : (kp.getA4CounterTape() < paper.getMaxTape()) ? SIAP_SIAP_GANTI : WAKTUNYA_GANTI;
            kp.setA4TapeDesc(paperDesc);

            paper = kioskPaperService.get(kp.getSsppMaxId());
            n = (int)kp.getSsppCounterTape().doubleValue()  + "x Cetak";
            paperDesc = (kp.getSsppCounterTape() < paper.getMaxPaper() - 25) ? n : (kp.getSsppCounterTape() < paper.getMaxPaper()) ? SIAP_SIAP_GANTI : WAKTUNYA_GANTI;
            kp.setSsppDesc(paperDesc);

            result.add(kp);
        }

        return result;
    }
}
