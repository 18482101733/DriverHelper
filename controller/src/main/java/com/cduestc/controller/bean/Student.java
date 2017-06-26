package com.cduestc.controller.bean;

/**
 * Created by c on 2017/5/2.
 */
public class Student extends User {

    private String stuElse;
    private String admissionTime;
    private int appointTnum;
    private int appointFnum;

    public String getStuElse() {
        return stuElse;
    }

    public String getAdmissionTime() {
        return admissionTime;
    }

    public int getAppointTnum() {
        return appointTnum;
    }

    public int getAppointFnum() {
        return appointFnum;
    }
}
