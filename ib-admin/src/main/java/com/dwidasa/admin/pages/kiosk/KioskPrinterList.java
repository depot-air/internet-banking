package com.dwidasa.admin.pages.kiosk;


import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.engine.model.KioskPrinter;
import com.dwidasa.engine.service.KioskPrinterService;
import com.dwidasa.engine.service.VersionService;
import org.apache.tapestry5.SelectModel;
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
 * Time: 14:14
 */
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
//@Import(library = "context:bprks/js/master/CellularPrefixList.js")
public class KioskPrinterList {
    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private KioskPrinter row;

    @Property
    @Persist
    private Long id;

    @Property
    @Persist
    private Long mKioskTerminalId;

    @Property
    @Persist
    private Double strukCounter;

    @Property
    @Persist
    private Double strukMax;

    @Property
    @Persist
    private int a4Counter;

    @Property
    @Persist
    private Long a4Max;

    @Property
    @Persist
    private Long a4Tinta;

    @Property
    @Persist
    private Long a4TintaMax;

    @Property
    @Persist
    private Long ssppTinta;

    @Property
    @Persist
    private Long ssppTintaMax;

    @Property
    @Persist
    private String description;

    @InjectPage
    private KioskPrinterDetail kioskPrinterDetail;

    @Inject
    private KioskPrinterService kioskPrinterService;

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;

        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (description != null && !description.equals("")) {
            restrictions.add(" m_location_id IN " +
                    "(SELECT id FROM m_location " +
                    " WHERE upper(m_location.description) like '%' || ? || '%'" +
                    ")");
            values.add(description.toUpperCase());
        }

        Transformer t = TransformerFactory.getTransformer(KioskPrinter.class.getSimpleName());
        dataSource = new BaseDataSource(KioskPrinter.class, Constants.PAGE_SIZE, restrictions, values, t);
        pageSize = Constants.PAGE_SIZE;
    }

    @DiscardAfter
    void onSelectedFromReset() {
        description = null;
    }

    @DiscardAfter
    Object onSelectedFromAdd() {
        kioskPrinterDetail.setId(null);
        return kioskPrinterDetail;
    }

    @Inject
    private VersionService versionService;

    @Inject
    private SessionManager sessionManager;

}
