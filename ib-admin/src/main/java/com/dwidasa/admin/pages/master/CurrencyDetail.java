package com.dwidasa.admin.pages.master;

import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.Currency;
import com.dwidasa.engine.service.CurrencyService;
import com.dwidasa.engine.service.VersionService;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/11/11
 * Time: 1:42
 */
@Import(library = "context:bprks/js/master/CurrencyDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class CurrencyDetail {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Property
    private Currency currency;

    @Inject
    private CurrencyService currencyService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Inject
    private SessionManager sessionManager;

    void setupRender() {
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
    }

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            currency = currencyService.get(id);
        } else {
            currency = new Currency();
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
        if (currency.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            currency.setCreated(new Date());
            currency.setCreatedby(userId);
            currency.setUpdated(new Date());
            currency.setUpdatedby(userId);
        }

        versionService.versionedSave(currency, currencyService);
        return CurrencyList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return CurrencyList.class;
    }
}
