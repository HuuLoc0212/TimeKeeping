package com.example.timekeeping.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Staff {
   private int id;
   private String name;
   private String usernanme;
   private String pass;
   private LocalDate bOD;
   private int role;
   private Double basicSalary;
   private List<CICO> cicoHis;

    public Staff(int id, String name, LocalDate bOD, int role, String usernanme, String pass, Double basicSalary) {
        this.id = id;
        this.name = name;
        this.usernanme = usernanme;
        this.pass = pass;
        this.bOD = bOD;
        this.role = role;
        this.basicSalary = basicSalary;
    }

    public Staff( String name, LocalDate bOD, int role, String usernanme, String pass, Double basicSalary) {
        this.id = id;
        this.name = name;
        this.usernanme = usernanme;
        this.pass = pass;
        this.bOD = bOD;
        this.role = role;
        this.basicSalary = basicSalary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsernanme() {
        return usernanme;
    }

    public void setUsernanme(String usernanme) {
        this.usernanme = usernanme;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getbOD() {
        return bOD;
    }

    public void setbOD(LocalDate bOD) {
        this.bOD = bOD;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public List<CICO> getCicoHis() {
        return cicoHis;
    }

    public void setCicoHis(List<CICO> cicoHis) {
        this.cicoHis = cicoHis;
    }
}
