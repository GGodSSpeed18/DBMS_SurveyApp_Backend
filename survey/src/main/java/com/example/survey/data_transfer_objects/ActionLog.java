package com.example.survey.data_transfer_objects;

import java.util.Date;

public class ActionLog {
    private Date date;
    private long user_id;
    private String device;
    private String location;
    private Boolean login;

    public ActionLog() {
    }

    public ActionLog(Date date, long user_id, String device, String location, Boolean login) {
        this.date = date;
        this.user_id = user_id;
        this.device = device;
        this.location = location;
        this.login = login;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

}
