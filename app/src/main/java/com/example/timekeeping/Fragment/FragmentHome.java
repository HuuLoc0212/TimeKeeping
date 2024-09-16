package com.example.timekeeping.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.R;
import com.example.timekeeping.adapter.ListRecentAdapter;
import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.CICO;
import com.example.timekeeping.model.Staff;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FragmentHome extends Fragment {

    private ListRecentAdapter listRecentAdapter;
    private ListView lstHis;
    private TextView txtName, txtRole;
    private DBHelper db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        db= new DBHelper(getActivity());

        lstHis= view.findViewById(R.id.lstRecent);
        txtName=view.findViewById(R.id.txtName);
        txtRole=view.findViewById(R.id.txtPosition);

        List<Account> lstAccount= db.getAllAccounts();
        Account account= lstAccount.get(lstAccount.size()-1);
        Staff staff= db.getStaffByAccount(account.getAccount());

        txtName.setText(staff.getName());
        txtRole.setText(db.getRoleById(staff.getRole()).getName());

        CICO[]checkins={
                new CICO(1,2,LocalDateTime.now(),LocalDateTime.now(),2,1),
                new CICO(2,2,LocalDateTime.now(),LocalDateTime.now(),3,5),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4,4),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),7,4),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),5,4),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),9,4),
        };
        listRecentAdapter=new ListRecentAdapter(Arrays.asList(checkins));
        lstHis.setAdapter(listRecentAdapter);
        return view;
    }

//func
}