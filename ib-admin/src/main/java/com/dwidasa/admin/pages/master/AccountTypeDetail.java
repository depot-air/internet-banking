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
import com.dwidasa.engine.model.AccountType;
import com.dwidasa.engine.service.AccountTypeService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 09/11/11
 * Time: 18:43
 */
@Import(library = "context:bprks/js/master/AccountTypeDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class AccountTypeDetail {

	@Inject
	private JavaScriptSupport javaScriptSupport;

    @Property
    private AccountType accountType;

    @Inject
    private AccountTypeService accountTypeService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    void setupRender(){
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
    }

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            accountType = accountTypeService.get(id);
        }
        else {
            accountType = new AccountType();
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
        if (accountType.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            accountType.setCreated(new Date());
            accountType.setCreatedby(userId);
            accountType.setUpdated(new Date());
            accountType.setUpdatedby(userId);
        }

        versionService.versionedSave(accountType, accountTypeService);
        return AccountTypeList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return AccountTypeList.class;
    }
}
