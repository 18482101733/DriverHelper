package com.cduestc.DriverHelper.bean;

/**
 * Created by c on 2017/3/21.
 */
public class ModifyResponseBody {
    /**
     * cnt : 0
     * message : string
     * success : true
     */

    private int cnt;
    private String message;
    private boolean success;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

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
}
