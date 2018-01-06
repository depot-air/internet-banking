package com.dwidasa.admin.pages.treasury;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import com.dwidasa.admin.Constants;
import com.dwidasa.admin.view.TreasuryStageView;
import com.dwidasa.engine.model.view.PlnPaymentView;
import com.dwidasa.engine.service.TransactionDataService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.util.EngineUtils;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 20/10/11
 * Time: 16:21
 */
public class TreasuryStagePrint {
    @Inject
    private ThreadLocale threadLocale;

    @Property
    private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());

    @Property
    private DateFormat longDate = new SimpleDateFormat(Constants.LONG_FORMAT, threadLocale.getLocale());
    
    @InjectPage
    private TreasuryStageDetail treasuryStageDetail;

    @Property
    private TreasuryStageView treasuryStageView;

    @Property
    private String vNoteReceipt;
    
    void setupRender() {
        treasuryStageView = treasuryStageDetail.getTreasuryStageView();
    }

    @Property
    private String _footer;

	public String getDashIfEmpty(String str) {
		if (str !=  null && str.trim().length() > 0) {
			return str;
		}
		return "-";
    }
}
