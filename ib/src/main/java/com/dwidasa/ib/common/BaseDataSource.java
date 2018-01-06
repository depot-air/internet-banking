package com.dwidasa.ib.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import com.dwidasa.engine.service.GenericService;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.ib.transform.Transformer;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseDataSource<T> implements GridDataSource {
    private List<String> restrictions;
    private List values;

    private List<T> rows;
    private Class<T> clazz;
    private GenericService service;
    private Transformer transformer;

    private int pageSize;
    private int firstRow;
    
    private List<String> orders;

    public BaseDataSource(Class<T> clazz, int pageSize, List<String> restrictions, List values) {
        this(clazz, pageSize, restrictions, values, null);
    }
    
    public BaseDataSource(Class<T> clazz, int pageSize, List<String> restrictions, List values,
                          Transformer transformer) {
    	this(clazz, pageSize, restrictions, values, transformer, null);
    }
    
    public BaseDataSource(Class<T> clazz, int pageSize, List<String> restrictions, List values,
            Transformer transformer, List<String> orders) {
    	this.clazz = clazz;
        this.pageSize = pageSize;
        this.orders = orders;

        String srvName = clazz.getSimpleName();
        if (transformer != null) {
            srvName = StringUtils.removeEnd(srvName, "View");
        }
        srvName += "Service";
        srvName = StringUtils.uncapitalize(srvName);
        this.service = (GenericService) ServiceLocator.getService(srvName);

        this.transformer = transformer;
        this.restrictions = restrictions;
        this.values = values;

        if (this.restrictions == null) {
            this.restrictions = new ArrayList<String>();
        }

        if (this.values == null) {
            this.values = new ArrayList();
        }

    }

    public int getAvailableRows() {
        return service.getRowCount(restrictions, values.toArray());
    }

    @SuppressWarnings("unchecked")
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        if (orders == null) {
        	orders = new ArrayList<String>();
        }
        if (transformer != null) {
            rows = transformer.transform(service.getCurrentPageRows(startIndex, pageSize, restrictions, orders,
                values.toArray()));
        }
        else {
            rows = service.getCurrentPageRows(startIndex, pageSize, restrictions, orders, values.toArray());
        }

        firstRow = startIndex;
    }

    public Object getRowValue(int index) {
        int pageNo = firstRow / pageSize + (firstRow % pageSize == 0 ? 0 : 1) + 1;
        return rows.get(index - (pageNo - 1) * pageSize);
    }

    public Class getRowType() {
        return clazz;
    }
}
