package com.dwidasa.ib.services.impl;

import java.util.Date;

import javax.ws.rs.QueryParam;

import com.dwidasa.engine.service.facade.KioskCheckStatusService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.util.ReferenceGenerator;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.model.view.RefundDelimaView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.DelimaResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 11/1/11
 * Time: 3:29 PM
 */
@PublicPage
public class DelimaResourceImpl implements DelimaResource {
    @Inject
    private DelimaService delimaService;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private KioskReprintService kioskReprintService;

    @Inject
    private KioskCheckStatusService kioskCheckStatusService;

    public DelimaResourceImpl() {
    }

    public String saveCashIn(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveCashInExtract(json);
    }

    public String saveCashInPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveCashInExtract(json);
    }

    public String saveCashInPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveCashInExtract(json);
    }

    private String saveCashInExtract(String json) {
        CashInDelimaView cv = PojoJsonMapper.fromJson(json, CashInDelimaView.class);
        if (cv.getCardData2() != null) cv.setCardData2(EngineUtils.getEncryptedPin(cv.getCardData2(), cv.getCardNumber()));
        cv.setTransactionType(Constants.CASHIN_DELIMA_CODE);
        delimaService.confirm(cv);
        delimaService.execute(cv);

        return PojoJsonMapper.toJson(cv);
    }

    public String inquiryCashIn(Long customerId, String sessionId, String json) {
        return inquiryCashInExtract(json);
    }

    public String inquiryCashInPost(Long customerId, String sessionId, String json) {
        return inquiryCashInExtract(json);
    }

    private String inquiryCashInExtract(String json) {
        CashInDelimaView cv = PojoJsonMapper.fromJson(json, CashInDelimaView.class);
        cv.setTransactionType(Constants.CASHIN_DELIMA_INQ_CODE);
        cv.setTransactionDate(new Date());
        cv = (CashInDelimaView) delimaService.inquiry(cv);
        return PojoJsonMapper.toJson(cv);
    }

    public String reprintCashIn(Long customerId, String sessionId, Long transactionId) {
        return reprintCashInExtract(transactionId);
    }

    public String reprintCashInPost(Long customerId, String sessionId, String json) {
        CashInDelimaView cv = PojoJsonMapper.fromJson(json, CashInDelimaView.class);
        cv.setTransactionType(Constants.CASHIN_DELIMA_REP_CODE);
        cv = (CashInDelimaView) kioskReprintService.reprint(cv);
        if (cv.getReferenceNumber() == null)
            cv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(cv);
    }

    private String reprintCashInExtract(Long transactionId) {
        CashInDelimaView cashInDelimaView = (CashInDelimaView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        cashInDelimaView.setTransactionType(Constants.CASHIN_DELIMA_REP_CODE);
        delimaService.reprint(cashInDelimaView, transactionId);

        return PojoJsonMapper.toJson(cashInDelimaView);
    }

    public String statusCashIn(Long customerId, String sessionId, String json) {
        return statusCashInExtract(json);
    }

    public String statusCashInPost(Long customerId, String sessionId, String json) {
        CashInDelimaView cv = PojoJsonMapper.fromJson(json, CashInDelimaView.class);
        cv.setTransactionType(Constants.CASHIN_DELIMA_CHK_CODE);
        cv = (CashInDelimaView) kioskCheckStatusService.checkStatus(cv);
        if (cv.getReferenceNumber() == null)
            cv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(cv);
    }

    private String statusCashInExtract(String json) {
        CashInDelimaView cv = PojoJsonMapper.fromJson(json, CashInDelimaView.class);
        cv.setTransactionType(Constants.CASHIN_DELIMA_CHK_CODE);
        delimaService.checkStatus(cv);

        return PojoJsonMapper.toJson(cv);
    }

    public String saveCashOut(Long customerId, String deviceId, String sessionId, String token, String json) {

        CashOutDelimaView cv = PojoJsonMapper.fromJson(json, CashOutDelimaView.class);
        cv.setTransactionType(Constants.CASHOUT_DELIMA_CODE);
        delimaService.confirm(cv);
        delimaService.execute(cv);

        return PojoJsonMapper.toJson(cv);
    }

    public String inquiryCashOut(Long customerId, String sessionId, String json) {

        CashOutDelimaView cv = PojoJsonMapper.fromJson(json, CashOutDelimaView.class);
        cv.setTransactionType(Constants.CASHOUT_DELIMA_INQ_CODE);
        cv.setTransactionDate(new Date());
        cv = (CashOutDelimaView) delimaService.inquiry(cv);
        return PojoJsonMapper.toJson(cv);
    }

    public String reprintCashOut(Long customerId, String sessionId, Long transactionId) {

        CashOutDelimaView cashOutDelimaView = (CashOutDelimaView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        cashOutDelimaView.setTransactionType(Constants.CASHOUT_DELIMA_REP_CODE);
        delimaService.reprint(cashOutDelimaView, transactionId);

        return PojoJsonMapper.toJson(cashOutDelimaView);
    }

    public String statusCashOut(Long customerId, String sessionId, String json) {

        CashInDelimaView cv = PojoJsonMapper.fromJson(json, CashInDelimaView.class);
        cv.setTransactionType(Constants.CASHOUT_DELIMA_CHK_CODE);
        delimaService.checkStatus(cv);

        return PojoJsonMapper.toJson(cv);
    }

    public String saveRefund(Long customerId, String deviceId, String sessionId, String token, String json) {

        RefundDelimaView rv = PojoJsonMapper.fromJson(json, RefundDelimaView.class);
        rv.setTransactionType(Constants.REFUND_DELIMA_CODE);
        delimaService.confirm(rv);
        delimaService.execute(rv);

        return PojoJsonMapper.toJson(rv);
    }

    
    public String inquiryRefund(Long customerId, String sessionId, String json) {

        RefundDelimaView rv = PojoJsonMapper.fromJson(json, RefundDelimaView.class);
        rv.setTransactionType(Constants.REFUND_DELIMA_INQ_CODE);
        rv.setTransactionDate(new Date());
        rv = (RefundDelimaView) delimaService.inquiry(rv);
        return PojoJsonMapper.toJson(rv);
    }

    public String reprintRefund(Long customerId, String sessionId, Long transactionId) {

        RefundDelimaView refundDelimaView = (RefundDelimaView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        refundDelimaView.setTransactionType(Constants.REFUND_DELIMA_REP_CODE);
        delimaService.reprint(refundDelimaView, transactionId);

        return PojoJsonMapper.toJson(refundDelimaView);
    }

    public String statusRefund(Long customerId, String sessionId, String json) {

        CashInDelimaView cv = PojoJsonMapper.fromJson(json, CashInDelimaView.class);
        cv.setTransactionType(Constants.REFUND_DELIMA_CHK_CODE);
        delimaService.checkStatus(cv);

        return PojoJsonMapper.toJson(cv);
    }
}
