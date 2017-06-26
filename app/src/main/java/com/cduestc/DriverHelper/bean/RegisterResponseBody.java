package com.cduestc.DriverHelper.bean;

/**
 * Created by c on 2017/3/8.
 */
public class RegisterResponseBody {
    private boolean success;
    private String message;
    private int power;

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

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }
}
