package com.example.timekeeping.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FragmentHome extends Fragment {

    private ListRecentAdapter listRecentAdapter;
    private ListView lstHis;
    private TextView txtName, txtRole, txtCurrentShift, txtStart, txtEnd;
    private LinearLayout btnCheckin, btnCheckout;
    private DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        db= new DBHelper(getActivity());

        lstHis= view.findViewById(R.id.lstRecent);
        txtName=view.findViewById(R.id.txtName);
        txtRole=view.findViewById(R.id.txtPosition);
        txtCurrentShift=view.findViewById(R.id.tvCurrentshift);
        txtStart=view.findViewById(R.id.tvStart);
        txtEnd=view.findViewById(R.id.tvEnd);
        btnCheckin=view.findViewById(R.id.btnCheckin);
        btnCheckout=view.findViewById(R.id.btnCheckout);

        //get staff information
        List<Account> lstAccount= db.getAllAccounts();
        Account account= lstAccount.get(lstAccount.size()-1);
        Staff staff= db.getStaffByAccount(account.getAccount());

        //set staff information
        txtName.setText(staff.getName());
        txtRole.setText(db.getRoleById(staff.getRole()).getName());

        //set shift information
        if(LocalDateTime.now().getDayOfWeek()!= DayOfWeek.SUNDAY)
        //today isn't Sunday
        {
            btnCheckin.setEnabled(true);
            btnCheckout.setEnabled(true);
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
            db.addShift(new Shift(LocalDate.parse(txtCurrentShift.getText(),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    LocalTime.parse(txtStart.getText(),DateTimeFormatter.ofPattern("HH:mm")),
                    LocalTime.parse(txtEnd.getText(),DateTimeFormatter.ofPattern("HH:mm"))));
        }
        else
        //today is Sunday
        {
            btnCheckin.setEnabled(false);
            btnCheckout.setEnabled(false);
            txtCurrentShift.setText("To day is weekend!!!");
            txtStart.setText("None");
            txtEnd.setText("None");
        }

        //fake recent history data
        CICO[]checkins={
                new CICO(1,2,LocalDateTime.now(),LocalDateTime.now(),2),
                new CICO(2,2,LocalDateTime.now(),LocalDateTime.now(),3),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),7),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),5),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),9),
        };

        //set adapter for Recent history listview
        listRecentAdapter=new ListRecentAdapter(Arrays.asList(checkins));
        lstHis.setAdapter(listRecentAdapter);

        //test onclick
        btnCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Checkin", Toast.LENGTH_SHORT).show();
            }
        });
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Checkout", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

//func
}