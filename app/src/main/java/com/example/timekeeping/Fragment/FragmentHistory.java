package com.example.timekeeping.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

        db = new DBHelper(getActivity());
        // Lấy thông tin tài khoản và nhân viên từ DB
        List<Account> lstAccount = db.getAllAccounts();
        Account account = lstAccount.get(lstAccount.size() - 1);
        Staff staff = db.getStaffByAccount(account.getAccount());

        // Hiển thị toàn bộ lịch sử CICO ban đầu
        List<CICO> checkinsArray = db.getCICOS(staff.getId());
        listRecentAdapter = new ListRecentAdapter(getActivity(), checkinsArray,db);
        lstHis.setAdapter(listRecentAdapter);

        // Xử lý sự kiện khi nhấn nút "Filter"
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra nếu người dùng chưa chọn ngày From hoặc To
                if (fromDateString == null || toDateString == null) {
                    tvMessage.setText("Please select the dates \"From\" and \"To.\".");
                    return; // Dừng lại nếu chưa chọn đủ ngày
                }

                // Kiểm tra nếu ngày 'From' lớn hơn ngày 'To'
                if (isToDateBeforeFromDate()) {
                    tvMessage.setText("The \"To\" date cannot be earlier than the \"From\" date.");
                    return; // Dừng lại nếu ngày không hợp lệ
                }

                // Nếu kiểm tra hợp lệ, thực hiện tìm kiếm dữ liệu
                tvMessage.setText(""); // Xóa thông báo lỗi
                showDatesBetweenFromTo(); // Hiển thị danh sách kết quả
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

                String dayOfWeek = new SimpleDateFormat("EEE", new Locale("en", "EN")).format(cal.getTime());

                fromDateString = dayOfWeek + ", " + day + "/" + month + "/" + year;
                edtFrom.setText(fromDateString);
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

                String dayOfWeek = new SimpleDateFormat("EEE", new Locale("en", "EN")).format(cal.getTime());
                toDateString = dayOfWeek + ", " + day + "/" + month + "/" + year;
                edtTo.setText(toDateString);
            }
        };

        return view;
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
    private boolean isToDateBeforeFromDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy", new Locale("en", "EN"));
        try {
            Date fromDate = sdf.parse(fromDateString);
            Date toDate = sdf.parse(toDateString);
            return toDate.before(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }

    // Hiển thị danh sách các ngày từ "From" đến "To"
    private void showDatesBetweenFromTo() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy", new Locale("en", "EN"));
        List<CICO> dates = new ArrayList<>();
        try {
            Date fromDate = sdf.parse(fromDateString);
            Date toDate = sdf.parse(toDateString);

            Calendar cal = Calendar.getInstance();
            cal.setTime(fromDate);

            int id = 1; // Giả định ID cho CICO
            while (!cal.getTime().after(toDate)) {
                // Tạo đối tượng CICO với các giá trị giả định, điều chỉnh theo yêu cầu
                LocalDateTime ciTime = LocalDateTime.now();
                LocalDateTime coTime = LocalDateTime.now();
                CICO cico = new CICO(id++, 2, ciTime, coTime, 1);
                dates.add(cico);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
