package com.dwidasa.ib.mixins;

import java.util.List;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.components.Grid;

public class GridSortingDisabled {
	@InjectContainer
	private Grid grid;

	void beginRender() {
		BeanModel<?> model = grid.getDataModel();
		List<String> propertyNames = model.getPropertyNames();
		for (String propName : propertyNames) {
			PropertyModel propModel = model.get(propName);
			propModel.sortable(false);
		}
	}
}
