package com.cduestc.DriverHelper.bean;

/**
 * Created by c on 2017/3/9.
 */
public class City {
    private int CityID;
    private String name;
    private int ProID;
    private int CitySort;

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProID() {
        return ProID;
    }

    public void setProID(int proID) {
        ProID = proID;
    }

    public int getCitySort() {
        return CitySort;
    }

    public void setCitySort(int citySort) {
        CitySort = citySort;
    }
}
