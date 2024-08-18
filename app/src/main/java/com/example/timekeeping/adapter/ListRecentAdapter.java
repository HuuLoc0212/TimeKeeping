package com.example.timekeeping.adapter;

import android.icu.text.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timekeeping.R;
import com.example.timekeeping.model.CICO;
import com.example.timekeeping.model.Shift;
import com.google.type.DateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListRecentAdapter extends BaseAdapter {
    private List<CICO> lstCheckin;

    public ListRecentAdapter(List<CICO> lstCheckin) {
        this.lstCheckin = lstCheckin;
    }

    @Override
    public int getCount() {
        return lstCheckin.size();
    }

    @Override
    public CICO getItem(int i) {
        return lstCheckin.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lstCheckin.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View recentItemView;
        if (view == null) {
            recentItemView = View.inflate(viewGroup.getContext(), R.layout.recent_item, null);
        } else recentItemView = view;

        //Bind sữ liệu phần tử vào View
        CICO checkin = (CICO) getItem(i);

        DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date=checkin.getCiTime().format(dateFormatter);
        String ciTime=checkin.getCiTime().format(timeFormatter);
        String coTime=checkin.getCoTime().format(timeFormatter);

        ((TextView) recentItemView.findViewById(R.id.itemDate)).setText(date);
        ((TextView) recentItemView.findViewById(R.id.itemTime)).setText(ciTime+"-"+coTime);


        return view;
    }
}
