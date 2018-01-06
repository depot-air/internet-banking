package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.dao.CellularPrefixDao;
import com.dwidasa.engine.dao.ProviderProductDao;
import com.dwidasa.engine.model.ProviderDenomination;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.ServiceLocator;
import com.dwidasa.engine.service.facade.LoggingService;
import com.dwidasa.engine.service.view.MessageCustomizer;
import com.dwidasa.engine.service.view.VoucherPurchaseViewService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.MoneyUtils;

/**
 * Service implementation specific to purchase voucher feature. Don't used
 * this service directly on client page, use PurchaseService instead.
 *
 * @author rk
 */
@Service("voucherPurchaseViewService")
public class VoucherPurchaseViewServiceImpl implements VoucherPurchaseViewService {

    @Autowired
    private ExtendedProperty extendedProperty;
    
    @Autowired
    private LoggingService loggingService;
    
    private static Logger logger = Logger.getLogger( VoucherPurchaseViewServiceImpl.class );
    private MessageCustomizer transactionMessageCustomizer;
    private MessageCustomizer reprintMessageCustomizer;

    @Autowired
    private ProviderProductDao providerProductDao;

    @Autowired
    private CellularPrefixDao cellularPrefixDao;
    
    public VoucherPurchaseViewServiceImpl() {
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

        private String setTranslationCode(String providerCode) {
            String translationCode = "";

            if (providerCode.equals(Constants.INDOSMART_CODE)) {
                translationCode = "00101";
            }
            else if (providerCode.equals(Constants.MKN_CODE)) {
                translationCode = "00401";
            }
            else if (providerCode.equals(Constants.AKSES_NUSANTARA_CODE)) {
                translationCode = "01702";
            }
            else if (providerCode.equals(Constants.AKSES_CIPTA_CODE)) {
                translationCode = "01800";
            }

            return translationCode;
        }

        /**
         * {@inheritDoc}
         */
        public void compose(BaseView view, Transaction transaction) {

            CacheManager cacheManager = (CacheManager) ServiceLocator.getService("cacheManager");

            VoucherPurchaseView vpv = (VoucherPurchaseView) view;
            String[] phone = EngineUtils.splitPhoneNumber(vpv.getCustomerReference());

            String prodId = String.valueOf(Long.valueOf(vpv.getDenomination()) / 1000);
            transaction.setTranslationCode(setTranslationCode(vpv.getProviderCode()) + vpv.getProviderCode() +
                "02" + vpv.getProductCode() + StringUtils.leftPad(prodId, 4, "0"));

            // set amount with server data
//            BigDecimal amount = cacheManager.getProviderDenomination(vpv.getTransactionType(), vpv.getBillerCode(), vpv.getProductCode(), vpv.getDenomination(), vpv.getProviderCode()).getPrice();
            if (vpv.getAmount() == null || vpv.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
	            ProviderDenomination pv = cacheManager.getProviderDenomination(vpv.getTransactionType(), vpv.getBillerCode(), vpv.getProductCode(), vpv.getDenomination(), vpv.getProviderCode()); 
	            BigDecimal amount = pv.getPrice();
	            vpv.setAmount(amount);
            }

            BigDecimal denom = new BigDecimal(vpv.getDenomination());
            
            if (denom.compareTo(vpv.getAmount()) >= 0) {
                vpv.setFeeIndicator("C");
                vpv.setFee(denom.subtract(vpv.getAmount()));
            }
            else {
                vpv.setFeeIndicator("D");
                vpv.setFee(vpv.getAmount().subtract(denom));
            }
            transaction.setFeeIndicator(vpv.getFeeIndicator());
            transaction.setFee(vpv.getFee());

            if (vpv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
                //biller Id n5 (industry code 02+ biller Code) + payee Id n4 + product Id n4
//                transaction.setFreeData2("02" + getSeraPurchaseBillerId(vpv.getProductCode()) + Constants.PAYEE_ID.SERA_BIT61 + getSeraPurchaseProductId(vpv.getProductCode(), vpv.getDenomination()));
                //payee ID + industry code + biller code + product ID
                //023 + process code + product code (payee ID + industry code + biller code + product ID)
                transaction.setTranslationCode("023" + Constants.TRANSLATION_PROCESS_CODE.SERA.PREPAID_POSTING +
                    Constants.PAYEE_ID.SERA_FOR_ALL + Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + getSeraPurchaseBillerId(vpv.getProductCode()) + getSeraPurchaseProductId(vpv.getProductCode(), vpv.getDenomination())
                );
                if (vpv.getCardData1() != null && vpv.getCardData2() != null) {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
                } else {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
                }
            } else if (vpv.getProviderCode().equals(Constants.AKSES_NUSANTARA_CODE)) {	//AN
            	transaction.setBit22("000");
            }
            transaction.setTransactionAmount(denom);
            
            if (vpv.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE) && vpv.getProductCode().equals(Constants.PREPAID_PRODCODE.FINNET_TELKOMSEL)) {	//FINNET Telkomsel
            	transaction.setFreeData1(vpv.getCustomerReference());
            	transaction.setFreeData2(vpv.getCustomerReference());
//            	List<CellularPrefix> cellularPrefixes = cellularPrefixDao.getByPrefix(vpv.getCustomerReference().substring(0, 4));
//            	for (CellularPrefix cellularPrefix : cellularPrefixes) {
//                	String prodCode = cellularPrefix.getBillerProduct().getProductCode();
//                	logger.info("prodCode=" + prodCode);
//					if (prodCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) || prodCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE)) {
//						vpv.setProductCode(prodCode);
//						transaction.setProductCode(prodCode);
//					}
//				}
            	String firstFour = vpv.getCustomerReference().substring(0, 4);
            	if (firstFour.equals("0811") || firstFour.equals("0812") || firstFour.equals("0813") || firstFour.equals("0821") || firstFour.equals("0822") || firstFour.equals("0828")) {
            		vpv.setProductCode(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE);
					transaction.setProductCode(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE);
            	} else if (firstFour.equals("0243") || firstFour.equals("0823") || firstFour.equals("0852") || firstFour.equals("0853")) {
            		vpv.setProductCode(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE);
					transaction.setProductCode(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE);
            	}
            	
            	transaction.setFromAccountType("10");
            } else if (vpv.getProviderCode().equals(Constants.PROVIDER_ARTAJASA_CODE) ) {	//ARTAJASA
            	transaction.setFreeData1(vpv.getCustomerReference());
            	
            } else if(vpv.getProviderCode().equals("ISB")){
            	
            	String customData = StringUtils.rightPad(vpv.getCustomerReference(), 13, "");
            	customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");
            	System.out.println("Ip Server Impl "+vpv.getIp());//
            	transaction.setBit42(vpv.getIp());
            	transaction.setFreeData1(customData);
            	
            }
            else {
	            String customData = "";
	            //-- country code(an2)
	            customData += "62";
	            //-- area / operator code(an4)
	            customData += phone[0];
	            //-- phone number(an10)
	            customData += String.format("%1$-10s", phone[1]);
	            //-- voucher nominal(n12) right justified zero padding
	            customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");
	            //-- fee indicator(a1)
	            customData += vpv.getFeeIndicator();
	            //-- fee amount(n12)
	            customData += String.format("%012d", vpv.getFee().longValue());
	
	            transaction.setFreeData1(customData);
            }
            
            if (extendedProperty.isMigration() && vpv.getProviderCode() != null && vpv.getProviderCode().equals(Constants.MKN_CODE)) {
            	transaction.setFromAccountType("00");
            }
        }

        /**
         * {@inheritDoc}
         */
        public Boolean decompose(Object view, Transaction transaction) {
            VoucherPurchaseView vpv = (VoucherPurchaseView) view;
            vpv.setResponseCode(transaction.getResponseCode());
            vpv.setReferenceNumber(transaction.getReferenceNumber());

            if (vpv.getProviderCode().equals(Constants.PROVIDER_ARTAJASA_CODE) ) {	//ARTAJASA
            	vpv.setSerialNumber("");            	
            } else {
            	if (vpv.getProviderCode().equals(Constants.PROVIDER_FINNET_CODE) ) {	//FINNET Telkomsel
    				vpv.setProductCode(Constants.PREPAID_PRODCODE.FINNET_TELKOMSEL);
    				transaction.setProductCode(Constants.PREPAID_PRODCODE.FINNET_TELKOMSEL);
    				vpv.setSerialNumber(transaction.getFreeData1().substring(36));
                }
            	else if(vpv.getProviderCode().equals("ISB")){
            		vpv.setSerialNumber(transaction.getFreeData1().substring(33));
            	}
            	else {
                	vpv.setSerialNumber(transaction.getFreeData1().substring(36, 52));
                }
            	//-- default serial number returned by biller in the case of timeout
                if (vpv.getSerialNumber().equals("2222222222222222") || Constants.TIMEOUT_CODE.equals(transaction.getResponseCode())) {
                    transaction.setStatus(Constants.PENDING_STATUS);
                    transaction.setResponseCode(Constants.TIMEOUT_CODE);
                }
                else {
					if (vpv.getProviderCode().equals("ISB")) {

						SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
						String dateTrans = transaction.getFreeData1()
								.substring(33, 41);
						
						
						try {
							vpv.setWindowPeriod(sdf.parse(dateTrans));
						} catch (ParseException e) {
							vpv.setWindowPeriod(new Date());
						}
						
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
						String dateTrans = transaction.getFreeData1()
								.substring(28, 36);
						try {
							vpv.setWindowPeriod(sdf.parse(dateTrans));
						} catch (ParseException e) {
							vpv.setWindowPeriod(new Date());
						}
					}
                }
            }
            transaction.setTransactionAmount(vpv.getAmount());
            
            
            vpv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(vpv.getCustomerId(), Constants.VOUCHER_PURCHASE_CODE, "Beli Pulsa, No HP = " + vpv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(vpv.getAmount()), vpv.getReferenceNumber(), vpv.getMerchantType(), vpv.getTerminalId());
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

            String customData = "";
            //-- country code(an2)
            customData += "62";
            //-- area / operator code(an4)
            customData += phone[0];
            //-- phone number(an10)
            customData += String.format("%1$-10s", phone[1]);
            //-- voucher nominal(n12) right justified zero padding
            customData += StringUtils.leftPad(vpv.getDenomination(), 12, "0");
            //-- fee indicator(a1)
            customData += vpv.getFeeIndicator();
            //-- fee amount(n12)
            customData += String.format("%012d", vpv.getFee().longValue());

            transaction.setFreeData1(customData);
            transaction.setTransactionAmount(new BigDecimal(vpv.getDenomination()));

            String prodId = String.valueOf(Long.valueOf(vpv.getDenomination()) / 1000);
            transaction.setTranslationCode(setTranslationCode(vpv.getProviderCode()) + vpv.getProviderCode() +
                "02" + vpv.getProductCode() + StringUtils.leftPad(prodId, 4, "0"));

            logger.info("vpv.getProviderCode()=" + vpv.getProviderCode());
	        if (vpv.getProviderCode().equals(Constants.PAYEE_ID.SERA)) {
//                transaction.setFreeData2("02" + VoucherPurchaseViewServiceImpl.getSeraPurchaseBillerId(vpv.getProductCode()) + Constants.PAYEE_ID.SERA_BIT61 + VoucherPurchaseViewServiceImpl.getSeraPurchaseProductId(vpv.getProductCode(), vpv.getDenomination()));
                //payee ID + industry code + biller code + product ID
                //biller code di URS = product code di DB
                //023 + process code + product code (payee ID + industry code + biller code + product ID)
                transaction.setTranslationCode("023" + Constants.TRANSLATION_PROCESS_CODE.SERA.PREPAID_CEK_STATUS +
                    Constants.PAYEE_ID.SERA_FOR_ALL + Constants.INDUSTRY_CODE.TELCO_PREPAID_POSTPAID + getSeraPurchaseBillerId(vpv.getProductCode()) + VoucherPurchaseViewServiceImpl.getSeraPurchaseProductId(vpv.getProductCode(), vpv.getDenomination())
                );

                if (vpv.getCardData1() != null && vpv.getCardData2() != null) {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.MAGNETIC_STRIPE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.PIN_ENTRY_CAPABILITY);
                } else {
                    transaction.setBit22(Constants.PO_SERVICE_ENTRY_MODE.PAN_EXPIRATION_DATE.UNKNOWN_OR_NOT_APPLICABLE + Constants.PO_SERVICE_ENTRY_MODE.PIN_ENTRY_CAPABILITY.UNKNOWN);
                }
            } else {
                transaction.setFreeData2(customData);
            }
        }

        public Boolean decompose(Object view, Transaction transaction) {
            VoucherPurchaseView vpv = (VoucherPurchaseView) view;
            vpv.setResponseCode(transaction.getResponseCode());
            vpv.setReferenceNumber(transaction.getReferenceNumber());

            vpv.setSerialNumber(transaction.getFreeData1().substring(36, 52));
            transaction.setTransactionAmount(vpv.getAmount());

            if (transaction.getFreeData1().charAt(52) == '0') {
                transaction.setResponseCode("05");
                transaction.setStatus(Constants.FAILED_STATUS);
            }
            else if (vpv.getSerialNumber().equals("2222222222222222") ||
                    transaction.getFreeData1().charAt(52) == '2') {

                transaction.setStatus(Constants.PENDING_STATUS);
                transaction.setResponseCode(Constants.TIMEOUT_CODE);
            }
            else {
                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                try {
                    vpv.setWindowPeriod(sdf.parse(transaction.getFreeData1().substring(28, 36)));
                } catch (ParseException e) {
                    vpv.setWindowPeriod(new Date());
                }
                transaction.setStatus(Constants.SUCCEED_STATUS);
            }

            vpv.setTraceNumber(transaction.getStanSixDigit());
            loggingService.logActivity(vpv.getCustomerId(), Constants.VOUCHER_PURCHASE_CODE, "Cek Status Beli Pulsa, No HP = " + vpv.getCustomerReference() + " sebesar " + MoneyUtils.getMoney(vpv.getAmount()), vpv.getReferenceNumber(), vpv.getMerchantType(), vpv.getTerminalId());
            return Boolean.TRUE;
        }
    }

    public static String getSeraPurchaseBillerId(String productCode) {
        if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) || productCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE)) return "001";
        else  return "003";
    }
    //untuk bit 61
    public static String getSeraPurchaseProductId(String productCode, String denomination) {
        if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_50K)) return "0001";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_100K)) return "0002";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_50K)) return "0003";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_100K)) return "0004";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_5K)) return "0005";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_10K)) return "0006";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_20K)) return "0007";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_5K)) return "0008";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_10K)) return "0009";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_20K)) return "0010";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.KARTUAS_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_25K)) return "0011";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_25K)) return "0012";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.SIMPATI_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_150K)) return "0013";

        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_REGULAR_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_25K)) return "0001";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_REGULAR_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_50K)) return "0002";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_REGULAR_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_100K)) return "0003";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_XTRA_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_50K)) return "0004";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_XTRA_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_100K)) return "0005";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_REGULAR_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_200K)) return "0006";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_REGULAR_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_5K)) return "0007";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_REGULAR_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_10K)) return "0008";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_XTRA_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_10K)) return "0009";
        else if (productCode.equals(Constants.PREPAID_PRODCODE.XL_V_XTRA_PRODCODE) && denomination.equals(Constants.DENOMINATION.VOUCHER_5K)) return "0010";

        return null;
    }

}
