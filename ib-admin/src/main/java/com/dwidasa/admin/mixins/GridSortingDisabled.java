package com.dwidasa.admin.mixins;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.components.Grid;

import java.util.List;

/**
 * Taken from http://tapestry.1045711.n5.nabble.com/Grid-disable-sorting-mixin-td3401410.html
 * with slight modification.
 *
 * Mixin to disable column sorting in a grid component.
 */
public class GridSortingDisabled {
    @InjectContainer
    private Grid grid;

    private void beginRender() {
        BeanModel model = grid.getDataModel();
        List<String> propertyNames = model.getPropertyNames();
        for (String propName : propertyNames) {
            PropertyModel propModel = model.get(propName);
            propModel.sortable(false);
        }
    }
}