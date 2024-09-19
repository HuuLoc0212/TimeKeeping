package com.example.timekeeping.DB;

public class CICOTable {
    private static final String TB_NAME="CICO";
    private static final String KEY_ID="ID";
    private static final String KEY_USER="USER";
    private static final String KEY_CI="CI";
    private static final String KEY_CO="CO";
    private static final String KEY_SHIFT="SHIFT";

    public static String getTbName() {
        return TB_NAME;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

    public static String getKeyUser() {
        return KEY_USER;
    }

    public static String getKeyCi() {
        return KEY_CI;
    }

    public static String getKeyCo() {
        return KEY_CO;
    }

    public static String getKeyShift() {
        return KEY_SHIFT;
    }

    public static String Create(){
        return String.format(
                "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s INTEGER NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT, " +
                        "%s INTEGER NOT NULL,)",
                TB_NAME,KEY_ID,KEY_USER,KEY_CI,KEY_CO,KEY_SHIFT);
    }
    public static String GetAll(){
        return String.format("SELECT * FROM %s ",TB_NAME);
    }
    public static String DeleteAll(){
        return String.format("DELETE FROM %s",TB_NAME);
    }
}