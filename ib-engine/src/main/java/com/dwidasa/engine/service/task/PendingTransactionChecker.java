package com.dwidasa.engine.service.task;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dwidasa.engine.service.transform.TransformerFactory;
import com.sleepycat.bind.tuple.BooleanBinding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.AccountStatementView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: ryoputranto
 * Date: 7/17/12
 * Time: 11:12 AM
 */
public class PendingTransactionChecker extends Thread {

    private Logger logger = Logger.getLogger(PendingTransactionChecker.class);
    private static Boolean isActive = false;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TransactionDataDao transactionDataDao;

    public PendingTransactionChecker(){

    }

    public void checkPendingTransaction(){

        logger.info("Initialize checking pending transaction");
        if (isActive) return;
        isActive = true;
        logger.info("Start checking pending transaction");

        Date today = new Date();

        List<Transaction> ts = transactionDao.getAll(Constants.PENDING_STATUS, today);
        List<PendingTransactionCheckerTask> tasks = new ArrayList<PendingTransactionCheckerTask>();
        int numberActiveThread = 0;

        if (ts != null) {
            for (Transaction t : ts) {
                numberActiveThread++;

                PendingTransactionCheckerTask task = new PendingTransactionCheckerTask(t);
                logger.info("starting pending checker task thread # " + numberActiveThread);
                task.start();
                tasks.add(task);

                if (numberActiveThread > 25000) {
                    for (PendingTransactionCheckerTask tsk: tasks){
                        try {
                            tsk.join();
                        }
                        catch (Exception e){

                        }
                    }

                    isActive = false;
                    return;
                }

            }
        }

        for (PendingTransactionCheckerTask task: tasks){
            try {
                task.join();
            }
            catch (Exception e){

            }
        }

        isActive = false;

    }

    /**
     * Class to compose and decompose custom data for checking transaction status feature.
     * Don't used this class as singleton !
     */
    private class TransactionStatusMessageCustomizer implements MessageCustomizer {
        private TransactionData transactionData;
        private BaseView oldView;

        private TransactionStatusMessageCustomizer(TransactionData transactionData, BaseView oldView) {
            this.transactionData = transactionData;
            this.oldView = oldView;
        }

        private String setTranslationCode(String providerCode) {
            String translationCode = "";

            if (providerCode.equals(Constants.INDOSMART_CODE)) {
                translationCode = "00102";
            }
            else if (providerCode.equals(Constants.MKN_CODE)) {
                translationCode = "00402";
            }
            else if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
                translationCode = "01703";
            }
            else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
                translationCode = "01801";
            }

            return translationCode;
        }

        public void compose(BaseView view, Transaction transaction) {
            VoucherPurchaseView vpv = (VoucherPurchaseView) view;
            String[] phone = EngineUtils.splitPhoneNumber(vpv.getCustomerReference());

            transaction.setTransactionAmount(new BigDecimal(vpv.getDenomination()));
            transaction.setMerchantType(vpv.getMerchantType());
            transaction.setTerminalId(vpv.getTerminalId());

            String customData = "";
            //-- country code(an2)
            customData += "62";
            //-- area / operator code(an4)
            customData += phone[0];
            //-- phone number(an10)
            customData += String.format("%1$-10s", phone[1]);
            //-- voucher nominal(n12) right justified zero padding
            customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");

            if (vpv.getFeeIndicator() == null) {
                vpv.setFeeIndicator("C");
            }
            //-- fee indicator(a1)
            customData += vpv.getFeeIndicator();
            //-- fee amount(n12)
            customData += String.format("%012d", vpv.getFee().longValue());

            transaction.setFreeData1(customData);
            transaction.setProviderCode(vpv.getProviderCode());
            transaction.setProductCode(vpv.getProductCode());
            transaction.setBillerCode(vpv.getBillerCode());

            String prodId = String.valueOf(Long.valueOf(vpv.getDenomination()) / 1000);
            transaction.setTranslationCode(setTranslationCode(vpv.getProviderCode()) + vpv.getProviderCode() +
                    "02" + vpv.getProductCode() + StringUtils.leftPad(prodId, 4, "0"));
        }

        public Boolean decompose(Object view, Transaction transaction) {
            AccountStatementView asv = (AccountStatementView) view;
            asv.setResponseCode(transaction.getResponseCode());
            asv.setReferenceNumber(transaction.getReferenceNumber());

            VoucherPurchaseView vpv = (VoucherPurchaseView) oldView;
            vpv.setResponseCode(transaction.getResponseCode());
            vpv.setReferenceNumber(transaction.getReferenceNumber());
            if (vpv.getResponseCode().equals(Constants.SUCCESS_CODE)) {
                //-- 0 = failed, 1 = succeed, 2 = pending
                String status = transaction.getFreeData1().substring(52, 53);

                if (status.equals("0") || status.equals("1")) {
                    asv.setStatus(Integer.valueOf(status));

                    SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                    try {
                        vpv.setWindowPeriod(sdf.parse(transaction.getFreeData1().substring(28, 36)));
                    } catch (ParseException e) {
                        vpv.setWindowPeriod(new Date());
                    }
                    vpv.setSerialNumber(transaction.getFreeData1().substring(36, 52));

                    if (status.equals("1")) {
                        transaction.setStatus(Constants.SUCCEED_STATUS);
                    }
                    if (status.equals("2")) {
                        transaction.setStatus(Constants.PENDING_STATUS);
                    }
                    else {
                        transaction.setStatus(Constants.FAILED_STATUS);
                    }

                    transaction.setTransactionAmount(vpv.getAmount());
                    transaction.setTransactionType(Constants.VOUCHER_PURCHASE_CODE);
                    transaction.setUpdated(new Date());
                    transactionDao.save(transaction);

                    transactionData.setTransactionData(PojoJsonMapper.toJson(vpv));
                    transactionData.setUpdated(new Date());
                    transactionDataDao.save(transactionData);
                }
            }

            return Boolean.TRUE;
        }
    }

    private class PendingTransactionCheckerTask extends Thread {
        private Transaction t;

        public PendingTransactionCheckerTask(Transaction t){
            this.t = t;
        }

        public void run() {
            try {
                if (t.getTransactionType().equals(Constants.VOUCHER_PURCHASE_CODE)) {

                    TransactionData transactionData = transactionDataDao.getByTransactionFk(t.getId());
                    BaseView oldView = (BaseView) EngineUtils.deserialize(transactionData);
                    t = TransformerFactory.getTransformer(oldView).transformTo(oldView, t);

                    //-- change transaction status just for inquiry
                    t.setTransactionType(Constants.VOUCHER_PURCHASE_CHK_CODE);
                    MessageCustomizer customizer = new TransactionStatusMessageCustomizer(transactionData, oldView);
                    customizer.compose(oldView, t);

                    CommLink link = new MxCommLink(t);
                    link.sendMessage();

                    //-- restore transaction status back
                    t.setTransactionType(Constants.VOUCHER_PURCHASE_CODE);
                    if (t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
                        t.setStatus(Constants.SUCCEED_STATUS);
                        transactionDao.save(t);
                    }
                    else if (t.getResponseCode().equals(Constants.TIMEOUT_CODE)) {
                        // do nothing, still a pending transaction
                    }
                    else {
                        //throw new BusinessException("IB-1009", t.getResponseCode());
                        t.setStatus(Constants.FAILED_STATUS);
                        transactionDao.save(t);
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
