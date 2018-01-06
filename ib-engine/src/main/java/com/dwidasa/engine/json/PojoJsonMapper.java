package com.dwidasa.engine.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;

import com.dwidasa.engine.BusinessException;
import com.dwidasa.engine.model.train.ApiGetSchedule;
import com.dwidasa.engine.model.train.TrainChooseSeat;
import com.dwidasa.engine.model.train.TrainPassenger;
import com.dwidasa.engine.model.train.TrainSchedule;
import com.dwidasa.engine.model.train.TrainSeat;
import com.dwidasa.engine.model.train.TrainSubclass;
import com.dwidasa.engine.model.train.TrainWagon;
import com.dwidasa.engine.model.view.TrainPurchaseView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Convenience class to convert POJO to JSON and vice versa.
 *
 * @author rk
 */
@SuppressWarnings("deprecation")
public class PojoJsonMapper {
	private static Logger logger = Logger.getLogger( PojoJsonMapper.class );
	
    private final static ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) {
    	
		String json = "{ \"err_code\": 0, \"org\":\"GMR\",\"des\":\"BD\",\"dep_date\":\"20140604\",\"arv_date\":\"20140604\",\"dep_time\":\"0700\",\"arv_time\":\"1000\",\"train_no\": \"1331\", \"book_code\": \"3MWUJ4\", \"num_code\":\"3784080628609\",\"pax_num\": [1,0,0], \"pax_name\": [\"YOHAN\"] ,\"seat\": [[\"KERETA_EKS\",\"1\",\"1\",\"A\"]], \"normal_sales\": 84000, \"extra_fee\": 0, \"book_balance\": 84000 }";
		TrainPurchaseView view = new TrainPurchaseView();
		view.setPassengerList(new ArrayList<TrainPassenger>());
		view.getPassengerList().add(new TrainPassenger());
		PojoJsonMapper.trainFromJsonBookingToView(json, view);

    	
	}
    
    //Train
    public static ApiGetSchedule trainFromJsonToApiGetSchedule(String json) {
    	ApiGetSchedule result = new ApiGetSchedule();
    	List<TrainSchedule> scheduleList = new ArrayList<TrainSchedule>();
    	JsonElement jelement = new JsonParser().parse(json);
        JsonObject obj = jelement.getAsJsonObject();
        result.setErrorCode(obj.get("err_code").getAsInt());
        if (result.getErrorCode() != 0) {
//        	throw new UnsupportedOperationException(obj.get("err_msg").getAsString());
        	throw new BusinessException("KAI-" + obj.get("err_code").getAsString(), obj.get("err_msg").getAsString());
        }
        result.setTrainSchedule(scheduleList);
        
        JsonElement element = obj.get("schedule");
        JsonArray arr = element.getAsJsonArray();
        
        DateFormat fmtDate = new SimpleDateFormat("yyyyMMdd HHmm");
        for (int l = 0; l < arr.size(); l++) { //loop jumlah kereta
        	TrainSchedule schedule = new TrainSchedule();
        	
        	List<TrainSubclass> subclassList = new ArrayList<TrainSubclass>();
        	
        	JsonArray arr2 = arr.get(l).getAsJsonArray();
        	schedule.setTrainNo(arr2.get(0).getAsString());
        	schedule.setTrainName(arr2.get(1).getAsString());
        	String strDepDate = arr2.get(2).getAsString();
        	String strArvDate = arr2.get(3).getAsString();
        	String strDepTime = arr2.get(4).getAsString();
        	String strArvTime = arr2.get(5).getAsString();
        	
        	try {
	        	schedule.setDepartureDate(fmtDate.parse(strDepDate + " " + strDepTime));
	        	schedule.setArrivalDate(fmtDate.parse(strArvDate + " " + strArvTime));
        	} catch (ParseException e) {
        		logger.error("Error parsing date: " + schedule.getDepartureDate() + " " + schedule.getArrivalDate());
        		continue;
        	}
        	
//        	DateTime now = new DateTime();
//        	DateTime jodaDeparture = new DateTime(schedule.getDepartureDate());
//        	if (now.plusHours(8).isAfter(jodaDeparture)) {
//        		//kereta dengan jam keberangkatan < 8 jam tidak usah ditampilkan
//        		continue;
//        	}
        	
        	scheduleList.add(schedule);
        	schedule.setSubclassList(subclassList);
        	
    		JsonArray arr3 = arr2.get(6).getAsJsonArray();
    		for (int j = 0; j < arr3.size(); j++) {
    			JsonArray arr4 = arr3.get(j).getAsJsonArray();
    			TrainSubclass subclass = new TrainSubclass();
    			subclassList.add(subclass);
    			subclass.setSubclass(arr4.get(0).getAsString());
    			subclass.setSeatClass(arr4.get(1).getAsString());
    			subclass.setAdultFare(new BigDecimal(arr4.get(2).getAsString()));
    		}
        }
        
                
        return result;
    }
    
    //Train
    public static Map<String, Integer> trainFromJsonToAvailableSeatMap(String[] arrJson) {
    	Map<String, Integer> result = new HashMap<String, Integer>();
        for (String json : arrJson) {
        	JsonElement jelement = new JsonParser().parse(json);
            JsonObject obj = jelement.getAsJsonObject();
            if (obj.get("err_code").getAsInt() != 0) {
//            	throw new UnsupportedOperationException(obj.get("err_msg").getAsString());
            	throw new BusinessException("KAI-" + obj.get("err_code").getAsString(), obj.get("err_msg").getAsString());
            }
            String trainNumber = obj.get("train_no").getAsString();
            JsonArray arr = obj.get("avb").getAsJsonArray();
            for (int l = 0; l < arr.size(); l++) { //l
            	JsonArray arr2 = arr.get(l).getAsJsonArray();
            	String subclass = arr2.get(0).getAsString(); 
            	int available = arr2.get(1).getAsInt();
            	String seatClass = arr2.get(2).getAsString();
            	result.put(trainNumber + "#" + seatClass + "#" + subclass, available);
            }
        }
        return result;
    }
    
    //Train
    public static Map<String, List<BigDecimal>> trainFromJsonToFareMap(String json) {
    	Map<String, List<BigDecimal>> result = new HashMap<String, List<BigDecimal>>();
    	JsonElement jelement = new JsonParser().parse(json);
        JsonObject obj = jelement.getAsJsonObject();
        if (obj.get("err_code").getAsInt() != 0) {
//        	throw new UnsupportedOperationException(obj.get("err_msg").getAsString());
        	throw new BusinessException("KAI-" + obj.get("err_code").getAsString(), obj.get("err_msg").getAsString());
        }
        JsonArray arr = obj.get("fare").getAsJsonArray();
        for (int l = 0; l < arr.size(); l++) { //l
        	JsonArray arr2 = arr.get(l).getAsJsonArray();
        	String subclass = arr2.get(0).getAsString(); 
        	String seatClass = arr2.get(1).getAsString();
        	List<BigDecimal> fareList = new ArrayList<BigDecimal>();
        	fareList.add(arr2.get(2).getAsBigDecimal());
        	fareList.add(arr2.get(3).getAsBigDecimal());
        	fareList.add(arr2.get(4).getAsBigDecimal());
        	result.put(seatClass + "#" + subclass, fareList);
        }
        return result;
    }
    
    // Train
    public static void trainFromJsonBookingToView(String json, TrainPurchaseView view) {
    	if (json == null || view == null || view.getPassengerList() == null) return;
    	JsonElement jelement = new JsonParser().parse(json);
    	JsonObject obj = jelement.getAsJsonObject();
    	
    	view.setResponseCode(obj.get("err_code").getAsString());
    	if (obj.get("err_code").getAsInt() != 0) {
//        	throw new UnsupportedOperationException(obj.get("err_msg").getAsString());
    		throw new BusinessException("KAI-" + obj.get("err_code").getAsString(), obj.get("err_msg").getAsString());
        }
    	view.setBookingCode(obj.get("book_code").getAsString());
    	view.setAmount(new BigDecimal(obj.get("normal_sales").getAsInt()));
//    	view.setFee(new BigDecimal(obj.get("extra_fee").getAsInt()));
    	view.setBookBalance(new BigDecimal(obj.get("book_balance").getAsInt()));
    	view.setDiscount(new BigDecimal(obj.get("discount").getAsInt()));
    	JsonArray arr = obj.get("seat").getAsJsonArray();
    	if (arr.size() != view.getPassengerList().size()) return; 
    	for (int i = 0; i < arr.size(); i++) { //l
    		JsonArray arr2 = arr.get(i).getAsJsonArray();
    		view.getPassengerList().get(i).setWagonCode(arr2.get(0).getAsString());
    		view.getPassengerList().get(i).setWagonNumber(arr2.get(1).getAsString());
    		view.getPassengerList().get(i).setSeatRow(arr2.get(2).getAsString());
    		view.getPassengerList().get(i).setSeatCol(arr2.get(3).getAsString());
    	}
    	if (view.getPassengerList().size() > 0) {
	    	view.setWagonCode(view.getPassengerList().get(0).getWagonCode());
	    	view.setWagonNumber(view.getPassengerList().get(0).getWagonNumber());
    	}
	}
    
    //Train
    public static TrainChooseSeat trainFromJsonToTrainChooseSeat(String json, TrainPurchaseView view) {
    	TrainChooseSeat result = new TrainChooseSeat();
    	if (json == null || view == null || view.getPassengerList() == null) return null;
    	JsonElement jelement = new JsonParser().parse(json);
    	JsonObject obj = jelement.getAsJsonObject();
    	
    	view.setResponseCode(obj.get("err_code").getAsString());
    	if (obj.get("err_code").getAsInt() != 0) {
//        	throw new UnsupportedOperationException(obj.get("err_msg").getAsString());
    		throw new BusinessException("KAI-" + obj.get("err_code").getAsString(), obj.get("err_msg").getAsString());
        }

    	List<TrainWagon> wagonList = new ArrayList<TrainWagon>();
//    	List<TrainSelectedSeat> selectedSeatList = new ArrayList<TrainSelectedSeat>();
    	result.setTrainWagonList(wagonList);
//    	result.setTrainSelectedSeatList(selectedSeatList);
    	StringBuilder strSeat = new StringBuilder();
    	for (TrainPassenger passenger : view.getPassengerList()) {
    		strSeat.append(",").append(passenger.getSeatRow()).append(passenger.getSeatCol());
    	}
    	if (strSeat.length() > 0) strSeat.delete(0,1);
    	result.setSelectedSeat(strSeat.toString());
    	result.setSelectedWagonCode(view.getWagonCode());
    	result.setSelectedWagonNumber(view.getWagonNumber());
    	JsonArray arr = obj.get("seat_null").getAsJsonArray();
    	for (int i = 0; i < arr.size(); i++) { //l
    		JsonArray arr2 = arr.get(i).getAsJsonArray();
    		String wagonCode = arr2.get(0).getAsString();
    		String wagonNumber = arr2.get(1).getAsString();
    		TrainWagon wagon = new TrainWagon();
    		Map<String, Integer> trainSeatMap = new HashMap<String, Integer>();
    		StringBuilder sbNoSeat = new StringBuilder();
    		StringBuilder sbFilledSeat = new StringBuilder();
    		StringBuilder sbActiveSeat = new StringBuilder();
    		wagon.setWagonCode(wagonCode);
    		wagon.setWagonNumber(wagonNumber);
    		wagon.setTrainSeatMap(trainSeatMap);
    		wagonList.add(wagon);
    		
    		JsonArray arr3 = arr2.get(2).getAsJsonArray();
    		int rowCount = 0;
    		int colCount = 0;
    		for (int l = 0; l < arr3.size(); l++) { //l
    			JsonArray arr4 = arr3.get(l).getAsJsonArray();
    			TrainSeat seat = new TrainSeat();
    			String seatRow = arr4.get(0).getAsString();
    			if (Integer.parseInt(seatRow) > rowCount) {
    				rowCount = Integer.parseInt(seatRow);
    			}
    			String seatColumn = arr4.get(1).getAsString();
    			int col = seatColumn.charAt(0)-64; 
    			if (col > colCount) { //A jadi 1, B jadi 2, dst
    				colCount = col;
    			}
    			String subclass = arr4.get(2).getAsString();
    			int status = arr4.get(3).getAsInt();
    			seat.setSeatRow(seatRow);
    			seat.setSeatColumn(seatColumn);
    			seat.setSubclass(subclass);
    			seat.setStatus(status);
    			for (TrainPassenger passenger : view.getPassengerList()) {
    				if (passenger.getSeatCol().equals(seatColumn) && passenger.getSeatRow().equals(seatRow)
    						&& wagonCode.equals(passenger.getWagonCode()) && wagonNumber.equals(passenger.getWagonNumber())) {
    					seat.setStatus(2);
    					sbActiveSeat.append(", .row").append(seatRow).append(" .col").append(col);
    				}
    			}
    			trainSeatMap.put(seatRow + "#" + seatColumn, seat.getStatus());
    			
    			if (status == 1) {
    				sbFilledSeat.append(", .row").append(seatRow).append(" .col").append(col);
    			}
    		}
    		//cari apakah ada yang noseat
    		for (int r = 1; r <= rowCount; r++) {
    			for (int c = 1; c <= colCount; c++) {
    				String key =  r + "#" + Character.toString((char) (c+64));
    				if (trainSeatMap.get(key) == null) {
    					sbNoSeat.append(", .row").append(r).append(" .col").append(c);
    				}
    			}
    		}
    		if (sbActiveSeat.length() > 0) sbActiveSeat.delete(0, 2);
    		if (sbFilledSeat.length() > 0) sbFilledSeat.delete(0, 2);
    		if (sbNoSeat.length() > 0) sbNoSeat.delete(0, 2);
    		wagon.setActiveSeat(sbActiveSeat.toString());
    		wagon.setFilledSeat(sbFilledSeat.toString());
    		wagon.setNoSeat(sbNoSeat.toString());
    		wagon.setRowCount(rowCount);
    		wagon.setColCount(colCount);
    	}
    	return result;
	}
    
    // Train
    public static void trainFromJsonGenericValidate(String json) {
    	if (json == null) return;
    	JsonElement jelement = new JsonParser().parse(json);
    	JsonObject obj = jelement.getAsJsonObject();
    	
    	if (obj.get("err_code").getAsInt() != 0) {
//        	throw new UnsupportedOperationException(obj.get("err_msg").getAsString());
    		throw new BusinessException("KAI-" + obj.get("err_code").getAsString(), obj.get("err_msg").getAsString());
        }
	}
    
    static {
        mapper.getSerializationConfig().setDateFormat(new SimpleDateFormat("dd MM yyyy HH:mm:ss"));
        mapper.getDeserializationConfig().setDateFormat(new SimpleDateFormat("dd MM yyyy HH:mm:ss"));
    }

    /**
     * Convert POJO to its JSON string.
     * @param pojo POJO to be converted
     * @param <T> type parameter
     * @return JSON string
     */
    public static <T> String toJson(T pojo) {
        String result;
        try {
            result = mapper.writeValueAsString(pojo);
        } catch (IOException e) {
            e.printStackTrace();
            //-- PJMTJ = POJO JSON MAPPER TO JSON
            throw new BusinessException("IB-0500", "PJMTJ");
        }

        return result;
    }

    /**
     * Convert JSON string to specified POJO class.
     * @param json JSON string
     * @param pojoClass POJO class
     * @param <T> type parameter
     * @return POJO object
     */
    public static <T> T fromJson(String json, Class<T> pojoClass) {
        T object;
        try {        	
            object = mapper.readValue(json, pojoClass);
        } catch (IOException e) {
            e.printStackTrace();
            //-- PJMFJ = POJO JSON MAPPER FROM JSON
            throw new BusinessException("IB-0500", "PJMFJ");
        }

        return object;
    }

    /**
     * Convert JSON string to specified POJO class.
     * @param json JSON string
     * @param valueTypeRef value of type reference
     * @return Object
     */
    public static <T> T fromJson(String json, TypeReference<T> valueTypeRef) {
        T object;
        try {
            object = mapper.<T>readValue(json, valueTypeRef);
        } catch (IOException e) {
            e.printStackTrace();
            //-- PJMFJ = POJO JSON MAPPER FROM JSON
            throw new BusinessException("IB-0500", "PJMFJ");
        }

        return object;
    }

	

	
    
}
