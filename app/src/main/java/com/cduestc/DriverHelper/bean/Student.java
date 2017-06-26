package com.cduestc.DriverHelper.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by c on 2017/3/4.
 */
public class Student extends User implements Serializable{

    @Expose private String admissionTime;
    @Expose private String phone;

    @SerializedName("stuElse")
    @Expose private String stuInfo;

    private String studentIcon;

    public Student(String uid, String address, String idNum, String schoolName, String name, String password) {
        this.uid = uid;
        this.address = address;
        this.idNum = idNum;
        this.schoolName = schoolName;
        this.name = name;
        this.password = password;
    }

    public String getStuInfo() {
        return stuInfo;
    }

    public void setStuInfo(String stuInfo) {
        this.stuInfo = stuInfo;
    }

    public String getAdmissionTime() {
        return admissionTime;
    }

    public void setAdmissionTime(String admissionTime) {
        this.admissionTime = admissionTime;
    }

    public String getStudentIcon() {
        return studentIcon;
    }

    public void setStudentIcon(String studentIcon) {
        this.studentIcon = studentIcon;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String toRegisterString(){

        return "{\n" +
                "  \"address\":\"" +getAddress()+ "\"," +
                "  \"idNum\": \""+getIdNum()+"\"," +
                "  \"name\": \""+getName()+"\"," +
                "  \"password\": \""+getPassword()+"\"," +
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
                "  \"schoolName\": \""+getSchoolName()+"\"," +
                "  \"phone\": \""+getPhone()+"\"," +
                "  \"stuElse\": \""+getStuInfo()+"\"," +
                "  \"uid\": \""+getUid()+"\"" +
                "}";
    }
}
