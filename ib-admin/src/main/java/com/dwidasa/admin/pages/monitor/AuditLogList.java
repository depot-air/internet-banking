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
import com.dwidasa.engine.model.ActivityCustomer;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.TransactionType;
import com.dwidasa.engine.service.TransactionTypeService;
import com.dwidasa.engine.util.DateUtils;

@Import(library = "context:bprks/js/monitor/AuditLogList_123.js")
@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, com.dwidasa.admin.Constants.RoleName.DAY_ADMIN, com.dwidasa.admin.Constants.RoleName.SUPERUSER})
public class AuditLogList {
	@Property
    private GridDataSource dataSource;

    @Property
    private int pageSize;

    @Property
    private ActivityCustomer row;
	
	@Property
	@Persist
	private String transactionType;
	
	@Property
	@Persist
	private Date startDate;
	
	@Property
	@Persist
	private Date endDate;
	
	@Property
	private SelectModel transactionTypeModel;
	
	@Inject
    private ThreadLocale threadLocale;
	
	@Property
    private DateFormat shortDate = new SimpleDateFormat(Constants.SHORT_FORMAT, threadLocale.getLocale());

	@Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
	
	@Inject
	private TransactionTypeService transactionTypeService;
	
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    @Persist
    private String strUsername;
    
	void setupRender() {
		List<String> restrictions = new ArrayList<String>();
        List values = new ArrayList();
        pageSize = Constants.PAGE_SIZE;
		
		//fill transactionTypeModel
		List<OptionModel> result = new ArrayList<OptionModel>();
		List<TransactionType> transactionTypeList = transactionTypeService.getAllInTransaction();
		for (TransactionType tt : transactionTypeList) {
			OptionModel om = new OptionModelImpl(tt.getDescription(), tt.getTransactionType());
			result.add(om);
		}
		transactionTypeModel = new SelectModelImpl(null, result);
		
		Transformer t = TransformerFactory.getTransformer(ActivityCustomer.class.getSimpleName());
		if (startDate != null && endDate != null) {
			if (transactionType != null && transactionType.trim().length() > 0) {
				restrictions.add("tac.activity_type=?");
				values.add(transactionType);
			}
			if (strUsername != null && !strUsername.trim().equals("")) {
				restrictions.add("((upper(mc.customer_name) like '%' || ? || '%') or (upper(mc.customer_username) like '%' || ? || '%'))");
		        values.add(strUsername.toUpperCase());
		        values.add(strUsername.toUpperCase());
	    	}
	    	
			restrictions.add("tac.created >= ?");
			values.add(DateUtils.generateStart(startDate));
			restrictions.add("tac.created < ?");
			values.add(DateUtils.generateEnd(endDate));
		    dataSource = new BaseDataSource(ActivityCustomer.class, Constants.PAGE_SIZE, restrictions, values, t);
		} else {
			restrictions.add("tac.id=-1");
			dataSource = new BaseDataSource(ActivityCustomer.class, Constants.PAGE_SIZE, restrictions, values, t);
		}
		
		//set default value for date
		if (startDate == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -30);
			startDate = cal.getTime(); 
		}
		if (endDate == null) {
			endDate = new Date();
		}		
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
    			//return transactionService.getCsvStream(startDate, endDate, transactionType);
    			return null;
    		}
    		public void prepareResponse(final Response response) {
    			response.setHeader("Content-disposition", "attachment;filename=export.csv");
    		}
    	};
    }
}
