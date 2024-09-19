package com.example.timekeeping.model;

import com.example.timekeeping.DB.DBHelper;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class CICO {
    private int id;
    private int user;
    private LocalDateTime ciTime;
    private LocalDateTime coTime;
    private int shift;


    public CICO(int id, int user, LocalDateTime ciTime, LocalDateTime coTime, int shift) {
        this.id = id;
        this.user = user;
        this.ciTime = ciTime;
        this.coTime = coTime;
        this.shift = shift;
    }

    public CICO(int user, LocalDateTime ciTime, int shift) {
        this.user = user;
        this.ciTime = ciTime;
        this.shift = shift;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
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

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

}
