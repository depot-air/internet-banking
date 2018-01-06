package com.dwidasa.admin.pages.kiosk;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.engine.model.KioskTerminal;
import com.dwidasa.engine.service.LocationService;
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
public class KioskTerminalList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private KioskTerminal row;

    @Property
    @Persist
    private String terminalId;

    @InjectPage
    private KioskTerminalDetail kioskTerminalDetail;

    @Inject
    private LocationService locationService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (terminalId != null && !terminalId.equals("")) {
            restrictions.add("upper(terminal_id) like '%' || ? || '%'");
            values.add(terminalId.toUpperCase());
        }

        Transformer t = TransformerFactory.getTransformer(KioskTerminal.class.getSimpleName());
        dataSource = new BaseDataSource(KioskTerminal.class, Constants.PAGE_SIZE, restrictions, values, t);
        pageSize = Constants.PAGE_SIZE;
    }

    @DiscardAfter
    void onSelectedFromReset() {
        terminalId = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        kioskTerminalDetail.setId(null);
        return kioskTerminalDetail;
    }

    @Inject
    private VersionService versionService;
    
    @Inject
    private SessionManager sessionManager;

    @DiscardAfter
    void onActionFromDelete(Long id) {
    	KioskTerminal tt = new KioskTerminal();
        tt.setId(id);
        Long userId = sessionManager.getLoggedUser().getId();
        versionService.versionedRemove(tt, locationService, userId);
    }
}
