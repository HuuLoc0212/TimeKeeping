package com.example.timekeeping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.model.Role;
import com.example.timekeeping.model.Staff;

import java.time.LocalDate;

public class SplashScreenActivity extends AppCompatActivity {
    DBHelper db;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        this.context=this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db=new DBHelper(context);
                //default records
                //Staff
                db.addFirstHR(new Staff(0,"ABC", LocalDate.of(2000,1,1),0,"HR@123","1",3000000.0));
                db.addFirstHR(new Staff(2,"Lạc Tiến Huy", LocalDate.of(2000,4,13),2,"tienhuylac1304@gmail.com","1",8000000.0));
                db.addFirstHR(new Staff(1,"Nguyễn Hữu Lộc", LocalDate.of(2000,1,1),1,"huuloc@gmail.com","1",8000000.0));
                //role
                db.addRole(new Role(0, "Human Resources"));
                db.addRole(new Role(1, "Front-end Developer"));
                db.addRole(new Role(2, "Back-end Developer"));

                if(db.getAllAccounts().size()==0){
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent=new Intent(context,MainActivity.class);
//                    if(db.getStaffByAccount(db.getAllAccounts().get(0).getAccount()).getRole()==0){
//                        intent= new Intent(context, HRMainActivity.class);
//                    }
                    startActivity(intent);
                }
            }
        },500);


    }
}