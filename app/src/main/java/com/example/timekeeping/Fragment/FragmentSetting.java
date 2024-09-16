package com.example.timekeeping.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.R;
import com.example.timekeeping.SplashScreenActivity;

public class FragmentSetting extends Fragment {

    private LinearLayout btnLogout;
    private DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_setting, container, false);

        db=new DBHelper(getActivity());
        btnLogout=v.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteAllAccount();
                Intent intent= new Intent(getActivity(), SplashScreenActivity.class);
                startActivity(intent);
            }
        });
        return v;

    }
}