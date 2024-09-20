package com.example.timekeeping.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.R;
import com.example.timekeeping.model.CICO;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListRecentAdapter extends BaseAdapter {
    private List<CICO> lstCheckin;
    private TextView txtState;
    private DBHelper db;
    private Context context;

    public ListRecentAdapter(Context context, List<CICO> lstCheckin, DBHelper db) {
        this.lstCheckin = lstCheckin;
        this.context=context;
        this.db=db;
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
        } else {
            recentItemView = view;
        }

        txtState= recentItemView.findViewById(R.id.tvState);

        CICO checkin = getItem(i);

        ((TextView) recentItemView.findViewById(R.id.itemDate)).setText(checkin.getCiTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if (checkin.getCoTime() == null) {
            ((TextView) recentItemView.findViewById(R.id.itemTime)).setText(checkin.getCiTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                    + " -  None");
            txtState.setText("Incomplete");
            txtState.setBackgroundResource(R.drawable.on_time_button);
        } else{
            ((TextView) recentItemView.findViewById(R.id.itemTime)).setText(checkin.getCiTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                    + " - "
                    + checkin.getCoTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm")));
            if(LocalTime.of(checkin.getCiTime().getHour(), checkin.getCiTime().getMinute()).isAfter(db.getShiftById(checkin.getShift()).getStart())) {
                txtState.setText("Late");
                txtState.setTextColor(context.getResources().getColor(R.color.red));
                txtState.setBackgroundResource(R.drawable.late_button);
            }
            else {
                txtState.setText("On time");
                txtState.setTextColor(context.getResources().getColor(R.color.green));
                txtState.setBackgroundResource(R.drawable.on_time_button);
            }
        }

        return recentItemView;
    }
}
