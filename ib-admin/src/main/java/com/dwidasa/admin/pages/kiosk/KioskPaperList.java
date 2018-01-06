package com.dwidasa.admin.pages.kiosk;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.model.KioskPaper;
import com.dwidasa.engine.service.KioskPaperService;
import com.dwidasa.engine.service.VersionService;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/11/11
 * Time: 1:11
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class KioskPaperList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private KioskPaper row;

    @Property
    @Persist
    private String description;

    @InjectPage
    private KioskPaperDetail kioskPaperDetail;

    @Inject
    private KioskPaperService kioskPaperService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (description != null && !description.equals("")) {
            restrictions.add("upper(description) like '%' || ? || '%'");
            values.add(description.toUpperCase());
        }

        dataSource = new BaseDataSource(KioskPaper.class, Constants.PAGE_SIZE, restrictions, values);
    }

    @DiscardAfter
    void onSelectedFromReset() {
        description = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        kioskPaperDetail.setId(null);
        return kioskPaperDetail;
    }

    @Inject
    private VersionService versionService;
    
    @Inject
    private SessionManager sessionManager;

    @DiscardAfter
    void onActionFromDelete(Long id) {
    	KioskPaper tt = new KioskPaper();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, kioskPaperService, userId);
    }
}
