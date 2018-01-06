package com.dwidasa.ib.pages.account;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.BusinessException;
import org.apache.log4j.Logger;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.custom.AttachmentStreamResponse;
import com.dwidasa.ib.pages.Index;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA. User: rk Date: 10/1/11 Time: 8:49 PM
 */
public class AccountStatementResult {
	private static final Date yyyyDDMM = null;
	private static Logger logger = Logger
			.getLogger(AccountStatementResult.class);
	@Persist
	private AccountView accountView;

	@Persist
	private AccountView view;

	@Property(write = false)
	@Persist
	private String referer;

	@Persist
	private Date startDate;

	@Persist
	private Date endDate;

	@Persist
	private List<AccountStatementView> asvList;

	@Property
	private AccountStatementView asv;

	@Inject
	private AccountService accountService;

	@Inject
	private ThreadLocale threadLocale;

	@Property
	private NumberFormat formatter = NumberFormat
			.getNumberInstance(threadLocale.getLocale());

	@Property
	private DateFormat shortDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());

	@Property
	private BigDecimal beginningBalance;

	@Property
	private BigDecimal credit;

	@Property
	private BigDecimal debet;

	@Property
	private BigDecimal endingBalance;

	@Property
	private EvenOdd evenOdd;


	@InjectPage
	private AccountSummary accountSummary;

	@InjectPage
	private AccountStatementResult accountStatementResult;

	@Inject
	private SessionManager sessionManager;

    @Property
    private String noErrorFlag;

    @InjectComponent
    private Form form;

	@Property
	private final ValueEncoder<AccountStatementView> encoder = new ValueEncoder<AccountStatementView>() {
		public String toClient(AccountStatementView value) {
			return String.valueOf(value.getAccountNumber());
		}

		public AccountStatementView toValue(String clientValue) {
			return new AccountStatementView();
		}
	};

	public List<AccountStatementView> getAsvList() {
		return asvList;
	}

	public void setAsvList(List<AccountStatementView> asvList) {
		this.asvList = asvList;
	}

	public AccountView getAccountView() {
		return accountView;
	}

	public void setAccountView(AccountView accountView) {
		this.accountView = accountView;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	Object onActivate() {
		if (accountView == null) {
			return Index.class;
		}

		return null;
	}

	void setupRender() {
        try {

            evenOdd = new EvenOdd();
		    evenOdd.setEven(false);
		    accountView.setCurrencyCode(Constants.CURRENCY_CODE_IDR);

		    if (startDate == null && endDate == null) {
			    // -- last n transaction
			    // asvList = accountService.getLastNTransaction(accountView, 10);
			    System.out.println("start date and end date null");
		    } else {
			    System.out.println("get mutasi");
			    // -- as specified by start and end date

                asvList = accountService.getAccountStatement(accountView,
					startDate, endDate);

		    }
		    if (asvList != null && asvList.size() > 0) {
			    AccountStatementView firstAsv = asvList.get(0);
			    AccountStatementView lastAsv = asvList.get(asvList.size() - 1);

                endingBalance = lastAsv.getRunningBalance();
                if ("D".equals(firstAsv.getTransactionIndicator())){
                    beginningBalance = firstAsv.getRunningBalance().add(firstAsv.getAmount().abs());
                }
                else {
                    beginningBalance = firstAsv.getRunningBalance().subtract(firstAsv.getAmount().abs());
                }

			    debet = BigDecimal.ZERO;
			    credit = BigDecimal.ZERO;
			    for (int i = 0; i < asvList.size(); i++) {
				    AccountStatementView asv = asvList.get(i);
				    if ("D".equals(asv.getTransactionIndicator())) {
					    debet = debet.add(asv.getAmount().abs());
                        asv.setFormattedAmount(formatter.format(asv.getAmount().abs()));
				    } else {
					    credit = credit.add(asv.getAmount());
                        asv.setFormattedAmount(formatter.format(asv.getAmount()));
				    }
			    }

		    } else {
			    System.out.println("avList is null");
		    }

            noErrorFlag = "Y";

        }
        catch (BusinessException be){
            be.printStackTrace();
            form.clearErrors();
            form.recordError(((BusinessException) be).getFullMessage());

            noErrorFlag = null;
        }
        catch (Exception e){
            e.printStackTrace();
            form.clearErrors();
            form.recordError(e.getMessage());

            noErrorFlag = null;
        }



	}

    public String getTransactionIndicator(AccountStatementView asv){
        if ("D".equals(asv.getTransactionIndicator())){
            return " ";
        }
        else {
            return "Cr";
        }
    }


	@InjectPage
	private AccountBalance accountBalance;
	
	 @Inject private ComponentResources resources;
	 Object onDownloadFile(){
	    Link link = resources.createEventLink("createCsv");
	    return link;
	 }
	  
    public StreamResponse onCreateCsv(){
		try {
			System.out.println("start downloading1");
	    	String destFileName = "result.csv";
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFileName), "UTF-8"));
			
			// Construct the BufferedWriter object		
			
//				DateFormat dfIso = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//				DateFormat dfYmd = new SimpleDateFormat("yyyy-MM-dd");
			StringBuilder sb = new StringBuilder();
			sb.append("No, ").append("Tanggal, ").append("Keterangan, ").append("Nomer Referensi, ").append("Mutasi, ").append("Saldo\n");
			System.out.println("string builder1 : \n " + sb);    
		    Date date;
			int i = 0;
			int j=1;
			String temp, temp2;
			for ( i = 0; i < asvList.size(); i++) {
				AccountStatementView asv = asvList.get(i);
				sb.append(""+j+", ");
				date = asv.getValueDate();
				sb.append(""+shortDate.format(date)+", ");
				sb.append(""+asv.getTransactionName()+", ");
				sb.append(""+asv.getReferenceNumber().toString()+", ");
				temp2 = asv.getFormattedAmount().replace(".", "").replace(",","");
				sb.append(""+temp2+", ");
				temp = formatter.format(asv.getRunningBalance()).replace(".", "").replace(",","");
				sb.append(""+temp+"\n");
				j++;
			}
			bw.write(sb.toString());
			bw.newLine();
			bw.flush();
			bw.close();
			
			File cFile = new File("result.csv");
		
			InputStream fis = new FileInputStream(cFile);
			
			//AttachmentStreamResponse fDownload = 
			
			return new AttachmentStreamResponse(fis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    public boolean isMerchant() {    
    	boolean isMerch = false;
    	String accountNumber = sessionManager.getLoggedCustomerView().getAccountNumber().substring(0, 4);
    	if(accountNumber.startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC1) || accountNumber.startsWith(com.dwidasa.ib.Constants.MERCHANT_EDC2) || accountNumber.startsWith(com.dwidasa.ib.Constants.HIPERWALLET)){
    		isMerch = true;
    	}
    	return isMerch;
    }
}
