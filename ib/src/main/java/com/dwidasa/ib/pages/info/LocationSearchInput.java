package com.dwidasa.ib.pages.info;

import com.dwidasa.engine.model.Location;
import com.dwidasa.engine.service.LocationService;
import com.dwidasa.engine.service.LocationTypeService;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 04/08/11
 * Time: 10:47
 */
public class LocationSearchInput {
    @Property
    private String type;
    @Property
    private String searchValue;
    @Property
    private SelectModel typeModel;
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    @Inject
    private LocationTypeService locationTypeService;
    @Inject
    private LocationService locationService;
    @InjectComponent
    private Zone searchZone;
    @Property
    private List<Location> locations;
    @Property
    private int pageSize = 10;

    public void setupRender() {
        buildTypeModel();
    }

    private void buildTypeModel() {
        typeModel = genericSelectModelFactory.create(locationTypeService.getAll());
        type = typeModel.getOptions().get(0).getValue().toString();
    }

    Object onSelectedFromSearch() {
        if (searchValue == null) {
            searchValue = "";
        }
        locations = locationService.getAll(Long.parseLong(type), searchValue, pageSize);
        return searchZone.getBody();
    }
}
