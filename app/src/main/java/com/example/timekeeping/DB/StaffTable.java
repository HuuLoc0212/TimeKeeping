package com.example.timekeeping.DB;

public class StaffTable {
    //table name
    final static private String TB_NAME="STAFF";
    //records
    final static private String KEY_ID="ID";
    final static private String KEY_NAME="NAME";
    final static private String KEY_BOD="BOD";
    final static private String KEY_ROLE="ROLE";
    final static private String KEY_ACCOUNT="ACCOUNT";
    final static private String KEY_PASS="PASS";
    final static private String KEY_BASIC_SALARY="BASIC_SALARY";
    public  static String getKeyName(){return KEY_NAME;}
    public  static String getKeyId(){return KEY_ID;}
    public  static String getTbName(){return TB_NAME;}
    public  static String getKeyBod(){return KEY_BOD;}
    public  static String getKeyRole(){return KEY_ROLE;}
    public  static String getKeyAccount(){return KEY_ACCOUNT;}
    public  static String getKeyPass(){return KEY_PASS;}
    public  static String getKeyBasicSalary(){return KEY_BASIC_SALARY;}
    //Create table
    public static String Create(){
        return String.format(
                "CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT NOT NULL, " +
                        "%s TEXT NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "%s TEXT UNIQUE," +
                        "%s TEXT NOT NULL," +
                        "%s NUMERIC NOT NULL)",
                TB_NAME,KEY_ID,KEY_NAME,KEY_BOD,KEY_ROLE,KEY_ACCOUNT,KEY_PASS,KEY_BASIC_SALARY);
    }
    //Get all staff
    public static String GetAll(){
        String query=String.format("SELECT * FROM %s ",TB_NAME);
        return query;
    }
}
