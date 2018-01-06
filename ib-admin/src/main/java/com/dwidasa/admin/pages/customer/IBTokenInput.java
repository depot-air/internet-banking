package com.dwidasa.admin.pages.customer;

import java.math.BigInteger;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.AbstractSelectModel;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.annotations.Restricted;
import com.dwidasa.admin.services.SessionManager;
import com.dwidasa.admin.view.IbTokenEncoder;
import com.dwidasa.engine.dao.IbTokenDao;
import com.dwidasa.engine.dao.MerchantDao;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.model.Customer;
import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.CustomerDevice;
import com.dwidasa.engine.model.IbMerchant;
import com.dwidasa.engine.model.IbToken;
import com.dwidasa.engine.model.Merchant;
import com.dwidasa.engine.model.User;
import com.dwidasa.engine.service.CustomerDeviceService;
import com.dwidasa.engine.service.CustomerService;
import com.dwidasa.engine.service.IbMerchantService;
import com.dwidasa.engine.service.IbTokenService;
import com.dwidasa.engine.service.TokenAgentService;
import com.dwidasa.engine.util.ReferenceGenerator;

@Restricted(groups={com.dwidasa.admin.Constants.RoleName.ADMIN, Constants.RoleName.DAY_ADMIN, Constants.RoleName.SUPERUSER})
public class IBTokenInput {
	private static Logger logger = Logger.getLogger( IBTokenInput.class );
	private Customer customer;
	private CustomerAccount customerAccount;
	private CustomerDevice ibsCustomerDevice;
	
	//for insert to m_merchant and m_ib_merchant
	private Customer customer2;
	private CustomerDevice ibsCustomerDevice2;
	private IbMerchant ibMerchant;
	private Merchant merchant;
	private MerchantDao merchantDao;
	 
    @Property
    private String userIdValue;

    @Property
    private String accountNumberValue;

    @Property
    private String merchantIdValue;

    @Property
    @Persist
    private IbToken ibToken;

    @Property
    @Persist
    private IbToken ibToken2;
    
    @Inject
    private SessionManager sessionManager;
    
    @Inject
    private IbTokenService ibTokenService;

    @Inject
    private CustomerService customerService;
    
    @Inject
    private CustomerDeviceService customerDeviceService;
    
	@Property
    @Persist
    private List<IbToken> ibTokens;

	@Property
	@Persist
	private List<IbToken> checkToken;
	
	@Property
    @Persist
	private IbTokenEncoder ibTokenEncoder;
    
    @Property
    private SelectModel ibTokenModel;

    @InjectPage
    private IBTokenReceipt ibTokenReceipt;

    @InjectComponent
    private Form form;

    @Inject
    private Messages message;

    @Inject
    private TokenAgentService tokenAgentService;

    @Inject
    private IbMerchantService ibMerchantService;
    
    @Property
    private boolean saveBoxValue;
    
    @Inject
    private IbTokenDao ibTokenDao;
    
    @Inject
    private TransactionDao transactionDao;
    
    void onPrepare() {

    }
    
    void setupRender() {
    	if(ibTokens == null){
 	   		ibTokens = new ArrayList<IbToken>();
		}    
		ibTokenModel = getModel();
		if(ibTokenEncoder == null){
			ibTokenEncoder = new IbTokenEncoder(ibTokenService);
		} 
    }

    public Object onSuccess() {
    	//ibTokenReceipt.setCustomer(customer);
    	//ibTokenReceipt.setCustomerAccount(customerAccount);
    	ibTokenReceipt.setSerialNumber(merchantIdValue);
        return ibTokenReceipt;
    }

    void onValidateFromForm() {	
    	
//    	if(userIdValue == null){
//    		form.recordError("User ID Harus Diisi");
//    	}
//    	if(accountNumberValue == null){
//    		form.recordError("Nomor Rekening Harus Diisi");
//    	}
    	if(merchantIdValue == null){
    		form.recordError("Serial Number Harus Diisi");
    	}
    	
//    	if(userIdValue != null){
//    		System.out.println("getting customerName");
//    		//cari user di m_customer yg id nya = userId
//    		customer = customerService.getByUsername(userIdValue.toUpperCase());
//    		if(customer == null){
//    			form.recordError(message.get("customer-null-message"));
//    		}else{
//    			
//    			System.out.println("getting Customer Account");
//        		//cari user di m_customer yg id nya = userId
//        		customerAccount = customerService.getCustomerAccount(customer.getId(), accountNumberValue);
//            	if (customerAccount == null) {
//            		form.recordError(message.get("customerAccount-null-message"));
//            	} 
//    			
//    		}
//    		
//    	}
    	
    	if (!form.getHasErrors()) {
    		User user = sessionManager.getLoggedUser();
    		
//    		IbToken ibTok = ibTokenDao.getByCustomerId(customer.getId());
//    		
//    		if(ibTok != null){
//    		
//    			ibTok.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_IMPORTED);
//    			ibTok.setSerialNumber(merchantIdValue);
//    			ibTok.setCustomerId(customer.getId());
//    			ibTok.setUpdatedby(user.getId());
//    			System.out.println("Update save ibt");
//    			ibTokenDao.save(ibTok);
//    		
//    		}else{
    			
    			IbToken ibt = new IbToken();
        		ibt.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_IMPORTED);
        		ibt.setSerialNumber(merchantIdValue);
    			ibt.setCustomerId(null);
    			ibt.setCreatedby(user.getId());
    			ibt.setCreated(new Date());
    			ibt.setUpdatedby(user.getId());
    			ibt.setUpdated(new Date());
    			System.out.println("save ibt");
        		ibTokenDao.save(ibt);
    		//}
    		
    	}
//    	List<CustomerDevice> customerDevices = customerDeviceService.getNotActiveByCustomerId(customer.getId());
//    	if(accountNumberValue == null){
//    		form.recordError(message.get("account-number-empty"));
//    	}
//    	else if (!accountNumberValue.matches("^[0-9]+$")) {
//    		form.recordError(message.get("account-number-format"));
//    	} else if (accountNumberValue.length() != 10) {
//    		form.recordError(message.get("account-number-length"));
//    	}  
//    	if(merchantIdValue == null){
//    		form.recordError(message.get("merchant-id-empty"));
//    	}
//    	else if (!merchantIdValue.matches("^[0-9a-zA-Z]+$")) {
//    		form.recordError(message.get("merchant-id-format"));
//    	} else if (merchantIdValue.length() != 8) {
//    		form.recordError(message.get("merchant-id-length"));
//    	}
//    	else {    	    			    		
//	    	if (customer == null) {
//	    		form.recordError(message.get("customer-null-message"));
//	    	} else {
//	    		System.out.println("getting customerAccount");
//	    		customerAccount = customerService.getCustomerAccount(customer.getId(), accountNumberValue);
//	        	if (customerAccount == null) {
//	        		form.recordError(message.get("customerAccount-null-message"));
//	        	} 
//	      
//	        	else {
//	        		checkToken = ibTokenService.getSelectedTokensByCustomerId(customer.getId());
//	            	if (checkToken != null && checkToken.size() > 0) {
//	            		ibToken2 = checkToken.get(0);
//	            		System.out.println("customer sudah aktif");
//	            		form.recordError(message.get("customer-sudah-aktif"));
//	            	}
//		    		//cari di m_customer_device yg m_customer_id nya sesuai dan statusnya 0
//	        		System.out.println("customer id : " + customer.getId());
//	        		
//	        		
//	        		
////	        		if (customerDevices != null && customerDevices.size() > 0) {
////	        			ibsCustomerDevice = getIBSCustomerDevice(customerDevices);
////		        		if (ibsCustomerDevice == null ) {
////		        			ibsCustomerDevice = new CustomerDevice();
////		        		}
////		            	if (customerDevices == null || customerDevices.size() == 0) {
////		            		System.out.println("customer device is null");
////		            		form.recordError(message.get("customerDevice-null-message"));
//////		            	} else if (ibsCustomerDevice == null) {
//////		            		form.recordError(message.get("customerDeviceIB-null-message"));
////		            	}	
////	        		}
//	        	}        	
//	    	}
//    	}
//    	if (!form.getHasErrors()) {
//    		for(int i=0; i<ibTokens.size(); i++) {
//        		IbToken ibt = ibTokens.get(i);
//        		if (ibt.getSerialNumber().equals(ibToken.getSerialNumber())) {
//        			
//        			User user = sessionManager.getLoggedUser();
//        			
//        			if (customerDevices != null && customerDevices.size() > 0) {
//            			try {
//        	            	customer = customerService.getByUsername(userIdValue.toUpperCase());
//          
//        	            	tokenAgentService.addCustomerRetail(customer.getCustomerUsername(), "1", customer.getCustomerName() );
//        	            	String result = tokenAgentService.addCustomerRetailRegistration(customer.getCustomerUsername(), customer.getCustomerName(), customer.getCifNumber(), ibToken.getSerialNumber());
//        	            	if (!result.equals("OK")) {
//        	            		form.recordError(result);
//        	            		break;
//        	            	}else{
//        	            		System.out.println("updating  m_customer_device");
//        	            		ibsCustomerDevice = customerDevices.get(0);
//        	            		ibsCustomerDevice.setTerminalId(merchantIdValue.toString());
//                    			ibsCustomerDevice.setDeviceId(ibt.getSerialNumber());
////                    			ibsCustomerDevice.setStatus(com.dwidasa.engine.Constants.STATUS.ONE);
//                    			customerDeviceService.save(ibsCustomerDevice);
//                    			//update m_customer
//                    			System.out.println("updating customer status at m_customer");
//                    			customer.setStatus(com.dwidasa.engine.Constants.STATUS.ONE);
////                    			customer.setFirstLogin(com.dwidasa.engine.Constants.STATUS.YES);
//                    			customerService.save(customer);
//        	            		System.out.println("updating merchant");
//        	            		IbMerchant ibMerchant = new IbMerchant();
//            	            	ibMerchant.setCustomerId(customer.getId());
//                				ibMerchant.setTerminalId(merchantIdValue.toString());
//                				ibMerchant.setSerialNumber(ibToken.getSerialNumber());
//                				ibMerchant.setSoftToken(saveBoxValue);
//                				ibMerchant.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_LINKED);
//                				ibMerchant.setCreatedby(user.getId());
//                				ibMerchant.setUpdatedby(user.getId());
//                				ibMerchant.setCreated(new Date());
//                				ibMerchant.setUpdated(new Date());
//        	            		ibMerchantService.save(ibMerchant);
//                				System.out.println("updating merchant end, start updating m_ib_token");
//                				ibt.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_LINKED);
//                    			ibt.setCustomerId(customer.getId());
//                    			ibt.setUpdatedby(user.getId());
//                    			System.out.println("save ibt");
//                        		ibTokenService.save(ibt);
//        	            	}
//    					} catch (Exception e) {
//    						System.out.println("error update : " + e );
//    					}
//        			}
//        		} else {
//        			System.out.println("tidak sama");
//        			ibt.setCustomerId(null);
//        			ibt.setStatus(com.dwidasa.engine.Constants.HARD_TOKEN.STATUS_IMPORTED);
//        			System.out.println("save ibt");
//            		ibTokenService.save(ibt);
//        		}
////        		System.out.println("save ibt");
////        		ibTokenService.save(ibt);
//        	}
//    	}
    }
    
    public static Connection getPostgreIgateConnection() throws ClassNotFoundException, SQLException {
		
		String driver = "org.postgresql.Driver";
		//String url = "jdbc:postgresql:ib2_20121205";
		String url = "jdbc:postgresql://192.168.77.30:5432/igate";
//		String url = "jdbc:postgresql:ib2";
		String username = "postgres";
		String password = "kungfup4nd4";
//		Class.forName(driver);
//		Connection conn = DriverManager.getConnection(url, username, password);
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.out.println("Oops! Can't find class oracle.jdbc.driver.OracleDriver");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("error : " + e);
			e.printStackTrace();
		} 
		return conn;
	}
    public static Connection getPostgreIbConnection() throws ClassNotFoundException, SQLException {
		
		String driver = "org.postgresql.Driver";
		//String url = "jdbc:postgresql:ib2_20121205";
//		String url = "jdbc:postgresql://192.168.77.32:5432/ib2_migration";
		String url = "jdbc:postgresql:ib2";
//		String url = "jdbc:postgresql:ib2";
		String username = "postgres";
		String password = "postgres";
//		Class.forName(driver);
//		Connection conn = DriverManager.getConnection(url, username, password);
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			System.out.println("Oops! Can't find class oracle.jdbc.driver.OracleDriver");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("error : " + e);
			e.printStackTrace();
		} 
		return conn;
	}
   
    public static Connection getSqlServerConnection() throws ClassNotFoundException, SQLException{
    	String username = "velis";
		String password = "velis";
		String url = "jdbc:sqlserver://172.16.100.145;database=va_5";
    	Connection conn = null;
    	try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(url, username, password);
		} catch(Exception e){
			e.printStackTrace();
			System.out.println("error : " + e);
		}    
    	return conn;
    }
    
    private static void insertMerchant(Customer customer, CustomerDevice customerDevice){
    	Connection conn = null;
		Statement stmt = null;
		Statement stmtInnerCount = null;
		Statement stmtInner = null;
		try {
			System.out.println("trying to insert m_merchant to igate");
			conn = getPostgreIgateConnection();
			stmt = conn.createStatement();
			stmtInnerCount = conn.createStatement();
			stmtInner = conn.createStatement();	
			String qInsert = "insert into m_merchant (m_customer_id, terminal_id, status, created, created_by, updated, updated_by) values ()";
        	System.out.println("qInsertMerchant= " + qInsert);
//        	stmt.executeUpdate(qInsert);
            
		} catch (ClassNotFoundException e) {
			System.out.println("error: failed to load Oracle driver.");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("error: failed to create a connection object.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("other error:");
			e.printStackTrace();
		} finally {
			try {			
				stmt.close();			
				conn.close();
			} catch (Exception e) {
			}
		}
    }
    
    CustomerDevice getIBSCustomerDevice(List<CustomerDevice> customerDevices) {

    	System.out.println("get Customer device");
    	for(int j=0; j < customerDevices.size(); j++) {
    		System.out.println("customer device ke i :" +j + " untuk jumlah : " + customerDevices.size());
			CustomerDevice customerDevice = customerDevices.get(j);			
			System.out.println("deviceId : " + customerDevice.getDeviceId() + " terminal ID :" +customerDevice.getTerminalId() );
			if (customerDevice != null && customerDevice.getTerminalId() != null ) {
				System.out.println("customer device is not null");
				return customerDevice;
			}else{
				return customerDevices.get(0);
			}
		}    	
    	return null;
    }

    public SelectModel getModel(){
    	ibTokens = ibTokenService.getSelectedTokens();
    	if (ibTokens != null && ibTokens.size() > 0) {
    		ibToken = ibTokens.get(0);
    	}
		return new AbstractSelectModel() {

			public List<OptionGroupModel> getOptionGroups() {
				return null;
			}

			public List<OptionModel> getOptions() {
				final List<OptionModel> options = new ArrayList<OptionModel>();
				for (final IbToken ibToken : ibTokens) {
					options.add(new OptionModelImpl(ibToken.getSerialNumber(), ibToken));
				}
				return options;
			}

		};
    } 
}
