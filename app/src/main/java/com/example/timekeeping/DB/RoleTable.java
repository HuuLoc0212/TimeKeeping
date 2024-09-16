package com.example.timekeeping.DB;

public class RoleTable {
    final static private String TB_NAME="ROLE";
    //records
    final static private String KEY_ID="ID";
    final static private String KEY_NAME="NAME";

    public static String getTbName(){return TB_NAME;}
    public static String getKeyId(){return KEY_ID;}
    public static String getKeyName(){return KEY_NAME;}

    public static String Create(){
        return String.format(
                "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL)",
                TB_NAME,KEY_ID,KEY_NAME);
    }
    public static String GetAll(){
        String query=String.format("SELECT * FROM %s ",TB_NAME);
        return query;
    }
}