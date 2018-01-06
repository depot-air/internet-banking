package com.dwidasa.engine.service.impl.facade;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.TransactionDao;
import com.dwidasa.engine.dao.TransactionDataDao;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.model.view.RefundDelimaView;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 5:58 PM
 */
@Service("delimaService")
public class DelimaServiceImpl extends BaseTransactionServiceImpl implements DelimaService {
    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private TransactionDataDao transactionDataDao;

    public DelimaServiceImpl() {
    }

    private String updateJsonView(CashInDelimaView view, TransactionData td) {
        String json = "";
        if (view.getTransactionType().equals(Constants.CASHIN_DELIMA_CHK_CODE)) {
            CashInDelimaView v = (CashInDelimaView) EngineUtils.deserialize(td);

            v.setAmount(view.getAmount());
            v.setProviderFee(view.getProviderFee());
            v.setBillerReference(view.getBillerReference());
            v.setReferenceNumber(view.getReferenceNumber());
            v.setFee(view.getFee());
            v.setFeeIndicator(view.getFeeIndicator());

            json = PojoJsonMapper.toJson(v);
        }
        else if (view.getTransactionType().equals(Constants.CASHOUT_DELIMA_CHK_CODE)) {
            CashOutDelimaView v = (CashOutDelimaView) EngineUtils.deserialize(td);

            v.setAmount(view.getAmount());
            v.setProviderFee(view.getProviderFee());
            v.setBillerReference(view.getBillerReference());
            v.setReferenceNumber(view.getReferenceNumber());
            v.setFee(view.getFee());
            v.setFeeIndicator(view.getFeeIndicator());

            json = PojoJsonMapper.toJson(v);
        }
        else if (view.getTransactionType().equals(Constants.REFUND_DELIMA_CHK_CODE)) {
            RefundDelimaView v = (RefundDelimaView) EngineUtils.deserialize(td);

            v.setAmount(view.getAmount());
            v.setProviderFee(view.getProviderFee());
            v.setBillerReference(view.getBillerReference());
            v.setReferenceNumber(view.getReferenceNumber());
            v.setFee(view.getFee());
            v.setFeeIndicator(view.getFeeIndicator());

            json = PojoJsonMapper.toJson(v);
        }

        return json;
    }

    /**
     * {@inheritDoc}
     */
    public CashInDelimaView checkStatus(CashInDelimaView view) {
        MessageCustomizer customizer = null;
        String transactionType = null;

        if (view.getTransactionType().equals(Constants.CASHIN_DELIMA_CHK_CODE)) {
            customizer = new CashInDelimaStatusMessageCustomizer();
            transactionType = Constants.CASHIN_DELIMA_CODE;
        }
        else if (view.getTransactionType().equals(Constants.CASHOUT_DELIMA_CHK_CODE)) {
            customizer = new CashOutDelimaStatusMessageCustomizer();
            transactionType = Constants.CASHOUT_DELIMA_CODE;
        }
        else if (view.getTransactionType().equals(Constants.REFUND_DELIMA_CHK_CODE)) {
            customizer = new RefundDelimaStatusMessageCustomizer();
            transactionType = Constants.REFUND_DELIMA_CODE;
        }

        Transaction t = TransformerFactory.getTransformer(view).transformTo(view, new Transaction());
        customizer.compose(view, t);
        CommLink link = new MxCommLink(t);

        link.sendMessage();

        if (t.getResponseCode().equals(Constants.SUCCESS_CODE) ||
                !t.getResponseCode().equals(Constants.TIMEOUT_CODE)) {
            //-- update transaction & transaction data
            customizer.decompose(view, t);
            Transaction t2 = transactionDao.get(transactionType, view.getCustomerReference());
            TransactionData td = transactionDataDao.getByTransactionFk(t2.getId());

            t2.setResponseCode(t.getResponseCode());
            t2.setStatus(null);
            EngineUtils.setTransactionStatus(t2);
            t2.setUpdated(new Date());
            transactionDao.save(t2);

            td.setTransactionData(updateJsonView(view, td));
            td.setUpdated(new Date());
            transactionDataDao.save(td);
        }


        if (!t.getResponseCode().equals(Constants.SUCCESS_CODE)) {
            throw new BusinessException("IB-1009", t.getResponseCode());
        }

        return view;
    }

    private class CashInDelimaStatusMessageCustomizer implements MessageCustomizer {
        private CashInDelimaStatusMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;

            String customData = "";
            //-- receiver id an(25) left justified padding with space
            customData += String.format("%1$-25s", " ");
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- reference number an(12) left justfied padding with space
            customData += String.format("%1$-12s", " ");
            //-- admin charges - bank n(12) left justified padding with space
            customData += String.format("%012d", 0);

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00208002015");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;

            String bit48 = transaction.getFreeData1();
            cv.setReceiverIdNumber(bit48.substring(0, 25).trim());
            cv.setCustomerReference(bit48.substring(25, 41).trim());
            cv.setAmount(new BigDecimal(bit48.substring(41,53)));
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setBillerReference(bit48.substring(65, 77).trim());
            cv.setFee(new BigDecimal(bit48.substring(77,89)));

            return Boolean.TRUE;
        }
    }

    private class CashOutDelimaStatusMessageCustomizer implements MessageCustomizer {
        private CashOutDelimaStatusMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;

            String customData = "";
            //-- receiver id an(25) left justified padding with space
            customData += String.format("%1$-25s", " ");
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- reference number an(12) left justfied padding with space
            customData += String.format("%1$-12s", " ");
            //-- admin charges - bank n(12) left justified padding with space
            customData += String.format("%012d", 0);

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00204002016");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;

            String bit48 = transaction.getFreeData1();
            cv.setReceiverIdNumber(bit48.substring(0, 25).trim());
            cv.setCustomerReference(bit48.substring(25, 41).trim());
            cv.setAmount(new BigDecimal(bit48.substring(41,53)));
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setBillerReference(bit48.substring(65, 77).trim());
            cv.setFee(new BigDecimal(bit48.substring(77,89)));

            return Boolean.TRUE;
        }
    }

    private class RefundDelimaStatusMessageCustomizer implements MessageCustomizer {
        private RefundDelimaStatusMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;

            String customData = "";
            //-- receiver id an(25) left justified padding with space
            customData += String.format("%1$-25s", " ");
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- reference number an(12) left justfied padding with space
            customData += String.format("%1$-12s", " ");
            //-- admin charges - bank n(12) left justified padding with space
            customData += String.format("%012d", 0);

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00212002017");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;

            String bit48 = transaction.getFreeData1();
            cv.setSenderIdNumber(bit48.substring(0, 25).trim());
            cv.setCustomerReference(bit48.substring(25, 41).trim());
            cv.setAmount(new BigDecimal(bit48.substring(41,53)));
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setBillerReference(bit48.substring(65, 77).trim());
            cv.setFee(new BigDecimal(bit48.substring(77,89)));

            return Boolean.TRUE;
        }
    }
}
