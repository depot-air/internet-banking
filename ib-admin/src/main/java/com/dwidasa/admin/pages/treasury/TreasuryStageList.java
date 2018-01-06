package com.dwidasa.admin.pages.treasury;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.BeanModelSource;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.transform.TreasuryStageTransformer;
import com.dwidasa.admin.view.TreasuryStageView;
import com.dwidasa.engine.dao.TreasuryStageDao;
import com.dwidasa.engine.model.TreasuryStage;
import com.dwidasa.engine.model.User;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/11/11
 * Time: 17:21
 */
@Restricted(groups={Constants.RoleName.TREASURY,Constants.RoleName.SUPERUSER})
public class TreasuryStageList {
    @Property
    private List<TreasuryStageView> treasuryStages;

    @Property
    private BeanModel<TreasuryStageView> model;

    @Inject
    private BeanModelSource beanModelSource;

    @Property
    private int pageSize;

    @Property
    private TreasuryStageView row;

    @Property
    @Persist
    private String periodValue;

    @Property
    @Persist
    private Date startDate;

    @Property
    @Persist
    private Date endDate;

    @InjectPage
    private TreasuryStageDetail treasuryStageDetail;

    @Inject
    private TreasuryStageDao treasuryStageDao;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    @Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, threadLocale.getLocale());
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());

    @ActivationRequestParameter
    private String type;
    
    public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }

    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;
        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        
        restrictions.add("ts.created >= ?");
        restrictions.add("ts.created < ?");
        
        if (periodValue == null || periodValue.equals("today")) {
        	periodValue = "today";
            startDate = new Date();
            endDate = startDate;
        }
        values.add(startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.DATE,1);
        values.add(cal.getTime());

        if (type != null) {
        	restrictions.add("ts.created >= ?");
        	values.add(type);
        }
        List<TreasuryStage> treasuryStagesInDB = treasuryStageDao.getCurrentPageRows(0, Constants.PAGE_SIZE, restrictions, null, values.toArray());
        
        TreasuryStageTransformer transformer = new TreasuryStageTransformer();
        treasuryStages = (List<TreasuryStageView>)transformer.transform(treasuryStagesInDB);
        
        model = beanModelSource.createDisplayModel(TreasuryStageView.class, messages);

        for (String propertyName : model.getPropertyNames()) {
            model.get(propertyName).sortable(false);
        }
    }

    @DiscardAfter
    void onSelectedFromReset() {
        startDate = null;
        endDate = null;
    }

}
