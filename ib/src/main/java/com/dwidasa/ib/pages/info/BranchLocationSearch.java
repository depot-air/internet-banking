package com.dwidasa.ib.pages.info;

import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.service.LocationService;
import com.dwidasa.ib.common.EvenOdd;

/**
 * Created by IntelliJ IDEA.
 * User: Frida
 * Date: 9/15/11
 * Time: 10:38 AM
 */
public class BranchLocationSearch {
	@Property
    private String strSearch;

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
    
    Object onSuccess() {
    	if (strSearch == null) {
            strSearch = "";
        }
        locations = locationService.getAll(Constants.LOCATION_TYPE_BRANCH, strSearch, com.dwidasa.ib.Constants.DEFAULT_TABLE_ROWS);
        return searchZone.getBody();    	
    }
    
    @Inject
    private Messages messages;
    
    public String getStrSearchResult() {
    	return String.format(messages.get("searchResult"), strSearch);
    }
    
    public boolean isHaveMap(Location location) {
    	if (location == null) return false;
    	if (location.getLatitude() != null && location.getLatitude().doubleValue() != 0
			&& location.getLongitude() != null && location.getLongitude().doubleValue() != 0) {
    		return true;
    	}
    	return false;
    }
    
    public String showMapLink(Location location) {
    	StringBuffer sb = new StringBuffer();
    	sb.append("<a href=");
    	sb.append("\"http://maps.google.com/maps?q=").append(location.getLatitude()).append("%2c").append(location.getLongitude()).append("&z=17\"");
    	sb.append("target=\"_blank\">");
    	sb.append(messages.get("googleMap"));
    	sb.append("</a>");		
    	return sb.toString();
    }
    
    public boolean isSearchMax() {
    	if (locations == null) return false;
    	if (locations.size() == com.dwidasa.ib.Constants.DEFAULT_TABLE_ROWS) return true;
    	return false;
    }
}
