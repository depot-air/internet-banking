package com.dwidasa.admin.pages.master;

import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/10/11
 * Time: 00:31 am
 */
@Import(library = "context:bprks/js/master/TransactionTypeDetail.js")
@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class TransactionTypeDetail {

    @Property
    private TransactionType transactionType;

    @Inject
    private TransactionTypeService transactionTypeService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private VersionService versionService;

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            transactionType = transactionTypeService.get(id);
        }
        else {
            transactionType = new TransactionType();
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

    @DiscardAfter
    Object onSelectedFromAdd() {
        if (transactionType.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            transactionType.setCreated(new Date());
            transactionType.setCreatedby(userId);
            transactionType.setUpdated(new Date());
            transactionType.setUpdatedby(userId);
        }

        versionService.versionedSave(transactionType, transactionTypeService);
        return TransactionTypeList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return TransactionTypeList.class;
    }
    
    @Inject
	private JavaScriptSupport javaScriptSupport;
    
    void setupRender(){
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
    }

}
