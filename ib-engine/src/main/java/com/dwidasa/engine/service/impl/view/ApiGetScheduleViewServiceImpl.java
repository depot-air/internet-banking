package com.dwidasa.engine.service.impl.view;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.Constants;
import com.dwidasa.engine.json.PojoJsonMapper;
import com.dwidasa.engine.model.Transaction;
import com.dwidasa.engine.model.train.ApiGetSchedule;
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.view.BaseView;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.dwidasa.engine.service.CacheManager;
import com.dwidasa.engine.service.link.CommLink;
import com.dwidasa.engine.service.link.MxCommLink;
import com.dwidasa.engine.service.transform.TransformerFactory;
import com.dwidasa.engine.service.view.ApiGetScheduleViewService;
import com.dwidasa.engine.service.view.MessageCustomizer;

@Service("apiGetScheduleViewService")
public class ApiGetScheduleViewServiceImpl implements ApiGetScheduleViewService{

	@Autowired
	private CacheManager cacheManager;

	private DateFormat fmtYmd = new SimpleDateFormat("yyyyMMdd");
	private static Logger logger = Logger.getLogger(ApiGetScheduleViewServiceImpl.class);
	
	public ApiGetScheduleViewServiceImpl(){
		
	}
	
	private class ApiGetScheduleMessageCustomizer implements MessageCustomizer {

		@Override
		public void compose(BaseView view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;
			
			StringBuilder bit48 = new StringBuilder();
			bit48.append(tpv.getOriginCode()).append("#").append(tpv.getDestinationCode()).append("#").append(fmtYmd.format(tpv.getDepartureDate()));
			transaction.setFreeData1(bit48.toString());
			
		}

		@Override
		public Boolean decompose(Object view, Transaction transaction) {
			TrainPurchaseView tpv = (TrainPurchaseView) view;
			
			return Boolean.TRUE;
		}
		
	}
	
	public ApiGetSchedule getTrainSchedule(TrainPurchaseView view){
		
		String[] arrJson;
		Transaction t = new Transaction();
		t.setTransactionType(Constants.KAI_GET_SCHEDULE_ONLY);
		t.setMerchantType(Constants.MERCHANT_TYPE.IB);
		t.setFromAccountType("00");
		t.setToAccountType("00");
		t.setFromAccountNumber(view.getAccountNumber());
		t.setCardNumber(view.getCardNumber());
		t.setProviderCode("PAC");

		MessageCustomizer customizer = new ApiGetScheduleMessageCustomizer();
        logger.info("view.getTransactionType()=" + view.getTransactionType());
        
        customizer.compose(view, t);
        CommLink link = new MxCommLink(t);
        logger.info("t.getTransactionType()=" + t.getTransactionType());
        link.sendMessage();
        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
        	throw new BusinessException("IB-1009", t.getResponseCode());
        }else{
        	customizer.decompose(view, t);
        }
        StringBuilder bit48 = new StringBuilder();
        bit48.append(t.getFreeData1());
        System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
        System.out.println("HOHO RESPON BIT 48:");
        ApiGetSchedule schedule = new ApiGetSchedule();
        schedule = PojoJsonMapper.trainFromJsonToApiGetSchedule(t.getFreeData1());
		List<String> jsonList = new ArrayList<String>();
		for (TrainSchedule sch : schedule.getTrainSchedule()) {
			t.setTransactionType(Constants.KAI_GET_AVB_ONLY);
			t.setReferenceNumber(null);
			bit48.delete(0, bit48.length());
			bit48.append(view.getOriginCode()).append("#").append(view.getDestinationCode()).append("#")
				 .append(fmtYmd.format(view.getDepartureDate())).append("#").append(sch.getTrainNo());
			t.setFreeData1(bit48.toString());
			link = new MxCommLink(t);
	        link.sendMessage();
	        if (!"00".equals(t.getResponseCode()) && !"10".equals(t.getResponseCode())) {
	        	throw new BusinessException("IB-1009", t.getResponseCode());
	        }else{
	        	customizer.decompose(view, t);
	        }
	        System.out.println("HOHO RESPON BIT 39: " + t.getResponseCode());
	        System.out.println("HOHO RESPON BIT 48:");
	        System.out.println(t.getFreeData1());
	        jsonList.add(t.getFreeData1());
		}
	arrJson = new String[jsonList.size()];
	arrJson = jsonList.toArray(arrJson);
	
	Map<String, Integer> availableSeat = PojoJsonMapper.trainFromJsonToAvailableSeatMap(arrJson);
	for (TrainSchedule sch: schedule.getTrainSchedule()) {
		if (sch.getSubclassList() == null) continue;
		for (TrainSubclass sc : sch.getSubclassList()) {
			String key = sch.getTrainNo() + "#" + sc.getSeatClass() + "#" + sc.getSubclass();
			Integer seat = availableSeat.get(key);
			if (seat != null) {
				sc.setAvailable(seat);
			}
		}
	}
	return schedule;
		
	}
	
}