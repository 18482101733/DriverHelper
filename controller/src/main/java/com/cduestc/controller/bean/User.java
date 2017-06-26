package com.cduestc.controller.bean;

/**
 * Created by c on 2017/4/27.
 */
public class User {

    protected String uid;
    protected String name;
    protected String idNum;
    protected int states;
    protected String address;
    protected String schoolName;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }
    public int getStates() {
        return states;
    }

    public void setStates(int states) {
        this.states = states;
    }

    public int getState() {
        return states;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }
}
