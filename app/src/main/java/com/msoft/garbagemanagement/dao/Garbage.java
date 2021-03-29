package com.msoft.garbagemanagement.dao;

import java.util.ArrayList;

public class Garbage {
    private String garbageType;
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    private ArrayList<String> types;
    private float size,price;
    boolean valid;
    double lat, log;

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }


    public Garbage() {
    }

    public boolean isValid(){
        valid=(garbageType!=null && garbageType.length()!=0)  && size!=0 && price!=0 && lat!=0 && log!=0;
        return valid;
    }

    public String getGarbageType() {
        return garbageType;
    }

    public void setGarbageType(String garbageType) {
        this.garbageType = garbageType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public Garbage(String garbageType, String id, String userId, ArrayList<String> types, float size, float price, double lat, double log) {
        this.garbageType = garbageType;
        this.id = id;
        this.userId = userId;
        this.types = types;
        this.size = size;
        this.price = price;
        this.lat = lat;
        this.log = log;
    }
}

