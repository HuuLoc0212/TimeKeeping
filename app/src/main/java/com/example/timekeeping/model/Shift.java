package com.example.timekeeping.model;

import com.google.type.Date;
import com.google.type.DateTime;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Shift {
    private int id;
    private LocalDate date;
    private LocalTime start;
    private LocalTime end;
    private Double breakTime;

    public Shift(int id,LocalDate date, LocalTime start, LocalTime end) {
        this.id = id;
        this.date=date;
        this.start = start;
        this.end = end;
        this.breakTime=8.0-(Duration.between(start,end).toHours());
    }

    public Shift(LocalDate date, LocalTime start, LocalTime end) {
        this.date = date;
        this.start = start;
        this.end = end;
        this.breakTime=8.0-(Duration.between(start,end).toHours());
    }


    public Shift() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public Double getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(Double restTime) {
        this.breakTime = restTime;
    }
}
