package com.dwidasa.ib.pages.info;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.service.LocationService;
import com.dwidasa.ib.common.EvenOdd;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Frida
 * Date: 9/16/11
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class MerchantLocationSearch {

    @Property
    private String activation_code;

    @Inject
    private LocationService locationService;

    @InjectComponent
    private Zone searchZone;

    @Property
    private List<Location> locations;

    @Property
    private Location location;

     @Property
	private EvenOdd evenOdd;


    public void pageAttached() {
        evenOdd = new EvenOdd();
    }

    Object onSelectedFromSearch() {
        if (activation_code == null) {
            activation_code = "";
        }
        locations = locationService.getAll(Constants.LOCATION_TYPE_MERCHANT, activation_code, com.dwidasa.ib.Constants.DEFAULT_TABLE_ROWS);
        return searchZone.getBody();
    }

}
