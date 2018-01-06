package com.dwidasa.ib.pages.popup;

import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Biller;
import com.dwidasa.engine.service.BillerService;
import com.dwidasa.ib.Constants;
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
 * User: emil
 * Date: 19/07/11
 * Time: 15:24
 */
@Import(library = {"context:layout/javascript/jquery-1.6.2.min.js",
        "context:layout/javascript/popup/BankTransfer.js"})
public class BankTransfer {
    public static final String SEARCHED_BANK = "com.dwidasa.ib.pages.popup.banktransfer";
    @Property
    private String receiverBank;

    @Property
    private DataSource dataSource;

    @Property
    private Biller row;

    @Inject
    private BillerService billerService;

    @ActivationRequestParameter
    private Long transactionTypeId;

    @Inject
    private Request request;

    @Property
    static private BeanModel<Biller> myModel;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private BeanModelSource beanModelSource;

    private void setupBeanModel() {
        if (myModel == null) {
            myModel = beanModelSource.createDisplayModel(Biller.class, componentResources.getMessages());
            myModel.include("billerCode","billerName");
            myModel.get("billerCode").label("Kode Bank");
            myModel.get("billerName").label("Nama Bank");
        }
    }

    public void setupRender() {
        setupBeanModel();
    }

    public void onSelectedFromNext() {
        request.getSession(false).setAttribute(SEARCHED_BANK, receiverBank);
    }

    private class DataSource implements GridDataSource {
        private List<String> restrictions;
        private List<Object> values;

        private List<Biller> rows;

        private int pageSize;
        private int firstRow;

        private DataSource(int pageSize) {
            this.pageSize = pageSize;

            restrictions = new ArrayList<String>();
            values = new ArrayList<Object>();

            restrictions.add("m_transaction_type_id = ?");
            values.add(transactionTypeId);

            if (receiverBank != null) {
                restrictions.add("biller_name || biller_code ilike ?");
                values.add("%" + receiverBank + "%");

            }
        }

        public int getAvailableRows() {
            return billerService.getRowCount(restrictions, values.toArray());
        }

        public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
            List<String> orders = new ArrayList<String>();

            rows = billerService.getCurrentPageRows(startIndex, pageSize, restrictions, orders,
                    values.toArray());
            firstRow = startIndex;
        }

        public Object getRowValue(int index) {
            int pageNo = firstRow / pageSize + (firstRow % pageSize == 0 ? 0 : 1) + 1;
            return rows.get(index - (pageNo - 1) * pageSize);
        }

        public Class getRowType() {
            return Biller.class;
        }
    }

    public String toJson(Biller biller) {
        return PojoJsonMapper.toJson(biller);
    }

    void beginRender() {
        if (request.getSession(false).getAttribute(SEARCHED_BANK) == null) {
            request.getSession(false).setAttribute(SEARCHED_BANK, "");
        }
        receiverBank = (String) request.getSession(true).getAttribute(SEARCHED_BANK);
        dataSource = new DataSource(Constants.PAGE_SIZE);
    }
}
