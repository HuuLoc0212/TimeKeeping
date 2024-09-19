package com.example.timekeeping.Fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timekeeping.R;
import com.example.timekeeping.adapter.ListRecentAdapter;
import com.example.timekeeping.model.CICO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FragmentHistory extends Fragment {

    private static final String TAG = "Datetimepicker";

    private EditText edtFrom, edtTo;
    private TextView tvMessage; // TextView để hiển thị thông báo lỗi
    private DatePickerDialog.OnDateSetListener mDateSetListenerFrom;
    private DatePickerDialog.OnDateSetListener mDateSetListenerTo;

    ListRecentAdapter listRecentAdapter;
    ListView lstHis;
    private String fromDateString, toDateString;

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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Khởi tạo các thành phần giao diện
        lstHis = view.findViewById(R.id.lstHis);
        edtFrom = view.findViewById(R.id.edtFrom);
        edtTo = view.findViewById(R.id.edtTo);
        tvMessage = view.findViewById(R.id.tvMessage); // TextView để hiển thị thông báo lỗi

        // Fake Data
        CICO[] checkinsArray = {
                new CICO(1, 2, LocalDateTime.now(), LocalDateTime.now(), 1),
                new CICO(2, 2, LocalDateTime.now(), LocalDateTime.now(), 5),
                new CICO(3, 3, LocalDateTime.now(), LocalDateTime.now(), 4),
                new CICO(4, 3, LocalDateTime.now(), LocalDateTime.now(), 4),
                new CICO(5, 3, LocalDateTime.now(), LocalDateTime.now(), 4),
                new CICO(6, 3, LocalDateTime.now(), LocalDateTime.now(), 4),
                new CICO(7, 3, LocalDateTime.now(), LocalDateTime.now(), 4),
                new CICO(8, 3, LocalDateTime.now(), LocalDateTime.now(), 4),
                new CICO(9, 3, LocalDateTime.now(), LocalDateTime.now(), 4),
                new CICO(10, 3, LocalDateTime.now(), LocalDateTime.now(), 4)
        };

        // Chuyển đổi mảng thành danh sách có thể thay đổi được
        List<CICO> checkinsList = new ArrayList<>(Arrays.asList(checkinsArray));

        listRecentAdapter = new ListRecentAdapter(checkinsList);
        lstHis.setAdapter(listRecentAdapter);

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

                validateDates(); // Kiểm tra và cập nhật trạng thái khi chọn ngày "From"

                // Kiểm tra nếu cả ngày "From" và "To" đều hợp lệ và hợp lý, sẽ hiển thị kết quả ngay lập tức
                if (!isToDateBeforeFromDate() && toDateString != null) {
                    showDatesBetweenFromTo();
                }
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

                validateDates(); // Kiểm tra và cập nhật trạng thái khi chọn ngày "To"

                // Kiểm tra nếu cả ngày "From" và "To" đều hợp lệ và hợp lý, sẽ hiển thị kết quả ngay lập tức
                if (!isToDateBeforeFromDate() && fromDateString != null) {
                    showDatesBetweenFromTo();
                }
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
        if (fromDateString == null || toDateString == null) {
            return false; // Trả về false nếu bất kỳ chuỗi ngày nào bị null
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy", new Locale("en", "EN"));
        try {
            Date fromDate = sdf.parse(fromDateString);
            Date toDate = sdf.parse(toDateString);
            return toDate.before(fromDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra ngày và cập nhật trạng thái của giao diện
    private void validateDates() {
        if (fromDateString == null || toDateString == null) {
            tvMessage.setText("Vui lòng chọn ngày 'From' và 'To'.");
            return;
        }

        if (isToDateBeforeFromDate()) {
            tvMessage.setText("Ngày 'To' không được nhỏ hơn ngày 'From'.");
        } else {
            tvMessage.setText(""); // Xóa thông báo lỗi nếu không có lỗi
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

            int id = 1; // Giả định ID cho CICO, bạn có thể thay đổi dựa trên logic của bạn
            while (!cal.getTime().after(toDate)) {
                // Tạo đối tượng CICO với các giá trị giả định, bạn có thể điều chỉnh theo yêu cầu
                LocalDateTime ciTime = LocalDateTime.now(); // Giả định thời gian vào
                LocalDateTime coTime = LocalDateTime.now(); // Giả định thời gian ra
                CICO cico = new CICO(id++, 2, ciTime, coTime, 1);
                dates.add(cico);

                cal.add(Calendar.DAY_OF_MONTH, 1);
            }

            // Cập nhật ListView
            listRecentAdapter.updateData(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
