package com.cduestc.DriverHelper.bean;

import java.util.ArrayList;

/**
 * Created by c on 2017/3/20.
 */
public class ReservationResponse {
    private String message;
    private boolean success;
    private ArrayList<ReservationBody> appoints;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<ReservationBody> getAppoints() {
        return appoints;
    }

    public void setAppoints(ArrayList<ReservationBody> appoints) {
        this.appoints = appoints;
    }
}
