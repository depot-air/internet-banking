package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.TransactionData;
import com.dwidasa.engine.model.view.PlnPurchaseView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.TransactionService;
import com.dwidasa.engine.service.facade.KioskReprintService;
import com.dwidasa.engine.service.facade.PurchaseService;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 12/10/11
 * Time: 19:38
 */
public class PlnPurchasePrint extends BasePrintPage {
	private Logger logger = Logger.getLogger(PlnPurchasePrint.class);
    @Inject
    private Locale locale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @Property
    private DateFormat shortDate = new SimpleDateFormat("MMMM yyyy", locale);

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);

    @InjectPage
    private PlnPurchaseReceipt plnPurchaseReceipt;

    @InjectComponent
    private Form form;
    
    @Property
    private PlnPurchaseView plnPurchaseView;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @ActivationRequestParameter
    private String reprint;

    @Inject
    private TransactionService transactionService;
    
    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private PurchaseService purchaseService;

    @Inject
    private KioskReprintService kioskReprintService;
    
    @Property
    private String vTokenNumber;


    private String getToken(String token) {
    	String vToken = token.substring(0, 4) + "  " + token.substring(4, 8) + "  " +  token.substring(8, 12) + "  " + token.substring(12, 16);
        if(token.length() >= 20)
            vToken = vToken + "  " + token.substring(16, 20);
        return vToken;
    }
    void setupRender() {    	
        if (reprint != null) {
        	form.clearErrors();
        	TransactionData transactionData = transactionDataService.getByTransactionFk(transactionId);
            plnPurchaseView = (PlnPurchaseView) EngineUtils.deserialize(transactionData);
            plnPurchaseView.setTransactionType(com.dwidasa.engine.Constants.PLN_PURCHASE_REP_CODE);
            //purchaseService.reprint(plnPurchaseView, transactionId);
            try {
            	purchaseService.reprint(plnPurchaseView, transactionId);
            	//plnPurchaseView = (PlnPurchaseView) kioskReprintService.reprint(plnPurchaseView);
            } catch (BusinessException e) {            	
                form.recordError(e.getFullMessage());
                return;
            } catch (Exception e) {            	
                form.recordError(Constants.EXCEPTION_OVERRIDE_MESSAGE);
                return;
            }		
            logger.info("after reprint, account=" + plnPurchaseView.getCustomerReference());
            
            //update ke table history transaksi
            plnPurchaseView.setTransactionType(com.dwidasa.engine.Constants.PLN_PURCHASE_CODE);
            transactionData.setTransactionData(PojoJsonMapper.toJson(plnPurchaseView));
            Transaction transaction = TransformerFactory.getTransformer(plnPurchaseView).transformTo(plnPurchaseView, new Transaction());            
            transactionData.setTransaction(transaction);
            logger.info("before update transaction data, transactionId=" + transactionData.getTransactionId());
            transactionDataService.save(transactionData);
            
            Transaction trans = transactionService.get(transactionId);
            trans.setStatus(com.dwidasa.engine.Constants.SUCCEED_STATUS);
            logger.info("before update transaction, status=" + trans.getStatus());
            transactionService.save(trans);            
            
            vTokenNumber = getToken(plnPurchaseView.getTokenNumber());
            
            return;
        }

        plnPurchaseView = plnPurchaseReceipt.getPlnPurchaseView();

        vTokenNumber = getToken(plnPurchaseView.getTokenNumber());
        
    }

    public boolean isGsp() {
    	return plnPurchaseView.getProviderCode().equals(Constants.GSP.PROVIDER_CODE);
    }

    @Property
    private String _footer;

    public String[] getFooters()
    {
        String[] words = plnPurchaseView.getInformasiStruk().split("\n");        
        return words;
    }

    public boolean isSuccess() {
    	return plnPurchaseView != null && plnPurchaseView.getInformasiStruk() != null;
    }
    
    public String getcurrencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n).replace("$", "");
    }
}
