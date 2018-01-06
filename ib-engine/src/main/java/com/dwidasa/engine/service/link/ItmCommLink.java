package com.dwidasa.engine.service.link;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.util.iso.IsoDigester;
import com.dwidasa.interlink.Caller;
import com.dwidasa.interlink.ServiceCaller;
import com.dwidasa.interlink.medium.SocketTransporter;
import com.dwidasa.interlink.medium.Transporter;
import com.dwidasa.interlink.utility.Constant;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 7/11/12
 */
public class ItmCommLink implements CommLink, Caller {
    private Transaction transaction;
    private String isoMsg;
    private Boolean reply;

    public ItmCommLink(Transaction transaction) {
        this.transaction = transaction;
    }

    public ItmCommLink(Transaction transaction, String isoMsg) {
        this.transaction = transaction;
        this.isoMsg = isoMsg;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMessage(String argMsg) {
        reply = Boolean.TRUE;

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

    /**
     * {@inheritDoc}
     */
    public void sendMessage() {
        reply = Boolean.FALSE;

        TransactionDao transactionDao = (TransactionDao) ServiceLocator.getService("transactionDao");

        transaction.setResponseCode(Constants.SUCCESS_CODE);
        transaction.setTransmissionDate(new Date());
        transaction.setStan(BigDecimal.valueOf(transactionDao.nextStanValue()));

        IsoDigester isoDigester = (IsoDigester) ServiceLocator.getService("isoDigester");
        String msg;
        try {
            msg = isoDigester.digestRequest(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("IB-0500", "MXDRQ");
        }

        String reqMti = transaction.getMti();

        ServiceCaller caller = ( ServiceCaller ) ServiceLocator.getService( "serviceCaller" );

        Transporter transporter = caller.getTransporter();
//		if( transporter.connectItm() ) {
//            try {
//                Thread.currentThread().sleep(31000);
//            }
//            catch( Exception e ) {
//
//            }
//            try {
//                if (transporter.transmit(msg)) {
//                    synchronized (this) {
//                        while (!reply) {
//                            try {
//                            	wait(5*60000); //5 menit
//                            } catch (InterruptedException e) {
//                                //-- do nothing
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                throw new BusinessException("IB-0500", "MXFAW");
//            }
//
//            if (reqMti.equals(transaction.getMti()) && transaction.getResponseCode().equals(Constants.SUCCESS_CODE)) {
//                throw new BusinessException("IB-1009", "--");
//            }
//        }
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
	public void sendMessage(boolean waitResponse) {
		// TODO Auto-generated method stub
		// not implemented
	}
}

