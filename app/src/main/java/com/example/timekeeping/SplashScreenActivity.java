package com.example.timekeeping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.model.Staff;

import java.time.LocalDate;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity {
    DBHelper db;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        this.context=this;

        db=new DBHelper(this);
        db.addFirstHR(new Staff(1,"ABC", LocalDate.of(2000,1,1),0,"HR@123","1",3000000.0));
        List<Staff> staffList= db.getAllStaffs();
        if(staffList.size()==0){
            Intent intent=new Intent(context,LoginActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(context,MainActivity.class);
            startActivity(intent);
        }
    }
}