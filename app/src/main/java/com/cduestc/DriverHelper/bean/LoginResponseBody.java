package com.cduestc.DriverHelper.bean;

import com.google.gson.Gson;

/**
 * Created by c on 2017/3/9.
 */
public class LoginResponseBody {
    private boolean success;
    private int power;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public int getPower() {
        return power;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
