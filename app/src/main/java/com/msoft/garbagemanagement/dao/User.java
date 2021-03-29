package com.msoft.garbagemanagement.dao;

import android.util.Log;

public class User {
    String userid,name,city,mobile;
    boolean isConsumer;

    @Override
    public String toString() {
        return "User{" +
                "userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", mobile='" + mobile + '\'' +
                ", isConsumer=" + isConsumer +
                ", isValid=" + valid +
                '}';
    }

    public User(String userid, String name, String city, String mobile, boolean isConsumer) {
        this.userid = userid;
        this.name = name;
        this.city = city;
        this.mobile = mobile;
        this.isConsumer = isConsumer;
    }

    boolean valid;

    public boolean isValid() {
        validate();
        return valid;
    }

    private static final String TAG = "User";
    public void validate(){
        valid =name!=null && name.length()!=0 && city!=null && city.length()!=0 && mobile!=null && mobile.length()!=0;
    }

    public User() {
    }

    public User(String name, String city, String mobile, boolean isConsumer) {
        this.name = name;
        this.city = city;
        this.mobile = mobile;
        this.isConsumer = isConsumer;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isConsumer() {
        return isConsumer;
    }

    public void setConsumer(boolean consumer) {
        isConsumer = consumer;
    }
}
