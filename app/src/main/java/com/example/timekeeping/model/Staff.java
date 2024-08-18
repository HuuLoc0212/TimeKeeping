package com.example.timekeeping.model;

import java.util.Date;
import java.util.List;

public class Staff {
   private int id;
   private String name;
   private String usernanme;
   private String pass;
   private Date bOD;
   private String role;
   private Double basicSalary;
   private List<CICO> cicoHis;

    public Staff(int id, String name, String usernanme, String pass, Date bOD, String role, Double basicSalary, List<CICO> cicoHis) {
        this.id = id;
        this.name = name;
        this.usernanme = usernanme;
        this.pass = pass;
        this.bOD = bOD;
        this.role = role;
        this.basicSalary = basicSalary;
        this.cicoHis = cicoHis;
    }

    public Staff(String name, String usernanme, String pass, Date bOD, String role, Double basicSalary) {
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

    public Date getbOD() {
        return bOD;
    }

    public void setbOD(Date bOD) {
        this.bOD = bOD;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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
