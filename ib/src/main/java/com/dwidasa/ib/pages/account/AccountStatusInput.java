package com.dwidasa.ib.pages.account;

import com.dwidasa.engine.model.CustomerAccount;
import com.dwidasa.engine.model.FormConfig;
import com.dwidasa.engine.model.view.ResultView;
import com.dwidasa.engine.model.view.TokenView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.facade.AccountService;
import com.dwidasa.engine.ui.GenericSelectModel;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: reza
 * Date: 8/11/11
 * Time: 00:43 am
 */
public class AccountStatusInput {
    @Property
    private TokenView tokenView;
    @Property
    private CustomerAccount customerAccount;
    @Inject
    private SessionManager sessionManager;
    @Inject
    private AccountService accountService;
    @Persist
    @Property
    private List<CustomerAccount> customerAccounts;
    @Property
    private int tokenType;
    @Inject
    private CacheManager cacheManager;
    @Persist
    private ResultView resultView;
    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;
    @Property
    private PkEncoder encoder;
    @InjectPage
    private AccountStatusReceipt accountStatusReceipt;
    @Inject
    private Messages messages;
    @Inject
    private OtpManager otpManager;
    @InjectComponent
    private Form form;
    @Persist
    private Map<String, Integer> customerAccountBeforeChanged;
    private Map<String, Integer> customerAccountChanged;


    private class PkEncoder implements ValueEncoder<CustomerAccount> {
        private final Map<Long, CustomerAccount> keyToValue = new HashMap<Long, CustomerAccount>();

        public String toClient(CustomerAccount value) {
            return String.valueOf(value.getId());
        }

        public CustomerAccount toValue(String clientValue) {
            List<CustomerAccount> cas = accountService.getRegisteredAccounts(sessionManager.getLoggedCustomerView().getId());
            Long id = Long.valueOf(clientValue);
            CustomerAccount result = null;
            for (CustomerAccount ca : cas) {
                if (ca.getId().equals(id)) {
                    result = ca;
                }
            }

            keyToValue.put(id, result);

            return result;
        }

        public final List<CustomerAccount> getAllValues() {
            List<CustomerAccount> result = CollectionFactory.newList();

            for (Map.Entry<Long, CustomerAccount> entry : keyToValue.entrySet()) {
                result.add(entry.getValue());
            }

            return result;
        }
    }

    ;
    @Property
    private SelectModel accountModel;

    void accountModel() {
        List<Status> stats = new ArrayList<Status>();
        stats.add(new Status("Aktif", "1"));
        stats.add(new Status("Non Aktif", "0"));

        customerAccounts = accountService.getRegisteredAccounts(sessionManager.getLoggedCustomerView().getId());
        accountModel = genericSelectModelFactory.create(stats);
        int size;
        customerAccountBeforeChanged = new HashMap<String, Integer>();
        size = customerAccounts.size();
        for (int w = 0; w < size; w++) {
            customerAccountBeforeChanged.put(customerAccounts.get(w).getAccountNumber(), customerAccounts.get(w).getStatus());
        }
    }

    void setupRender() {
        if (customerAccount == null) {
            customerAccount = new CustomerAccount();
        }
        setTokenType();
    }

    void onPrepare() {
        if (tokenView == null) {
            tokenView = new TokenView();
        }
        accountModel();
        encoder = new PkEncoder();
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }


    void onValidateFromForm() {
        if (tokenView.getToken() == null) {
            form.recordError(messages.get("tokenError"));
//        } else if (tokenView.getToken().length() != 8) {
//            form.recordError(messages.get("lengthTokenError"));
        } else if (!tokenView.getToken().matches("[0-9]+")) {
            form.recordError(messages.get("formatTokenError"));
        }
        if (!form.getHasErrors()) {
            if (otpManager.validateToken(sessionManager.getLoggedCustomerView().getId(), this.getClass().getSimpleName(), tokenView)) {
                customerAccounts = encoder.getAllValues();
                customerAccountChanged = new HashMap<String, Integer>();

                for (CustomerAccount account : customerAccounts) {
                    if (!customerAccountBeforeChanged.get(account.getAccountNumber()).equals(account.getStatus())) {
                        customerAccountChanged.put(account.getAccountNumber(), account.getStatus());
                    }
                }
                resultView = accountService.changeAccountStatus(sessionManager.getLoggedCustomerView().getId(), customerAccountChanged);
            }
        }
    }

    @DiscardAfter
    Object onSuccess() {

        accountStatusReceipt.setAccountNumber(new ArrayList<String>(customerAccountChanged.keySet()));
        accountStatusReceipt.setResultView(resultView);
        return accountStatusReceipt;

    }


    class Status implements GenericSelectModel {
        String label;
        String value;

        Status() {
        }

        Status(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel(Locale locale) {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }


    }

}
