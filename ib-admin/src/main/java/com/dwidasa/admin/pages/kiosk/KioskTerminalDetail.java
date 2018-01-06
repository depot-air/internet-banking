package com.dwidasa.admin.pages.kiosk;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.service.*;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/11/11
 * Time: 1:42
 */
//@Import(library = "context:bprks/js/master/CurrencyDetail.js")
@Restricted(groups={Constants.RoleName.ADMIN, Constants.RoleName.SUPERUSER})
public class KioskTerminalDetail {
    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Property
    private KioskTerminal kioskTerminal;

    @Inject
    private KioskTerminalService kioskTerminalService;

    @Inject
    private LocationService locationService;

    @Inject
    private UserService userService;

    @Inject
    private Messages messages;

    @Persist
    @Property(write = false)
    private Long id;

    @Property
    private SelectModel mLocationIdBranchModel;

    @Property
    private SelectModel mLocationIdTerminalModel;

    @Property
    private SelectModel mUserIdModel;

    @Inject
    private SessionManager sessionManager;

    void setupRender() {
        javaScriptSupport.addScript(String.format("new DisabledComponent();"));
        buildLocationBranchModel();
        buildLocationTerminalModel();
        buildAdministratorModel();
    }

    private void buildLocationBranchModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<Location> locationList = locationService.getByLocationTypeId(1L);  //Branch
    	for (Location location : locationList) {
    		optionList.add(new OptionModelImpl(location.getDescription(), location.getId()));
    	}
    	mLocationIdBranchModel = new SelectModelImpl(null, optionList);
	}

    private void buildLocationTerminalModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<Location> locationList = locationService.getByLocationTypeId(4L);  //Terminal
    	for (Location location : locationList) {
    		optionList.add(new OptionModelImpl(location.getDescription(), location.getId()));
    	}
    	mLocationIdTerminalModel = new SelectModelImpl(null, optionList);
	}

    private void buildAdministratorModel() {
    	List<OptionModel> optionList = new ArrayList<OptionModel>();
    	List<User> userList = userService.getAll();
    	for (User user : userList) {
    		optionList.add(new OptionModelImpl(user.getName(), user.getId()));
    	}
    	mUserIdModel = new SelectModelImpl(null, optionList);
	}

    void onActivate(Long id) {
        this.id = id;
    }

    void onPrepare() {
        if (id != null) {
            kioskTerminal = kioskTerminalService.get(id);
        } else {
            kioskTerminal = new KioskTerminal();
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
        if (kioskTerminal.getId() == null) {
            Long userId = sessionManager.getLoggedUser().getId();
            kioskTerminal.setCreated(new Date());
            kioskTerminal.setCreatedby(userId);
            kioskTerminal.setUpdated(new Date());
            kioskTerminal.setUpdatedby(userId);
        }

        versionService.versionedSave(kioskTerminal, kioskTerminalService);
        return KioskTerminalList.class;
    }

    @DiscardAfter
    Object onSelectedFromBack() {
        return KioskTerminalList.class;
    }
}
