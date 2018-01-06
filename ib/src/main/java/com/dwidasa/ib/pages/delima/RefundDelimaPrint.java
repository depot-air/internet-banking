package com.dwidasa.ib.pages.delima;

import com.dwidasa.engine.Constants;
import com.dwidasa.engine.model.view.RefundDelimaView;
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
 * Date: 10/12/11
 * Time: 00:47 am
 */
public class RefundDelimaPrint {
    @Inject
    private Locale locale;

    @Property
    private RefundDelimaView refundDelimaView;

    @InjectPage
    private RefundDelimaReceipt refundDelimaReceipt;

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
            refundDelimaView = (RefundDelimaView) EngineUtils.deserialize(
                    transactionDataService.getByTransactionFk(transactionId));
            refundDelimaView.setTransactionType(Constants.REFUND_DELIMA_REP_CODE);
            delimaService.reprint(refundDelimaView, transactionId);

            return;
        }

        refundDelimaView = refundDelimaReceipt.getRefundDelimaView();
    }
}
