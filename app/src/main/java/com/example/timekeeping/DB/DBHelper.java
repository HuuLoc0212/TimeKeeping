package com.example.timekeeping.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    private static final String DATABASE_NAME="TimeKeeping.db";
    private static final int DATABASE_VERSION=1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    public void createStaffTable(SQLiteDatabase db){
        String create_students_table = String.format("CREATE TABLE STAFF(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)");
        db.execSQL(create_students_table);
    }
    public SQLiteDatabase OpenDB(){
        return context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
