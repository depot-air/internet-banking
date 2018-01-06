package com.dwidasa.ib.pages.popup;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/18/11
 * Time: 9:57 PM
 */
@Import(library = {"context:bprks/js/jquery-1.6.2.min.js",
                   "context:bprks/js/popup/CustomerTransfer.js"})
public class CustomerTransfer {
    public static final String SEARCHED_CUSTOMER = "com.dwidasa.ib.pages.popup.CustomerTransfer";

    @Property
    private String nameAccount;

    @Property
    private DataSource dataSource;

    @Property
    private CustomerRegister row;

    @Inject
    private CustomerRegisterService customerRegisterService;

    @Inject
    private SessionManager sessionManager;

    @ActivationRequestParameter
    private String transactionType;

    @ActivationRequestParameter
    private String billerCode;

    @Inject
    private Request request;

    @Property
    static private BeanModel<CustomerRegister> myModel;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private BeanModelSource beanModelSource;

    public void setupRender(){
        setupBeanModel();
    }
    private void setupBeanModel() {
        if (myModel == null) {
            myModel = beanModelSource.createDisplayModel(CustomerRegister.class, componentResources.getMessages());
            myModel.include("customerReference", "data3");
            myModel.get("customerReference").label("Nomor Rekening");
            myModel.get("data3").label("Nama Pemilik");
        }
    }
    void onSelectedFromNext() {
        request.getSession(false).setAttribute(SEARCHED_CUSTOMER, nameAccount);
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

            restrictions.add("data1 = ?");
            values.add(billerCode);
            if (nameAccount != null) {
                restrictions.add("customer_reference || data3 ilike ?");
                values.add("%" + nameAccount + "%");
            }
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

    public String toJson(CustomerRegister customerRegister) {
        return PojoJsonMapper.toJson(customerRegister);
    }

    void beginRender() {
        if (request.getSession(false).getAttribute(SEARCHED_CUSTOMER) == null) {
            request.getSession(false).setAttribute(SEARCHED_CUSTOMER, "");
        }
        nameAccount = (String) request.getSession(true).getAttribute(SEARCHED_CUSTOMER);
        dataSource = new DataSource(Constants.PAGE_SIZE);
    }
}
