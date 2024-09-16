package com.example.timekeeping.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.R;
import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.Staff;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


public class FragmentProfile extends Fragment {

    DBHelper db;
    TextView txtName, txtPosition, txtBOD, txtEmail, txtSalary;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        txtName=v.findViewById(R.id.tv_user);
        txtPosition=v.findViewById(R.id.tv_position);
        txtBOD=v.findViewById(R.id.tv_birthday);
        txtEmail=v.findViewById(R.id.tv_email);
        txtSalary=v.findViewById(R.id.tv_salary);

        db= new DBHelper(getActivity());

        List<Account> lstAccount= db.getAllAccounts();
        Account account= lstAccount.get(lstAccount.size()-1);
        Staff staff= db.getStaffByAccount(account.getAccount());

        txtName.setText(staff.getName());
        txtPosition.setText(db.getRoleById(staff.getRole()).getName());
        txtBOD.setText(staff.getbOD()
                        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtEmail.setText(staff.getUsernanme());
        txtSalary.setText(NumberFormat
                .getCurrencyInstance(
                        new Locale("vi","VN"))
                        .format(staff.getBasicSalary()));

        return v;
    }
}