package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.VoucherGamePurchaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.VoucherGamePurchaseViewService;
import com.dwidasa.engine.util.EngineUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 10/1/12
 */
@Service("voucherGamePurchaseViewService")
public class VoucherGamePurchaseViewServiceImpl implements VoucherGamePurchaseViewService {
    private static Logger logger = Logger.getLogger( VoucherPurchaseViewServiceImpl.class );
    private MessageCustomizer transactionMessageCustomizer;
    private MessageCustomizer reprintMessageCustomizer;

    @Autowired
    private ProviderProductDao providerProductDao;

    public VoucherGamePurchaseViewServiceImpl() {
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        VoucherPurchaseView vpv = (VoucherPurchaseView) view;
        vpv.setToAccountType("00");
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validate(BaseView view) {
        Boolean result = view.validate();
        if (result) {
            //-- validate number prefix for specified biller
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void composeInquiry(BaseView view, Transaction transaction) {
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeInquiry(BaseView view, Transaction transaction) {
        return Boolean.TRUE;
    }

    /**
     * {@inheritDoc}
     */
    public void composeTransaction(BaseView view, Transaction transaction) {
        transactionMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeTransaction(BaseView view, Transaction transaction) {
        return transactionMessageCustomizer.decompose(view, transaction);
    }

    public void composeReprint(BaseView view, Transaction transaction) {
        reprintMessageCustomizer.compose(view, transaction);
    }

    public Boolean decomposeReprint(BaseView view, Transaction transaction) {
        return reprintMessageCustomizer.decompose(view, transaction);
    }

    /**
     * Class to compose and decompose message for execute phase
     */
    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        /**
         * {@inheritDoc}
         */
        public void compose(BaseView view, Transaction transaction) {

            CacheManager cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");

            VoucherGamePurchaseView vpv = (VoucherGamePurchaseView) view;

            //Translation Code =  Routing Code  + Process Code  + Product Code (if available)
            //032  01 = Inquiry, 02 = Posting, 03 = Cek Status
            //Value=Payee ID + Industry Code + Biller Code + ProductID
            transaction.setTranslationCode(
                    Constants.VOUCHER_GAME.ROUTING_CODE + "02" +
                    Constants.VOUCHER_GAME.PAYEE_ID + Constants.INDUSTRY_CODE.ENTERTAINMENT + Constants.VOUCHER_GAME.BILLER_CODE + Constants.VOUCHER_GAME.PRODUCT_ID);
//            transaction.setFeeIndicator(vpv.getFeeIndicator());
//            transaction.setFee(vpv.getFee());

            String customData = "";
            //Biller ID   n5  Identitas dari Biller(*); rata kanan(right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.BILLER_CODE;
            //Payee ID  n4  Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.PAYEE_ID;
            //Product ID   n4  Identitas dari Produk(*); rata kanan (right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.PRODUCT_ID;
            //Nama Voucher   ans20  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherName(), 20, " ");
            //Kode Product Voucher ans20  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getProductCode(), 20, " ");  //voucher product code = product code, bukan Constants.VOUCHER_GAME.PRODUCT_ID
            //Konversi Mata Uang Game ans40  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getCurrencyConversion(), 40, " ");
            //Kode Voucher   ans30  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherCode(), 30, " ");
            //No Serial Voucher   ans30  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherSerial(), 30, " ");
            //Bill Reference   an-10  -Rata kiri -Padding with space -Blank on posting & reprint request
            customData += StringUtils.rightPad("          ", 10, " ");
            //Denom   n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");
            //Harga Jual  n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getAmount().toPlainString(), 12, "0");    //amount = harga jual
            //Fee   n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getFee().toPlainString(), 12, "0");    //amount = harga jual

            transaction.setFreeData1(customData);
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
            VoucherGamePurchaseView vpv = (VoucherGamePurchaseView) view;
            vpv.setResponseCode(transaction.getResponseCode());
            vpv.setReferenceNumber(transaction.getReferenceNumber());

            transaction.setTransactionAmount(vpv.getAmount());

            String bit48 = transaction.getFreeData1();

            //Biller ID   n5  Identitas dari Biller(*); rata kanan(right justified); zero leading padding;See Section 5
            //Payee ID  n4  Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;See Section 5
            //Product ID   n4  Identitas dari Produk(*); rata kanan (right justified); zero leading padding;See Section 5
            //Nama Voucher   ans20  -Rata kiri -Padding with space
            vpv.setVoucherName(bit48.substring(13, 33).trim());
            //Kode Product Voucher ans20  -Rata kiri -Padding with space
            vpv.setProductCode(bit48.substring(33, 53).trim());     ////voucher product code = product code, bukan Constants.VOUCHER_GAME.PRODUCT_ID
            //Konversi Mata Uang Game ans40  -Rata kiri -Padding with space
            vpv.setCurrencyConversion(bit48.substring(53, 93).trim());
            //Kode Voucher   ans30  -Rata kiri -Padding with space
            vpv.setVoucherCode(bit48.substring(93, 123).trim());
            //No Serial Voucher   ans30  -Rata kiri -Padding with space
            vpv.setVoucherSerial(bit48.substring(123, 153).trim());
            //Bill Reference   an-10  -Rata kiri -Padding with space -Blank on posting & reprint request
            vpv.setBillReference(bit48.substring(153, 163).trim());
            //Denom   n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setDenomination(new BigDecimal(bit48.substring(163, 175)).toString());
            //Harga Jual  n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setAmount(new BigDecimal(bit48.substring(175, 187)));
            //Fee   n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setFee(new BigDecimal(bit48.substring(187, 199)));

            vpv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for reprint phase
     */
    private class ReprintMessageCustomizer implements MessageCustomizer {
        private ReprintMessageCustomizer() {
        }


        public void compose(BaseView view, Transaction transaction) {
            VoucherGamePurchaseView vpv = (VoucherGamePurchaseView) view;

            //Translation Code =  Routing Code  + Process Code  + Product Code (if available)
            //032  01 = Inquiry, 02 = Posting, 03 = Cek Status
            //Value=Payee ID + Industry Code + Biller Code + ProductID
            transaction.setTranslationCode(
                    Constants.VOUCHER_GAME.ROUTING_CODE + "03" +
                    Constants.VOUCHER_GAME.PAYEE_ID + Constants.INDUSTRY_CODE.ENTERTAINMENT + Constants.VOUCHER_GAME.BILLER_CODE + Constants.VOUCHER_GAME.PRODUCT_ID);
//            transaction.setFeeIndicator(vpv.getFeeIndicator());
//            transaction.setFee(vpv.getFee());

            String customData = "";
            //Biller ID   n5  Identitas dari Biller(*); rata kanan(right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.BILLER_CODE;
            //Payee ID  n4  Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.PAYEE_ID;
            //Product ID   n4  Identitas dari Produk(*); rata kanan (right justified); zero leading padding;See Section 5
            customData += Constants.VOUCHER_GAME.PRODUCT_ID;
            //Nama Voucher   ans20  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherName(), 20, " ");
            //Kode Product Voucher ans20  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getProductCode(), 20, " ");  //voucher product code = product code, bukan Constants.VOUCHER_GAME.PRODUCT_ID
            //Konversi Mata Uang Game ans40  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getCurrencyConversion(), 40, " ");
            //Kode Voucher   ans30  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherCode(), 30, " ");
            //No Serial Voucher   ans30  -Rata kiri -Padding with space
            customData += StringUtils.rightPad(vpv.getVoucherSerial(), 30, " ");
            //Bill Reference   an-10  -Rata kiri -Padding with space -Blank on posting & reprint request
            customData += StringUtils.rightPad("          ", 10, " ");
            //Denom   n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");
            //Harga Jual  n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getAmount().toPlainString(), 12, "0");    //amount = harga jual
            //Fee   n12  -Rata kanan -Padding with zero -No Decimal
            customData += StringUtils.leftPad(vpv.getFee().toPlainString(), 12, "0");    //amount = harga jual

            transaction.setFreeData1(customData);
        }

        public Boolean decompose(Object view, Transaction transaction) {
            VoucherGamePurchaseView vpv = (VoucherGamePurchaseView) view;
            vpv.setResponseCode(transaction.getResponseCode());
            vpv.setReferenceNumber(transaction.getReferenceNumber());

            transaction.setTransactionAmount(vpv.getAmount());

            String bit48 = transaction.getFreeData1();

            //Biller ID   n5  Identitas dari Biller(*); rata kanan(right justified); zero leading padding;See Section 5
            //Payee ID  n4  Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding;See Section 5
            //Product ID   n4  Identitas dari Produk(*); rata kanan (right justified); zero leading padding;See Section 5
            //Nama Voucher   ans20  -Rata kiri -Padding with space
            vpv.setVoucherName(bit48.substring(13, 33).trim());
            //Kode Product Voucher ans20  -Rata kiri -Padding with space
            vpv.setProductCode(bit48.substring(33, 53).trim());     ////voucher product code = product code, bukan Constants.VOUCHER_GAME.PRODUCT_ID
            //Konversi Mata Uang Game ans40  -Rata kiri -Padding with space
            vpv.setCurrencyConversion(bit48.substring(53, 93).trim());
            //Kode Voucher   ans30  -Rata kiri -Padding with space
            vpv.setVoucherCode(bit48.substring(93, 123).trim());
            //No Serial Voucher   ans30  -Rata kiri -Padding with space
            vpv.setVoucherSerial(bit48.substring(123, 153).trim());
            //Bill Reference   an-10  -Rata kiri -Padding with space -Blank on posting & reprint request
            vpv.setBillReference(bit48.substring(153, 163).trim());
            //Denom   n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setDenomination(new BigDecimal(bit48.substring(163, 175)).toString());
            //Harga Jual  n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setAmount(new BigDecimal(bit48.substring(175, 187)));
            //Fee   n12  -Rata kanan -Padding with zero -No Decimal
            vpv.setFee(new BigDecimal(bit48.substring(187, 199)));

            vpv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

}
