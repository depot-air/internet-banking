package com.dwidasa.engine.service.link;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.util.iso.IsoDigester;
import com.dwidasa.interlink.Caller;
import com.dwidasa.interlink.ServiceCaller;

/**
 * CommLink implementation for MX.
 *
 * @author rk
 */
public class MxCommLink implements CommLink, Caller {
    private Transaction transaction;
    private String isoMsg, Msg;
    private Boolean reply;
    private static Logger logger = Logger.getLogger( MxCommLink.class );
    public MxCommLink(Transaction transaction) {
        this.transaction = transaction;
    }

    public MxCommLink(Transaction transaction, String isoMsg) {
        this.transaction = transaction;
        this.isoMsg = isoMsg;
    }
    
    public MxCommLink() {
    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessage(String argMsg) {
    	
        reply = Boolean.TRUE;
        Msg = argMsg;
        
        if( transaction != null ) {
            if( "timeout".equals( argMsg ) ) {
            	
                System.out.println("timeout called !");
                reply = Boolean.TRUE;
                transaction.setResponseCode(Constants.TIMEOUT_CODE);
            }
            else {
            	
    	        // -- get the response message from back end application
    	        IsoDigester digester = (IsoDigester) ServiceLocator.getService("isoDigester");
    	
    	        try {
    	            digester.digestResponse(transaction, argMsg);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
            }
        }
    }
    
    

    /**
     * {@inheritDoc}
     */
    public void sendMessage(boolean waitResponse) {
        reply = Boolean.FALSE;

        TransactionDao transactionDao = (TransactionDao) ServiceLocator.getService("transactionDao");

        //0100, 0200 di transfer ATMB, pembayaran palyja, HP pasca tidak ada bit 39
        if (transaction.getTransactionType().equals(Constants.WATER.TRANSACTION_TYPE.INQUIRY) || transaction.getTransactionType().equals(Constants.WATER.TRANSACTION_TYPE.POSTING) || transaction.getTransactionType().equals(Constants.WATER.TRANSACTION_TYPE.REPRINT) ||
        		transaction.getTransactionType().equals(Constants.ATMB.TT_INQUIRY) || transaction.getTransactionType().equals(Constants.ALTO.TT_INQUIRY) || transaction.getTransactionType().equals(Constants.ATMB.TT_POSTING) || transaction.getTransactionType().equals(Constants.ALTO.TT_POSTING) || transaction.getTransactionType().equals(Constants.ATMB.TT_CEK_STATUS) ||
        		transaction.getTransactionType().equals(Constants.TELCO_PAYMENT_INQ_CODE) || transaction.getTransactionType().equals(Constants.TELCO_PAYMENT_INQ_CODE) || transaction.getTransactionType().equals(Constants.TELCO_PAYMENT_REP_CODE)) {
        	
        } else {
        	transaction.setResponseCode(Constants.SUCCESS_CODE);
        }
        transaction.setTransmissionDate(new Date());
        transaction.setStan(BigDecimal.valueOf(transactionDao.nextStanValue()));

        IsoDigester isoDigester = (IsoDigester) ServiceLocator.getService("isoDigester");
        String msg;
        try {
        	msg = isoDigester.digestRequest(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("e.getMessage()=" + e.getMessage());
            throw new BusinessException("IB-0500", "MXDRQ");
        }
        logger.info("msg=" + msg);
        String reqMti = transaction.getMti();
        logger.info("reqMti=" + reqMti);
        ServiceCaller caller = ( ServiceCaller ) ServiceLocator.getService( "serviceCaller" );

        try {
        	if (waitResponse) {
	            if (caller.fireAndWait(this, msg)) {
	                synchronized (this) {
	                    while (!reply) {
	                        try {
	                        	wait(5*60000); //5 menit
	                        } catch (InterruptedException e) {
	                            //-- do nothing
	                        }
	                    }
	                }
	            }
        	} else {
        		caller.fireAndForget(msg);
        	}
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "MXFAW");
        }

        if (reqMti.equals(transaction.getMti()) && transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            throw new BusinessException("IB-1009", "--");
        }
    }
    
    public String sendMessage( String argMsg ) {
    	
    	Msg = argMsg;
    	reply = Boolean.FALSE;

        ServiceCaller caller = ( ServiceCaller ) ServiceLocator.getService( "serviceCaller" );
        
        try {
        	
            if( caller.fireAndWait( this, Msg ) ) {
                synchronized( this ) {
                    while( !reply ) {
                        try {
                            wait();
                        } 
                        catch( InterruptedException e ) {

                        }
                    }
                }
            }
        } 
        catch( Exception e ) {
            e.printStackTrace();
        }
        
        return Msg;
    }

    /**
     * {@inheritDoc}
     */
    public void replyMessage() {
        if (transaction.getResponseCode() == null || transaction.getResponseCode().trim().equals("")) {
            transaction.setResponseCode(Constants.SUCCESS_CODE);
        }

        IsoDigester isoDigester = (IsoDigester) ServiceLocator.getService("isoDigester");
        String msg = null;
        try {
            msg = isoDigester.digestRequest(transaction, isoMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ServiceCaller caller = ( ServiceCaller ) ServiceLocator.getService( "serviceCaller" );
        caller.fireAndForget(msg);
    }

	@Override
	public void sendMessage() {
		try {
			sendMessage(true);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("e.getMessage()=" + e.getMessage());
            throw new BusinessException("IB-1009", "--");
        }
	}
}
