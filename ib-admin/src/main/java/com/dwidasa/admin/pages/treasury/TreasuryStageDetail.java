package com.dwidasa.admin.pages.treasury;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.transform.TreasuryStageTransformer;
import com.dwidasa.admin.view.TreasuryStageView;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TreasuryStage;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TreasuryStageService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 14/11/11
 * Time: 17:22
 */
@Import(library = "context:bprks/js/treasury/TreasuryDetail.js")
@Restricted(groups={Constants.RoleName.TREASURY,Constants.RoleName.SUPERUSER})
public class TreasuryStageDetail {
    @Persist
    private TreasuryStage treasuryStage;
    
    @Persist
    private TreasuryStageView treasuryStageView;

    @Property
    private Transaction transaction;

    @Inject
    private TreasuryStageService treasuryStageService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private CacheManager cacheManager;

    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.admin.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    @Property
    @Persist
    private String changeStatus;

    @Property
    private List<String> changeStatusList;
    
    void onActivate(Long id) {
        this.id = id;
    }

    public void setupRender() {
    	buildChangeStatusModel();
    	treasuryStage = treasuryStageService.get(id);
        
        List<TreasuryStage> tsList = new ArrayList<TreasuryStage>();
        tsList.add(treasuryStage);
        
        TreasuryStageTransformer transformer = new TreasuryStageTransformer();
        List<TreasuryStageView> tsvList = transformer.transform(tsList);
        
        treasuryStageView = new TreasuryStageView();
        if (tsvList != null && tsvList.size() > 0) {
        	treasuryStageView = tsvList.get(0);
        }
        
        if(treasuryStageView.getStatus().equals(com.dwidasa.engine.Constants.PENDING_STATUS)) {
        	treasuryStageView.setOfficerName(null);
        }  
    }

    public boolean isHandled() {
    	return treasuryStage.getStatus().equals(com.dwidasa.engine.Constants.HANDLED_STATUS);
    }
    
    public void setTreasuryStageView(TreasuryStageView treasuryStageView) {
        this.treasuryStageView = treasuryStageView;
    }
    
    public TreasuryStageView getTreasuryStageView() {
        return treasuryStageView;
    }

    private void buildChangeStatusModel() {
        changeStatusList = new ArrayList<String>();
        changeStatusList.add(messages.get("SUCCEED"));
        changeStatusList.add(messages.get("FAILED"));
    }

    void onValidateFromForm() {
    	if(treasuryStage.getStatus().equals(com.dwidasa.engine.Constants.HANDLED_STATUS)) {
    		if (changeStatus.equals(messages.get("SUCCEED"))) {
    			treasuryStage.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);	
    		} else {
    			treasuryStage.setStatus(com.dwidasa.engine.Constants.FAILED_STATUS);
    		}
        }
    	treasuryStageService.save(treasuryStage);
    }
}
