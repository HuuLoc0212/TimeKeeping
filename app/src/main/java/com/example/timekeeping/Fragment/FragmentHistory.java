package com.example.timekeeping.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.R;
import com.example.timekeeping.adapter.ListRecentAdapter;
import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.CICO;
import com.example.timekeeping.model.Shift;
import com.example.timekeeping.model.Staff;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentHistory extends Fragment {

    private EditText edtFrom, edtTo;
    private TextView tvMessage; // TextView để hiển thị thông báo lỗi
    private Button btnFilter; // Nút để lọc kết quả
    private DatePickerDialog.OnDateSetListener mDateSetListenerFrom;
    private DatePickerDialog.OnDateSetListener mDateSetListenerTo;

    ListRecentAdapter listRecentAdapter;
    ListView lstHis;
    private DBHelper db;
    private String fromDateString, toDateString;
    private Staff staff;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Khởi tạo các thành phần giao diện
        lstHis = view.findViewById(R.id.lstHis);
        edtFrom = view.findViewById(R.id.edtFrom);
        edtTo = view.findViewById(R.id.edtTo);
        tvMessage = view.findViewById(R.id.tvMessage);
        btnFilter = view.findViewById(R.id.btnFilter);  // Nút Filter (đã đặt id là btnLogin)

        db = new DBHelper(getActivity());
        // Lấy thông tin tài khoản và nhân viên từ DB
        List<Account> lstAccount = db.getAllAccounts();
        Account account = lstAccount.get(lstAccount.size() - 1);
        staff = db.getStaffByAccount(account.getAccount());

        //set default text for edit text
        edtTo.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        edtFrom.setText(LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Truy xuất tất cả dữ liệu CICO cho nhân viên
        List<CICO> allCICOList = db.getCICOS(staff.getId());

        //Load data for list view
        loadData(edtFrom.getText().toString(),edtTo.getText().toString());

        // Xử lý sự kiện chọn ngày "From"
        edtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvMessage.setText("");
                showDatePickerDialog(mDateSetListenerFrom);
            }
        });
        // Xử lý sự kiện chọn ngày "To"
        edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvMessage.setText("");
                showDatePickerDialog(mDateSetListenerTo);
            }
        });
        // Thiết lập DatePickerDialog cho ngày "From"
        mDateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Tăng tháng lên 1 vì DatePicker trả về giá trị tháng từ 0 - 11
                month = month + 1;

                LocalDate selectedDate = LocalDate.of(year, month, day);
                if (selectedDate!=null){
                    edtFrom.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }

            }
        };
        // Thiết lập DatePickerDialog cho ngày "To"
        mDateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Tăng tháng lên 1 vì DatePicker trả về giá trị tháng từ 0 - 11
                month = month + 1;

                LocalDate selectedDate = LocalDate.of(year, month, day);
                if(selectedDate!=null){
                    edtTo.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }

            }
        };

        //set filter button click handler
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData(edtFrom.getText().toString(),edtTo.getText().toString());
            }
        });

        return view;
    }


    private void InitDialog(CICO cico) {
        TextView txtDateTv, txtCI, txtCO, txtStatus, txtStart, txtEnd, txtBreakingTime;

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_design);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // set dialog background
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_background);

        txtDateTv = dialog.findViewById(R.id.dateTV);
        txtCI = dialog.findViewById(R.id.tvCI);
        txtCO = dialog.findViewById(R.id.tvCO);
        txtStatus = dialog.findViewById(R.id.statusTV);
        txtStart = dialog.findViewById(R.id.tvStart);
        txtEnd = dialog.findViewById(R.id.tvEnd);
        txtBreakingTime = dialog.findViewById(R.id.tvBreakingTime);

        Shift shift=db.getShiftById(cico.getShift());


        txtDateTv.setText(shift.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtStart.setText(shift.getStart().format((DateTimeFormatter.ofPattern("HH:mm"))));
        txtEnd.setText(shift.getEnd().format((DateTimeFormatter.ofPattern("HH:mm"))));
        if(shift.getBreakTime()==0.0){
            txtBreakingTime.setText("None");
        }
        else {
            LocalTime breakStart = LocalTime.of(12,0);
            txtBreakingTime.setText("12:00 - "+ breakStart.plus(Duration.ofHours(shift.getBreakTime().longValue())).
                    format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        txtCI.setText(cico.getCiTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        if (cico.getCoTime() == null) {
            txtCO.setText("None");
            txtStatus.setText("Incomplete");
            txtStatus.setTextColor(getActivity().getResources().getColor(R.color.white));
            txtStatus.setBackgroundResource(R.drawable.dialog_incomplete_button);
        } else {
            txtCO.setText(cico.getCoTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            if (LocalTime.of(cico.getCiTime().getHour(), cico.getCiTime().getMinute()).isAfter(db.getShiftById(cico.getShift()).getStart())) {
                txtStatus.setText("Late");
                txtStatus.setTextColor(getActivity().getResources().getColor(R.color.red));
                txtStatus.setBackgroundResource(R.drawable.late_button);
            } else {
                txtStatus.setText("On time");
                txtStatus.setTextColor(getActivity().getResources().getColor(R.color.green));
                txtStatus.setBackgroundResource(R.drawable.on_time_button);
            }
        }

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    // Hiển thị DatePickerDialog để chọn ngày
    private void showDatePickerDialog( DatePickerDialog.OnDateSetListener listener) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Dialog_MinWidth,
                listener,
                year, month, day
        );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    // Lọc danh sách CICO theo khoảng ngày
    private List<CICO> filterCICOByDateRange( LocalDate fromDate, LocalDate toDate) {
        List<CICO> allCICOList = db.getCICOS(staff.getId());

        List<CICO> filteredList = new ArrayList<>();
        for (CICO cico : allCICOList) {
            Shift cicoShift = db.getShiftById(cico.getShift());
            if ((cicoShift.getDate().isAfter(fromDate) || cicoShift.getDate().isEqual(fromDate)) &&
                    (cicoShift.getDate().isBefore(toDate) || cicoShift.getDate().isEqual(toDate))) {
                filteredList.add(cico);
            }
        }
        return filteredList;
    }
    @Override
    public void onResume() {
        super.onResume();

        loadData(edtFrom.getText().toString(),edtTo.getText().toString());
    }

    private void loadData(String fromDate, String toDate) {
        if(fromDate==null||fromDate.isEmpty()||toDate==null||toDate.isEmpty()) {
            tvMessage.setText("No results found");
        }
        else {
            if(StringToLocalDate(fromDate).isAfter(StringToLocalDate(toDate))) {
                tvMessage.setText("From date can be later than to date");
            }
            else {
                List<CICO> cicoList = filterCICOByDateRange(StringToLocalDate(fromDate), StringToLocalDate(toDate));
                if (cicoList.isEmpty()) {
                    tvMessage.setText("No results found");
                } else {
                    tvMessage.setText("");
                }
                listRecentAdapter= new ListRecentAdapter(getActivity(), cicoList,db);
                lstHis.setAdapter(listRecentAdapter);

                lstHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        CICO cico = cicoList.get(i);
                        InitDialog(cico);
                    }
                });

            }
        }

    }
    private LocalDate StringToLocalDate(String string){
        if(string!=null&&!string.isEmpty()){
            return LocalDate.parse(string,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return null;
    }
    private String LocalDateToString(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
