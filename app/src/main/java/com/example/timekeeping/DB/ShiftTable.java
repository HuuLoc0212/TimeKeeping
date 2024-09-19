package com.example.timekeeping.DB;

public class ShiftTable {
    private static final String TB_NAME="SHIFT";
    private static final String KEY_ID="ID";
    private static final String KEY_DATE="DATE";
    private static final String KEY_START="START";
    private static final String KEY_END="END";
    private static final String KEY_BREAKTIME="BREAKTIME";

    public static String getTbName() {
        return TB_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getKeyDate() {
        return KEY_DATE;
    }

    public static String getKeyStart() {
        return KEY_START;
    }

    public static String getKeyEnd() {
        return KEY_END;
    }


    public static String Create(){
        return String.format(
                "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT UNIQUE NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL) " ,
                TB_NAME,KEY_ID,KEY_DATE,KEY_START,KEY_END);
    }
    public static String GetAll(){
        return String.format("SELECT * FROM %s ",TB_NAME);
    }
    public static String DeleteAll(){
        return String.format("DELETE FROM %s",TB_NAME);
    }
}