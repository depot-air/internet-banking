package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.model.view.CashOutDelimaView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.DelimaService;
import com.dwidasa.engine.util.EngineUtils;
import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 10/10/11
 * Time: 00:36 am
 */
public class CashOutElmoDelimaPrint {
    @Inject
    private Locale locale;

    @Property
    private CashOutDelimaView cashOutDelimaView;

    @InjectPage
    private CashOutDelimaReceipt cashOutDelimaReceipt;

    @Property
    private String nominalTransfer;

    @Property
    private String biayaTransfer;

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
        if (reprint != null) {
            cashOutDelimaView = (CashOutDelimaView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
            cashOutDelimaView.setTransactionType(com.dwidasa.engine.Constants.CASHOUT_DELIMA_REP_CODE);
            delimaService.reprint(cashOutDelimaView, transactionId);

            return;
        }

        cashOutDelimaView = cashOutDelimaReceipt.getCashOutDelimaView();
    }
}
