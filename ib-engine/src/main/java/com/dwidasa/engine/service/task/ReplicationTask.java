package com.dwidasa.engine.service.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.HousekeepingDao;
import com.dwidasa.engine.model.Balance;
import com.dwidasa.engine.model.TransactionHistory;
import com.dwidasa.engine.service.BalanceService;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.TransactionHistoryService;

/**
 * Replication task from core to local database. These files is being replicated,
 * H8PO, H8BL. Format of file name is H8POMMDD, H8BLMMDD respectively.
 */
public class ReplicationTask implements Executable {

    @Autowired
    private ExtendedProperty extendedProperty;
    
	private Logger logger = Logger.getLogger(ReplicationTask.class);

    private final BalanceService balanceService;
    private final TransactionHistoryService transactionHistoryService;
    private final CacheManager cacheManager;
	private final HousekeepingDao housekeepingDao;
    


    public ReplicationTask() {

        balanceService = (BalanceService) ServiceLocator.getService("balanceService");
        transactionHistoryService = (TransactionHistoryService) ServiceLocator.getService("transactionHistoryService");
        cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");
		housekeepingDao = (HousekeepingDao) ServiceLocator.getService("housekeepingDao");

    }

    public void execute(Date processingDate, Long userId) throws Exception {
    	commitSize = Integer.valueOf(cacheManager.getParameter("COMMIT_SIZE").getParameterValue());
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(processingDate);
        cal.add(Calendar.DATE, -1);
        processingDate = cal.getTime();

        transactionHistoryService.remove(processingDate);
        balanceService.remove(processingDate);

        logger.info("Menjalankan replikasi mutasi rekening untuk tanggal " + sdf.format(processingDate));

        String replicationDirectory = cacheManager.getParameter("REPLICATION_DIR").getParameterValue();

        String statementCandidateFilename = "MUTASI-HARIAN-" + sdf.format(processingDate);
        String balanceCandidateFilename = "MUTASI-HARIAN-ACCT-" + sdf.format(processingDate);

        File statementFile = null;
        File balanceFile = null;

        logger.info("Mengakses direktori replikasi : " + replicationDirectory);
        File replicationDir = new File(replicationDirectory);

        logger.info("Membaca direktori list ");

        String[] files = replicationDir.list();
        for (int i = 0; i < files.length; i++){

            String strFile = files[i];
            logger.info(strFile);

            if (strFile.startsWith(statementCandidateFilename)){

                File file = new File(replicationDir + System.getProperty("file.separator") + strFile);
                if (file.isFile()) {
                    statementFile = file;
                    logger.info("Menemukan file mutasi : " + statementFile.getName());

                }

            }
            else if (strFile.startsWith(balanceCandidateFilename)){

                File file = new File(replicationDir + System.getProperty("file.separator") + strFile);
                if (file.isFile()) {
                    balanceFile = file;
                    logger.info("Menemukan file balance : " + balanceFile.getName());

                }
            }

        }


        if (statementFile != null && balanceFile != null){
            logger.info("Membaca file...");
            replicate(statementFile, balanceFile, processingDate, userId);
        }

    }

    public void cleanup(Date processingDate) {
        transactionHistoryService.remove(processingDate);
        balanceService.remove(processingDate);
    }
    
    private PreparedStatement insertBl;
	private PreparedStatement insertPo;

	private Connection conn;
	
	private int commitSize = 10000;

    private void prepareConnection() throws Exception{
        DataSource ds = (DataSource) ServiceLocator.getService("dataSource");
		conn = ds.getConnection();

		StringBuffer sbInsertBl = new StringBuffer()
			.append("insert into h_balance ( ")
            .append("   account_number, date, currency_code, amount, created, createdby, ")
            .append("   updated, updatedby ")
            .append(") ")
            .append("values ( ")
            .append("   ?, ?, ?, ?, ?, ?, ")
            .append("   ?, ? ")
            .append(") ");
		StringBuffer sbInsertPo = new StringBuffer()
			.append("insert into h_transaction ( ")
	        .append("   account_number, currency, transaction_indicator, transaction_amount, reference_number, ")
	        .append("   entry_date, posting_date, processing_date, description, created, createdby, updated, updatedby ")
	        .append(") ")
	        .append("values ( ")
	        .append("   ?, ?, ?, ?, ?, ")
	        .append("   ?, ?, ?, ?, ?, ?, ?, ?")
	        .append(") ");

        conn.setAutoCommit(false);
		insertBl = conn.prepareStatement(sbInsertBl.toString());
		insertPo = conn.prepareStatement(sbInsertPo.toString());
    }
    
    private void replicate(File statementFile, File balanceFile, Date processingDate, Long userId) throws Exception {

		prepareConnection();
				
		BufferedReader input = null;
		String line = null;
		try {
			int i;
			
			// Mutasi rekening
			input = new BufferedReader(new FileReader(statementFile));
			i = 0;
			while ((line = input.readLine()) != null) {
				i++;
				processPo(line, processingDate, userId);
				if (i % commitSize == 0) {
					conn.commit();
                    conn.close();
                    prepareConnection();
					if (i % 500000 == 0) {					
						conn.setAutoCommit(true);
						Statement stmt = conn.createStatement();
						stmt.executeUpdate("vacuum h_transaction");
						stmt.close();
                        conn.setAutoCommit(false);
					}
				}
			}
			input.close();
			conn.commit();
            conn.close();
            prepareConnection();
			
			// Running Balance
			i = 0;
			input = new BufferedReader(new FileReader(balanceFile));
			while ((line = input.readLine()) != null) {
				i++;
				processBl(line, processingDate, userId);
				if (i % commitSize == 0) {
					conn.commit();
                    conn.close();
                    prepareConnection();
					if (i % 500000 == 0) {
						conn.setAutoCommit(true);
						Statement stmt = conn.createStatement();
						stmt.executeUpdate("vacuum h_balance");
						stmt.close();
						conn.setAutoCommit(false);
					}
				}
			}
			input.close();
			conn.commit();
            conn.close();
        }
        catch (Exception e){

            throw e;

        }
    }

    private void processBl(String line, Date processingDate, Long userId) throws Exception {

        Balance b = new Balance();

        String[] items = line.split(",");

        if (items.length < 3) {
            logger.info("dirty bl data: " + line);
            return;
        }

        try {

            String accountNumber = StringUtils.leftPad(items[0],10,"0");

            b.setDate(processingDate);
            b.setCurrencyCode("IDR");
            b.setAccountNumber(accountNumber);
            b.setAmount(new BigDecimal(items[2].trim()));
            b.setCreated(new Date());
            b.setCreatedby(userId);
            b.setUpdated(new Date());
            b.setUpdatedby(userId);

            insertBalance(b);
        }
        catch (Exception e){

            logger.info("error on parsing bl : " + line);
            logger.info(e);
        }
    }
    


    private void processPo(String line, Date processingDate, Long userId) throws Exception {

    	TransactionHistory th = new TransactionHistory();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String[] items = line.split(",");

        if (items.length < 13) {
            logger.info("dirty po data: " + line);
            return;
        }

        try {
//        	927070006,IDR,D,25000,FT13203LWPK7\BNK,20130722,20130722,DC Purchase?DC Purchase,087828784533    ,DC Purchase?DC Purchase - 087828784533    ,ID0010009,YOSEPH GUNAWAN AGUS PRIYADI TA,1635
            String hhmm = items[12].trim();

            String accountNumber = StringUtils.leftPad(items[0],10,"0");
            String currencyCode = items[1].trim();
            String transactionIndicator = items[2].trim();
            BigDecimal amount = new BigDecimal(items[3].trim());
            String referenceNumber = items[4].trim();
            Date postingDate = sdf.parse(items[5].trim() + hhmm);
            Date entryDate = sdf.parse(items[6].trim() + hhmm);
            String transactionDescription = items[9].trim();

            th.setAccountNumber(accountNumber);
            th.setReferenceNumber(referenceNumber);

            th.setPostingDate(postingDate);
            th.setDescription(transactionDescription);
            th.setTransactionAmount(amount);
            th.setTransactionIndicator(transactionIndicator);
            th.setCurrency(currencyCode);
            th.setEntryDate(entryDate);

            Date currentDate = new Date();
            th.setProcessingDate(processingDate);
            th.setCreated(currentDate);
            th.setCreatedby(userId);
            th.setUpdated(currentDate);
            th.setUpdatedby(userId);

            insertTransactionHistory(th);
        }
        catch (Exception e){
            logger.info("error on parsing po : " + line);
            logger.info(e);
        }
    }
    
    private void insertTransactionHistory(TransactionHistory th) throws Exception {
    	insertPo.setString(1, th.getAccountNumber());
    	insertPo.setString(2, th.getCurrency());
    	insertPo.setString(3, th.getTransactionIndicator());
    	insertPo.setBigDecimal(4, th.getTransactionAmount());
    	insertPo.setString(5, th.getReferenceNumber());
    	insertPo.setTimestamp(6, new Timestamp(th.getEntryDate().getTime()));
    	insertPo.setTimestamp(7, new Timestamp(th.getPostingDate().getTime()));
    	insertPo.setTimestamp(8, new Timestamp(th.getProcessingDate().getTime()));
    	insertPo.setString(9, th.getDescription());
    	insertPo.setTimestamp(10, new Timestamp(th.getCreated().getTime()));
    	insertPo.setLong(11, th.getCreatedby());
    	insertPo.setTimestamp(12, new Timestamp(th.getUpdated().getTime()));
    	insertPo.setLong(13, th.getUpdatedby());
    	insertPo.executeUpdate();
    }
    
    private void insertBalance(Balance b) throws Exception {
    	insertBl.setString(1, b.getAccountNumber());
    	insertBl.setDate(2, new java.sql.Date(b.getDate().getTime()));
    	insertBl.setString(3, b.getCurrencyCode());
    	insertBl.setBigDecimal(4, b.getAmount());
    	insertBl.setTimestamp(5, new Timestamp(b.getCreated().getTime()));
    	insertBl.setLong(6, b.getCreatedby());
    	insertBl.setTimestamp(7, new Timestamp(b.getUpdated().getTime()));
    	insertBl.setLong(8, b.getUpdatedby());
    	insertBl.executeUpdate();
    }

    
}
