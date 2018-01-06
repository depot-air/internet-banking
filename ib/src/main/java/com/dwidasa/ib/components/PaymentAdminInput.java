package com.dwidasa.ib.components;

import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 02/08/11
 * Time: 15:32
 */
public class PaymentAdminInput {
    @Inject
    private CustomerRegisterService customerRegisterService;
    @Inject
    private SessionManager sessionManager;
    @Persist
    @Property
    private DataSource dataSource;
    @Property
    private CustomerRegister row;
    @Property
    static private BeanModel<CustomerRegister> myModel;
    @Inject
    private BeanModelSource beanModelSource;
    @Inject
    private ComponentResources componentResources;
    @Parameter
    private String transactionType;
    private boolean delete;
    @Parameter
    private List<CustomerRegister> customerToDelete;
    @Parameter
    @Property
    private String token;
    @Parameter
    @Property
    private int tokenType;

    public List<CustomerRegister> getCustomersToDelete() {
        if (customerToDelete == null) {
            customerToDelete = new ArrayList<CustomerRegister>();
        }
        return customerToDelete;
    }

    // The Grid component will automatically call this for every row as it is rendered.
    public boolean isDelete() {
        return delete;
    }

    // The Grid component will automatically call this for every row on submit.
    public void setDelete(boolean delete) {
        if (delete) {
            getCustomersToDelete().add(row);
        }
    }

    public void setupRender() {
        setupBeanModel();
    }

    private void setupBeanModel() {
        if (myModel == null) {
            myModel = beanModelSource.createDisplayModel(CustomerRegister.class, componentResources.getMessages());
            myModel.include("customerReference", "data3");
            myModel.add("delete", null);
            myModel.get("customerReference").label("Nomor Rekening");
            myModel.get("data3").label("Nama Pemilik");
        }
    }

    private class DataSource implements GridDataSource {
        private List<String> restrictions;
        private List<Object> values;

        private List<CustomerRegister> rows;

        private int pageSize;
        private int firstRow;

        private DataSource(int pageSize) {
            this.pageSize = pageSize;

            restrictions = new ArrayList<String>();
            values = new ArrayList<Object>();

            restrictions.add("m_customer_id = ?");
            values.add(sessionManager.getLoggedCustomerView().getId());

            restrictions.add("transaction_type = ?");
            values.add(transactionType);
        }

        public int getAvailableRows() {
            return customerRegisterService.getRowCount(restrictions, values.toArray());
        }

        public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
            List<String> orders = new ArrayList<String>();
            rows = customerRegisterService.getCurrentPageRows(startIndex, pageSize, restrictions, orders,
                    values.toArray());
            firstRow = startIndex;
        }

        public Object getRowValue(int index) {
            int pageNo = firstRow / pageSize + (firstRow % pageSize == 0 ? 0 : 1) + 1;
            return rows.get(index - (pageNo - 1) * pageSize);
        }

        public Class getRowType() {
            return CustomerRegister.class;
        }
    }

    public void beginRender() {
        dataSource = new DataSource(com.dwidasa.ib.Constants.PAGE_SIZE);
    }
}
