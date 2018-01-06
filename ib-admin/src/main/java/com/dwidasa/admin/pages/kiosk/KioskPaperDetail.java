package com.dwidasa.admin.pages.kiosk;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.KioskPaper;
import com.dwidasa.engine.service.CurrencyService;
import com.dwidasa.engine.service.KioskPaperService;
import com.dwidasa.engine.service.VersionService;
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
 * User: emil
 * Date: 10/11/11
 * Time: 1:42
 */
//@Import(library = "context:bprks/js/master/CurrencyDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class KioskPaperDetail {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Property
    private KioskPaper kioskPaper;

    @Inject
    private KioskPaperService kioskPaperService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Property
    private SelectModel printerTypeModel;

    @Inject
    private SessionManager sessionManager;

    void setupRender() {
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
        buildStrukMaxIdModel();
    }

    private void buildStrukMaxIdModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
        optionList.add(new OptionModelImpl(KioskPaper.PRINTER_TYPE.STRUK_DESC, KioskPaper.PRINTER_TYPE.STRUK));
        optionList.add(new OptionModelImpl(KioskPaper.PRINTER_TYPE.MUTASI_DESC, KioskPaper.PRINTER_TYPE.MUTASI));
        optionList.add(new OptionModelImpl(KioskPaper.PRINTER_TYPE.SSPP_DESC, KioskPaper.PRINTER_TYPE.SSPP));

    	printerTypeModel = new SelectModelImpl(null, optionList);
	}

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            kioskPaper = kioskPaperService.get(id);
        } else {
            kioskPaper = new KioskPaper();
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
        if (kioskPaper.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            kioskPaper.setCreated(new Date());
            kioskPaper.setCreatedby(userId);
            kioskPaper.setUpdated(new Date());
            kioskPaper.setUpdatedby(userId);
        }

        versionService.versionedSave(kioskPaper, kioskPaperService);
        return KioskPaperList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return KioskPaperList.class;
    }
}
