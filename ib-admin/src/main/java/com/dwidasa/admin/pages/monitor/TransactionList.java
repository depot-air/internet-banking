package com.dwidasa.admin.pages.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.util.EngineUtils;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.Response;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.common.BaseDataSource;
import com.dwidasa.admin.transform.Transformer;
import com.dwidasa.admin.transform.TransformerFactory;
import com.dwidasa.admin.view.TransactionView;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.util.DateUtils;

@Import(library = "context:bprks/js/monitor/TransactionList.js")
@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.DAY_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class TransactionList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private TransactionView row;

    @Property
    private Integer availableRows;
	
	@Property
	@Persist
	private String transactionType;

    @Property
	@Persist
	private String transactionStatus;
	
	@Property
	@Persist
	private Date startDate;
	
	@Property
	@Persist
	private Date endDate;
	
	@Property
	private SelectModel transactionTypeModel;

    @Property
    private SelectModel transactionStatusModel;
	
	@Inject
    private ThreadLocale threadLocale;
	
	@Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, threadLocale.getLocale());
	
	@Inject
	private TransactionTypeService transactionTypeService;
	
	@Inject
	private TransactionService transactionService;
	
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM);

    @Property
    private NumberFormat numberFormat = NumberFormat.getIntegerInstance();
	
	void setupRender() {
		List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        pageSize = Constants.PAGE_SIZE;
		
		//fill transactionTypeModel
		List<OptionModel> result = new ArrayList<OptionModel>();
		List<TransactionType> transactionTypeList = transactionTypeService.getAllFinancial();
		for (TransactionType tt : transactionTypeList) {
			OptionModel om = new OptionModelImpl(tt.getDescription(), tt.getTransactionType());
			result.add(om);
		}
		transactionTypeModel = new SelectModelImpl(null, result);

        //fill transactionStatusModel
        result = new ArrayList<OptionModel>();
        OptionModel om = new OptionModelImpl(com.dwidasa.engine.Constants.SUCCEED_STATUS,com.dwidasa.engine.Constants.SUCCEED_STATUS);
        result.add(om);
        om = new OptionModelImpl(com.dwidasa.engine.Constants.FAILED_STATUS,com.dwidasa.engine.Constants.FAILED_STATUS);
        result.add(om);
        om = new OptionModelImpl(com.dwidasa.engine.Constants.PENDING_STATUS,com.dwidasa.engine.Constants.PENDING_STATUS);
        result.add(om);
		transactionStatusModel = new SelectModelImpl(null, result);

		Transformer t = TransformerFactory.getTransformer(Transaction.class.getSimpleName());
		if (startDate != null && endDate != null) {
			if (transactionType != null && transactionType.trim().length() > 0) {
				restrictions.add("t.transaction_type=?");
				values.add(transactionType);
			}

            if (transactionStatus != null && transactionStatus.trim().length() > 0) {
                restrictions.add("t.status=?");
                values.add(transactionStatus);
            }
			restrictions.add("t.value_date >= ?");
			values.add(DateUtils.generateStart(startDate));
			restrictions.add("t.value_date < ?");
			values.add(DateUtils.generateEnd(endDate));

            if(transactionType == null || transactionType.trim().length() == 0) {
                restrictions.add("t.transaction_type in (" + getListFinancialTransactionType() + ")");

            }

            List<String> orders = new ArrayList<String>();
            orders.add("t.value_date desc");

		    dataSource = new BaseDataSource(TransactionView.class, Constants.PAGE_SIZE, restrictions, values, t, orders);
		} else {
			restrictions.add("t.id=-1");
			dataSource = new BaseDataSource(TransactionView.class, Constants.PAGE_SIZE, restrictions, values, t);
		}
		
		//set default value for date
		if (startDate == null) {
			//Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -30);
			//startDate = cal.getTime();
            startDate = new Date();
		}
		if (endDate == null) {
			endDate = new Date();
		}

        availableRows =  dataSource.getAvailableRows();
	}
	
	public String getShortPattern() {
        return Constants.SHORT_FORMAT;
    }
	
	@DiscardAfter
    void onSelectedFromReset() {
        startDate = null;
        endDate = null;
        transactionType = null;
    }
	
    void pageReset() {
    	startDate = null;
    	endDate = null;
    	transactionType = null;
        transactionStatus = null;
    }
    
    public boolean isNotEmpty() {
    	if (dataSource == null) return false;
    	if (dataSource.getAvailableRows() == 0) return false;
    	return true;
    }
    
    Object onExport() {
    	if (startDate == null || endDate == null) return null;
    	return new StreamResponse() { 
    		public String getContentType() {
    			return "text/csv";
    		}
    		public InputStream getStream() throws IOException {    
    			return transactionService.getCsvStream(startDate, endDate, transactionType);
    		}
    		public void prepareResponse(final Response response) {
    			response.setHeader("Content-disposition", "attachment;filename=export.csv");
    		}
    	};
    }

    private String getListFinancialTransactionType(){

        StringBuffer str = new StringBuffer("");
        List<TransactionType> transactionTypeList = transactionTypeService.getAllFinancial();
        for (int i = 0; i < transactionTypeList.size(); i++){
            TransactionType tt = transactionTypeList.get(i);
            str.append("'");
            str.append(tt.getTransactionType());
            str.append("'");

            if (i != transactionTypeList.size()-1) {
                str.append(",");
            }
        }

        return str.toString();
    }
}
