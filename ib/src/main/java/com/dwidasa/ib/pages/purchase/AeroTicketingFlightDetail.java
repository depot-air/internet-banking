package com.dwidasa.ib.pages.purchase;

import com.dwidasa.engine.AirConstants;
import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.ExtendedProperty;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.*;
import com.dwidasa.engine.model.view.AeroFlightView;
import com.dwidasa.engine.model.view.AeroTicketingView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.CustomerRegisterService;
import com.dwidasa.engine.service.facade.PaymentService;
import com.dwidasa.engine.service.view.AeroTicketingPriceDetailViewService;
import com.dwidasa.engine.service.view.AeroTicketingSearchBookingViewService;
import com.dwidasa.engine.service.view.AeroTicketingSearchDetailViewService;
import com.dwidasa.engine.ui.GenericSelectModel;
import com.dwidasa.engine.util.DateUtils;
import com.dwidasa.engine.util.ListUtils;
import com.dwidasa.ib.pages.eula.EulaWelcome;
import com.dwidasa.ib.services.GenericSelectModelFactory;
import com.dwidasa.ib.services.OtpManager;
import com.dwidasa.ib.services.SessionManager;
import org.apache.log4j.Logger;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ThreadLocale;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//@Import(library = {"context:layout/javascript/jquery-1.6.2.min.js"})
public class AeroTicketingFlightDetail {
	private static Logger logger = Logger.getLogger( AeroTicketingFlightDetail.class );
    @Persist
    private AeroTicketingView aeroTicketingView;

    @Persist @Property
    private AeroCustomer contactPerson;

	@Inject
	private ThreadLocale threadLocale;

    @Property
    private DateFormat dd_mm_yyyy = new SimpleDateFormat(com.dwidasa.ib.Constants.SHORT_FORMAT, threadLocale.getLocale());
    @Property
    private DateFormat hhmm = new SimpleDateFormat(com.dwidasa.ib.Constants.DATEFORMAT.HH_MM, threadLocale.getLocale());
    @Property
    private DateFormat mediumFormat = new SimpleDateFormat(com.dwidasa.ib.Constants.MEDIUM_FORMAT, threadLocale.getLocale());
    
    @InjectPage
    private AeroTicketingFlightInsurance aeroTicketingFlightInsurance;

    @Property
    private String transactionType;

    @Property
    private boolean saveBoxValue;
    
    @Property
    private int tokenType;

    @Persist @Property private String sPass1Dob;
    @Persist @Property private String sPass2Dob;
    @Persist @Property private String sPass3Dob;
    @Persist @Property private String sPass4Dob;
    @Persist @Property private String sPass5Dob;
    @Persist @Property private String sPass6Dob;
    @Persist @Property private String sPass7Dob;

    @Persist @Property private boolean saveBoxPass1;
    @Persist @Property private boolean saveBoxPass2;
    @Persist @Property private boolean saveBoxPass3;
    @Persist @Property private boolean saveBoxPass4;
    @Persist @Property private boolean saveBoxPass5;
    @Persist @Property private boolean saveBoxPass6;
    @Persist @Property private boolean saveBoxPass7;

	@Property
	private NumberFormat formatter = NumberFormat.getNumberInstance(threadLocale.getLocale());

    @Persist @Property private AeroPassenger aeroPassenger1;
    @Persist @Property private AeroPassenger aeroPassenger2;
    @Persist @Property private AeroPassenger aeroPassenger3;
    @Persist @Property private AeroPassenger aeroPassenger4;
    @Persist @Property private AeroPassenger aeroPassenger5;
    @Persist @Property private AeroPassenger aeroPassenger6;
    @Persist @Property private AeroPassenger aeroPassenger7;

    @Property private String hiddenAccount;
    @Property private String hiddenContacts;
    @Property private String hiddenCustRegAdult, hiddenCustRegChild, hiddenCustRegInfant;
    @Property @Persist private String hiddenAeroTicketingView;
    @Property private String hiddenCountries;
    @Property private String hiddenContactPerson, hiddenAeroPassenger1,hiddenAeroPassenger2,hiddenAeroPassenger3,hiddenAeroPassenger4,hiddenAeroPassenger5,hiddenAeroPassenger6,hiddenAeroPassenger7;


    @Property @Persist private List<String> titleModel;
    @Property @Persist private List<String> titleChildModel;
    @Property @Persist private List<String> countryModel;
    @Property @Persist private List<String> adultAssocModel;

    @Property @Persist private SelectModel customerReferenceModel;

    @Inject private GenericSelectModelFactory genericSelectModelFactory;

    @Inject private CacheManager cacheManager;

    @Inject private SessionManager sessionManager;

    @Inject private OtpManager otpManager;

    @InjectComponent private Form form;

    @Inject private Messages message;

    @Inject private CustomerRegisterService customerRegisterService;

    @Inject private ExtendedProperty extendedProperty;

    List<CustomerRegister> customerRegisters;

    public String getDateFieldFormat() {
        return com.dwidasa.ib.Constants.SHORT_FORMAT;
    }

    public Object onActivate() {
    	if (sessionManager.isNotActivatedYet()) {
    		return EulaWelcome.class;
    	}
    	return null;
    }

    public void onPrepare() {
      
    }

    void setTokenType() {
        FormConfig formConfig = cacheManager.getFormConfig(this.getClass().getSimpleName());
        tokenType = formConfig.getTokenType();
    }

    public void setupRender() {
        if (aeroTicketingView.getReturnFlight() != null) {
            logger.info("aeroTicketingView.returnFlight.arrivalAirportFullName" +
                aeroTicketingView.getReturnFlight().getArrivalAirportFullName());
        }
        aeroPassenger1 = null;
        aeroPassenger2 = null;
        aeroPassenger3 = null;
        aeroPassenger4 = null;
        aeroPassenger5 = null;
        aeroPassenger6 = null;
        aeroPassenger7 = null;
    	sessionManager.setSessionLastPage(AeroTicketingFlightDetail.class.toString());

        titleModel = getTitles();
        titleChildModel = getChildTitles();
        countryModel = ListUtils.getCountries();
        hiddenCountries = PojoJsonMapper.toJson(countryModel);
        adultAssocModel = getAdultAssocs();
        buildCustomerReferenceModel();
	    setPassenger();
        hiddenAccount = PojoJsonMapper.toJson(sessionManager.getLoggedCustomerView());
        hiddenAeroTicketingView = PojoJsonMapper.toJson(aeroTicketingView);

        hiddenAeroPassenger1 = PojoJsonMapper.toJson(aeroPassenger1);
        hiddenAeroPassenger2 = PojoJsonMapper.toJson(aeroPassenger2); hiddenAeroPassenger5 = PojoJsonMapper.toJson(aeroPassenger5);
        hiddenAeroPassenger3 = PojoJsonMapper.toJson(aeroPassenger3); hiddenAeroPassenger6 = PojoJsonMapper.toJson(aeroPassenger6);
        hiddenAeroPassenger4 = PojoJsonMapper.toJson(aeroPassenger4); hiddenAeroPassenger7 = PojoJsonMapper.toJson(aeroPassenger7);

        if (aeroTicketingView.getAeroCustomer() == null) {
            contactPerson = new AeroCustomer();
        } else {
            contactPerson = aeroTicketingView.getAeroCustomer();
        }
        hiddenContactPerson = PojoJsonMapper.toJson(contactPerson);
        if (aeroTicketingView.getReturnFlight() != null) {
            logger.info("aeroTicketingView.returnFlight.arrivalAirportFullName" +
                aeroTicketingView.getReturnFlight().getArrivalAirportFullName());
        }
    }

    public List<String> getTitles() {
   		List<String> titles = new ArrayList<String>();
           titles.add("Mr");
           titles.add("Mrs");
           titles.add("Ms");
           return titles;
       }

    public List<String> getChildTitles() {
        List<String> titles = new ArrayList<String>();
        if (isVoltras() ) {
            titles.add("Mstr");
            titles.add("Miss");
        } else {
            titles.add("CHD");
        }
        return titles;
    }

    public List<String> getAdultAssocs() {
   		List<String> adultAssocs = new ArrayList<String>();
        for (int i = 0; i < aeroTicketingView.getTotalAdult(); i++)
            adultAssocs.add("Adult Assoc." + (i+1) );
        return adultAssocs;
    }


    private void buildCustomerReferenceModel() {
        List<CustomerRegister> crsContacts = new ArrayList<CustomerRegister>();
        List<CustomerRegister> crsPassengersAdult = new ArrayList<CustomerRegister>();
        List<CustomerRegister> crsPassengersChild = new ArrayList<CustomerRegister>();
        List<CustomerRegister> crsPassengersInfant = new ArrayList<CustomerRegister>();

        customerRegisters = customerRegisterService.getAeroPassangers(sessionManager.getLoggedCustomerView().getId(),
            AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, "Contact");
        if (customerRegisters.size() > 10) { customerRegisters.remove(customerRegisters.size() - 1); }
        for (CustomerRegister cr : customerRegisters) { crsContacts.add(cr); }

        customerRegisters = customerRegisterService.getAeroPassangers(sessionManager.getLoggedCustomerView().getId(),
            AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, AeroPassenger.PASSENGER_TYPE.ADULT);
        if (customerRegisters.size() > 10) { customerRegisters.remove(customerRegisters.size() - 1); }
        for (CustomerRegister cr : customerRegisters) { crsPassengersAdult.add(cr); }

        customerRegisters = customerRegisterService.getAeroPassangers(sessionManager.getLoggedCustomerView().getId(),
            AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, AeroPassenger.PASSENGER_TYPE.CHILD);
        if (customerRegisters.size() > 10) { customerRegisters.remove(customerRegisters.size() - 1); }
        for (CustomerRegister cr : customerRegisters) { crsPassengersChild.add(cr); }

        customerRegisters = customerRegisterService.getAeroPassangers(sessionManager.getLoggedCustomerView().getId(),
            AirConstants.AEROTICKETING.TRANSACTION_TYPE.ISSUE, AeroPassenger.PASSENGER_TYPE.INFANT);
        if (customerRegisters.size() > 10) { customerRegisters.remove(customerRegisters.size() - 1); }
        for (CustomerRegister cr : customerRegisters) { crsPassengersInfant.add(cr); }

        hiddenContacts = PojoJsonMapper.toJson(crsContacts);
        hiddenCustRegAdult = PojoJsonMapper.toJson(crsPassengersAdult);
        hiddenCustRegChild = PojoJsonMapper.toJson(crsPassengersChild);
        hiddenCustRegInfant = PojoJsonMapper.toJson(crsPassengersInfant);

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

    }

    void onValidateFromForm() {
        aeroTicketingView = PojoJsonMapper.fromJson(hiddenAeroTicketingView, AeroTicketingView.class);
        Date threeMonths = DateUtils.getDateMonthsBefore(-3);
        Date twoYears = DateUtils.getDateMonthsBefore(-2 * 12);
        Date twelveYears = DateUtils.getDateMonthsBefore(-12 * 12);
        if (sPass1Dob == null || sPass1Dob.equals("") || aeroPassenger1.getPassengerFirstName() == null || aeroPassenger1.getPassengerLastName() == null
            || aeroPassenger1.getPassengerIdCard() == null) {
            form.recordError("Mohon melengkapi data Penumpang 1");
        } else {
            try { aeroPassenger1.setPassengerDob(dd_mm_yyyy.parse(sPass1Dob));
            } catch (ParseException e) { }
        }

        System.out.println("aeroPassenger1.getPassengerDob()=" + aeroPassenger1.getPassengerDob());
        System.out.println("twelveYears=" + twelveYears);
        if (DateUtils.compareOnlyDate(aeroPassenger1.getPassengerDob(), twelveYears) > 0)
            form.recordError("Penumpang 1 Harus Berusia Diatas 12 Tahun");
        if (isTwoPassengers()) {
            if (sPass2Dob == null || sPass2Dob.equals("") || aeroPassenger2.getPassengerFirstName() == null || aeroPassenger2.getPassengerLastName() == null ||
                    (isPassanger2Adult() && aeroPassenger2.getPassengerIdCard() == null) ||
                    (!isPassanger2Infant() && (aeroPassenger2.getPassengerTitle() == null || aeroPassenger2.getPassengerTitle().equals("")))) {
                form.recordError("Mohon melengkapi data Penumpang 2");
            } else {
                try { aeroPassenger2.setPassengerDob(dd_mm_yyyy.parse(sPass2Dob));
                } catch (ParseException e) { }
                if (isPassanger2Adult()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger2.getPassengerDob(), twelveYears) > 0) form.recordError("Penumpang 2 Harus Berusia Diatas 12 Tahun");
                } else if (isPassanger2Child()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger2.getPassengerDob(), twoYears) > 0 || DateUtils.compareOnlyDate(aeroPassenger2.getPassengerDob(), twelveYears) < 0)
                        form.recordError("Penumpang 2 Harus Berusia Diantara 2 s.d 12 Tahun");
                } else {
                    if (DateUtils.compareOnlyDate(aeroPassenger2.getPassengerDob(), threeMonths) > 0 || DateUtils.compareOnlyDate(aeroPassenger2.getPassengerDob(), twoYears) < 0)
                        form.recordError("Penumpang 2 Harus Berusia Diantara 3 Bulan s.d 24 Bulan");
                    if (isVoltras()) {  //untuk infant, nilai adult association adalah angka nya saja
                        String[] adultAssocs = aeroPassenger2.getParent().split("\\.");
                        aeroPassenger2.setParent(adultAssocs[1]);
                    }
                }
            }
        }
        if (isThreePassengers()) {
            if (sPass3Dob == null || sPass3Dob.equals("") || aeroPassenger3.getPassengerFirstName() == null || aeroPassenger3.getPassengerLastName() == null ||
                    (isPassanger3Adult() && aeroPassenger3.getPassengerIdCard() == null) ||
                    (!isPassanger3Infant() && (aeroPassenger3.getPassengerTitle() == null || aeroPassenger3.getPassengerTitle().equals("")))) {
                form.recordError("Mohon melengkapi data Penumpang 3");
            } else {
                try { aeroPassenger3.setPassengerDob(dd_mm_yyyy.parse(sPass3Dob));
                } catch (ParseException e) { }
                if (isPassanger3Adult()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger3.getPassengerDob(), twelveYears) > 0) form.recordError("Penumpang 3 Harus Berusia Diatas 12 Tahun");
                } else if (isPassanger3Child()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger3.getPassengerDob(), twoYears) > 0 || DateUtils.compareOnlyDate(aeroPassenger3.getPassengerDob(), twelveYears) < 0)
                        form.recordError("Penumpang 3 Harus Berusia Diantara 2 s.d 12 Tahun");
                } else {
                    if (DateUtils.compareOnlyDate(aeroPassenger3.getPassengerDob(), threeMonths) > 0 || DateUtils.compareOnlyDate(aeroPassenger3.getPassengerDob(), twoYears) < 0)
                        form.recordError("Penumpang 3 Harus Berusia Diantara 3 Bulan s.d 24 Bulan");
                    if (isVoltras()) {  //untuk infant, nilai adult association adalah angka nya saja
                        String[] adultAssocs = aeroPassenger3.getParent().split("\\.");
                        aeroPassenger3.setParent(adultAssocs[1]);
                    }
                }
            }
        }
        if (isFourPassengers()) {
            if (sPass4Dob == null || sPass4Dob.equals("") || aeroPassenger4.getPassengerFirstName() == null || aeroPassenger4.getPassengerLastName() == null ||
                    (isPassanger4Adult() && aeroPassenger4.getPassengerIdCard() == null) ||
                    (!isPassanger4Infant() && (aeroPassenger4.getPassengerTitle() == null || aeroPassenger4.getPassengerTitle().equals("")))) {
                form.recordError("Mohon melengkapi data Penumpang 4");
            } else {
                try { aeroPassenger4.setPassengerDob(dd_mm_yyyy.parse(sPass4Dob));
                } catch (ParseException e) { }
                if (isPassanger4Adult()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger4.getPassengerDob(), twelveYears) > 0) form.recordError("Penumpang 4 Harus Berusia Diatas 12 Tahun");
                } else if (isPassanger4Child()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger4.getPassengerDob(), twoYears) > 0 || DateUtils.compareOnlyDate(aeroPassenger4.getPassengerDob(), twelveYears) < 0)
                        form.recordError("Penumpang 4 Harus Berusia Diantara 2 s.d 12 Tahun");
                } else {
                    if (DateUtils.compareOnlyDate(aeroPassenger4.getPassengerDob(), threeMonths) > 0 || DateUtils.compareOnlyDate(aeroPassenger4.getPassengerDob(), twoYears) < 0)
                        form.recordError("Penumpang 4 Harus Berusia Diantara 3 Bulan s.d 24 Bulan");
                    if (isVoltras()) {  //untuk infant, nilai adult association adalah angka nya saja
                        String[] adultAssocs = aeroPassenger4.getParent().split("\\.");
                        aeroPassenger4.setParent(adultAssocs[1]);
                    }
                }
            }
        }
        if (isFivePassengers()) {
            if (sPass5Dob == null || sPass5Dob.equals("") || aeroPassenger5.getPassengerFirstName() == null || aeroPassenger5.getPassengerLastName() == null ||
                    (isPassanger5Adult() && aeroPassenger5.getPassengerIdCard() == null) ||
                    (!isPassanger5Infant() && (aeroPassenger5.getPassengerTitle() == null || aeroPassenger5.getPassengerTitle().equals("")))) {
                form.recordError("Mohon melengkapi data Penumpang 5");
            } else {
                try { aeroPassenger5.setPassengerDob(dd_mm_yyyy.parse(sPass5Dob));
                } catch (ParseException e) { }
                if (isPassanger5Adult()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger5.getPassengerDob(), twelveYears) > 0) form.recordError("Penumpang 5 Harus Berusia Diatas 12 Tahun");
                } else if (isPassanger5Child()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger5.getPassengerDob(), twoYears) > 0 || DateUtils.compareOnlyDate(aeroPassenger5.getPassengerDob(), twelveYears) < 0)
                        form.recordError("Penumpang 5 Harus Berusia Diantara 2 s.d 12 Tahun");
                } else {
                    if (DateUtils.compareOnlyDate(aeroPassenger5.getPassengerDob(), threeMonths) > 0 || DateUtils.compareOnlyDate(aeroPassenger5.getPassengerDob(), twoYears) < 0)
                        form.recordError("Penumpang 5 Harus Berusia Diantara 3 Bulan s.d 24 Bulan");
                    if (isVoltras()) {  //untuk infant, nilai adult association adalah angka nya saja
                        String[] adultAssocs = aeroPassenger5.getParent().split("\\.");
                        aeroPassenger5.setParent(adultAssocs[1]);
                    }
                }
            }
        }
        if (isSixPassengers()) {
            if (sPass6Dob == null || sPass6Dob.equals("") || aeroPassenger6.getPassengerFirstName() == null || aeroPassenger6.getPassengerLastName() == null ||
                    (isPassanger6Adult() && aeroPassenger6.getPassengerIdCard() == null) ||
                    (!isPassanger6Infant() && (aeroPassenger6.getPassengerTitle() == null || aeroPassenger6.getPassengerTitle().equals("")))) {
                form.recordError("Mohon melengkapi data Penumpang 6");
            } else  {
                try { aeroPassenger6.setPassengerDob(dd_mm_yyyy.parse(sPass6Dob));
                } catch (ParseException e) { }
                if (isPassanger6Adult()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger6.getPassengerDob(), twelveYears) > 0) form.recordError("Penumpang 6 Harus Berusia Diatas 12 Tahun");
                } else if (isPassanger6Child()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger6.getPassengerDob(), twoYears) > 0 || DateUtils.compareOnlyDate(aeroPassenger6.getPassengerDob(), twelveYears) < 0)
                        form.recordError("Penumpang 6 Harus Berusia Diantara 2 s.d 12 Tahun");
                } else {
                    if (DateUtils.compareOnlyDate(aeroPassenger6.getPassengerDob(), threeMonths) > 0 || DateUtils.compareOnlyDate(aeroPassenger6.getPassengerDob(), twoYears) < 0)
                        form.recordError("Penumpang 6 Harus Berusia Diantara 3 Bulan s.d 24 Bulan");
                    if (isVoltras()) {  //untuk infant, nilai adult association adalah angka nya saja
                        String[] adultAssocs = aeroPassenger6.getParent().split("\\.");
                        aeroPassenger6.setParent(adultAssocs[1]);
                    }
                }
            }
        }
        if (isSevenPassengers()) {
            if (sPass7Dob == null || sPass7Dob.equals("") || aeroPassenger7.getPassengerFirstName() == null || aeroPassenger7.getPassengerLastName() == null ||
                    (isPassanger7Adult() && aeroPassenger7.getPassengerIdCard() == null) ||
                    (!isPassanger7Infant() && (aeroPassenger7.getPassengerTitle() == null || aeroPassenger7.getPassengerTitle().equals("")))) {
                form.recordError("Mohon melengkapi data Penumpang 7");
            } else  {
                try { aeroPassenger7.setPassengerDob(dd_mm_yyyy.parse(sPass7Dob));
                } catch (ParseException e) { }
                if (isPassanger7Adult()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger7.getPassengerDob(), twelveYears) > 0) form.recordError("Penumpang 7 Harus Berusia Diatas 12 Tahun");
                } else if (isPassanger7Child()) {
                    if (DateUtils.compareOnlyDate(aeroPassenger7.getPassengerDob(), twoYears) > 0 || DateUtils.compareOnlyDate(aeroPassenger7.getPassengerDob(), twelveYears) < 0)
                        form.recordError("Penumpang 7 Harus Berusia Diantara 2 s.d 12 Tahun");
                } else {
                    if (DateUtils.compareOnlyDate(aeroPassenger7.getPassengerDob(), threeMonths) > 0 || DateUtils.compareOnlyDate(aeroPassenger7.getPassengerDob(), twoYears) < 0)
                        form.recordError("Penumpang 7 Harus Berusia Diantara 3 Bulan s.d 24 Bulan");
                    if (isVoltras()) {  //untuk infant, nilai adult association adalah angka nya saja
                        String[] adultAssocs = aeroPassenger7.getParent().split("\\.");
                        aeroPassenger7.setParent(adultAssocs[1]);
                    }
                }
            }
        }

        if (contactPerson.getFirstName() == null || contactPerson.getLastName() == null || contactPerson.getCustomerPhone() == null || contactPerson.getCustomerEmail() == null) {
            form.recordError("Mohon melengkapi data Contact Person Detail");
        } else {
            contactPerson.setCustomerName(contactPerson.getFirstName() + " " + contactPerson.getLastName());
            aeroTicketingView.setAeroCustomer(contactPerson);
        }

        try {
            if (!form.getHasErrors()) {
                List<AeroPassenger> passengers = new ArrayList<AeroPassenger>();
                passengers.add(aeroPassenger1);
                if (aeroPassenger2 != null && aeroPassenger2.getPassengerFirstName() != null) passengers.add(aeroPassenger2);
                if (aeroPassenger3 != null && aeroPassenger3.getPassengerFirstName() != null) passengers.add(aeroPassenger3);
                if (aeroPassenger4 != null && aeroPassenger4.getPassengerFirstName() != null) passengers.add(aeroPassenger4);
                if (aeroPassenger5 != null && aeroPassenger5.getPassengerFirstName() != null) passengers.add(aeroPassenger5);
                if (aeroPassenger6 != null && aeroPassenger6.getPassengerFirstName() != null) passengers.add(aeroPassenger6);
                if (aeroPassenger7 != null && aeroPassenger7.getPassengerFirstName() != null) passengers.add(aeroPassenger7);
                aeroTicketingView.setPassengers(passengers);
            }
        } catch (BusinessException e) {
        	form.recordError(e.getFullMessage());
            e.printStackTrace();
        }
    }

//	@DiscardAfter
    public Object onSuccess() {
    	//save data penumpang dsini
        System.out.println("saveBoxPass1=" + saveBoxPass1 + " saveBoxPass2=" + saveBoxPass2 + " saveBoxPass3=" + saveBoxPass3 + " saveBoxPass4=" + saveBoxPass4 +
                " saveBoxPass5=" + saveBoxPass5 + " saveBoxPass6=" + saveBoxPass6 + " saveBoxPass7=" + saveBoxPass7);
        logger.info("saveBoxPass1=" + saveBoxPass1 + " saveBoxPass2=" + saveBoxPass2 + " saveBoxPass3=" + saveBoxPass3 + " saveBoxPass4=" + saveBoxPass4 +
                " saveBoxPass5=" + saveBoxPass5 + " saveBoxPass6=" + saveBoxPass6 + " saveBoxPass7=" + saveBoxPass7);
//        if (saveBoxPass1) { savingPassenger(aeroPassenger1, sPass1Dob); }
//        if (saveBoxPass2) { savingPassenger(aeroPassenger2, sPass2Dob); }
//        if (saveBoxPass3) { savingPassenger(aeroPassenger3, sPass3Dob); }
//        if (saveBoxPass4) { savingPassenger(aeroPassenger4, sPass4Dob); }
//        if (saveBoxPass5) { savingPassenger(aeroPassenger5, sPass5Dob); }
//        if (saveBoxPass6) { savingPassenger(aeroPassenger6, sPass6Dob); }
//        if (saveBoxPass7) { savingPassenger(aeroPassenger7, sPass7Dob); }

        aeroPassenger1 = null;
        aeroPassenger2 = null;
        aeroPassenger3 = null;
        aeroPassenger4 = null;
        aeroPassenger5 = null;
        aeroPassenger6 = null;
        aeroPassenger7 = null;

        if (aeroTicketingView.getDepartureFlight().getChildPassengerSummary() == null) {
            AeroFlightView departFlight = aeroTicketingView.getDepartureFlight();
            AeroPassengerSummary childPassengerSummary =  new AeroPassengerSummary();
            childPassengerSummary.setPax(0);
            departFlight.setChildPassengerSummary(childPassengerSummary);
            aeroTicketingView.setDepartureFlight(departFlight);
        }
        if (aeroTicketingView.getDepartureFlight().getInfantPassengerSummary() == null) {
            AeroFlightView departFlight = aeroTicketingView.getDepartureFlight();
            AeroPassengerSummary infantPassengerSummary =  new AeroPassengerSummary();
            infantPassengerSummary.setPax(0);
            departFlight.setInfantPassengerSummary(infantPassengerSummary);
            aeroTicketingView.setDepartureFlight(departFlight);
        }
        aeroTicketingFlightInsurance.setAeroTicketingView(aeroTicketingView);
        return aeroTicketingFlightInsurance;
    }

    void pageReset() {
    	
    }

	public AeroTicketingView getAeroTicketingView() {
		return aeroTicketingView;
	}

	public void setAeroTicketingView(AeroTicketingView aeroTicketingView) {
		this.aeroTicketingView = aeroTicketingView;
	}
	public String getDepartureClass(){
    	return aeroTicketingView.getDepartureFlight().getSelectedClass().getClassId() + "(" +
    			aeroTicketingView.getDepartureFlight().getSelectedClass().getClassName() + ")";
    }
	public String getReturnClass(){
    	return aeroTicketingView.getReturnFlight().getSelectedClass().getClassId() + "(" +
    			aeroTicketingView.getReturnFlight().getSelectedClass().getClassName() + ")";
    }

    private AeroFlightView getDepartureFlight() {
		return aeroTicketingView.getDepartureFlight();
	}
    public boolean isAdultOnly() {
    	boolean res = (isAdultPassenger() && !isChildPassenger() && !isInfantPassenger()) &&
    			(!isAdultPassenger() && isChildPassenger() && !isInfantPassenger());
    	return res;
    }
    public boolean isAdultAndChildOrInfant() {
    	boolean res = (isAdultPassenger() && isChildPassenger() && !isInfantPassenger() ) || (isAdultPassenger()  && !isChildPassenger() && isInfantPassenger());
    	return res;
    }
    public boolean isAdultChildInfant() {
    	boolean res = (isAdultPassenger() && isChildPassenger() && isInfantPassenger());
    	return res;
    }
    public boolean isAdultPassenger() {
        return aeroTicketingView.getTotalAdult() > 0;
    }
    public boolean isChildPassenger() {
        return aeroTicketingView.getTotalChildren() > 0;
    }
    public boolean isInfantPassenger() {
        return aeroTicketingView.getTotalInfant() > 0;
    }

	private void setPassenger() {
	    for (int i = 0; i < aeroTicketingView.getPassengers().size(); i++) {
	    	AeroPassenger aeroPassenger = aeroTicketingView.getPassengers().get(i);
			if (i == 0) {
	    		aeroPassenger1 = aeroPassenger;
	    	} else if (i == 1) {
	    		aeroPassenger2 = aeroPassenger;
	    	} else if (i == 2) {
	    		aeroPassenger3 = aeroPassenger;
	    	} else if (i == 3) {
	    		aeroPassenger4 = aeroPassenger;
	    	} else if (i == 4) {
	    		aeroPassenger5 = aeroPassenger;
	    	} else if (i == 5) {
	    		aeroPassenger6 = aeroPassenger;
	    	} else if (i == 6) {
	    		aeroPassenger7 = aeroPassenger;
	    	}
	    }
	}

    private String getAirlineCodeById(String theId) {
        if (aeroTicketingView.getProviderCode() != null && aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            Airline airline = cacheManager.getAirlineById(theId);
            if (airline != null) {
                return airline.getAirlineCode();
            }
        }
        return theId;
   	}

    public String getDepartLogo(){
        String code = aeroTicketingView.getDepartAirlineCode();
        if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            code = getAirlineCodeById(code);
        }
        String logo = ListUtils.getLogo(code);
        return logo;
    }

    public String getReturnLogo(){
        String code = aeroTicketingView.getReturnAirlineCode();
        if (aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE)) {
            code = getAirlineCodeById(code);
        }
        String logo = ListUtils.getLogo(code);
        return logo;
    }

    public boolean isTwoPassengers() {
//        return (aeroPassenger2 != null);
        int totPass = aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalChildren() + aeroTicketingView.getTotalInfant();
        return (totPass >= 2);
    }
    public boolean isThreePassengers() {
        int totPass = aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalChildren() + aeroTicketingView.getTotalInfant();
        return (totPass >= 3);
    }
    public boolean isFourPassengers() {
        int totPass = aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalChildren() + aeroTicketingView.getTotalInfant();
        return (totPass >= 4);
    }
    public boolean isFivePassengers() {
        int totPass = aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalChildren() + aeroTicketingView.getTotalInfant();
        return (totPass >= 5);
    }
    public boolean isSixPassengers() {
        int totPass = aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalChildren() + aeroTicketingView.getTotalInfant();
        return (totPass >= 6);
    }
    public boolean isSevenPassengers() {
        int totPass = aeroTicketingView.getTotalAdult() + aeroTicketingView.getTotalChildren() + aeroTicketingView.getTotalInfant();
        return (totPass >= 7);
    }

    public boolean isPassanger2Adult() { return aeroPassenger2.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger2Child() { return aeroPassenger2.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD); }
    public boolean isPassanger2Infant() { return isVoltras() && aeroPassenger2.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.INFANT); }

    public boolean isPassanger3Adult() { return aeroPassenger3.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger3Child() { return aeroPassenger3.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD); }
    public boolean isPassanger3Infant() { return isVoltras() && aeroPassenger3.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.INFANT); }

    public boolean isPassanger4Adult() { return aeroPassenger4.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger4Child() { return aeroPassenger4.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD); }
    public boolean isPassanger4Infant() { return isVoltras() && aeroPassenger4.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.INFANT); }

    public boolean isPassanger5Adult() { return aeroPassenger5.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger5Child() { return aeroPassenger5.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD); }
    public boolean isPassanger5Infant() { return isVoltras() && aeroPassenger5.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.INFANT); }

    public boolean isPassanger6Adult() { return aeroPassenger6.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger6Child() { return aeroPassenger6.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD); }
    public boolean isPassanger6Infant() { return isVoltras() && aeroPassenger6.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.INFANT); }

    public boolean isPassanger7Adult() { return aeroPassenger7.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.ADULT); }
    public boolean isPassanger7Child() { return aeroPassenger7.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.CHILD); }
    public boolean isPassanger7Infant() { return isVoltras() && aeroPassenger7.getPassengerType().equals(AeroPassenger.PASSENGER_TYPE.INFANT); }

    public boolean isReturnFlight() { return !aeroTicketingView.getIsDepartOnly(); }

    public boolean isVoltras() { return aeroTicketingView.getProviderCode().equals(AirConstants.VOLTRAS.PROVIDER.CODE); }
    public boolean isAeroticket() { return aeroTicketingView.getProviderCode().equals(AirConstants.AEROTICKETING.PROVIDER.CODE); }

    public boolean isDepartConnecting() {
        logger.info("aeroTicketingView.getDepartureFlight().getAeroConnectingFlight()=" + aeroTicketingView.getDepartureFlight().getAeroConnectingFlight());
        return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight() != null;
    }
    public boolean isDepartConnecting2() {
        return aeroTicketingView.getDepartureFlight().getAeroConnectingFlight2() != null;
    }
    public boolean isReturnConnecting() {
        return aeroTicketingView.getReturnFlight().getAeroConnectingFlight() != null;
    }
    public boolean isReturnConnecting2() {
        return aeroTicketingView.getReturnFlight().getAeroConnectingFlight2() != null;
    }

    public boolean isAeroChildPassenger() {
    	return isAeroticket() && aeroTicketingView.getTotalChildren().intValue() > 0;
    }

    public boolean isAeroInfantPassenger() {
        return isAeroticket() && aeroTicketingView.getTotalInfant().intValue() > 0;
    }

}
