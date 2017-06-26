package com.cduestc.DriverHelper.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by c on 2017/3/12.
 */
public class User implements Serializable{

    @Expose protected String uid;
    @Expose protected String address;
    @Expose protected String idNum;
    @Expose protected String schoolName;
    @Expose protected String name;
    @Expose protected String password;
    @Expose protected int states;


    public int getStates() {
        return states;
    }

    public void setStates(int states) {
        this.states = states;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
