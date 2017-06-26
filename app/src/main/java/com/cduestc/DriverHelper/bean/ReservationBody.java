package com.cduestc.DriverHelper.bean;

import com.google.gson.Gson;

/**
 * Created by c on 2017/3/13.
 */
public class ReservationBody {

    /**
     * appointDate : 2017-03-13T05:35:11.127Z
     * appointTime : 0
     * studentId : string
     * studentName : string
     * teacherId : string
     * teacherName : string
     */

    private String appointDate;
    private int appointTime;
    private String studentId;
    private String studentName;
    private String teacherId;
    private String teacherName;

    public String getAppointDate() {
        return appointDate;
    }

    public void setAppointDate(String appointDate) {
        this.appointDate = appointDate;
    }

    public int getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(int appointTime) {
        this.appointTime = appointTime;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public ReservationBody(String appointDate, int appointTime, String studentId, String studentName, String teacherId, String teacherName) {
        this.appointDate = appointDate;
        this.appointTime = appointTime;
        this.studentId = studentId;
        this.studentName = studentName;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
