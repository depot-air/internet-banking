package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.view.AccountView;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.service.view.MessageCustomizer;

/**
 * Created by IntelliJ IDEA.
 * User: IBaihaqi
 * Date: 3/28/12
 * Time: 7:01 PM
 */
public class IGatePurposeMessageCustomizer {
    private static Logger logger = Logger.getLogger( IGatePurposeMessageCustomizer.class );
    private static IGatePurposeMessageCustomizer _instance = new IGatePurposeMessageCustomizer();
	private Map<String, MessageCustomizer> customizerDict = new HashMap<String, MessageCustomizer>();

	private IGatePurposeMessageCustomizer() {
        customizerDict.put(AccountView.class.getSimpleName(), new SmsTokenMessageCustomizer());
	}

	public static IGatePurposeMessageCustomizer instance() {
		return _instance;
	}

	public MessageCustomizer getCheckStatusMessageCustomizer(BaseView view) {
		return customizerDict.get(view.getClass().getSimpleName());
	}

    private static class SmsTokenMessageCustomizer implements MessageCustomizer{
		private SmsTokenMessageCustomizer() {}

        public void compose(BaseView view, Transaction transaction) {
        	AccountView  av = (AccountView) view;

        	logger.info("SmsTokenMessageCustomizer av.getCardNumber()=" + av.getCardNumber() );
        	String customData = "";
            //No HP an-20 -Rata kiri -Padding with space
            customData += StringUtils.rightPad(av.getCardNumber(), 20, " ");
            //token an-10 -Rata kiri -Padding with space
            customData += StringUtils.rightPad(av.getCustomerName(), 10, " ");
        	transaction.setFreeData1(customData);
        }

        public Boolean decompose(Object view, Transaction transaction) {
			AccountView av = (AccountView) view;
            return Boolean.TRUE;
        }
    }
}