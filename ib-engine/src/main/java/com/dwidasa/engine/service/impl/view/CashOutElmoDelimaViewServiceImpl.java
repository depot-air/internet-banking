package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.service.view.CashOutElmoDelimaViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 7/25/11
 * Time: 5:44 PM
 */
@Service("cashOutElmoDelimaViewService")
public class CashOutElmoDelimaViewServiceImpl implements CashOutElmoDelimaViewService {
    private final MessageCustomizer inquiryMessageCustomizer;
    private final MessageCustomizer transactionMessageCustomizer;
    private final MessageCustomizer reprintMessageCustomizer;

    public CashOutElmoDelimaViewServiceImpl() {
        inquiryMessageCustomizer = new InquiryMessageCustomizer();
        transactionMessageCustomizer = new TransactionMessageCustomizer();
        reprintMessageCustomizer = new ReprintMessageCustomizer();
    }

    /**
     * {@inheritDoc}
     */
    public void preProcess(BaseView view) {
        CashInDelimaView cv = (CashInDelimaView) view;
        cv.setToAccountType("00");
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validate(BaseView view) {
        Boolean result = view.validate();
        if (result) {
            //-- database validation if required
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public void composeInquiry(BaseView view, Transaction transaction) {
        inquiryMessageCustomizer.compose(view, transaction);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean decomposeInquiry(BaseView view, Transaction transaction) {
        return inquiryMessageCustomizer.decompose(view, transaction);
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
     * Class to compose and decompose message for inquiry phase
     */
    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            
            //25:id-card-no;
            //16:customer-id;
            //12:amount;
            //12:provider-fee;
            //12:referensi;            
            String customData = "";
            //-- receiver id an(25) left justified padding with space
            customData += String.format("%1$-25s", cv.getSenderIdNumber());
            //-- transfer code n(16) left justified padding with space
            customData += String.format("%1$-16s", cv.getSenderPhoneNumber());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", cv.getAmount().longValue());
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- reference an(12) left justified padding with space
            customData += String.format("%1$-12s", " ");

            //-- sender information
            //30:nama-pengirim;
            //1:jenis-kelamin1;
            //30:alamat1;
            //20:kota1;
            //10:kode-pos1;
            //16:negara1;
            //10:tipe-idcard1;
            //25:nomor-idcard1;
            //20:tempat-lahir1;
            //8:tanggal-lahir1;
            //15:nomer-telepon1;
            //-- name an(30) left justified padding with space
            customData += String.format("%1$-30s", cv.getSenderName());
            //-- gender a(1)
            customData += " ";
//            customData += cv.getSenderGender();
            //-- address an(30) left justified padding with space
            customData += String.format("%1$-30s", cv.getSenderAddress());
            //-- city an(20) left justified padding with space
            customData += String.format("%1$-20s", cv.getSenderCity());
            //-- postal code an(10) left justified padding with space
            customData += String.format("%1$-10s", (cv.getSenderPostalCode() != null) ? cv.getSenderPostalCode() : "" );
            //-- country an(16) left justified padding with space
            customData += String.format("%1$-16s", cv.getSenderCountry());
            //-- card type an(10) left justified padding with space
            customData += String.format("%1$-10s", (cv.getSenderCardType() != null) ? cv.getSenderCardType() : "" );
            //-- id card number an(25) left justified padding with space
            customData += String.format("%1$-25s", cv.getSenderIdNumber());
            //-- place of birth an(20) left justified padding with space
            customData += String.format("%1$-20s", cv.getSenderPob());
            //-- date of birth n(8)
            customData += sdf.format(cv.getSenderDob());
            //-- phone number an(15) left justified padding with space
            customData += String.format("%1$-15s", cv.getSenderPhoneNumber());

            //-- receiver information
            //30:nama-penerima;
            //1:jenis-kelamin2;
            //30:alamat2;
            //20:kota2;
            //10:kode-pos2;
            //16:negara2;
            //10:tipe-idcard2;
            //25:nomor-idcard2;
            //20:tempat-lahir2;
            //8:tanggal-lahir2;
            //15:nomer-telepon2
            //-- name an(30) left justified padding with space
            customData += String.format("%1$-30s", cv.getReceiverName());
            //-- gender a(1)
            customData += " ";
//            customData += cv.getReceiverGender();
            //-- address an(30) left justified padding with space
            customData += String.format("%1$-30s", cv.getReceiverAddress());
            //-- city an(20) left justified padding with space
            customData += String.format("%1$-20s", cv.getReceiverCity());
            //-- postal code an(10) left justified padding with space
            customData += String.format("%1$-10s", (cv.getReceiverPostalCode() != null) ? cv.getReceiverPostalCode() : "" );
            //-- country an(16) left justified padding with space
            customData += String.format("%1$-16s", cv.getReceiverCountry());
            //-- card type an(10) left justified padding with space
            customData += String.format("%1$-10s", cv.getReceiverCardType());
            //-- id card number an(25) left justified padding with space
            customData += String.format("%1$-25s", cv.getReceiverIdNumber());
            //-- place of birth an(20) left justified padding with space
            customData += String.format("%1$-20s", cv.getReceiverPob());
            //-- date of birth n(8)
            customData += sdf.format(cv.getReceiverDob());
//            customData += sdf.format(cv.getReceiverDob());
            //-- phone number an(15) left justified padding with space
            customData += String.format("%1$-15s", cv.getReceiverPhoneNumber());

            //-- admin charge n(12) right justified zero padding
            customData += String.format("%012d", 0);

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00205002013");
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	//25:R: :id-card-no;
            //16:R: :customer-id;
            //12:L:0:amount;
            //12:L:0:provider-fee;
            //12:R: :referensi;
        	
        	//-- sender information
            //30:R: :nama-pengirim;
            //1:R: :jenis-kelamin1;
        	//30:R: :alamat1;
        	//20:R: :kota1;
        	//10:R: :kode-pos1;
        	//16:R: :negara1;
        	//10:R: :tipe-idcard1;
        	//25:R: :nomor-idcard1;
        	//20:R: :tempat-lahir1;
        	//8:R: :tanggal-lahir1;
        	//15:R: :nomer-telepon1;
        	
        	//-- receiver information
        	//30:R: :nama-penerima;
            //1:R: :jenis-kelamin2;
        	//30:R: :alamat2;
        	//20:R: :kota2;
        	//10:R: :kode-pos2;
        	//16:R: :negara2;
        	//10:R: :tipe-idcard2;
        	//25:R: :nomor-idcard2;
        	//20:R: :tempat-lahir2;
        	//8:R: :tanggal-lahir2;
        	//15:R: :nomer-telepon2;
        	//12:L:0:admin-fee;
        	//1:L:0:reprint-counter
          
            CashInDelimaView cv = (CashInDelimaView) view;

            String bit48 = transaction.getFreeData1();
            cv.setCustomerReference(bit48.substring(25, 41).trim());
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setAmount(transaction.getTransactionAmount());
            cv.setFee(transaction.getFee());
            cv.setFeeIndicator(transaction.getFeeIndicator());
            cv.setBit48(bit48);

            cv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for execute phase
     */
    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        public void compose(BaseView view, Transaction transaction) {
        	//25:customer-id;
            //16:kode-transfer;
            //12:amount;
            //12:provider-fee;
            //12:referensi;
        	//12:admin-fee
            CashInDelimaView cv = (CashInDelimaView) view;
            
            String customData = "";
            //-- receiver id an(25) left justified padding with space
            customData += String.format("%1$-25s", cv.getSenderIdNumber());
            //-- transfer code n(16) left justified padding with space
            customData += String.format("%1$-16s", cv.getSenderPhoneNumber());
            //-- amount n(12) right justified zero padding
            customData += String.format("%012d", cv.getAmount().longValue());
            //-- provider fee n(12) right justified zero padding
            customData += String.format("%012d", 0);
            //-- reference an(12) left justified padding with space
            customData += String.format("%1$-12s", " ");
            customData += String.format("%012d", cv.getFee().longValue());
            
            transaction.setFreeData1(customData);	//cv.getBit48()
            transaction.setTranslationCode("00206002013");
        }

        public Boolean decompose(Object view, Transaction transaction) {
        	//25:R: :id-card-no;
            //16:R: :customer-id;
            //12:L:0:amount;
            //12:L:0:provider-fee;
            //12:R: :referensi;
        	
        	//-- sender information
            //30:R: :nama-pengirim;
            //1:R: :jenis-kelamin1;
        	//30:R: :alamat1;
        	//20:R: :kota1;
        	//10:R: :kode-pos1;
        	//16:R: :negara1;
        	//10:R: :tipe-idcard1;
        	//25:R: :nomor-idcard1;
        	//20:R: :tempat-lahir1;
        	//8:R: :tanggal-lahir1;
        	//15:R: :nomer-telepon1;
        	
        	//-- receiver information
        	//30:R: :nama-penerima;
            //1:R: :jenis-kelamin2;
        	//30:R: :alamat2;
        	//20:R: :kota2;
        	//10:R: :kode-pos2;
        	//16:R: :negara2;
        	//10:R: :tipe-idcard2;
        	//25:R: :nomor-idcard2;
        	//20:R: :tempat-lahir2;
        	//8:R: :tanggal-lahir2;
        	//15:R: :nomer-telepon2;
        	//12:L:0:admin-fee;
        	//1:L:0:reprint-counter
            CashInDelimaView cv = (CashInDelimaView) view;
            cv.setBillerReference(transaction.getFreeData1().substring(65, 77).trim());
            cv.setResponseCode(transaction.getResponseCode());
            cv.setReferenceNumber(transaction.getReferenceNumber());

            cv.setTraceNumber(transaction.getStanSixDigit());
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
            CashInDelimaView cv = (CashInDelimaView) view;

            String customData = "";
            //-- transfer code n(16) left justfied padding with space
            customData += String.format("%1$-16s", cv.getCustomerReference());

            transaction.setFreeData1(customData);
            transaction.setTranslationCode("00207002013");
        }

        public Boolean decompose(Object view, Transaction transaction) {
            CashInDelimaView cv = (CashInDelimaView) view;
            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");

            String bit48 = transaction.getFreeData1();
            cv.setProviderFee(new BigDecimal(bit48.substring(53, 65)));
            cv.setBillerReference(bit48.substring(65, 77).trim());

            //-- sender data
            cv.setSenderName(bit48.substring(77, 107).trim());
            cv.setSenderGender(bit48.substring(107, 108));
            cv.setSenderAddress(bit48.substring(108, 138).trim());
            cv.setSenderCity(bit48.substring(138, 158).trim());
            cv.setSenderPostalCode(bit48.substring(158, 168).trim());
            cv.setSenderCountry(bit48.substring(168, 184).trim());
            cv.setSenderCardType(bit48.substring(184, 194).trim());
            cv.setSenderIdNumber(bit48.substring(194, 219).trim());
            cv.setSenderPob(bit48.substring(219, 239).trim());
            try {
                cv.setSenderDob(sdf.parse(bit48.substring(239, 247)));
            } catch (ParseException e) {
                cv.setSenderDob(new Date());
            }
            cv.setSenderPhoneNumber(bit48.substring(247, 262));
            //-- receiver data
            cv.setReceiverName(bit48.substring(262, 292).trim());
            cv.setReceiverGender(bit48.substring(292, 293));
            cv.setReceiverAddress(bit48.substring(293, 323).trim());
            cv.setReceiverCity(bit48.substring(323, 343).trim());
            cv.setReceiverPostalCode(bit48.substring(343, 353).trim());
            cv.setReceiverCountry(bit48.substring(353, 369).trim());
            cv.setReceiverCardType(bit48.substring(369, 379).trim());
            cv.setReceiverIdNumber(bit48.substring(379, 404).trim());
            cv.setReceiverPob(bit48.substring(404, 424).trim().trim());
            try {
                cv.setReceiverDob(sdf.parse(bit48.substring(424, 432)));
            } catch (ParseException e) {
                cv.setReceiverDob(new Date());
            }
            cv.setReceiverPhoneNumber(bit48.substring(432, 447).trim());

            cv.setAmount(transaction.getTransactionAmount());
            cv.setFee(transaction.getFee());
            cv.setFeeIndicator(transaction.getFeeIndicator());

            cv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }
}
