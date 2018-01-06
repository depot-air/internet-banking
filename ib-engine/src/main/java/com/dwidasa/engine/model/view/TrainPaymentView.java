package com.dwidasa.engine.model.view;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: rk
 * Date: 8/10/11
 * Time: 2:36 PM
 */
public class TrainPaymentView extends PaymentView {
    /**
     * Number of passenger, output field.
     */
    private Integer numOfPassenger;
    /**
     * Train name, output field.
     */
    private String trainName;
    /**
     * Contain origin and destination, output field.
     */
    private String tripInfo;
    /**
     * Date of departure
     */
    private Date departureDate;

    public TrainPaymentView() {
        super();
    }

    public Integer getNumOfPassenger() {
        return numOfPassenger;
    }

    public void setNumOfPassenger(Integer numOfPassenger) {
        this.numOfPassenger = numOfPassenger;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTripInfo() {
        return tripInfo;
    }

    public void setTripInfo(String tripInfo) {
        this.tripInfo = tripInfo;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }
}
