package com.example.timekeeping.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.Role;
import com.example.timekeeping.model.Staff;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    private static final String DATABASE_NAME="TimeKeeping";
    private static final int DATABASE_VERSION=1;
    //Create database
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    //Staff
    public void createStaffTable(SQLiteDatabase db){
        db.execSQL(StaffTable.Create());
    }
    public void addStaff(Staff staff){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StaffTable.getKeyName(), staff.getName());
        values.put(StaffTable.getKeyBod(), staff.getbOD().toString());
        values.put(StaffTable.getKeyRole(), staff.getRole());
        values.put(StaffTable.getKeyAccount(), staff.getUsernanme());
        values.put(StaffTable.getKeyPass(), "1");
        values.put(StaffTable.getKeyBasicSalary(), staff.getBasicSalary());

        db.insert(StaffTable.getTbName(), null, values);
        db.close();
    }
    public void addFirstHR(Staff staff){

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(StaffTable.getKeyId(),staff.getId());
            values.put(StaffTable.getKeyName(), staff.getName());
            values.put(StaffTable.getKeyBod(), staff.getbOD().toString());
            values.put(StaffTable.getKeyRole(), staff.getRole());
            values.put(StaffTable.getKeyAccount(), staff.getUsernanme());
            values.put(StaffTable.getKeyPass(), staff.getPass());
            values.put(StaffTable.getKeyBasicSalary(), staff.getBasicSalary());

            db.insert(StaffTable.getTbName(), null, values);

            db.close();


    }
    public Staff getStaffByID(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Staff staff = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    StaffTable.getTbName(), // Table name
                    null, // All columns
                    StaffTable.getKeyId() + " = ?", // WHERE clause
                    new String[]{String.valueOf(id)}, // WHERE args
                    null, // GROUP BY clause
                    null, // HAVING clause
                    null // ORDER BY clause
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                staff = new Staff(cursor.getInt(0), cursor.getString(1), LocalDate.parse(cursor.getString(2)),cursor.getInt(3),cursor.getString(4),cursor.getString(5),cursor.getDouble(6));
            }
        } catch (Exception e) {
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return staff;
    }
    public Staff getStaffByAccount(String account) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Staff staff = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    StaffTable.getTbName(), // Table name
                    null, // All columns
                    StaffTable.getKeyAccount() + " = ?", // WHERE clause
                    new String[]{account}, // WHERE args
                    null, // GROUP BY clause
                    null, // HAVING clause
                    null // ORDER BY clause
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                staff = new Staff(cursor.getInt(0), cursor.getString(1), LocalDate.parse(cursor.getString(2)),cursor.getInt(3),cursor.getString(4),cursor.getString(5),cursor.getDouble(6));
            }
        } catch (Exception e) {
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return staff;
    }
    public List<Staff> getAllStaffs() {
        List<Staff>  staffList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(StaffTable.GetAll(), null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Staff staff = new Staff(cursor.getInt(0), cursor.getString(1), LocalDate.parse(cursor.getString(2)),cursor.getInt(3),cursor.getString(4),cursor.getString(5),cursor.getDouble(6));
            staffList.add(staff);
            cursor.moveToNext();
        }
        return staffList;
    }
    public Role getRoleById(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Role role = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    RoleTable.getTbName(), // Table name
                    null, // All columns
                    RoleTable.getKeyId() + " = ?", // WHERE clause
                    new String[]{String.valueOf(id)}, // WHERE args
                    null, // GROUP BY clause
                    null, // HAVING clause
                    null // ORDER BY clause
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                role = new Role(cursor.getInt(0), cursor.getString(1));
            }
        } catch (Exception e) {
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return role;
    }
    //login
    public void createLoginTable(SQLiteDatabase db) {
        try {
            db.execSQL(LoginTable.Create());
            Log.d("DB_SUCCESS", "Login table created successfully.");
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error creating login table", e);
        }
    }
    public void addLogin(Account account){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(LoginTable.getKeyAccount(),account.getAccount());
        values.put(LoginTable.getKeyPass(),account.getPass());

        db.insert(LoginTable.getTbName(),null,values);
        db.close();
    }
    public void deleteAllAccount(){
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL(LoginTable.DeleteAll());
        db.close();
    }
    public List<Account> getAllAccounts() {
        List<Account>  accountList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(LoginTable.GetAll(), null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Account account = new Account(cursor.getInt(0), cursor.getString(1),cursor.getString(2));
            accountList.add(account);
            cursor.moveToNext();
        }
        return accountList;
    }

    //role
    public void createRoleTable(SQLiteDatabase db) {
        try {
            db.execSQL(RoleTable.Create());
            Log.d("DB_SUCCESS", "Login table created successfully.");
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error creating login table", e);
        }
    }
    public void addRole(Role role){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(RoleTable.getKeyId(), role.getId());
        values.put(RoleTable.getKeyName(),role.getName());;

        db.insert(RoleTable.getTbName(),null,values);
        db.close();
    }


    public void dropTableIfExit(SQLiteDatabase db,String tableName){
        String query=String.format("DROP TABLE IF EXISTS %s", tableName);
        db.execSQL(query);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createStaffTable(sqLiteDatabase);
        createLoginTable(sqLiteDatabase);
        createRoleTable(sqLiteDatabase);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropTableIfExit(sqLiteDatabase,StaffTable.getTbName());
        dropTableIfExit(sqLiteDatabase,LoginTable.getTbName());
        dropTableIfExit(sqLiteDatabase,RoleTable.getTbName());
        onCreate(sqLiteDatabase);
    }
}
