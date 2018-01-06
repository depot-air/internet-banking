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

import com.dwidasa.engine.model.view.CcPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.base.BasePrintPage;

public class CcPaymentPrint extends BasePrintPage {
	@Inject
	private ThreadLocale threadLocale;

	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

	@Property
	private DateFormat shortDate = new SimpleDateFormat("MMyy",threadLocale.getLocale());

	@Property
	private DateFormat mediumDate = new SimpleDateFormat(Constants.MEDIUM_FORMAT, threadLocale.getLocale());

	@InjectPage
	private CcPaymentReceipt ccPaymentReceipt;

	@Property
	private CcPaymentView ccPaymentView;

	@SessionAttribute(value = "reprintTransactionId")
	private Long transactionId;

	@ActivationRequestParameter
	private String reprint;

	@Inject
	private TransactionDataService transactionDataService;

	@Inject
	private PaymentService paymentService;

	void setupRender() {
		ccPaymentView = ccPaymentReceipt.getCcPaymentView();
	}
}
