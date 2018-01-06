package com.dwidasa.ib.pages.purchase;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry5.annotations.DiscardAfter;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.joda.time.DateTime;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.train.ApiGetSchedule;
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.TrainStationService;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.ib.Constants;

@Import(stylesheet = {"context:bprks/css/train/select2.css","context:bprks/css/style_train.css",
		"context:bprks/css/train/ui-lightness/jquery-ui-1.10.4.custom.min.css"}, 
		library={
			"context:bprks/js/aero/jquery.js", "context:bprks/js/aero/jquery-ui.js",
			"context:bprks/js/aero/select2.min.js", 			
			"context:bprks/js/purchase/trainfLSScript.js"})
public class TrainPurchaseDetail {
	@Persist
	@Property(write = false)
	private TrainPurchaseView view;
	
	@Property
	private TrainPassenger passenger;
	
	public void setView(TrainPurchaseView view) {
		this.view = view;
	}

	@Inject
	private TrainStationService trainStationService;
	
	@InjectComponent
	private Form form;
	
	void onValidateFromForm() {
		if (isBackButton) {
			form.clearErrors();
			return;
		}
		
		for (TrainPassenger passenger : view.getPassengerList()) {
			if (passenger.getFirstName() == null && passenger.getLastName() == null) {
				form.recordError("Nama Penumpang " + passenger.getLabel() + " harus diisi");
			}
			if ("A".equals(passenger.getType())) { //adult
				if (passenger.getIdNumber() == null) {
					form.recordError("Nomor Identitas Penumpang " + passenger.getLabel() + " harus diisi");
				}
				if (passenger.getPhone() == null) {
					form.recordError("Nomor Telepon Penumpang " + passenger.getLabel() + " harus diisi");
				}
			} else {
				if (passenger.getBirthDate() == null) {
					form.recordError("Tanggal Lahir " + passenger.getLabel() + " harus diisi");
				} else {
					DateTime dateLimit = new DateTime(view.getDepartureDate());
					dateLimit = dateLimit.minusYears(3);
					if (new DateTime(passenger.getBirthDate()).isBefore(dateLimit)) {
						form.recordError("Penumpang " + passenger.getLabel() + " tidak termasuk kategori infant (0-3 tahun)");
					}
				}
				
			}
		}
		
		if (form.getHasErrors()) {
			return;
		}
				
		for (TrainPassenger passenger : view.getPassengerList()) {
			System.out.println(passenger.getLabel() + " : " + passenger.getFirstName() + " " + passenger.getLastName());
		}
		
		try {
			bookTicket(view);
		} catch (BusinessException e) {
			form.recordError(e.getFullMessage());
            e.printStackTrace();
            return;
		} catch (Exception e) {
			form.recordError(e.getMessage());
			e.printStackTrace();
			return;
		}		
		
		
	}
	
	private void bookTicket(TrainPurchaseView view2) {
		String json = "";
		if ("LOCAL".equals(TrainPurchaseInput.MODE)) {
			json = "{ \"err_code\": 0, \"org\":\"GMR\",\"des\":\"BD\",\"dep_date\":\"20140604\",\"arv_date\":\"20140604\",\"dep_time\":\"0700\",\"arv_time\":\"1000\",\"train_no\": \"1331\", \"book_code\": \"3MWUJ4\", \"num_code\":\"3784080628609\",\"pax_num\": [1,0,0], \"pax_name\": [\"YOHAN\"] ,\"seat\": [[\"KERETA_EKS\",\"1\",\"1\",\"A\"]], \"normal_sales\": 84000, \"extra_fee\": 0, \"book_balance\": 84000, \"discount\": 0 }";
		} else {
			DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
			Transaction t = new Transaction();
			t.setTransactionType(com.dwidasa.engine.Constants.KAI_BOOKING_WITH_ARV_INFO);
			t.setMerchantType(com.dwidasa.engine.Constants.MERCHANT_TYPE.IB);
			t.setFromAccountType("00");
			t.setToAccountType("00");
			t.setFromAccountNumber(view.getAccountNumber());
			t.setCardNumber(view.getCardNumber());
			t.setProviderCode("PAC");
			StringBuilder bit48 = new StringBuilder();
			view.setContactPhone(view.getPassengerList().get(0).getPhone());
			bit48.append(view.getOriginCode()).append("#").append(view.getDestinationCode()).append("#").append(fmtYmd.format(view.getDepartureDate())).append("#").append(view.getTrainNumber()).append("#");
			bit48.append(view.getAdult()).append("#").append(view.getChild()).append("#").append(view.getInfant()).append("#");
			bit48.append(view.getSubclass()).append("#").append(view.getContactPhone()).append("#");
			for (TrainPassenger passenger : view.getPassengerList()) {
				if ("A".equals(passenger.getType())) {
					bit48.append(passenger.getStrName()).append("#");
					bit48.append(passenger.getPhone()).append("#");
					bit48.append(passenger.getIdNumber()).append("#");
					bit48.append(passenger.getMember()).append("#");
				} else {
					bit48.append(passenger.getStrName()).append("#");
				}
			}
			bit48.delete(bit48.length()-1, bit48.length()); //hapus # di paling belakang
			
			t.setFreeData1(bit48.toString());
			CommLink link = new MxCommLink(t);
	        link.sendMessage();
	        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
	        	throw new BusinessException("IB-1009", t.getResponseCode());
	        }
			json = t.getFreeData1();	
			BigDecimal fee = t.getFee();
			if (fee == null) {
				fee = BigDecimal.ZERO;
			}
			view.setFee(fee);
		}

		PojoJsonMapper.trainFromJsonBookingToView(json, view);
	}

	@Persist
	@Property(write = false)
	private ApiGetSchedule apiGetSchedule;

	public void setApiGetSchedule(ApiGetSchedule apiGetSchedule) {
		this.apiGetSchedule = apiGetSchedule;
	}
	
	@Inject
    private ThreadLocale threadLocale;
	
	@Property
	private TrainSchedule trainSchedule;
	
	@Property
	private TrainSubclass trainSubclass;
	
	@Property
	private NumberFormat formatter = NumberFormat.getInstance(threadLocale.getLocale());
	
	@Property
	private DateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	
	public String getStrPenumpang() {
		StringBuilder sb = new StringBuilder();
//		2 Dewasa, 1 Anak, 1 Infant
		if (view.getAdult() > 0) {
			sb.append(", ").append(view.getAdult()).append(" Dewasa");
		}
		if (view.getChild() > 0) {
			sb.append(", ").append(view.getChild()).append(" Anak");
		}
		if (view.getInfant() > 0) {
			sb.append(", ").append(view.getInfant()).append(" Infant");
		}
		if (sb.length() > 0) {
			sb.delete(0,2);
		}
		return sb.toString();
	}
	
	public String getStrDeparture() {
//		Selasa, 27 Mei 2014
		if (view == null) return "";
		Date depDate = view.getDepartureDate();
		if (depDate == null) return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(depDate);
		StringBuilder sb = new StringBuilder();
		sb.append(Constants.strDay[cal.get(Calendar.DAY_OF_WEEK)-1]).append(", ");
		sb.append(cal.get(Calendar.DAY_OF_MONTH)).append(" ").append(Constants.strMonth[cal.get(Calendar.MONTH)]);
		sb.append(" ").append(cal.get(Calendar.YEAR));
		return sb.toString();
	}
	

	public String getStrTime(Date date) {
		if (date == null) return "";
		DateFormat fmt = new SimpleDateFormat("HH:mm");
		return fmt.format(date);
	}
	
	@Property
	private Integer index;
	
	@Property
	private String label;
	
	public boolean isAdult() {
		if (passenger == null) return false;
		return "A".equals(passenger.getType());
	}
	
	private boolean isBackButton;
	
	void onSelectedFromBack() {
		isBackButton = true;
	}
	
	@InjectPage
	private TrainPurchaseInput trainPurchaseInput;
	
	@InjectPage
	private TrainPurchaseConfirm trainPurchaseConfirm;
	
	@DiscardAfter
	Object onSuccessFromForm() {
		if (isBackButton) {
			trainPurchaseInput.setView(view);
			return trainPurchaseInput;
		}
		trainPurchaseConfirm.setView(view);
		return trainPurchaseConfirm;
	}
	
	public String getFormClass() {
		if (index == 0) {
			return "pass" + index;
		} else {
			return "pass" + index + " hide";
		}
	}
}
