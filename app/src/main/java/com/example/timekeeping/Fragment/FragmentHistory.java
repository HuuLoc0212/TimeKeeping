package com.example.timekeeping.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.timekeeping.R;
import com.example.timekeeping.adapter.ListRecentAdapter;
import com.example.timekeeping.model.CICO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class FragmentHistory extends Fragment {

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
        CICO[]checkins={
                new CICO(1,2, LocalDateTime.now(),LocalDateTime.now(),1,2),
                new CICO(2,2,LocalDateTime.now(),LocalDateTime.now(),5,6),
                new CICO(3,3,LocalDateTime.now(),LocalDateTime.now(),4,2),
        };
        listRecentAdapter=new ListRecentAdapter(Arrays.asList(checkins));
        lstHis.setAdapter(listRecentAdapter);
        return view;
    }
}