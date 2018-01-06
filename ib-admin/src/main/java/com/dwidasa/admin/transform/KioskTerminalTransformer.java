package com.dwidasa.admin.transform;

import com.dwidasa.engine.model.KioskTerminal;
import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.LocationService;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: DSI-Admin
 * Date: 6/8/12
 * Time: 1:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class KioskTerminalTransformer implements Transformer {
    public static final String SIAP_SIAP_GANTI = "Siap-siap Ganti";
    public static final String WAKTUNYA_GANTI = "Waktunya Ganti";

    private LocationService locationService;
    private UserService userService;

    public KioskTerminalTransformer() {
        locationService = (LocationService) ServiceLocator.getService("locationService");
        userService = (UserService) ServiceLocator.getService("userService");
    }

    public List transform(List entities) {
        List<KioskTerminal> result = new ArrayList<KioskTerminal>();
        for (Object e : entities) {
            KioskTerminal kioskTerminal = (KioskTerminal) e;

            Location branch = locationService.get(kioskTerminal.getmLocationIdBranch());
            Location kioskLocation = locationService.get(kioskTerminal.getmLocationIdTerminal());
            User admin = userService.get(kioskTerminal.getmUserId());

            kioskTerminal.setmLocationIdTerminal(kioskLocation.getId());
            kioskTerminal.setLocationTerminal(kioskLocation);
            kioskTerminal.setLocationTerminalDesc(kioskLocation.getDescription());

            kioskTerminal.setmLocationIdBranch(branch.getId());
            kioskTerminal.setLocationBranch(branch);
            kioskTerminal.setLocationBranchDesc(branch.getDescription());

            kioskTerminal.setmUserId(admin.getId());
            kioskTerminal.setUserAdmin(admin);
            kioskTerminal.setUserAdminDesc(admin.getName());

            result.add(kioskTerminal);
        }

        return result;
    }
}
