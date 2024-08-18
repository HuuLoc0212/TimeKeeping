package com.example.timekeeping.model;

import com.google.type.DateTime;

public class CICO {
    private String id;
    private String user;
    private DateTime ciTime;
    private DateTime coTime;
    private String shift;
    private String state;

    public CICO(String id, String user, DateTime ciTime, DateTime coTime, String shift, String state) {
        this.id = id;
        this.user = user;
        this.ciTime = ciTime;
        this.coTime = coTime;
        this.shift = shift;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public DateTime getCiTime() {
        return ciTime;
    }

    public void setCiTime(DateTime ciTime) {
        this.ciTime = ciTime;
    }

    public DateTime getCoTime() {
        return coTime;
    }

    public void setCoTime(DateTime coTime) {
        this.coTime = coTime;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
