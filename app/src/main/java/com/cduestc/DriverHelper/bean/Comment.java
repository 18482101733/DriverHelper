package com.cduestc.DriverHelper.bean;

import com.google.gson.Gson;

/**
 * Created by c on 2017/3/16.
 */
public class Comment  {
    private String discussMatter;
    private String senderId;
    private String receiverId;
    private String discussTime;

    public String getDiscussMatter() {
        return discussMatter;
    }

    public void setDiscussMatter(String discussMatter) {
        this.discussMatter = discussMatter;
    }

    public String getSenderID() {
        return senderId;
    }

    public void setSenderID(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getDiscussTime() {
        return discussTime;
    }

    public void setDiscussTime(String discussTime) {
        this.discussTime = discussTime;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
