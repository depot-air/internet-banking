package com.dwidasa.engine.service.impl.view;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.HpPaymentView;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.HpPaymentViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.MoneyUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 25/07/11
 * Time: 11:43
 */
@Service("hpPaymentViewService")
public class HpPaymentViewServiceImpl extends PaymentViewServiceImpl implements HpPaymentViewService {
    @Autowired
    private LoggingService loggingService;
    
    private static Logger logger = Logger.getLogger( HpPaymentViewServiceImpl.class );

    public HpPaymentViewServiceImpl() {
        super();

        setInquiryMessageCustomizer(new InquiryMessageCustomizer());
        setTransactionMessageCustomizer(new TransactionMessageCustomizer());
        setReprintMessageCustomizer(new ReprintMessageCustomizer());
    }

    private class InquiryMessageCustomizer implements MessageCustomizer {
        private InquiryMessageCustomizer() {
        }

        private String setTranslationCode(String providerCode) {
            String translationCode = "";

            if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
                translationCode = "01712";
            }
            else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
                translationCode = "01802";
            }

            return translationCode;
        }

        public void compose(BaseView view, Transaction transaction) {
            HpPaymentView hv = (HpPaymentView) view;
            String phone[] = EngineUtils.splitPhoneNumber(hv.getCustomerReference());
            String customData = "";
            if (hv.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE) && hv.getProductCode().equals(Constants.POSTPAID_PRODCODE.TELKOMSEL_KATU_HALO)) {	//Telkomsel Halo via FINNET
            	transaction.setFreeData1(hv.getCustomerReference());
            	transaction.setFreeData2(hv.getCustomerReference());
            } else {
	            if (hv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
	                //-- industrial code(n2) + biller id(n3)
	                customData += Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + getSeraPaymentProductId(hv.getProductCode()); //"02" + hv.getProductCode();
	                //-- payee id (n4) right justified zero padding
	                customData += Constants.PAYEE_ID.SERA_FOR_ALL;
	            } else {
	                //-- industrial code(n2) + biller id(n3)
	                customData += "02" + hv.getProductCode();
	                //-- payee id (n4) right justified zero padding
	                customData += StringUtils.leftPad(hv.getProviderCode(), 4, '0');
	            }
	
	            //-- product id n(4) right justified zero padding
	            customData += "0001";
	            //-- country code an(2)
	            customData += "62";
	            //-- area / operator code an(4) right justified zero padding
	            customData += phone[0];
	            //-- phone number an(10) left justified space padding
	            customData += String.format("%1$-10s", phone[1]);
            
	            transaction.setFreeData1(customData);
            }
            transaction.setResponseCode(null);
            
            transaction.setTranslationCode(setTranslationCode(hv.getProviderCode()) + hv.getProviderCode() +
                "02" + hv.getProductCode() + "0001");

            if (hv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
                //transaction.setFreeData2("02" + getSeraPaymentProductId(hv.getProductCode()) + Constants.PAYEE_ID.SERA_BIT61 + "0001");

                //023 + process code + product code (payee ID + industry code + biller code + product ID)
                transaction.setTranslationCode("023" + Constants.TRANSLATION_PROCESS_CODE.SERA.POSTPAID_INQUIRY +
                    Constants.PAYEE_ID.SERA_FOR_ALL + Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + getSeraPaymentProductId(hv.getProductCode()) + "0001"
                );

                if (hv.getCardData1() != null && hv.getCardData2() != null) {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
                } else {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
                }
            }
        }

        public Boolean decompose(Object view, Transaction transaction) {
            HpPaymentView hv = (HpPaymentView) view;
            hv.setResponseCode(transaction.getResponseCode());

            String bit48 = transaction.getFreeData1();
            if (hv.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE) && hv.getProductCode().equals(Constants.POSTPAID_PRODCODE.TELKOMSEL_KATU_HALO)) {	//Telkomsel Halo via FINNET
            	//02003A0040001100002100000072001000000910746WARREN CUCURULLO_426                         
            	//13:R: :customer-id;
            	//6:L:0:bill-code;2 divre 3 kandatel
		        //1:L:0:total-bill;
		        hv.setDescription(bit48.substring(0, 20).trim());	//ditaruh di description untuk posting request
            	//11:R: :bill-ref;
		        hv.setReferenceNumber(bit48.substring(20, 31).trim());
            	//12:L:0:bill-amount;
		        hv.setAmount(new BigDecimal(bit48.substring(31, 43)));
		        hv.setTotal(hv.getAmount());	
            	//45:R: :customer-name
            	hv.setReferenceName(bit48.substring(43).trim());
            	
            	hv.setRef1(hv.getReferenceNumber());
                hv.setRef2("");
                hv.setRef3("");
            	hv.setAmount1(hv.getAmount());
                hv.setAmount2(BigDecimal.ZERO);
                hv.setAmount3(BigDecimal.ZERO);
                hv.setFee(BigDecimal.ZERO);
                hv.setNumOfBill(1);
            } else {
            	hv.setAmount(transaction.getTransactionAmount());
                hv.setFeeIndicator(transaction.getFeeIndicator());
                hv.setFee(transaction.getFee());
                hv.setReferenceName(bit48.substring(35, 65).trim());

                int base = 91;
                int len = 23;
                int numOfBill = Integer.valueOf(bit48.substring(82, 83));

                hv.setRef1(null);
                hv.setRef2(null);
                hv.setRef3(null);
                hv.setAmount1(null);
                hv.setAmount2(null);
                hv.setAmount3(null);
                hv.setNumOfBill(numOfBill);

                for (int i = 0; i < numOfBill; i++) {
                    switch (i) {
                        case 0 :
                            hv.setRef1(bit48.substring(base + i * len, base + i * len + 11).trim());
                            hv.setAmount1(new BigDecimal(bit48.substring(base + i * len + 11, base + i * len + 23)));
                            break;
                        case 1 :
                            hv.setRef2(bit48.substring(base + i * len, base + i * len + 11).trim());
                            hv.setAmount2(new BigDecimal(bit48.substring(base + i * len + 11, base + i * len + 23)));
                            break;
                        case 2 :
                            hv.setRef3(bit48.substring(base + i * len, base + i * len + 11).trim());
                            hv.setAmount3(new BigDecimal(bit48.substring(base + i * len + 11, base + i * len + 23)));
                    }
                }
                logger.info("hv.getAmount()=" + hv.getAmount() + " hv.getFee()=" + hv.getFee());
                if (hv.getAmount() == null) hv.setAmount(BigDecimal.ZERO);
                if (hv.getFee() == null) hv.setFee(BigDecimal.ZERO);

                hv.setTotal(hv.getAmount().add(hv.getFee()));	
            }
            
            hv.setBit48(transaction.getFreeData1());

            hv.setTraceNumber(transaction.getStanSixDigit());
            return Boolean.TRUE;
        }
    }

    private class TransactionMessageCustomizer implements MessageCustomizer {
        private TransactionMessageCustomizer() {
        }

        private String setTranslationCode(String providerCode) {
            String translationCode = "";

            if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
                translationCode = "01713";
            }
            else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
                translationCode = "01803";
            }

            return translationCode;
        }

        public void compose(BaseView view, Transaction transaction) {
            HpPaymentView hv = (HpPaymentView) view;
            transaction.setFreeData1(hv.getBit48());
            transaction.setTranslationCode(setTranslationCode(hv.getProviderCode()) + hv.getProviderCode() +
                "02" + hv.getProductCode() + "0001");
            if (hv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
                //transaction.setFreeData2("02" + getSeraPaymentProductId(hv.getProductCode()) + Constants.PAYEE_ID.SERA_BIT61 + "0001");

                //payee ID + industry code + biller code + product ID
                //biller code di URS = product code di DB
                //023 + process code + product code (payee ID + industry code + biller code + product ID)
                transaction.setTranslationCode("023" + Constants.TRANSLATION_PROCESS_CODE.SERA.POSTPAID_POSTING +
                    Constants.PAYEE_ID.SERA_FOR_ALL + Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + getSeraPaymentProductId(hv.getProductCode()) + "0001"
                );

                //Biller ID n5 Identitas dari Biller(*); rata kanan (right justified); zero leading padding.; See Section 5
                //Payee ID n4 Identitas dari Payment Gateway atau pemilik biller (*); rata kanan (right justified); zero leading padding.; See Section 5
                //Product ID n4 Identitas dari Produk(*); rata kanan (right justified); zero leading padding; See Section 5

                String customData = "";
                customData += Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + getSeraPaymentProductId(hv.getProductCode()); //"02" + hv.getProductCode();
                customData += Constants.PAYEE_ID.SERA_FOR_ALL;    //StringUtils.leftPad(hv.getProviderCode(), 4, '0');
                customData += "0001";
                customData += hv.getBit48().substring(13);
                transaction.setFreeData1(customData);
                transaction.setResponseCode(null);

                if (hv.getCardData1() != null && hv.getCardData2() != null) {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
                } else {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
                }
            }
        }

        public Boolean decompose(Object view, Transaction transaction) {
            HpPaymentView hv = (HpPaymentView) view;
            hv.setResponseCode(transaction.getResponseCode());
            hv.setReferenceNumber(transaction.getReferenceNumber());
            String bit48 = transaction.getFreeData1();
            if (hv.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE) && hv.getProductCode().equals(Constants.POSTPAID_PRODCODE.TELKOMSEL_KATU_HALO)) {	//Telkomsel Halo via FINNET
            	//02003A0040001100002100000072001000000910746WARREN CUCURULLO_426                         
            	//13:R: :customer-id;
            	//6:L:0:bill-code;2 divre 3 kandatel
		        //1:L:0:total-bill;
            	//11:R: :bill-ref;
		        hv.setReferenceNumber(bit48.substring(20, 31).trim());
            	//12:L:0:bill-amount;
		        hv.setAmount(new BigDecimal(bit48.substring(31, 43)));
            	//45:R: :customer-name
            	hv.setReferenceName(bit48.substring(43).trim());
            } 

            hv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(hv.getCustomerId(), Constants.TELCO_PAYMENT_CODE, "Bayar Pulsa, No HP = " + hv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(hv.getAmount()), hv.getReferenceNumber(), hv.getMerchantType(), hv.getTerminalId());
            return Boolean.TRUE;
        }
    }

    /**
     * Class to compose and decompose message for reprint phase
     */
    private class ReprintMessageCustomizer implements MessageCustomizer {
        private ReprintMessageCustomizer() {
        }

        private String setTranslationCode(String providerCode) {
            String translationCode = "";

            if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
                translationCode = "01714";
            }
            else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
                translationCode = "01804";
            }

            return translationCode;
        }

        public void compose(BaseView view, Transaction transaction) {
            HpPaymentView hv = (HpPaymentView) view;
            String phone[] = EngineUtils.splitPhoneNumber(hv.getCustomerReference());

            String customData = "";
            //-- industrial code(n2) + biller id(n3)
            //-- update by conf 11 Oct 2011
            customData += "02" + hv.getProductCode();
            //-- payee id (n4) right justified zero padding
            customData += StringUtils.leftPad(hv.getProviderCode(), 4, '0');
            //-- product id n(4) right justified zero padding
            customData += "0001";
            //-- country code an(2)
            customData += "62";
            //-- area / operator code an(4) right justified zero padding
            customData += phone[0];
            //-- phone number an(10) left justified space padding
            customData += String.format("%1$-10s", phone[1]);

            transaction.setFreeData1(customData);
            transaction.setTranslationCode(setTranslationCode(hv.getProviderCode()) + hv.getProviderCode() +
                "02" + hv.getProductCode() + "0001");

            logger.info("hv.getProviderCode()=" + hv.getProviderCode());
            if (hv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
                //transaction.setFreeData2("02" + HpPaymentViewServiceImpl.getSeraPaymentProductId(hv.getProductCode()) + Constants.PAYEE_ID.SERA_BIT61 + "0001");

                //023 + process code + product code (payee ID + industry code + biller code + product ID)
                transaction.setTranslationCode("023" + Constants.TRANSLATION_PROCESS_CODE.SERA.POSTPAID_REPRINT +
                    Constants.PAYEE_ID.SERA_FOR_ALL + Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + getSeraPaymentProductId(hv.getProductCode()) + "0001"
                );

                if (hv.getCardData1() != null && hv.getCardData2() != null) {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
                } else {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
                }
            }
        }

        public Boolean decompose(Object view, Transaction transaction) {
            HpPaymentView hv = (HpPaymentView) view;
            hv.setResponseCode(transaction.getResponseCode());
            hv.setReferenceNumber(transaction.getReferenceNumber());

            hv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(hv.getCustomerId(), Constants.TELCO_PAYMENT_REP_CODE, "Cek Status Bayar Pulsa, No HP = " + hv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(hv.getAmount()), hv.getReferenceNumber(), hv.getMerchantType(), hv.getTerminalId());
            return Boolean.TRUE;
        }
    }

    public static String getSeraPaymentProductId(String productCode) {

        if (productCode.equals(Constants.POSTPAID_PRODCODE.TELKOMSEL_KATU_HALO)) return "002";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.XL_PASCABAYAR)) return "004";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.MOBI8_FREN)) return "006";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.ESIA_PASCA)) return "008";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.FLEXI_PASCA)) return "0011";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.INDOSAT_STARONE_PASCA)) return "0012";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.AXIS_PASCA)) return "0015";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.SMARTFREN_PASCA)) return "0017";
        else if (productCode.equals(Constants.POSTPAID_PRODCODE.THREE_PASCA)) return "0019";

        return null;
    }

    public static String getProductIdSeraReverse(String seraProductCode) {

        if (seraProductCode.equals("002")) return Constants.POSTPAID_PRODCODE.TELKOMSEL_KATU_HALO;
        else if (seraProductCode.equals("004")) return Constants.POSTPAID_PRODCODE.XL_PASCABAYAR;
        else if (seraProductCode.equals("006")) return Constants.POSTPAID_PRODCODE.MOBI8_FREN;
        else if (seraProductCode.equals("008")) return Constants.POSTPAID_PRODCODE.ESIA_PASCA;
        else if (seraProductCode.equals("0011")) return Constants.POSTPAID_PRODCODE.FLEXI_PASCA;
        else if (seraProductCode.equals("0012")) return Constants.POSTPAID_PRODCODE.INDOSAT_STARONE_PASCA;
        else if (seraProductCode.equals("0015")) return Constants.POSTPAID_PRODCODE.AXIS_PASCA;
        else if (seraProductCode.equals("0017")) return Constants.POSTPAID_PRODCODE.SMARTFREN_PASCA;
        else if (seraProductCode.equals("0019")) return Constants.POSTPAID_PRODCODE.THREE_PASCA;
        return null;
    }
}
