package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.model.view.CashInDelimaView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.engine.util.EngineUtils;
import com.dwidasa.ib.Constants;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 10/9/11
 * Time: 00:38 am
 */
public class CashOutToBankDelimaPrint {
    @Inject
    private Locale locale;

    @Property
    private CashInDelimaView cashInDelimaView;

    @InjectPage
    private CashInDelimaReceipt cashInDelimaReceipt;

    @Property
    private String senderDob;

    @Property
    private String receiverDob;

    @Property
    private String mediumDate;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(locale);

    @ActivationRequestParameter
    private String reprint;

    @SessionAttribute(value = "reprintTransactionId")
    private Long transactionId;

    @Inject
    private TransactionDataService transactionDataService;

    @Inject
    private DelimaService delimaService;

    void setupRender() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.MEDIUM_FORMAT, locale);

        if (reprint != null) {
            cashInDelimaView = (CashInDelimaView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
            cashInDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHIN_DELIMA_REP_CODE);
            delimaService.reprint(cashInDelimaView, transactionId);

            senderDob = sdf.format(cashInDelimaView.getSenderDob());
            receiverDob = sdf.format(cashInDelimaView.getReceiverDob());
            mediumDate = sdf.format(cashInDelimaView.getTransactionDate());

            return;
        }

        cashInDelimaView = cashInDelimaReceipt.getCashInDelimaView();

        senderDob = sdf.format(cashInDelimaView.getSenderDob());
        mediumDate = sdf.format(cashInDelimaView.getTransactionDate());
    }
}
