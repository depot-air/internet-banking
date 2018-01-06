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
import com.dwidasa.engine.model.ResponseCode;
import com.dwidasa.engine.service.ResponseCodeService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 11/10/11
 * Time: 00:15 am
 */
@Import(library = "context:bprks/js/master/ResponseCodeDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class ResponseCodeDetail {
    @Property
    private ResponseCode responseCode;

    @Inject
    private ResponseCodeService responseCodeService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            responseCode = responseCodeService.get(id);
        } else {
            responseCode = new ResponseCode();
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
        if (responseCode.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            responseCode.setCreated(new Date());
            responseCode.setCreatedby(userId);
            responseCode.setUpdated(new Date());
            responseCode.setUpdatedby(userId);
        }

        versionService.versionedSave(responseCode, responseCodeService);
        return ResponseCodeList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return ResponseCodeList.class;
    }
    
    @Inject
    private JavaScriptSupport javaScriptSupport;
    
    void setupRender() {
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
    }
}
