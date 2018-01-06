package com.dwidasa.ib.pages.payment;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 20/10/11
 * Time: 16:21
 */
public class PlnPaymentPrint extends BasePrintPage {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());

    @InjectPage
    private PlnPaymentReceipt plnPaymentReceipt;

    @Property
    private PlnPaymentView plnPaymentView;

    @Property
    private String paidPeriods;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @ActivationRequestParameter
    private String reprint;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private PaymentService paymentService;

    @Property
    private String vNoteReceipt;
    
    @Property
    private String vGspRef;
    
    @Property
    private String vNote;
    
    void setupRender() {
        if (reprint != null) {
            plnPaymentView = (PlnPaymentView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
            plnPaymentView.setTransactionType(com.dwidasa.engine.Constants.PLN_PAYMENT_REP_CODE);
            paymentService.reprint(plnPaymentView, transactionId);
            return;
        }

        plnPaymentView = plnPaymentReceipt.getPlnPaymentView();
        paidPeriods = plnPaymentView.getPaidPeriods(threadLocale.getLocale());

        int nBill = Integer.parseInt(plnPaymentView.getBit48().substring(0, 1));
        int totBill = Integer.parseInt(plnPaymentView.getBit48().substring(2, 4));
        if (totBill > 4) {
            vNoteReceipt = "Anda masih memiliki tunggakan " + (totBill - nBill) + " bulan";
        }
        
        int totPengurangan = (totBill - nBill);
        if(totPengurangan > 0){
        	vNote = "Anda Masih Memiliki";
        }else{
        	vNote = "";
        }
        
        if (isGsp()) {
    		vGspRef = plnPaymentView.getGspRef();
    	} else {
            vGspRef = plnPaymentView.getBit48().substring(4, 36);    		
    	}
        
        
    }

    public boolean isGsp() {
    	return plnPaymentView.getProviderCode().equals(Constants.GSP.PROVIDER_CODE);
    }
    
    @Property
    private String _footer;

    public String[] getFooters()
    {
    	//informasiStruk mengandung \n, ganti dg <br>
        String[] words = plnPaymentView.getInformasiStruk().split("\n");        
        return words;
    }
}
