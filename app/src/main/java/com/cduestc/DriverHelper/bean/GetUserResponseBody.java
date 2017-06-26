package com.cduestc.DriverHelper.bean;

/**
 * Created by c on 2017/3/11.
 */
public class GetUserResponseBody<T> {
    private boolean success;
    private String message;
    private T user;


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

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }
}
