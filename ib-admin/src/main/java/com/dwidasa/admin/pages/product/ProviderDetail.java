package com.dwidasa.admin.pages.product;

import java.util.Date;

import org.apache.tapestry5.SelectModel;
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
import com.dwidasa.engine.model.Provider;
import com.dwidasa.engine.service.ProviderService;
import com.dwidasa.engine.service.VersionService;

@Import(library = "context:bprks/js/product/ProviderDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class ProviderDetail {
	@Inject
	private JavaScriptSupport javaScriptSupport;

    @Property
    private Provider provider;

    @Inject
    private ProviderService providerService;

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
            provider = providerService.get(id);
        }
        else {
            provider = new Provider();
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
        if (provider.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            provider.setCreated(new Date());
            provider.setCreatedby(userId);
            provider.setUpdated(new Date());
            provider.setUpdatedby(userId);
        }

        versionService.versionedSave(provider, providerService);
        return ProviderList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return ProviderList.class;
    }
    

}
