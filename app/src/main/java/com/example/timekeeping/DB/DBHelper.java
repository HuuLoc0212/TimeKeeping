package com.example.timekeeping.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.timekeeping.model.Account;
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
        SQLiteDatabase db = this.getReadableDatabase();
        Staff staff = null;
        Cursor cursor = null;

        try {
            cursor = db.query(
                    StaffTable.getTbName(),
                    null,
                    StaffTable.getKeyId() + " = ?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve column indices
                int idIndex = cursor.getColumnIndexOrThrow(StaffTable.getKeyId());
                int nameIndex = cursor.getColumnIndexOrThrow(StaffTable.getKeyName());
                int bodIndex = cursor.getColumnIndexOrThrow(StaffTable.getKeyBod());
                int roleIndex = cursor.getColumnIndexOrThrow(StaffTable.getKeyRole());
                int accountIndex = cursor.getColumnIndexOrThrow(StaffTable.getKeyAccount());
                int passIndex = cursor.getColumnIndexOrThrow(StaffTable.getKeyPass());
                int salaryIndex = cursor.getColumnIndexOrThrow(StaffTable.getKeyBasicSalary());

                // Extract values
                int staffId = cursor.getInt(idIndex);
                String name = cursor.getString(nameIndex);
                String bodString = cursor.getString(bodIndex);
                LocalDate bOD = null;
                if (bodString != null) {
                    bOD = LocalDate.parse(bodString);
                }
                int role = cursor.getInt(roleIndex);
                String username = cursor.getString(accountIndex);
                String password = cursor.getString(passIndex);
                double basicSalary = cursor.getDouble(salaryIndex);

                // Create Staff object
                staff = new Staff(staffId, name, bOD, role, username, password, basicSalary);
            } else {
                // Handle case where no data is found
            }
        } catch (Exception e) {
            // Handle exceptions
        } finally {
            // Close cursor and database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
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
    public void deleteAll(){
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


    public void dropTableIfExit(SQLiteDatabase db,String tableName){
        String query=String.format("DROP TABLE IF EXISTS %s", tableName);
        db.execSQL(query);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createStaffTable(sqLiteDatabase);
        createLoginTable(sqLiteDatabase);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropTableIfExit(sqLiteDatabase,StaffTable.getTbName());
        dropTableIfExit(sqLiteDatabase,LoginTable.getTbName());
        onCreate(sqLiteDatabase);
    }
}
