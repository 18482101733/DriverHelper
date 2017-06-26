package com.cduestc.DriverHelper.bean;

import java.util.List;

/**
 * Created by c on 2017/3/9.
 */
public class GetSchoolResponseBody {

    private boolean success;
    private String message;
    private List<School> data;

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

    public List<School> getData() {
        return data;
    }

    public void setData(List<School> data) {
        this.data = data;
    }
}
