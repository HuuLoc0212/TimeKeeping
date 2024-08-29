package com.example.timekeeping.DB;

public class LoginTable {
    private static final String TB_NAME="LOGIN";
    private static final String KEY_ACCOUNT="ACCOUNT";
    private static final String KEY_PASS="PASS";

    public static String getTbName() {
        return TB_NAME;
    }

    public static String getKeyAccount() {
        return KEY_ACCOUNT;
    }

    public static String getKeyPass() {
        return KEY_PASS;
    }

    public static String Create(){
        return  String.format("CREATE TABLE %s(" +
                "%s TEXT NOT NULL," +
                "%s TEXT NOT NULL)",TB_NAME,KEY_ACCOUNT,KEY_PASS);
    }
    public static String GetAll(){
        return String.format("SELECT * FROM %s ",TB_NAME);
    }
    public static String DeleteAll(){
        return String.format("DELETE FROM %s",TB_NAME);
    }
}