package com.example.timekeeping.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.timekeeping.R;
import com.example.timekeeping.adapter.ListRecentAdapter;
import com.example.timekeeping.model.CICO;
import com.google.type.DateTime;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FragmentHome extends Fragment {

    private ListRecentAdapter listRecentAdapter;
    private ListView lstHis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        lstHis= view.findViewById(R.id.lstRecent);
        CICO[]checkins={
                new CICO(1,2,LocalDateTime.now(),LocalDateTime.now(),1,2),
                new CICO(2,2,LocalDateTime.now(),LocalDateTime.now(),5,6),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4,2),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4,2),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4,2),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4,2),
        };
        listRecentAdapter=new ListRecentAdapter(Arrays.asList(checkins));
        lstHis.setAdapter(listRecentAdapter);
        return view;
    }

//func
}