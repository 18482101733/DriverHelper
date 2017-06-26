package com.cduestc.DriverHelper.bean;

/**
 * Created by c on 2017/3/9.
 */
public class School {

    /**
     * address : 驾校地址
     * city : 驾校所在城市
     * name : 驾校名字
     * phoneNum : 驾校电话
     * uid : 驾校uid
     */

    private String address;
    private String city;
    private String name;
    private String phoneNum;
    private String uid;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
