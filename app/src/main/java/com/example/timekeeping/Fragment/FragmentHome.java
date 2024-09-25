package com.example.timekeeping.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.R;
import com.example.timekeeping.adapter.ListRecentAdapter;
import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.CICO;
import com.example.timekeeping.model.Shift;
import com.example.timekeeping.model.Staff;
import com.example.timekeeping.untils.EnableCallback;
import com.example.timekeeping.untils.NetworkCheck;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class FragmentHome extends Fragment {
    private TextView txtName, txtRole, txtCurrentShift, txtStart, txtEnd, txtCI, txtCO, txtShift;
    private LinearLayout btnCheckin, btnCheckout;
    private DBHelper db;
    private CICO todayCICO=null;
    private Shift todayShift=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        txtName=view.findViewById(R.id.txtName);
        txtRole=view.findViewById(R.id.txtPosition);
        txtCurrentShift=view.findViewById(R.id.tvCurrentshift);
        txtStart=view.findViewById(R.id.tvStart);
        txtEnd=view.findViewById(R.id.tvEnd);
        txtCI=view.findViewById(R.id.tvCI);
        txtCO=view.findViewById(R.id.tvCO);
        txtShift=view.findViewById(R.id.tvShift);
        btnCheckin=view.findViewById(R.id.btnCheckin);
        btnCheckout=view.findViewById(R.id.btnCheckout);

        db= new DBHelper(getActivity());

        //get staff information
        List<Account> lstAccount= db.getAllAccounts();
        Account account= lstAccount.get(lstAccount.size()-1);
        Staff staff= db.getStaffByAccount(account.getAccount());

        //get staff's check-in
        List<CICO> lstCICO=db.getCICOS(staff.getId());

        //get today shift
        todayShift=db.getShiftByDate(LocalDate.now());

        //set staff information
        txtName.setText(staff.getName());
        txtRole.setText(db.getRoleById(staff.getRole()).getName());

        //set shift information
        if(todayShift==null){
            if(LocalDateTime.now().getDayOfWeek()!= DayOfWeek.SUNDAY) {

                if (LocalDateTime.now().getDayOfWeek()!= DayOfWeek.SATURDAY)
                //today isn't Saturday
                {
                    txtCurrentShift.setText(LocalDate.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    txtStart.setText(LocalTime.of(8,30)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                    txtEnd.setText(LocalTime.of(17,30)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                else
                //today is Saturday
                {
                    txtCurrentShift.setText(LocalDate.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    txtStart.setText(LocalTime.of(8,30)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                    txtEnd.setText(LocalTime.of(12,00)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                todayShift=new Shift(LocalDate.parse(txtCurrentShift.getText(),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        LocalTime.parse(txtStart.getText(),DateTimeFormatter.ofPattern("HH:mm")),
                        LocalTime.parse(txtEnd.getText(),DateTimeFormatter.ofPattern("HH:mm")));
                db.addShift(todayShift);
            }
            else{
                btnCheckin.setEnabled(false);
                btnCheckout.setEnabled(false);
                txtCurrentShift.setText("To day is weekend!!!");
                txtStart.setText("None");
                txtEnd.setText("None");
            }
        }
        else {
            txtCurrentShift.setText(todayShift.getDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtStart.setText(todayShift.getStart()
                    .format(DateTimeFormatter.ofPattern("HH:mm")));
            txtEnd.setText(todayShift.getEnd()
                    .format(DateTimeFormatter.ofPattern("HH:mm")));
        }


        //check today is checked-in
        for(CICO cico : lstCICO){
            if(cico.getShift()==todayShift.getId()){
                todayCICO=cico;
                break;
            }
        }
        //show today's check-in and set check-in, check-out buttons state
        if(todayCICO!=null){
            btnCheckin.setEnabled(false);
            btnCheckout.setEnabled(true);
            txtShift.setText(todayShift
                    .getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtCI.setText(todayCICO.getCiTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            if(todayCICO.getCoTime()!=null){
                btnCheckout.setEnabled(false);
                txtCO.setText(todayCICO.getCoTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            else {
                txtCO.setText("None");
            }
        }
        else {

            btnCheckin.setEnabled(true);
            btnCheckout.setEnabled(false);
        }

        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NetworkCheck networkCheck = new NetworkCheck(getActivity());
//                networkCheck.getPublicIP(new EnableCallback() {
//                    @Override
//                    public void onResult(boolean enable) {
//                        if (enable) {
//                            if(true){
                            if(LocalTime.now().isBefore(todayShift.getEnd())){
                                CICO cico=new CICO(staff.getId(),
                                        LocalDateTime.now(),
                                        db.getShiftByDate(LocalDate.now()).getId());
                                db.addCICO(cico);
                                todayCICO=cico;
                                txtShift.setText(db.getShiftById(cico.getShift()).getDate()
                                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                txtCI.setText(cico.getCiTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                                txtCO.setText("None");
                                btnCheckin.setEnabled(false);
                                btnCheckout.setEnabled(true);
                              showSnackbar("Check-in succeed!!!","success");
                            }
                            else {
                                showSnackbar("Too late for check-in now!!!","error");
                            }
                        }
//                        else {
//                            showSnackbar("No permission to execute!!!","error");
//
//                        }
//                    }
//                });
//            }
        });
        //ss tg check out co wa gio lm hay ko
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkCheck networkCheck= new NetworkCheck(getActivity());
                networkCheck.getPublicIP(new EnableCallback() {
                    @Override
                    public void onResult(boolean enable) {
                        if(enable){
                            if(!LocalDate.now().isAfter(todayShift.getDate())){
                                List<CICO> cicos=db.getCICOS(staff.getId());
                                CICO cico= cicos.get(cicos.size()-1);
                                cico.setCoTime(LocalDateTime.now());
                                int rows = db.checkout(cico);
                                if (rows > 0) {
                                    txtCO.setText(cico.getCoTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                                    showSnackbar("Check-out succeed!!!","warning");
                                    btnCheckout.setEnabled(false);
                                } else {
                                    showSnackbar("Check-out failure!!!","error");
                                }
                            }
                            else {
                                showSnackbar("Too late for check-out now!!!","error");

                            }
                        }
                        else {
                            showSnackbar("No permission to execute!!!","error");
                        }
                    }
                });

            }
        });

        return view;
    }
    private  void  showSnackbar(String message, String type ) {

        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        if (type.equals("error")) {
            snackbar.setTextColor(getResources().getColor(R.color.text_red));
            snackbar.setBackgroundTint(getResources().getColor(R.color.red));
        } else if (type.equals("success")) {
                snackbar.setTextColor(getResources().getColor(R.color.text_green));
                snackbar.setBackgroundTint(getResources().getColor(R.color.green));
            }
            else if (type.equals("warning")) {
            snackbar.setTextColor(getResources().getColor(R.color.text_yellow));
                snackbar.setBackgroundTint(getResources().getColor(R.color.yellow));
            } else {
            snackbar.setTextColor(getResources().getColor(R.color.text_blue));
                snackbar.setBackgroundTint(getResources().getColor(R.color.blue));
            }
         //   snackbar.getView().setBackground(getResources().getDrawable(R.drawable.bottom_background));
        View snackbarView = snackbar.getView();
        ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT; // Set width to wrap content
        snackbarView.setLayoutParams(params);
        if (params instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()); // Set bottom margin to 20dp// Set gravity to center
            int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()); // Example top padding
            int paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()); // Example bottom padding
            int paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()); // Example left padding
            int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()); // Example right padding
            snackbarView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            snackbarView.setLayoutParams(layoutParams);
        } else {
            snackbarView.setLayoutParams(params);
        }
            snackbar.show();

        }
    private  void  showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message,0);
        snackbar.setBackgroundTint(getResources().getColor(R.color.grey));
        snackbar.show();
    }

}