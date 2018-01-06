package com.dwidasa.ib.services.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.CashInDompetkuView;
import com.dwidasa.engine.model.view.CcPaymentView;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.model.view.MultiFinancePaymentView;
import com.dwidasa.engine.model.view.NonTagListPaymentView;
import com.dwidasa.engine.model.view.PaymentView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.model.view.TelkomPaymentView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.model.view.TvPaymentView;
import com.dwidasa.engine.model.view.WaterPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.services.PaymentResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 10/13/11
 * Time: 12:13 PM
 */
@PublicPage
public class PaymentResourceImpl implements PaymentResource {
    private static Logger logger = Logger.getLogger(PaymentResourceImpl.class);
    @Inject
    private PaymentService paymentService;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private KioskReprintService kioskReprintService;

    @Inject
    private TransactionService transactionService;

    @Inject
    private CacheManager cacheManager;

    public PaymentResourceImpl() {
    }

    public String savePLN(Long customerId, String deviceId, String sessionId, String token, String json) {
        return savePLNExtract(json);
    }

    public String savePLN2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return savePLNExtract(json);
    }

    public String savePLNPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return savePLNExtract(json);
    }

    public String savePLNPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return savePLNExtract(json);
    }

    private String savePLNExtract(String json) {
        PlnPaymentView pv = PojoJsonMapper.fromJson(json, PlnPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.PLN_PAYMENT_CODE);
        paymentService.confirm(pv);
        paymentService.execute(pv);

        if (pv.getSave()) {
            paymentService.register(pv.transform());
        }

        return PojoJsonMapper.toJson(pv);
    }

    public String inquiryPln(Long customerId, String sessionId, String json) {
        return inquiryPlnExtract(json);
    }

    public String inquiryPlnPost(Long customerId, String sessionId, String json) {
        return inquiryPlnExtract(json);
    }

    private String inquiryPlnExtract(String json) {
        PlnPaymentView pv = PojoJsonMapper.fromJson(json, PlnPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.PLN_PAYMENT_INQ_CODE);
        pv.setTransactionDate(new Date());
        pv = (PlnPaymentView) paymentService.inquiry(pv);
        return PojoJsonMapper.toJson(pv);
    }

    public String reprintPln(Long customerId, String sessionId, Long transactionId) {
        return reprintPlnExtract(transactionId);
    }

    public String reprintPlnPost(Long customerId, String sessionId, String json) {
        PlnPaymentView pv = PojoJsonMapper.fromJson(json, PlnPaymentView.class);
        pv.setTransactionType(Constants.PLN_PAYMENT_REP_CODE);
        pv = (PlnPaymentView) kioskReprintService.reprint(pv);
        if (pv.getReferenceNumber() == null)
            pv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(pv);
    }

    private String reprintPlnExtract(Long transactionId) {
        PlnPaymentView plnPaymentView = (PlnPaymentView) EngineUtils.deserialize(transactionDataService.getByTransactionFk(transactionId));
        if (plnPaymentView.getCardData2() != null) plnPaymentView.setCardData2(EngineUtils.getEncryptedPin(plnPaymentView.getCardData2(), plnPaymentView.getCardNumber()));
        plnPaymentView.setTransactionType(Constants.PLN_PAYMENT_REP_CODE);
        paymentService.reprint(plnPaymentView, transactionId);

        return PojoJsonMapper.toJson(plnPaymentView);
    }

    public String saveCc(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveCcExtract(json);
    }

    public String saveCcPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveCcExtract(json);
    }

    public String saveCcPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveCcExtract(json);
    }

    private String saveCcExtract(String json) {
        CcPaymentView pv = PojoJsonMapper.fromJson(json, CcPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.CC_PAYMENT_CODE);
        paymentService.confirm(pv);
        paymentService.execute(pv);

        if (pv.getSave()) {
            paymentService.register(pv.transform());
        }

        return PojoJsonMapper.toJson(pv);
    }

    public String inquiryCc(Long customerId, String sessionId, String json) {
        return inquiryCcExtract(json);
    }

    public String inquiryCcPost(Long customerId, String sessionId, String json) {
        return inquiryCcExtract(json);
    }

    private String inquiryCcExtract(String json) {
        CcPaymentView pv = PojoJsonMapper.fromJson(json, CcPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.CC_PAYMENT_INQ_CODE);
        pv.setTransactionDate(new Date());
        pv = (CcPaymentView) paymentService.inquiry(pv);
        return PojoJsonMapper.toJson(pv);
    }

    public String getRegisteredPayment(Long customerId, String sessionId, String transactionType, String billerCode) {
        return getRegisteredPaymentExtract(customerId, transactionType, billerCode);
    }

    public String getRegisteredPaymentPost(Long customerId, String sessionId, String transactionType, String billerCode) {
        return getRegisteredPaymentExtract(customerId, transactionType, billerCode);
    }

    private String getRegisteredPaymentExtract(Long customerId, String transactionType, String billerCode) {
        List<CustomerRegister> crs = paymentService.getRegisters(customerId, transactionType, billerCode);
        return PojoJsonMapper.toJson(crs);
    }

    public String saveHp2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveHp(customerId, deviceId, sessionId, token, json);
    }

    public String saveHp(Long customerId, String deviceId, String sessionId, String token, String json) {
        PaymentView pv;

        try {
            pv = PojoJsonMapper.fromJson(json, HpPaymentView.class);
        }
        catch (Exception e) {
            // try to parse as TelkomPaymentView
            pv = PojoJsonMapper.fromJson(json, TelkomPaymentView.class);
        }

        if (pv.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            return saveTelkomExtract(json);
        }
        else {
            return saveHpExtract(json);
        }
    }

    private String saveHpExtract(String json) {
        HpPaymentView pv = PojoJsonMapper.fromJson(json, HpPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.TELCO_PAYMENT_CODE);
        paymentService.confirm(pv);
        paymentService.execute(pv);

        if (pv.getSave()) {
            paymentService.register(pv.transform());
        }
        //XL Sera, no referensi kosong diganti "-"
        if (pv.getReferenceNumber() == null || pv.getReferenceNumber().equals("")) {
            pv.setReferenceNumber("-");
        }
        return PojoJsonMapper.toJson(pv);
    }

    public String inquiryHp(Long customerId, String sessionId, String json) {
        PaymentView pv = PojoJsonMapper.fromJson(json, PaymentView.class);
        if (pv.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            return inquiryTelkomExtract(json);
        }
        return inquiryHpExtract(json);
    }
    
    public String saveHpPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        HpPaymentView pv = PojoJsonMapper.fromJson(json, HpPaymentView.class);

        if (pv.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            return saveTelkomExtract(json);
        }
        else {
            return saveHpExtract(json);
        }
    }

    
    public String saveHpPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        HpPaymentView pv = PojoJsonMapper.fromJson(json, HpPaymentView.class);

        if (pv.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            return saveTelkomExtract(json);
        }
        else {
            return saveHpExtract(json);
        }

    }

    public String inquiryHpPost(Long customerId, String sessionId, String json) {
        HpPaymentView pv = PojoJsonMapper.fromJson(json, HpPaymentView.class);
        if (pv.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            return inquiryTelkomExtract(json);
        }
        return inquiryHpExtract(json);
    }

    private String inquiryHpExtract(String json) {
        HpPaymentView pv = PojoJsonMapper.fromJson(json, HpPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.TELCO_PAYMENT_INQ_CODE);
        pv.setTransactionDate(new Date());
        pv = (HpPaymentView) paymentService.inquiry(pv);

        return PojoJsonMapper.toJson(pv);
    }

    
    public String reprintHp(Long customerId, String sessionId, Long transactionId) {
        return reprintHpExtract(transactionId);
    }

    public String reprintHpPost(Long customerId, String sessionId, String json) {
        HpPaymentView pv = PojoJsonMapper.fromJson(json, HpPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
    	pv.setTransactionType(Constants.TELCO_PAYMENT_REP_CODE);
        if (pv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {            
            Transaction trans = transactionService.getByTransType_AccountNo_CustRef_Amount(Constants.TELCO_PAYMENT_CODE, pv.getAccountNumber(), pv.getCustomerReference(), pv.getAmount());
//            TransactionData transData = transactionDataService.getByTransactionFk(trans.getId());
//            String jsonFromDB = transData.getTransactionData();
//            HpPaymentView pvFromDB = PojoJsonMapper.fromJson(jsonFromDB, HpPaymentView.class);
            pv.setAmount(trans.getTransactionAmount());
        }
    	pv = (HpPaymentView) kioskReprintService.reprint(pv);
        if (pv.getReferenceNumber() == null)
            pv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(pv);
    }

    private String reprintHpExtract(Long transactionId) {
        PaymentView pv = (PaymentView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        if (pv.getProductCode().equals(Constants.FLEXI_POSTPAID)){
            TelkomPaymentView telkomPaymentView = (TelkomPaymentView) pv;
            telkomPaymentView.setTransactionType(com.dwidasa.engine.Constants.TELKOM_PAYMENT_REP_CODE);
            paymentService.reprint(telkomPaymentView, transactionId);

            return PojoJsonMapper.toJson(telkomPaymentView);
        }
        else {
            HpPaymentView hpPaymentView = (HpPaymentView) pv;
            hpPaymentView.setTransactionType(Constants.TELCO_PAYMENT_REP_CODE);
            paymentService.reprint(hpPaymentView, transactionId);

            return PojoJsonMapper.toJson(hpPaymentView);
        }


    }


    public String saveTelkom2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTelkomExtract(json);
    }

    public String saveTelkom(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTelkomExtract(json);
    }

    
    public String saveTelkomPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTelkomExtract(json);
    }

    public String saveTelkomPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveTelkomExtract(json);
    }

    private String saveTelkomExtract(String json) {
        TelkomPaymentView tv = PojoJsonMapper.fromJson(json, TelkomPaymentView.class);
        if (tv.getCardData2() != null) tv.setCardData2(EngineUtils.getEncryptedPin(tv.getCardData2(), tv.getCardNumber()));
        logger.info("tv.getCardData2() posting=" + tv.getCardData2());
        tv.setTransactionType(Constants.TELKOM_PAYMENT_CODE);

        String billerCode = cacheManager.getBillers(Constants.TELKOM_PAYMENT_CODE).get(0).getBillerCode();
        String productCode = cacheManager.getBillerProducts(Constants.TELKOM_PAYMENT_CODE, billerCode)
                    .get(0).getProductCode();

        tv.setBillerCode(billerCode);
        tv.setProductCode(productCode);

        paymentService.confirm(tv);
        paymentService.execute(tv);

        if (tv.getSave()) {
            paymentService.register(tv.transform());
        }

        return PojoJsonMapper.toJson(tv);
    }

    public String inquiryTelkom(Long customerId, String sessionId, String json) {
        return inquiryTelkomExtract(json);
    }

    public String inquiryTelkomPost(Long customerId, String sessionId, String json) {
        return inquiryTelkomExtract(json);
    }

    private String inquiryTelkomExtract(String json) {
        TelkomPaymentView pv = PojoJsonMapper.fromJson(json, TelkomPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        logger.info("tv.getCardData2() inquiry=" + pv.getCardData2());
        pv.setTransactionType(Constants.TELKOM_PAYMENT_INQ_CODE);
        pv.setTransactionDate(new Date());
        pv = (TelkomPaymentView) paymentService.inquiry(pv);

        return PojoJsonMapper.toJson(pv);
    }

    public String reprintTelkom(Long customerId, String sessionId, Long transactionId) {
        return reprintTelkomExtract(transactionId);
    }

    public String reprintTelkomPost(Long customerId, String sessionId, String json) {
        TelkomPaymentView pv = PojoJsonMapper.fromJson(json, TelkomPaymentView.class);
        pv.setTransactionType(Constants.TELKOM_PAYMENT_REP_CODE);
        pv = (TelkomPaymentView) kioskReprintService.reprint(pv);
        if (pv.getReferenceNumber() == null)
            pv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(pv);
    }

    private String reprintTelkomExtract(Long transactionId) {
        TelkomPaymentView telkomPaymentView = (TelkomPaymentView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        if (telkomPaymentView.getCardData2() != null) telkomPaymentView.setCardData2(EngineUtils.getEncryptedPin(telkomPaymentView.getCardData2(), telkomPaymentView.getCardNumber()));
        telkomPaymentView.setTransactionType(Constants.TELKOM_PAYMENT_REP_CODE);
        paymentService.reprint(telkomPaymentView, transactionId);

        return PojoJsonMapper.toJson(telkomPaymentView);
    }

    public String saveTrain(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTrainExtract(json);
    }

    public String saveTrain2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTrainExtract(json);
    }

    public String saveTrainPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTrainExtract(json);
    }

    public String saveTrainPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveTrainExtract(json);
    }

    private String saveTrainExtract(String json) {
        TrainPaymentView pv = PojoJsonMapper.fromJson(json, TrainPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.TRANSPORTATION_PAYMENT_CODE);
        paymentService.confirm(pv);
        paymentService.execute(pv);

        if (pv.getSave()) {
            paymentService.register(pv.transform());
        }

        return PojoJsonMapper.toJson(pv);
    }

    public String inquiryTrain(Long customerId, String sessionId, String json) {
        return inquiryTrainExtract(json);
    }

    public String inquiryTrainPost(Long customerId, String sessionId, String json) {
        return inquiryTrainExtract(json);
    }

    private String inquiryTrainExtract(String json) {
        TrainPaymentView tv = PojoJsonMapper.fromJson(json, TrainPaymentView.class);
        if (tv.getCardData2() != null) tv.setCardData2(EngineUtils.getEncryptedPin(tv.getCardData2(), tv.getCardNumber()));
        tv.setTransactionType(Constants.TRANSPORTATION_PAYMENT_INQ_CODE);
        tv.setTransactionDate(new Date());
        tv = (TrainPaymentView) paymentService.inquiry(tv);

        return PojoJsonMapper.toJson(tv);
    }

    public String reprintTrain(Long customerId, String sessionId, Long transactionId) {
        return reprintTrainExtract(transactionId);
    }

    public String reprintTrainPost(Long customerId, String sessionId, String json) {
        TrainPaymentView tv = PojoJsonMapper.fromJson(json, TrainPaymentView.class);
        tv.setTransactionType(Constants.TRANSPORTATION_PAYMENT_REP_CODE);
        tv = (TrainPaymentView) kioskReprintService.reprint(tv);
        if (tv.getReferenceNumber() == null)
            tv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(tv);
    }

    private String reprintTrainExtract(Long transactionId) {
        TrainPaymentView trainPaymentView = (TrainPaymentView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        if (trainPaymentView.getCardData2() != null) trainPaymentView.setCardData2(EngineUtils.getEncryptedPin(trainPaymentView.getCardData2(), trainPaymentView.getCardNumber()));
        trainPaymentView.setTransactionType(Constants.TRANSPORTATION_PAYMENT_REP_CODE);
        paymentService.reprint(trainPaymentView, transactionId);

        return PojoJsonMapper.toJson(trainPaymentView);
    }

    public String saveTv(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTvExtract(json);
    }

    public String saveTv2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTvExtract(json);
    }

    public String saveTvPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveTvExtract(json);
    }

    public String saveTvPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveTvExtract(json);
    }

    private String saveTvExtract(String json) {
        TvPaymentView pv = PojoJsonMapper.fromJson(json, TvPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_CODE);
        paymentService.confirm(pv);
        paymentService.execute(pv);

        if (pv.getSave()) {
            paymentService.register(pv.transform());
        }

        return PojoJsonMapper.toJson(pv);
    }

    public String inquiryTv(Long customerId, String sessionId, String json) {
        return inquiryTvExtract(json);
    }

    public String inquiryTvPost(Long customerId, String sessionId, String json) {
        return inquiryTvExtract(json);
    }

    private String inquiryTvExtract(String json) {
        TvPaymentView tv = PojoJsonMapper.fromJson(json, TvPaymentView.class);
        if (tv.getCardData2() != null) tv.setCardData2(EngineUtils.getEncryptedPin(tv.getCardData2(), tv.getCardNumber()));
        tv.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_INQ_CODE);
        tv = (TvPaymentView) paymentService.inquiry(tv);

        return PojoJsonMapper.toJson(tv);
    }

    public String reprintTv(Long customerId, String sessionId, Long transactionId) {
        return reprintTvExtract(transactionId);
    }

    public String reprintTvPost(Long customerId, String sessionId, String json) {
        TvPaymentView tv = PojoJsonMapper.fromJson(json, TvPaymentView.class);
        if (tv.getCardData2() != null) tv.setCardData2(EngineUtils.getEncryptedPin(tv.getCardData2(), tv.getCardNumber()));
        tv.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_REP_CODE);
        tv = (TvPaymentView) kioskReprintService.reprint(tv);
        if (tv.getReferenceNumber() == null)
            tv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(tv);
    }

    private String reprintTvExtract(Long transactionId) {
        TvPaymentView tvPaymentView = (TvPaymentView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        if (tvPaymentView.getCardData2() != null) tvPaymentView.setCardData2(EngineUtils.getEncryptedPin(tvPaymentView.getCardData2(), tvPaymentView.getCardNumber()));
        tvPaymentView.setTransactionType(Constants.ENTERTAINMENT_PAYMENT_REP_CODE);
        paymentService.reprint(tvPaymentView, transactionId);

        return PojoJsonMapper.toJson(tvPaymentView);
    }

    public String saveInternet(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveInternetExtract(json);
    }

    public String saveInternet2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveInternetExtract(json);
    }

    public String saveInternetPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveInternetExtract(json);
    }

    public String saveInternetPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveInternetExtract(json);
    }

    private String saveInternetExtract(String json) {
        InternetPaymentView pv = PojoJsonMapper.fromJson(json, InternetPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.INTERNET_PAYMENT_CODE);
        paymentService.confirm(pv);
        paymentService.execute(pv);

        if (pv.getSave()) {
            paymentService.register(pv.transform());
        }

        return PojoJsonMapper.toJson(pv);
    }

    public String inquiryInternet(Long customerId, String sessionId, String json) {
        return inquiryInternetExtract(json);
    }

    public String inquiryInternetPost(Long customerId, String sessionId, String json) {
        return inquiryInternetExtract(json);
    }

    private String inquiryInternetExtract(String json) {
        InternetPaymentView iv = PojoJsonMapper.fromJson(json, InternetPaymentView.class);
        if (iv.getCardData2() != null) iv.setCardData2(EngineUtils.getEncryptedPin(iv.getCardData2(), iv.getCardNumber()));
        iv.setTransactionType(Constants.INTERNET_PAYMENT_INQ_CODE);
        iv.setTransactionDate(new Date());
        iv = (InternetPaymentView) paymentService.inquiry(iv);

        return PojoJsonMapper.toJson(iv);
    }

    public String reprintInternet(Long customerId, String sessionId, Long transactionId) {
        return reprintInternetExtract(transactionId);
    }

    public String reprintInternetPost(Long customerId, String sessionId, String json) {
        InternetPaymentView iv = PojoJsonMapper.fromJson(json, InternetPaymentView.class);
        iv.setTransactionType(Constants.INTERNET_PAYMENT_REP_CODE);
        iv = (InternetPaymentView) kioskReprintService.reprint(iv);
        if (iv.getReferenceNumber() == null)
            iv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(iv);
    }

    private String reprintInternetExtract(Long transactionId) {
        InternetPaymentView internetPaymentView = (InternetPaymentView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        if (internetPaymentView.getCardData2() != null) internetPaymentView.setCardData2(EngineUtils.getEncryptedPin(internetPaymentView.getCardData2(), internetPaymentView.getCardNumber()));
        internetPaymentView.setTransactionType(Constants.INTERNET_PAYMENT_REP_CODE);
        paymentService.reprint(internetPaymentView, transactionId);

        return PojoJsonMapper.toJson(internetPaymentView);
    }

    public String saveNonTagList(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveNonTagListExtract(json);
    }

    public String saveNonTagList2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveNonTagListExtract(json);
    }

    public String saveNonTagListPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveNonTagListExtract(json);
    }


    public String saveNonTagListPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveNonTagListExtract(json);
    }

    private String saveNonTagListExtract(String json) {
        NonTagListPaymentView nv = PojoJsonMapper.fromJson(json, NonTagListPaymentView.class);
        if (nv.getCardData2() != null) nv.setCardData2(EngineUtils.getEncryptedPin(nv.getCardData2(), nv.getCardNumber()));
        nv.setTransactionType(Constants.NONTAGLIST_PAYMENT_CODE);
        paymentService.confirm(nv);
        paymentService.execute(nv);

        if (nv.getSave()) {
            paymentService.register(nv.transform());
        }

        return PojoJsonMapper.toJson(nv);
    }


    public String inquiryNonTagList(Long customerId, String sessionId, String json) {
        return inquiryNonTagListExtract(json);
    }

    public String inquiryNonTagListPost(Long customerId, String sessionId, String json) {
        return inquiryNonTagListExtract(json);
    }

    private String inquiryNonTagListExtract(String json) {
        NonTagListPaymentView nv = PojoJsonMapper.fromJson(json, NonTagListPaymentView.class);
        if (nv.getCardData2() != null) nv.setCardData2(EngineUtils.getEncryptedPin(nv.getCardData2(), nv.getCardNumber()));
        nv.setTransactionType(Constants.NONTAGLIST_PAYMENT_INQ_CODE);
        nv.setTransactionDate(new Date());
        nv = (NonTagListPaymentView) paymentService.inquiry(nv);

        return PojoJsonMapper.toJson(nv);
    }

    public String reprintNonTagList(Long customerId, String sessionId, Long transactionId) {
        return reprintNonTagListExtract(transactionId);
    }

    public String reprintNonTagListPost(Long customerId, String sessionId, String json) {
        NonTagListPaymentView iv = PojoJsonMapper.fromJson(json, NonTagListPaymentView.class);
        if (iv.getCardData2() != null) iv.setCardData2(EngineUtils.getEncryptedPin(iv.getCardData2(), iv.getCardNumber()));
        iv.setTransactionType(Constants.NONTAGLIST_PAYMENT_REP_CODE);
        iv = (NonTagListPaymentView) kioskReprintService.reprint(iv);
        if (iv.getReferenceNumber() == null)
            iv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(iv);
    }

    private String reprintNonTagListExtract(Long transactionId) {
        NonTagListPaymentView nv = (NonTagListPaymentView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        if (nv.getCardData2() != null) nv.setCardData2(EngineUtils.getEncryptedPin(nv.getCardData2(), nv.getCardNumber()));
        paymentService.reprint(nv, transactionId);

        return PojoJsonMapper.toJson(nv);
    }

/***Palyja***/
    public String saveWater(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveWaterExtract(json);
    }

    public String saveWater2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveWaterExtract(json);
    }

    public String saveWaterPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveWaterExtract(json);
    }

    public String saveWaterPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveWaterExtract(json);
    }

    private String saveWaterExtract(String json) {
        WaterPaymentView pv = PojoJsonMapper.fromJson(json, WaterPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        if(pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAMSurabaya) || 
                pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAMSemarang)){
             
                pv.setBillerCode(Constants.WATER.BILLER_CODE.PAM_BILLER);
             	
             }
        pv.setTransactionType(Constants.WATER.TRANSACTION_TYPE.POSTING);
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        pv.setBillDateYyyyMMdd((pv.getBillDate() == null) ? "00000000" : sdf.format(pv.getBillDate()));
        pv.setDueDateDdMMyyyy((pv.getDueDate() == null) ? "00000000" : sdf.format(pv.getDueDate()));
        pv.setEndServiceDdMMyyyy((pv.getEndService() == null) ? "00000000" : sdf.format(pv.getEndService()));
        pv.setStartServiceDdMMyyyy((pv.getStartService() == null) ? "00000000" : sdf.format(pv.getStartService()));

        paymentService.confirm(pv);
        paymentService.execute(pv);

        if (pv.getSave()) {
            paymentService.register(pv.transform());
        }

        return PojoJsonMapper.toJson(pv);
    }

    public String inquiryWater(Long customerId, String sessionId, String json) {
        return inquiryWaterExtract(json);
    }

    public String inquiryWaterPost(Long customerId, String sessionId, String json) {
        return inquiryWaterExtract(json);
    }

    private String inquiryWaterExtract(String json) {
        WaterPaymentView pv = PojoJsonMapper.fromJson(json, WaterPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        if(pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAMSurabaya) || 
                pv.getBillerCode().equals(Constants.WATER.BILLER_CODE.PAMSemarang)){
             
                pv.setBillerCode(Constants.WATER.BILLER_CODE.PAM_BILLER);
             	
        }
        pv.setTransactionType(Constants.WATER.TRANSACTION_TYPE.INQUIRY);
        pv.setAccountType(Constants.WATER.FROM_ACCOUNT);
        pv.setToAccountType(Constants.WATER.TO_ACCOUNT.INQUIRY);
        pv.setTransactionDate(new Date());
        pv = (WaterPaymentView) paymentService.inquiry(pv);

        return PojoJsonMapper.toJson(pv);
    }

    public String reprintWater(Long customerId, String sessionId, Long transactionId) {
        return reprintWaterExtract(transactionId);
    }

    public String reprintWaterPost(Long customerId, String sessionId, String json) {
        WaterPaymentView pv = PojoJsonMapper.fromJson(json, WaterPaymentView.class);
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.WATER.TRANSACTION_TYPE.REPRINT);
        pv.setAccountType(Constants.WATER.FROM_ACCOUNT);
        pv.setToAccountType(Constants.WATER.TO_ACCOUNT.REPRINT);
        pv = (WaterPaymentView) kioskReprintService.reprint(pv);
        if (pv.getReferenceNumber() == null)
            pv.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(pv);
    }

    private String reprintWaterExtract(Long transactionId) {
        WaterPaymentView pv = (WaterPaymentView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        if (pv.getCardData2() != null) pv.setCardData2(EngineUtils.getEncryptedPin(pv.getCardData2(), pv.getCardNumber()));
        pv.setTransactionType(Constants.WATER.TRANSACTION_TYPE.REPRINT);
        
        paymentService.reprint(pv, transactionId);

        return PojoJsonMapper.toJson(pv);
    }

	@Override
	public String indosatDompetku(Long customerId, String sessionId, String json) {
		
		CashInDompetkuView pv = PojoJsonMapper.fromJson(json, CashInDompetkuView.class);
		logger.info("Status "+pv.getStatus());
		
		if(Constants.STATUS_CASH_IN.equals(pv.getStatus())){
			logger.info("Cash In");
			pv.setTransactionType(Constants.DOMPETKU_CASH_IN);
		}else 
			if(Constants.STATUS_CASH_OUT.equals(pv.getStatus())){
				logger.info("Cash Out");
				pv.setTransactionType(Constants.DOMPETKU_CASH_OUT);
		}else 
			if(Constants.STATUS_CASH_TRANSFER_TOKEN.equals(pv.getStatus())){
				logger.info("Cash Transfer Token");
				pv.setTransactionType(Constants.DOMPETKU_TRANSFER_TOKEN);
		}
		
		pv.setToAccountType("00");
        pv.setProviderCode(Constants.PROVIDER_FINNET_CODE);
        paymentService.execute(pv);
		return PojoJsonMapper.toJson(pv);
	}
	
	
	@Override
	public String multiFinanceInq(Long customerId, String sessionId, String json, String jenis) {
		
		if(jenis.toUpperCase().equals("MTF")){
			
			MultiFinancePaymentView pv = PojoJsonMapper.fromJson(json, MultiFinancePaymentView.class);
			pv.setTransactionDate(new Date());
	    	pv.setBillerCode("");
	    	pv.setBillerName("");
	    	pv.setProductCode("");
	    	pv.setProductName("");
	    	pv.setProviderCode("");
			pv.setToAccountType("00");
	        pv.setTransactionType(Constants.MULTIFINANCE_NEW.INQ_MULTI_FINANCE);
	        paymentService.inquiry(pv);
			return PojoJsonMapper.toJson(pv);
			
		}else{
			
		MultiFinancePaymentView pv = PojoJsonMapper.fromJson(json, MultiFinancePaymentView.class);
		pv.setTransactionDate(new Date());
    	pv.setBillerCode("");
    	pv.setBillerName("");
    	pv.setProductCode("");
    	pv.setProductName("");
    	pv.setProviderCode("");
		pv.setToAccountType("00");
//        pv.setTransactionType(Constants.MULTIFINANCE_NEW.INQ_MULTI_FINANCE_MNC);
        paymentService.inquiry(pv);
		return PojoJsonMapper.toJson(pv);
		
		
		}
		
		
	}

	@Override
	public String multiFinancePay(Long customerId, String deviceId, String sessionId, String token, String json, String jenis) {
		
		return paymentMultiFinance(json, jenis);
	}
	
	
	@Override
	public String multiFinancePay2(Long customerId, String deviceId, String sessionId, String token, String json, String jenis) {
		
		return paymentMultiFinance(json, jenis);
		
	}
	
	
	public String paymentMultiFinance(String json, String jenis){
		
		
		if(jenis.toUpperCase().equals("MTF")){
		
		MultiFinancePaymentView pv = PojoJsonMapper.fromJson(json, MultiFinancePaymentView.class);
		pv.setTransactionDate(new Date());
    	pv.setBillerCode("");
    	pv.setBillerName("");
    	pv.setProductCode("");
    	pv.setProductName("");
    	pv.setProviderCode("");
		pv.setToAccountType("00");
        pv.setTransactionType(Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE);
        paymentService.execute(pv);
        
        if(pv.getSave()){
        	paymentService.register(pv.transform());
        }
        
		return PojoJsonMapper.toJson(pv);
		
		}else{
			
			MultiFinancePaymentView pv = PojoJsonMapper.fromJson(json, MultiFinancePaymentView.class);
			pv.setTransactionDate(new Date());
	    	pv.setBillerCode("");
	    	pv.setBillerName("");
	    	pv.setProductCode("");
	    	pv.setProductName("");
	    	pv.setProviderCode("");
			pv.setToAccountType("00");
//	        pv.setTransactionType(Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE_MNC);
	        pv.setProviderCode(Constants.PROVIDER_FINNET_CODE);
	        paymentService.execute(pv);
	        
	        if(pv.getSave()){
	        	paymentService.register(pv.transform());
	        }
	        
			return PojoJsonMapper.toJson(pv);
			
			
		}
	}
	
	
}
