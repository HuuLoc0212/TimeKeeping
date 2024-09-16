package com.example.timekeeping.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timekeeping.R;
import com.example.timekeeping.adapter.ListRecentAdapter;
import com.example.timekeeping.model.CICO;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class FragmentHistory extends Fragment {

    private static final String TAG = "Datetimepicker";

    private EditText edtFrom, edtTo;
    private DatePickerDialog.OnDateSetListener mDateSetListernerFrom;
    private DatePickerDialog.OnDateSetListener mDateSetListernerTo;



    ListRecentAdapter listRecentAdapter;
    ListView lstHis;

    public FragmentHistory() {
    }

    public static FragmentHistory newInstance(String param1, String param2) {
        FragmentHistory fragment = new FragmentHistory();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history, container, false);
        lstHis= view.findViewById(R.id.lstHis);
        edtFrom = view.findViewById(R.id.edtFrom);
        edtTo = view.findViewById(R.id.edtTo);

        CICO[]checkins={
                new CICO(1,2, LocalDateTime.now(),LocalDateTime.now(),1,2),
                new CICO(2,2,LocalDateTime.now(),LocalDateTime.now(),5,6),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4,2),
        };
        listRecentAdapter=new ListRecentAdapter(Arrays.asList(checkins));
        lstHis.setAdapter(listRecentAdapter);

        edtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListernerFrom,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();

                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateSetListernerTo,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListernerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                // Tạo đối tượng Calendar với ngày đã chọn
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DAY_OF_MONTH, day);

                // Lấy tên ngày trong tuần bằng tiếng Anh
                String dayOfWeek = new SimpleDateFormat("EEE", new Locale("en", "EN")).format(cal.getTime());

                Log.d(TAG, "onDateSet: dd/mm/yyyy " + day + "/" + month + "/" + year);
                String date = dayOfWeek + ", " + day + "/" + month + "/" + year; // Hiển thị tên ngày trong tuần
                edtFrom.setText(date);
            }
        };

        mDateSetListernerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                // Tạo đối tượng Calendar với ngày đã chọn
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DAY_OF_MONTH, day);

                // Lấy tên ngày trong tuần bằng tiếng Anh
                String dayOfWeek = new SimpleDateFormat("EEE", new Locale("en", "EN")).format(cal.getTime());

                Log.d(TAG, "onDateSet: dd/mm/yyyy " + day + "/" + month + "/" + year);
                String date = dayOfWeek + ", " + day + "/" + month + "/" + year; // Hiển thị tên ngày trong tuần
                edtTo.setText(date);
            }
        };

        return view;
    }
}