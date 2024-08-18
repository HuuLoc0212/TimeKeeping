package com.example.timekeeping.model;

import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CICO {
    private int id;
    private String user;
    private LocalDateTime ciTime;
    private LocalDateTime coTime;
    private String shift;
    private String state;

    public CICO(int id, String user, LocalDateTime ciTime, LocalDateTime coTime, String shift, String state) {
        this.id = id;
        this.user = user;
        this.ciTime = ciTime;
        this.coTime = coTime;
        this.shift = shift;
        this.state = state;
    }

    public CICO(String user, LocalDateTime ciTime, LocalDateTime coTime, String shift, String state) {
        this.user = user;
        this.ciTime = ciTime;
        this.coTime = coTime;
        this.shift = shift;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getCiTime() {
        return ciTime;
    }

    public void setCiTime(LocalDateTime ciTime) {
        this.ciTime = ciTime;
    }

    public LocalDateTime getCoTime() {
        return coTime;
    }

    public void setCoTime(LocalDateTime coTime) {
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
