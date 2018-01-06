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
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.SessionManager;

public class AtmLocationSearch {

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

    @Inject
    private SessionManager sessionManager;

    public void pageAttached() {
        evenOdd = new EvenOdd();
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    Object onSuccess() {
    	if (strSearch == null) {
            strSearch = "";
        }
        locations = locationService.getAll(Constants.LOCATION_TYPE_ATM_ADM, strSearch, com.dwidasa.ib.Constants.DEFAULT_TABLE_ROWS);
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
