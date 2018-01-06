package com.dwidasa.ib.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.CustomerRegister;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.ui.GenericSelectModel;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.FormSupport;

import com.dwidasa.engine.model.AeroPassenger;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.Constants;
import com.dwidasa.ib.services.SessionManager;

/**
 * Created by IntelliJ IDEA.
 * User: emil
 * Date: 08/07/11
 * Time: 14:03
 */
public class AeroPassengerInput {
    @Parameter
    @Property
    private AeroPassenger aeroPassenger;

    @Parameter
    @Property
    private String passengerNo;

    @Property
    private String hiddenCustReg;

    @Property
    private List<String> titleModel;

    @Property
    private List<String> countryModel;

    @Property
    private SelectModel customerReferenceModel;

    @Inject
    private GenericSelectModelFactory genericSelectModelFactory;

    @Inject
    private PaymentService paymentService;

    @Inject
    private SessionManager sessionManager;

    @Inject
    private FormSupport formSupport;

    @Inject
   	private ThreadLocale threadLocale;

    @Persist
    List<CustomerRegister> customerRegisters;

    public String getDateFieldFormat() {
        return Constants.SHORT_FORMAT;
    }

    public Object beginRender(MarkupWriter writer) {
    	titleModel = getTitles();
    	countryModel = ListUtils.getCountries();
        buildCustomerReferenceModel();
    	return true;
    }

    private void buildCustomerReferenceModel() {
        customerRegisters = paymentService.getRegisters(sessionManager.getLoggedCustomerView().getId(),
                            AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, AirConstants.VOLTRAS.PROVIDER.CODE);

                hiddenCustReg = PojoJsonMapper.toJson(customerRegisters);
                GenericSelectModel object = new GenericSelectModel() {
                    @Override
                    public String getLabel(Locale locale) {
                        return "Pilih Penumpang";
                    }

                    @Override
                    public String getValue() {
                        return "";
                    }
                };

                final List<OptionModel> options = CollectionFactory.newList();

                if (customerRegisters != null) {
                    options.add(new OptionModelImpl(object.getLabel(threadLocale.getLocale()), object.getValue()));
                    for (GenericSelectModel obj : customerRegisters) {
                        options.add(new OptionModelImpl(obj.getLabel(threadLocale.getLocale()), obj.getValue()));
                    }
                }
                customerReferenceModel = new SelectModelImpl(null, options);
    }

	public List<String> getTitles() {
		List<String> countries = new ArrayList<String>();
        countries.add("Mr");     
        countries.add("Mrs");     
        countries.add("Ms");
        return countries;
    }
    public boolean isAdult() {
    	return aeroPassenger.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT);
    }
    public boolean isChild() {
    	return aeroPassenger.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD);
    }
    public boolean isInfant() {
    	return aeroPassenger.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.INFANT);
    }
}
