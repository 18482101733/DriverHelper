package com.cduestc.DriverHelper.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by c on 2017/3/4.
 */
public class Coach extends User implements Serializable {

    @Expose private String plateNum;
    @Expose private String phone;

    @SerializedName("staffElse")
    @Expose private String coachInfo;

    private String userIcon;

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getCoachInfo() {
        return coachInfo;
    }

    public void setCoachInfo(String coachInfo) {
        this.coachInfo = coachInfo;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Coach(String name, String idNum, String uid, String address, String password, String schoolName, String plateNum) {
        this.name = name;
        this.idNum = idNum;
        this.uid = uid;
        this.address = address;
        this.password = password;
        this.schoolName = schoolName;
        this.plateNum = plateNum;
    }

    public Coach(){

    }

    public String toRegisterString(){
        return "{\n" +
                "  \"address\":\"" +getAddress()+ "\"," +
                "  \"idNum\": \""+getIdNum()+"\"," +
                "  \"name\": \""+getName()+"\"," +
                "  \"password\": \""+getPassword()+"\"," +
                "  \"plateNum\": \""+getPlateNum()+"\"," +
                "  \"schoolName\": \""+getSchoolName()+"\"," +
                "  \"uid\": \""+getUid()+"\"" +
                "}";
    }


    public String toModifyString(){
        return "{\n" +
                "  \"address\":\"" +getAddress()+ "\"," +
                "  \"idNum\": \""+getIdNum()+"\"," +
                "  \"name\": \""+getName()+"\"," +
                "  \"password\": \""+getPassword()+"\"," +
                "  \"plateNum\": \""+getPlateNum()+"\"," +
                "  \"schoolName\": \""+getSchoolName()+"\"," +
                "  \"uid\": \""+getUid()+"\"," +
                "  \"staffElse\": \""+getCoachInfo()+"\"," +
                "  \"phone\": \""+getPhone()+"\"" +
                "}";
    }
}
