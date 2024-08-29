package com.example.timekeeping.model;

import com.google.type.Date;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Shift {
    private int id;
    private LocalDateTime date;
    private LocalDateTime start;
    private LocalDateTime end;
    private int restTime;

    public Shift(int id,LocalDateTime date, LocalDateTime start, LocalDateTime end,int restTime) {
        this.id = id;
        this.date=date;
        this.start = start;
        this.end = end;
        this.restTime=restTime;
    }
    public Shift(int id,LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.date=date;
        this.start = start;
        this.end = end;
        this.restTime=3600;
    }
    public Shift(LocalDateTime date, LocalDateTime start, LocalDateTime end) {
        this.date=date;
        this.start = start;
        this.end = end;
        this.restTime=3600;
    }
    public Shift() {}


    public int getRestTime() {
        return restTime;
    }

    public void setRestTime(int restTime) {
        this.restTime = restTime;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
