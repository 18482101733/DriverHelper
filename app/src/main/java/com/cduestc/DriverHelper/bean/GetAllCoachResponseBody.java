package com.cduestc.DriverHelper.bean;

import java.util.ArrayList;

/**
 * Created by c on 2017/3/12.
 */
public class GetAllCoachResponseBody {
    private boolean success;
    private String message;
    private ArrayList<Coach> staffEntities;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Coach> getStaffEntities() {
        return staffEntities;
    }

    public void setStaffEntities(ArrayList<Coach> staffEntities) {
        this.staffEntities = staffEntities;
    }
}
