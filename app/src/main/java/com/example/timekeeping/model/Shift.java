package com.example.timekeeping.model;

import com.google.type.Date;
import com.google.type.DateTime;

public class Shift {
    private String id;
    private DateTime start;
    private DateTime end;

    public Shift(String id, DateTime start, DateTime end) {
        this.id = id;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }
}
