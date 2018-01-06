package com.dwidasa.admin.pages.treasury;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.admin.view.TransactionStageView;
import com.dwidasa.engine.model.TransactionStage;
import com.dwidasa.engine.service.TransactionStageService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/11/11
 * Time: 17:21
 */
@Restricted(groups={Constants.RoleName.TREASURY,Constants.RoleName.SUPERUSER})
public class TreasuryList {

    @Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private TransactionStageView row;

    @Property
    @Persist
    private String status;

    @Property
    @Persist
    private Date startDate;

    @Property
    @Persist
    private Date endDate;

    @InjectPage
    private TreasuryDetail transactionStageDetail;

    @Inject
    private TransactionStageService transactionStageService;

    @Property
    private List statusList;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;
        buildStatusModel();
        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (status != null && !status.equals("")) {
            if (!status.equals(messages.get("ALL"))) {
                restrictions.add("ts.status like '%' || ? || '%'");
                String statusValue = new String();
                if (status.equalsIgnoreCase(messages.get("DELIVERED"))) {
                    statusValue = "DELIVERED";
                } else if (status.equalsIgnoreCase(messages.get("HANDLED"))) {
                    statusValue = "HANDLED";
                } else if (status.equalsIgnoreCase(messages.get("SUCCEED"))) {
                    statusValue = "SUCCEED";
                } else if (status.equalsIgnoreCase(messages.get("FAILED"))) {
                    statusValue = "FAILED";
                }
                values.add(statusValue);
            } else if (status.equals(messages.get("ALL"))) {
                restrictions.add("ts.status not like '%' || ? || '%'");
                values.add("PENDING");
            }
        }

        if (startDate != null) {
            restrictions.add("ts.created >= ?");
            values.add(startDate);
        }
        if (endDate != null) {
            restrictions.add("ts.created < ?");
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.add(Calendar.DATE,1);
            values.add(cal.getTime());
        }

        Transformer t = TransformerFactory.getTransformer(TransactionStage.class.getSimpleName());
        dataSource = new BaseDataSource(TransactionStageView.class, Constants.PAGE_SIZE, restrictions, values, t);
    }

    private void buildStatusModel() {
        statusList = new ArrayList();
        statusList.add(messages.get("ALL"));
        statusList.add(messages.get("DELIVERED"));
        statusList.add(messages.get("HANDLED"));
        statusList.add(messages.get("SUCCEED"));
        statusList.add(messages.get("FAILED"));
    }

    @DiscardAfter
    void onSelectedFromReset() {
        status = null;
        startDate = null;
        endDate = null;
    }
}
