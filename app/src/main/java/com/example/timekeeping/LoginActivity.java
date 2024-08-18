package com.example.timekeeping;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    Context context;
    Button btnLogin;
    EditText email, password;
    boolean passwordVissble;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.InputEmailAdress);
        password=findViewById(R.id.Inputpassword);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MainActivity.class);
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int Right=2;
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
    }

}