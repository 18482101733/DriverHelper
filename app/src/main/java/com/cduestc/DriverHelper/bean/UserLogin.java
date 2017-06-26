package com.cduestc.DriverHelper.bean;

import com.google.gson.Gson;

/**
 * Created by c on 2017/3/3.
 */
public class UserLogin {
    private String userName;
    private String password;

    public UserLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    public String toString(){
        Gson gson = new Gson();
        return  gson.toJson(this);
    }
}
