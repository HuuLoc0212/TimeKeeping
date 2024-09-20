package com.example.timekeeping.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.timekeeping.model.Staff;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Khởi tạo các thành phần giao diện
        lstHis = view.findViewById(R.id.lstHis);
        edtFrom = view.findViewById(R.id.edtFrom);
        edtTo = view.findViewById(R.id.edtTo);
        tvMessage = view.findViewById(R.id.tvMessage);
        btnFilter = view.findViewById(R.id.btnLogin);  // Nút Filter (đã đặt id là btnLogin)


        edtTo.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        edtFrom.setText(LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        db = new DBHelper(getActivity());

        // Lấy thông tin tài khoản và nhân viên từ DB
        List<Account> lstAccount = db.getAllAccounts();
        Account account = lstAccount.get(lstAccount.size() - 1);
        Staff staff = db.getStaffByAccount(account.getAccount());

        // Truy xuất tất cả dữ liệu CICO cho nhân viên
        List<CICO> allCICOList = db.getCICOS(staff.getId());



        // Xử lý sự kiện khi nhấn nút "Filter"
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu người dùng chưa chọn ngày From hoặc To
                if (edtFrom.getText() == null || edtTo.getText() == null) {
                    tvMessage.setText("Please select the dates \"From\" and \"To.\".");
                    return; // Dừng lại nếu chưa chọn đủ ngày
                }else {
                    // Kiểm tra nếu ngày 'From' lớn hơn ngày 'To'
                    if (isToDateBeforeFromDate(edtFrom,edtTo)) {
                        tvMessage.setText("The \"To\" date cannot be earlier than the \"From\" date.");
                    }
                }

                // Lọc danh sách theo khoảng ngày đã chọn
         List<CICO>  filteredCICOList = filterCICOByDateRange(allCICOList,
                        getDateFromTextView(edtFrom),
                        getDateFromTextView(edtTo));

                if (filteredCICOList.isEmpty()) {
                    tvMessage.setText("No records found for the selected dates.");
                } else {
                    tvMessage.setText(""); // Xóa thông báo lỗi
                }
                // Cập nhật ListView với dữ liệu mới
                listRecentAdapter = new ListRecentAdapter(getActivity(), filteredCICOList, db);
                lstHis.setAdapter(listRecentAdapter);
            }
        });
        // Khi người dùng chạm vào EditText "From" hoặc "To", thông báo lỗi sẽ biến mất
        edtFrom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    tvMessage.setText("");
                return false;
            }
        });  tvMessage.setText(""); // Xóa thông báo lỗi
        edtTo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP)
                    tvMessage.setText("");
                return false;
            }
        });

        // Xử lý sự kiện chọn ngày "From"
        edtFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(edtFrom, mDateSetListenerFrom);
            }
        });

        // Xử lý sự kiện chọn ngày "To"
        edtTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(edtTo, mDateSetListenerTo);
            }
        });

        // Thiết lập DatePickerDialog cho ngày "From"
        mDateSetListenerFrom = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DAY_OF_MONTH, day);


                edtFrom.setText(formatDate(cal.getTime()));
            }
        };

        // Thiết lập DatePickerDialog cho ngày "To"
        mDateSetListenerTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month - 1);
                cal.set(Calendar.DAY_OF_MONTH, day);


                edtTo.setText(formatDate(cal.getTime()));
            }
        };

        //Show popup
        lstHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CICO cico = (CICO) adapterView.getItemAtPosition(i);
                InitDialog(cico);
            }
        });

        return view;
    }
    private void InitDialog(CICO cico) {
        TextView txtDateTv, txtCI, txtCO, txtStatus;

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_design);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT); // Đặt chiều rộng và chiều cao

        txtDateTv = dialog.findViewById(R.id.dateTV);
        txtCI = dialog.findViewById(R.id.tvCI);
        txtCO = dialog.findViewById(R.id.tvCO);
        txtStatus = dialog.findViewById(R.id.statusTV);

        txtDateTv.setText(db.getShiftById(cico.getShift()).getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtCI.setText(cico.getCiTime().format(DateTimeFormatter.ofPattern("HH:mm")));

        if (cico.getCoTime() == null) {
            txtCO.setText("None");
            txtStatus.setText("Incomplete");
            txtStatus.setBackgroundResource(R.drawable.on_time_button);
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

    private String getDateFromTextView(EditText editText) {

        return  LocalDate.parse(editText.getText()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    }

    // Hiển thị DatePickerDialog để chọn ngày
    private void showDatePickerDialog(EditText editText, DatePickerDialog.OnDateSetListener listener) {
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

    // Kiểm tra xem ngày "To" có nhỏ hơn ngày "From" không
    private boolean isToDateBeforeFromDate(EditText editText , EditText editText2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        LocalDate fromDate = LocalDate.parse(editText.getText(), formatter);
        LocalDate toDate = LocalDate.parse(editText2.getText(), formatter);
        return toDate.isBefore(fromDate);
    }

    // Lọc danh sách CICO theo khoảng ngày
    private List<CICO> filterCICOByDateRange(List<CICO> allCICOList, String fromDateString, String toDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        List<CICO> filteredList = new ArrayList<>();
        if (fromDateString !=null && toDateString !=null){
            LocalDate fromDate = LocalDate.parse(fromDateString, formatter);
            LocalDate toDate = LocalDate.parse(toDateString, formatter);
            for (CICO cico : allCICOList) {
                LocalDate checkInDate = cico.getCiTime().toLocalDate();
                if ((checkInDate.isAfter(fromDate) || checkInDate.isEqual(fromDate)) &&
                        (checkInDate.isBefore(toDate) || checkInDate.isEqual(toDate))) {
                    filteredList.add(cico);
                }
            }
        }
        return filteredList;
    }

    // Định dạng ngày thành chuỗi
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}

