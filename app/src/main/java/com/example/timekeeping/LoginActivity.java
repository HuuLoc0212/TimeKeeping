package com.example.timekeeping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.Staff;

import java.util.List;
import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {
    DBHelper db;
    Context context;
    Button btnLogin;
    EditText email, password;
    TextView txt_account_warning,txt_pass_warning;
    boolean passwordVissble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.context=this;
        db=new DBHelper(this);
        email=findViewById(R.id.InputEmailAdress);
        password=findViewById(R.id.Inputpassword);
        btnLogin=findViewById(R.id.btnLogin);
        txt_account_warning=findViewById(R.id.txt_account_warning);
        txt_pass_warning=findViewById(R.id.txt_pass_warning);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Staff staff;
                // check edit text is empty
                if(TextUtils.isEmpty(email.getText())||TextUtils.isEmpty(password.getText()))
                {
                    if(TextUtils.isEmpty(email.getText())) txt_account_warning.setText("Please fill the email!!");
                    if (TextUtils.isEmpty(password.getText()))txt_pass_warning.setText("Please fill the password!!");
                }
                //check account is correct
                else {
                    if(db.getStaffByAccount(email.getText().toString())==null){

                        txt_account_warning.setText("Email incorrect!!");

                    }else {staff=db.getStaffByAccount(email.getText().toString());
                        if(staff.getPass()!=password.getText().toString()){
                        txt_pass_warning.setText("Password incorrect!!");
                    }else {
                        db.addLogin(new Account(email.getText().toString(),password.getText().toString()));
                        Intent intent=new Intent(context,MainActivity.class);
                        startActivity(intent);
                    }
                }}

            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
                txt_pass_warning.setText("");
                if(event.getAction()==MotionEvent.ACTION_UP)
                    if(event.getRawX()>=password.getRight()-password.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=password.getSelectionEnd();
                        if(passwordVissble){
                            //set drable img here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.hide_icon, 0);
                            //for hide password
                            password.setTransformationMethod((PasswordTransformationMethod.getInstance()));
                            passwordVissble=false;
                    }else {
                            //set drable img here
                            password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,R.drawable.pw_icon, 0);
                            //for show password
                            password.setTransformationMethod((HideReturnsTransformationMethod.getInstance()));
                            passwordVissble=true;
                        }
                        password.setSelection(selection);
                        return true;
                    }

                return false;

            }
        });
        email.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    txt_account_warning.setText("");
                return false;
            }
        });
    }

}