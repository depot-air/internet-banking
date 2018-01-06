package com.dwidasa.ib.pages.payment;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.DetilMultiFinance;
import com.dwidasa.engine.model.view.MultiFinancePaymentView;
import com.dwidasa.engine.model.view.TiketKeretaDjatiPurchaseView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.model.view.TrainPaymentView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.common.EvenOdd;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.pages.purchase.TiketKeretaDjatiPurchaseConfirm;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 10/08/11
 * Time: 22:43
 */
public class MultiFinanceMncPaymentConfirm {
    @Property
    @Persist
    private MultiFinancePaymentView MultiFinancePaymentView;
    
    @Property
    @Persist
    private DetilMultiFinance detilMultiFinance;
    
    @Persist
    private List<DetilMultiFinance> financePaymentViews;

    @Property
    private TokenView tokenView;

    @InjectComponent
    private Form form;

    @Inject
    private OtpManager otpManager;

    @Inject
    private PaymentService paymentService;

    @Inject
    private CacheManager cacheManager;

    @Property
    private int tokenType;

    @InjectPage
    private MultiFinanceMncPaymentReceipt multiFinancePaymentReceipt;
    
    @Inject
    private ThreadLocale threadLocale;

    @Inject
    private SessionManager sessionManager;

    @Property
    private EvenOdd evenOdd;
    
    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
    
    @Persist
    private String noKartu, noRekening, nomorKontrak, namaPelanggan, deskripsi;
    
    @Persist
    private int jmlTagihan;
    
    @Persist
    private String bit48;
    
    @Property
    private DateFormat longDate = new SimpleDateFormat(com.dwidasa.ib.Constants.LONG_FORMAT,
            threadLocale.getLocale());

    public Object onActivate(){
    	if (sessionManager.isNotActivatedYet()) {
			return EulaWelcome.class;
		}
		return null;
    }

    public void onPrepare() {
    	evenOdd = new EvenOdd();
        evenOdd.setEven(true);
    	sessionManager.setSessionLastPage(MultiFinanceMncPaymentConfirm.class.toString());
		if (tokenView == null) {
            tokenView = new TokenView();
        }
    }

    public void setupRender() {
    	setTokenType();
    }


	private void setTokenType() {
    	tokenType = sessionManager.getTokenType();
    }

    public void setTrainPaymentView(MultiFinancePaymentView trainPaymentView) {
        this.MultiFinancePaymentView = trainPaymentView;
    }
    
    public String getNoKartu() {
		return noKartu;
	}
    
    public void setNoKartu(String noKartu) {
		this.noKartu = noKartu;
	}
    
    public String getNoRekening() {
		return noRekening;
	}
    
    public void setNoRekening(String noRekening) {
		this.noRekening = noRekening;
	}
    
    public String getNomorKontrak() {
		return nomorKontrak;
	}
    
    public void setNomorKontrak(String nomorKontrak) {
		this.nomorKontrak = nomorKontrak;
	}
    
    public String getNamaPelanggan() {
		return namaPelanggan;
	}
    
    public void setNamaPelanggan(String namaPelanggan) {
		this.namaPelanggan = namaPelanggan;
	}
    
    
    public String getDeskripsi() {
		return deskripsi;
	}
    
    public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
    
    public int getJmlTagihan() {
		return jmlTagihan;
	}
    
    public void setJmlTagihan(int jmlTagihan) {
		this.jmlTagihan = jmlTagihan;
	}
    
    
    public String getBit48() {
		return bit48;
	}
    
    public void setBit48(String bit48) {
		this.bit48 = bit48;
	}
    
    public void setFinancePaymentViews(
			List<DetilMultiFinance> financePaymentViews) {
		this.financePaymentViews = financePaymentViews;
	}
    
    public List<DetilMultiFinance> getFinancePaymentViews() {
		return financePaymentViews;
	}
    

    void onValidateFromForm() {
        try {
            if (otpManager.validateToken(sessionManager.getLoggedCustomerView().getId(), this.getClass().getSimpleName(),
                    tokenView)) {
            	setPaymentView();
            	MultiFinancePaymentView.setTransactionType(Constants.MULTIFINANCE_NEW.PAYMENT_MULTI_FINANCE_MNC);
            	
                paymentService.execute(MultiFinancePaymentView);
            }
        } catch (BusinessException e) {
            form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }
    
    
    private void setPaymentView() {
    	MultiFinancePaymentView.setCardNumber(getNoKartu());
    	MultiFinancePaymentView.setTransactionDate(new Date());
    	MultiFinancePaymentView.setCustomerId(sessionManager.getLoggedCustomerView().getId());
    	MultiFinancePaymentView.setAccountType(sessionManager.getLoggedCustomerView().getAccountType());
    	MultiFinancePaymentView.setBillerCode("");
    	MultiFinancePaymentView.setBillerName("");
    	MultiFinancePaymentView.setProductCode("");
    	MultiFinancePaymentView.setProductName("");
    	MultiFinancePaymentView.setMerchantType(sessionManager.getDefaultMerchantType());
    	MultiFinancePaymentView.setTerminalId(sessionManager.getLoggedCustomerView().getTerminalId());
    	MultiFinancePaymentView.setCurrencyCode(com.dwidasa.ib.Constants.CURRENCY_CODE);
    	MultiFinancePaymentView.setInputType("M");
    	MultiFinancePaymentView.setToAccountType("00");
    	MultiFinancePaymentView.setProviderCode(Constants.PROVIDER_FINNET_CODE);
    	MultiFinancePaymentView.setBit48(getBit48());
    	MultiFinancePaymentView.setAccountNumber(getNoRekening());
    	BigDecimal totalAmount = BigDecimal.ZERO;
    	for(DetilMultiFinance financePaymentView : financePaymentViews){
    		BigDecimal sum = new BigDecimal("0.00");
            totalAmount = sum.add(financePaymentView.getMinimalPembayaran());
    	}
    	MultiFinancePaymentView.setAmount(totalAmount);
    }

    @DiscardAfter
    public Object onSuccess() {
    	multiFinancePaymentReceipt.setMultiFinancePaymentView(MultiFinancePaymentView);
        return multiFinancePaymentReceipt;
    }
    
    @Property
	private final ValueEncoder<DetilMultiFinance> encoder = new ValueEncoder<DetilMultiFinance>() {
		public String toClient(DetilMultiFinance value) {
			return String.valueOf(value.getAngsuranKe());
		}

		public DetilMultiFinance toValue(String clientValue) {
			return new DetilMultiFinance();
		}
	};
    
    public String getMasked(String str) {
     	return EngineUtils.getCardNumberMasked(str);
     }
    
    public String getFormatTgl(Date dt){
    	SimpleDateFormat format = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_FORMAT);
    	return format.format(dt).toString();
    }

}
