package com.example.timekeeping.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.CICO;
import com.example.timekeeping.model.Role;
import com.example.timekeeping.model.Shift;
import com.example.timekeeping.model.Staff;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    // luu tru thong tin cua bang
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
    // truy van du lieu tu bang
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
            Log.d("DB_SUCCESS", "Role table created successfully.");
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error creating Role table", e);
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

    //shift
    public void createShiftTable(SQLiteDatabase db) {
        try {
            db.execSQL(ShiftTable.Create());
            Log.d("DB_SUCCESS", "Shift table created successfully.");
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error creating Shift table", e);
        }
    }
    public void addShift(Shift shift){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(ShiftTable.getKeyDate(),shift.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        values.put(ShiftTable.getKeyStart(),shift.getStart().format(DateTimeFormatter.ofPattern("HH:mm")));
        values.put(ShiftTable.getKeyEnd(),shift.getEnd().format(DateTimeFormatter.ofPattern("HH:mm")));

        db.insert(ShiftTable.getTbName(),null,values);
        db.close();
    }
    public Shift getShiftByDate(LocalDate date) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Shift shift = null;

        try {
            db = this.getReadableDatabase();

            // Định dạng LocalDate thành chuỗi theo định dạng lưu trong database
            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            cursor = db.query(
                    ShiftTable.getTbName(), // Table name
                    null, // All columns
                    ShiftTable.getKeyDate() + " = ?", // WHERE clause
                    new String[]{formattedDate}, // WHERE args
                    null, // GROUP BY clause
                    null, // HAVING clause
                    null // ORDER BY clause
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                shift = new Shift(
                        cursor.getInt(0), // ID or primary key
                        LocalDate.parse(cursor.getString(1), DateTimeFormatter.ofPattern("dd/MM/yyyy")), // Date column
                        LocalTime.parse(cursor.getString(2), DateTimeFormatter.ofPattern("HH:mm")), // Start time
                        LocalTime.parse(cursor.getString(3), DateTimeFormatter.ofPattern("HH:mm")) // End time
                );
            }
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return shift;
    }
    public Shift getShiftById(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Shift shift = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    ShiftTable.getTbName(), // Table name
                    null, // All columns
                    ShiftTable.getKeyId() + " = ?", // WHERE clause
                    new String[]{String.valueOf(id)}, // WHERE args
                    null, // GROUP BY clause
                    null, // HAVING clause
                    null // ORDER BY clause
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                shift = new Shift(cursor.getInt(0),
                        LocalDate.parse(cursor.getString(1),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        LocalTime.parse(cursor.getString(2),DateTimeFormatter.ofPattern("HH:mm")),
                        LocalTime.parse(cursor.getString(3),DateTimeFormatter.ofPattern("HH:mm")));
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

        return shift;
    }
    public List<Shift> getAllShifts() {
        List<Shift>  shiftList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(ShiftTable.GetAll(), null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Shift shift = new Shift(cursor.getInt(0),
                    LocalDate.parse(cursor.getString(1),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    LocalTime.parse(cursor.getString(2),DateTimeFormatter.ofPattern("HH:mm")),
                    LocalTime.parse(cursor.getString(3),DateTimeFormatter.ofPattern("HH:mm")));
            shiftList.add(shift);
            cursor.moveToNext();
        }
        return shiftList;
    }

    //CICO
    public void createCICOTable(SQLiteDatabase db) {
        try {
            db.execSQL(CICOTable.Create());
            Log.d("DB_SUCCESS", "CICO table created successfully.");
        } catch (SQLException e) {
            Log.e("DB_ERROR", "Error creating CICO table", e);
        }
    }
    public List<CICO> getCICOS(int userID) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<CICO>cicoList=new ArrayList<>();

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    CICOTable.getTbName(), // Table name
                    null, // All columns
                    CICOTable.getKeyUser() + " = ?", // WHERE clause
                    new String[]{String.valueOf(userID)}, // WHERE args
                    null, // GROUP BY clause
                    null, // HAVING clause
                    CICOTable.getKeyId() + " DESC"  // ORDER BY clause
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                while(cursor.isAfterLast() == false) {
                    CICO cico;
                    if(cursor.getString(3)!=null){
                        cico = new CICO(cursor.getInt(0),
                                cursor.getInt(1),
                                LocalDateTime.parse(cursor.getString(2),DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                                LocalDateTime.parse(cursor.getString(3),DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                                cursor.getInt(4));
                    }
                    else {
                        cico = new CICO(cursor.getInt(0),
                                cursor.getInt(1),
                                LocalDateTime.parse(cursor.getString(2), DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                                null,
                                cursor.getInt(4));
                    }
                    cicoList.add(cico);
                    cursor.moveToNext();
                }
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

        return cicoList;
    }
    public CICO getCICOById(int id) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        CICO cico = null;

        try {
            db = this.getReadableDatabase();
            cursor = db.query(
                    CICOTable.getTbName(), // Table name
                    null, // All columns
                    CICOTable.getKeyId() + " = ?", // WHERE clause
                    new String[]{String.valueOf(id)}, // WHERE args
                    null, // GROUP BY clause
                    null, // HAVING clause
                    null // ORDER BY clause
            );

            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                if(cursor.getString(3)!=null){
                    cico = new CICO(cursor.getInt(0),
                            cursor.getInt(1),
                            LocalDateTime.parse(cursor.getString(2),DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                            LocalDateTime.parse(cursor.getString(3),DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                            cursor.getInt(4));
                }
                else {
                    cico = new CICO(cursor.getInt(0),
                            cursor.getInt(1),
                            LocalDateTime.parse(cursor.getString(2), DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")),
                            null,
                            cursor.getInt(4));
                }

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

        return cico;
    }
    public void addCICO(CICO cico){
        SQLiteDatabase db= this.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(CICOTable.getKeyUser(), cico.getUser());
        values.put(CICOTable.getKeyCi(),cico.getCiTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));
        values.put(CICOTable.getKeyShift(),cico.getShift());

        db.insert(CICOTable.getTbName(),null,values);
        db.close();
    }
    public int checkout(CICO cico) {
        SQLiteDatabase db = null;
        int rowsAffected = 0;

        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(CICOTable.getKeyCo(), cico.getCoTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")));

            // Điều kiện để xác định bản ghi cần cập nhật (ví dụ theo ID hoặc user)
            String whereClause = CICOTable.getKeyId() + " = ?";
            String[] whereArgs = {String.valueOf(cico.getId())};

            // Thực hiện update, trả về số bản ghi bị ảnh hưởng
            rowsAffected = db.update(CICOTable.getTbName(), values, whereClause, whereArgs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return rowsAffected; // Trả về số hàng bị ảnh hưởng
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
        createShiftTable(sqLiteDatabase);
        createCICOTable(sqLiteDatabase);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropTableIfExit(sqLiteDatabase,StaffTable.getTbName());
        dropTableIfExit(sqLiteDatabase,LoginTable.getTbName());
        dropTableIfExit(sqLiteDatabase,RoleTable.getTbName());
        dropTableIfExit(sqLiteDatabase,ShiftTable.getTbName());
        dropTableIfExit(sqLiteDatabase,CICOTable.getTbName());
        onCreate(sqLiteDatabase);
    }
}
