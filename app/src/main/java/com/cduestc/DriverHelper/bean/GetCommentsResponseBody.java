package com.cduestc.DriverHelper.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by c on 2017/3/17.
 */
public class GetCommentsResponseBody {

    @Expose private boolean success;
    @Expose private String message;
    @Expose private ArrayList<Comment> discusses;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Comment> getDiscusses() {
        return discusses;
    }

    public void setAppoints(ArrayList<Comment> discusses) {
        this.discusses = discusses;
    }

}
