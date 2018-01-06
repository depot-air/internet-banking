package com.dwidasa.ib.services.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.InternetPaymentView;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.model.view.TransferView;
import com.dwidasa.engine.model.view.VoucherGamePurchaseView;
import com.dwidasa.engine.model.view.VoucherPurchaseView;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.KioskCheckStatusService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.engine.util.ReferenceGenerator;
import com.dwidasa.ib.annotations.PublicPage;
import com.dwidasa.ib.annotations.SessionValidate;
import com.dwidasa.ib.services.PurchaseResource;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/8/11
 * Time: 3:49 PM
 */
@PublicPage
public class PurchaseResourceImpl implements PurchaseResource {
	
    @Override
	@SessionValidate
	@GET
	@Produces("application/json")
	@Path("inquiryVoucherGame")
	public String inquiryVoucherGame(@QueryParam("customerId") Long customerId,
			@QueryParam("sessionId") String sessionId,
			@QueryParam("json") String json) {
		return null;
	}
	private static Logger logger = Logger.getLogger(PurchaseResourceImpl.class);
	
	@Inject
    private PurchaseService purchaseService;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private KioskReprintService kioskReprintService;

    @Inject
    private KioskCheckStatusService kioskCheckStatusService;
    
    @Inject
    private CustomerRegisterService customerRegisterService;

    public PurchaseResourceImpl() {
    }

    public String saveCellular(Long customerId, String deviceId, String sessionId,
            String token, String json) {
        return saveCellularExtract(json);
    }

    public String saveCellular2(Long customerId, String deviceId, String sessionId,
            String token, String json) {
        return saveCellular(customerId, deviceId, sessionId, token, json);
    }


    public String saveCellularPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return saveCellularExtract(json);
    }

    public String saveCellularPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return saveCellularExtract(json);
    }

    private String saveCellularExtract(String json) {
        VoucherPurchaseView view = PojoJsonMapper.fromJson(json, VoucherPurchaseView.class);
        logger.info("view.getCardData2()=" + view.getCardData2() + " view.getCardNumber()="+ view.getCardNumber());
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        view.setTransactionType(Constants.VOUCHER_PURCHASE_CODE);
        view.setTransactionDate(new Date());
        purchaseService.confirm(view);
        purchaseService.execute(view);
        //XL Sera, no referensi kosong diganti "-"
        if (view.getReferenceNumber() == null || view.getReferenceNumber().equals("")) {
            view.setReferenceNumber("-");
        }
        
        if (view.getSave()) {
            purchaseService.register(view.transform());
        }

        return PojoJsonMapper.toJson(view);
    }

    
    public String reprintCellular(Long customerId, String sessionId, Long transactionId) {
        return reprintCellularExtract(transactionId);
    }

    public String checkTopupPost(Long customerId, String sessionId, String json) {
        VoucherPurchaseView vv = PojoJsonMapper.fromJson(json, VoucherPurchaseView.class);
        vv.setTransactionType(Constants.VOUCHER_PURCHASE_CHK_CODE);
        vv = (VoucherPurchaseView) kioskCheckStatusService.checkStatus(vv);
        return PojoJsonMapper.toJson(vv);
    }

    private String reprintCellularExtract(Long transactionId) {
        VoucherPurchaseView voucherPurchaseView = (VoucherPurchaseView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        voucherPurchaseView.setTransactionType(Constants.VOUCHER_PURCHASE_CHK_CODE);
        purchaseService.reprint(voucherPurchaseView, transactionId);
        if (voucherPurchaseView.getReferenceNumber() == null)
            voucherPurchaseView.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(voucherPurchaseView);
    }

    
    public String inquiryPln(Long customerId, String sessionId, String json) {
        return inquiryPlnExtract(json);
    }

    public String inquiryPlnPost(Long customerId, String sessionId, String json) {
        return inquiryPlnExtract(json);
    }

    private String inquiryPlnExtract(String json) {
        PlnPurchaseView view = PojoJsonMapper.fromJson(json, PlnPurchaseView.class);
        view.setTransactionType(Constants.PLN_PURCHASE_INQ_CODE);
        view.setTransactionDate(new Date());
        view = (PlnPurchaseView) purchaseService.inquiry(view);
        return PojoJsonMapper.toJson(view);
    }

    public String reprintPln(Long customerId, String sessionId, Long transactionId) {
        return reprintPlnExtract(transactionId);
    }

    public String reprintPlnPost(Long customerId, String sessionId, String jsonParam) {
        PlnPurchaseView pv = PojoJsonMapper.fromJson(jsonParam, PlnPurchaseView.class);
        String messageError = "Pembelian Listrik PLN no " + pv.getCustomerReference() + " nominal " + pv.getDenomination() + " tidak ditemukan";
        try
        {
        	TransactionData transactionData = transactionDataService.getForPlnReprint(pv.getDenomination(), pv.getAccountNumber(), pv.getCustomerReference());
        	logger.info("transactionData=" + transactionData);
        	if (transactionData == null) {
            	String params[] = {"IB-1009", messageError};
                throw new BusinessException("IB-1009", params);  //RCxx, pembelian tidak ditemukan, krn reprint butuh query ke DB lokal
            } else {
            	logger.info("transactionData.getTransactionData()=" + transactionData.getTransactionData());

				pv = jsonToPojo(transactionData);
        		if (pv.getResponseCode().equals(Constants.SUCCESS_CODE)) {
        			return PojoJsonMapper.toJson(pv);
        		} else {
        			pv = (PlnPurchaseView) kioskReprintService.reprint(pv);
        		}
            }
        }
        catch (Exception ex)
        {
        	String params[] = {"IB-1009",messageError};
            throw new BusinessException("IB-1009", params);  //RCxx, pembelian tidak ditemukan, krn reprint butuh query ke DB lokal
        }
        
        return PojoJsonMapper.toJson(pv);
    }

	private PlnPurchaseView jsonToPojo(TransactionData transactionData) {
		String json = transactionData.getTransactionData();
		
		logger.info("json=" +json);
		//remove {}
		json = json.substring(1);
		json = json.substring(0, json.length() -1);
		json = json.replaceAll("\n", "");
		
		String newJson = "";
		String[] parsed = json.split(",");
		for (int i = 0; i < parsed.length; i++) {
			if (parsed[i].contains("kwh")) {
				if (!parsed[i+1].contains(":")) {
					parsed[i] = parsed[i] + "." + parsed[i+1];
				}
			}
			if (parsed[i].contains(":")) {
				newJson += parsed[i] + ",";
			}
		}
		newJson = "{" + newJson.substring(0, newJson.length() - 1) + "}";
		logger.info("newJson=" + newJson);
		PlnPurchaseView pv = PojoJsonMapper.fromJson(newJson, PlnPurchaseView.class);
		logger.info("pv.getInformasiStruk()=" + pv.getInformasiStruk());
//		if (pv.getInformasiStruk().contains("Atau")) {
//			pv.setInformasiStruk(pv.getInformasiStruk().replace("Atau", "\nAtau\n"));
//		}
//		if (pv.getInformasiStruk().contains("ATAU")) {
//			pv.setInformasiStruk(pv.getInformasiStruk().replace("Atau", "\nATAU\n"));
//		}
		logger.info("pv.getInformasiStruk()=" + pv.getInformasiStruk());
		return pv;
	}

    private String reprintPlnExtract(Long transactionId) {
        PlnPurchaseView plnPurchaseView = (PlnPurchaseView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        plnPurchaseView.setTransactionType(Constants.PLN_PURCHASE_REP_CODE);
        purchaseService.reprint(plnPurchaseView, transactionId);

        return PojoJsonMapper.toJson(plnPurchaseView);
    }

    public String savePln(Long customerId, String deviceId, String sessionId, String token, String json) {
        return savePlnExtract(json);
    }

    public String savePln2(Long customerId, String deviceId, String sessionId, String token, String json) {
        return savePln(customerId, deviceId, sessionId, token, json);
    }


    public String savePlnPost(Long customerId, String deviceId, String sessionId, String token, String json) {
        return savePlnExtract(json);
    }

    public String savePlnPostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
        return savePlnExtract(json);
    }

    private String savePlnExtract(String json) {
        PlnPurchaseView view = PojoJsonMapper.fromJson(json, PlnPurchaseView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        view.setTransactionType(Constants.PLN_PURCHASE_CODE);
        purchaseService.confirm(view);
        purchaseService.execute(view);

        if (view.getSave()) {
            purchaseService.register(view.transform());
        }

        return PojoJsonMapper.toJson(view);
    }

    public String getRegisteredPurchase(Long customerId, String sessionId, String transactionType, String billerCode) {
        List<CustomerRegister> crs = purchaseService.getRegisters(customerId, transactionType, billerCode);
        return PojoJsonMapper.toJson(crs);
    }

    public String getRegisteredPurchasePost(Long customerId, String sessionId, String transactionType, String billerCode) {
        List<CustomerRegister> crs = purchaseService.getRegisters(customerId, transactionType, billerCode);
        return PojoJsonMapper.toJson(crs);
    }

    public String getCustomerRegisters(Long customerId, String sessionId, String transactionType) {
        List<CustomerRegister> customerRegisters = customerRegisterService.getByTransactionType(customerId, transactionType);
        return PojoJsonMapper.toJson(customerRegisters);
    }

	public String voucherGame(Long customerId, String deviceId, String sessionId, String token, String json) {
		return voucherGameExtract(json);
	}
	
	public String voucherGame2(Long customerId, String deviceId, String sessionId, String token, String json) {
		return voucherGameExtract(json);
	}
	
	public String voucherGamePost(Long customerId, String deviceId, String sessionId, String token, String json) {
		return voucherGameExtract(json);
	}

	public String voucherGamePostPin(Long customerId, String deviceId, String sessionId, String pin, String json) {
		return voucherGameExtract(json);
	}

    private String voucherGameExtract(String json) {
    	VoucherPurchaseView view = PojoJsonMapper.fromJson(json, VoucherPurchaseView.class);
        if (view.getCardData2() != null) view.setCardData2(EngineUtils.getEncryptedPin(view.getCardData2(), view.getCardNumber()));
        view.setTransactionType(Constants.VOUCHER_GAME.TRANS_TYPE_POSTING);
        view.setTransactionDate(new Date());
        purchaseService.confirm(view);
        purchaseService.execute(view);
        if (view.getReferenceNumber() == null || view.getReferenceNumber().equals("")) {
            view.setReferenceNumber("-");
        }
        return PojoJsonMapper.toJson(view);    	        
    }

	public String checkStatusVoucherGame(Long customerId, String sessionId, Long transactionId) {
		return checkStatusVoucherGame(transactionId);
	}

	public String checkStatusVoucherGamePost(Long customerId, String sessionId, String json) {
		VoucherGamePurchaseView vv = PojoJsonMapper.fromJson(json, VoucherGamePurchaseView.class);
        vv.setTransactionType(Constants.VOUCHER_GAME.TRANS_TYPE_CHECKSTATUS);
        vv = (VoucherGamePurchaseView) kioskCheckStatusService.checkStatus(vv);
        return PojoJsonMapper.toJson(vv);
	}

    private String checkStatusVoucherGame(Long transactionId) {
        VoucherGamePurchaseView voucherGamePurchaseView = (VoucherGamePurchaseView) EngineUtils.deserialize(
                transactionDataService.getByTransactionFk(transactionId));
        voucherGamePurchaseView.setTransactionType(Constants.VOUCHER_GAME.TRANS_TYPE_CHECKSTATUS);
        purchaseService.reprint(voucherGamePurchaseView, transactionId);
        if (voucherGamePurchaseView.getReferenceNumber() == null)
        	voucherGamePurchaseView.setReferenceNumber(ReferenceGenerator.generate());
        return PojoJsonMapper.toJson(voucherGamePurchaseView);
    }

	public String inquiryVoucherGamePost(Long customerId, String sessionId, String json) {
		// TODO Auto-generated method stub
		return null;
	}

	public String checkStatusVoucherGame(Long customerId, String sessionId, String json) {
		// TODO Auto-generated method stub
		return null;
	}

}
