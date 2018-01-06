package com.dwidasa.admin.pages.treasury;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.BeanModelSource;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.services.GenericSelectModelFactory;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.admin.transform.TreasuryStageTransformer;
import com.dwidasa.admin.view.TreasuryStageView;
import com.dwidasa.engine.dao.TreasuryStageDao;
import com.dwidasa.engine.model.TreasuryStage;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.TreasuryStageService;
import com.dwidasa.engine.service.UserService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/11/11
 * Time: 17:21
 */
@Restricted(groups={Constants.RoleName.TREASURY,Constants.RoleName.SUPERUSER})
public class TreasuryStagePenugasan {

    @Property
    private GridDataSource dataSource;

    @Property
    private List<TreasuryStageView> treasuryStages;

    private List<TreasuryStage> treasuryStagesInDB;

    @Property
    private BeanModel<TreasuryStageView> model;

    @Inject
    private BeanModelSource beanModelSource;
    
    @Property
    private final TreasuryStageEncoder treasuryStageEncoder = new TreasuryStageEncoder();
    
    @Property
    private int pageSize;

    @Property
    private TreasuryStageView row;

    @Property
    @Persist
    private String status;

    @Property
    private String officerUsername;
    
    @Property
    @Persist
    private Date startDate;

    @Property
    @Persist
    private Date endDate;

    @Inject
    private TreasuryStageService treasuryStageService;

    @Inject
    private TreasuryStageDao treasuryStageDao;
    
    @Inject
    private UserService userService;
    
    @Property
    private List<String> statusList;

    @Property
    private SelectModel officerListModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private Messages messages;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @InjectComponent
    private Checkbox select;
    
    private boolean currentSelected;

    @Persist
    private List<TreasuryStageView> selectedTreasuryStages;
    
    @SuppressWarnings("unchecked")
    void setupRender() {
        pageSize = Constants.PAGE_SIZE;
        buildStatusModel();
        buildOfficerListModel();
        List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();

        if (status != null && !status.equals("")) {
            if (!status.equals(messages.get("ALL"))) {
                restrictions.add("ts.status like '%' || ? || '%'");
                String statusValue = new String();
                if (status.equalsIgnoreCase(messages.get("DELIVERED"))) {
                    statusValue = com.dwidasa.engine.Constants.QUEUED_STATUS;
                } else if (status.equalsIgnoreCase(messages.get("HANDLED"))) {
                    statusValue = com.dwidasa.engine.Constants.HANDLED_STATUS;
                } else if (status.equalsIgnoreCase(messages.get("SUCCEED"))) {
                    statusValue = com.dwidasa.engine.Constants.SUCCEED_STATUS;
                } else if (status.equalsIgnoreCase(messages.get("FAILED"))) {
                    statusValue = com.dwidasa.engine.Constants.FAILED_STATUS;
                }
                values.add(statusValue);
//            } else if (status.equals(messages.get("ALL"))) {
//                restrictions.add("ts.status not like '%' || ? || '%'");
//                values.add("PENDING");
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

        Transformer t = TransformerFactory.getTransformer(TreasuryStage.class.getSimpleName());
        dataSource = new BaseDataSource(TreasuryStageView.class, Constants.PAGE_SIZE, restrictions, values, t);

        treasuryStagesInDB = treasuryStageDao.getCurrentPageRows(0, Constants.PAGE_SIZE, restrictions, null, values.toArray());
        
        TreasuryStageTransformer transformer = new TreasuryStageTransformer();
        treasuryStages = (List<TreasuryStageView>)transformer.transform(treasuryStagesInDB);
        selectedTreasuryStages = new ArrayList<TreasuryStageView>();
        
        model = beanModelSource.createDisplayModel(TreasuryStageView.class, messages);

        for (String propertyName : model.getPropertyNames()) {
            model.get(propertyName).sortable(false);
        }
    }

    private void buildStatusModel() {
        statusList = new ArrayList<String>();
        statusList.add(messages.get("ALL"));
        statusList.add(messages.get("DELIVERED"));
        statusList.add(messages.get("HANDLED"));
        statusList.add(messages.get("SUCCEED"));
        statusList.add(messages.get("FAILED"));
    }

    private void buildOfficerListModel() {
		List<User> users = userService.getTreasuryUsers();
		officerListModel = genericSelectModelFactory.create(users);
    }

    @DiscardAfter
    void onSelectedFromReset() {
        status = null;
        startDate = null;
        endDate = null;
    }

    void onValidateFromSelect() {
        // Unfortunately, this method is never called because Checkbox doesn't bubble up VALIDATE. It's a shame because
        // this would be the perfect place to validate whether deleting is OK, or to put an entry in deleteCopyByRowNum.
        // Please vote for https://issues.apache.org/jira/browse/TAP5-2075 .
    }
    
    // This encoder is used by our Loop:
    // - during render, to convert each person to an id (Loop then stores the ids in the form, hidden).
    // - during form submission, to convert each id back to a person which it puts in our person field.
    private class TreasuryStageEncoder implements ValueEncoder<TreasuryStageView> {

        @Override
        public String toClient(TreasuryStageView value) {
            Long id = row.getId();
            return id == null ? null : id.toString();
        }

        @Override
        public TreasuryStageView toValue(String idAsString) {
            Long id = idAsString == null ? null : new Long(idAsString);
//            TreasuryStageView tsv = findTreasuryStageView(id);
            TreasuryStage ts = treasuryStageDao.get(id);
            List<TreasuryStage> tsList = new ArrayList<TreasuryStage>();
            tsList.add(ts);
            
            TreasuryStageTransformer transformer = new TreasuryStageTransformer();
            List<TreasuryStageView> tsvList = transformer.transform(tsList);
            
            TreasuryStageView tsv = new TreasuryStageView();
            // If person has since been deleted from the DB. Create a skeleton person.
            if (tsvList != null && tsvList.size() > 0) {
                tsv = tsvList.get(0);
            }

            // Loop will overwrite the firstName of the person returned.
            return tsv;
        }

        private TreasuryStageView findTreasuryStageView(Long id) {

            // We could find the person in the database, but it's cheaper to search the list we got in
            // onPrepareForSubmit().

            for (TreasuryStageView stv : treasuryStages) {
                if (stv.getId().equals(id)) {
                    return stv;
                }
            }
            return null;
        }
    };
    // The Loop component will automatically call this for every row as it is rendered.
    public boolean getCurrentSelected() {
        return currentSelected;
    }

    // The Loop component will automatically call this for every row on submit.
    public void setCurrentSelected(boolean currentSelected) {
    	if ( currentSelected ) { 
            selectedTreasuryStages.add(row);
	    } else { 
            selectedTreasuryStages.remove(row);
	    }	
    	this.currentSelected = currentSelected;
    }

    void onValidateFromForm() {
        // Populate our list of persons to delete with the submitted versions (see setDelete(...) for more).
        // Also, simulate a server-side validation error: return error if deleting a person with first name BAD_NAME.
    	
    	User selectedOfficer = userService.getByUsername(officerUsername);
    	
        for (TreasuryStageView tsv : selectedTreasuryStages) {
        	treasuryStageDao.updateOfficerById(tsv.getId(), selectedOfficer.getId());
        }
    }
}
