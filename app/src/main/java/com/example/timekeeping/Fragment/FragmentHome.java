package com.example.timekeeping.Fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.timekeeping.DB.DBHelper;
import com.example.timekeeping.R;
import com.example.timekeeping.model.Account;
import com.example.timekeeping.model.CICO;
import com.example.timekeeping.model.Shift;
import com.example.timekeeping.model.Staff;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FragmentHome extends Fragment {
    private TextView txtName, txtRole, txtCurrentShift, txtStart, txtEnd, txtCI, txtCO, txtShift;
    private LinearLayout btnCheckIn, btnCheckOut;
    private DBHelper db;
    private CICO todayCICO = null;
    private Shift todayShift = null;
    private Staff staff;
    private OkHttpClient client = new OkHttpClient();

    private final String companyIp = "171.252.153.46";
    //private final String companyIp = "116.106.195.231";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtName = view.findViewById(R.id.txtName);
        txtRole = view.findViewById(R.id.txtPosition);
        txtCurrentShift = view.findViewById(R.id.tvCurrentshift);
        txtStart = view.findViewById(R.id.tvStart);
        txtEnd = view.findViewById(R.id.tvEnd);
        txtCI = view.findViewById(R.id.tvCI);
        txtCO = view.findViewById(R.id.tvCO);
        txtShift = view.findViewById(R.id.tvShift);
        btnCheckIn = view.findViewById(R.id.btnCheckin);
        btnCheckOut = view.findViewById(R.id.btnCheckout);

        db = new DBHelper(getActivity());

        //get staff information
        List<Account> lstAccount = db.getAllAccounts();
        Account account = lstAccount.get(lstAccount.size() - 1);
        staff = db.getStaffByAccount(account.getAccount());

        //get staff's check-in
        List<CICO> lstCICO = db.getCICOS(staff.getId());

        //get today shift
        todayShift = db.getShiftByDate(LocalDate.now());

        //set staff information
        txtName.setText(staff.getName());
        txtRole.setText(db.getRoleById(staff.getRole()).getName());

        //set shift information
        if (todayShift == null) {
            if (LocalDateTime.now().getDayOfWeek() != DayOfWeek.SUNDAY) {
//              today isn't Saturday
                if (LocalDateTime.now().getDayOfWeek() != DayOfWeek.SATURDAY) {
                    txtCurrentShift.setText(LocalDate.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    txtStart.setText(LocalTime.of(8, 30)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                    txtEnd.setText(LocalTime.of(17, 30)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                } else
                //today is Saturday
                {
                    txtCurrentShift.setText(LocalDate.now()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    txtStart.setText(LocalTime.of(8, 30)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                    txtEnd.setText(LocalTime.of(12, 00)
                            .format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                todayShift = new Shift(LocalDate.parse(txtCurrentShift.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        LocalTime.parse(txtStart.getText(), DateTimeFormatter.ofPattern("HH:mm")),
                        LocalTime.parse(txtEnd.getText(), DateTimeFormatter.ofPattern("HH:mm")));
                db.addShift(todayShift);
            } else {
                btnCheckIn.setEnabled(false);
                btnCheckOut.setEnabled(false);
                txtCurrentShift.setText("To day is weekend!!!");
                txtStart.setText("None");
                txtEnd.setText("None");
            }
        } else {
            txtCurrentShift.setText(todayShift.getDate()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtStart.setText(todayShift.getStart()
                    .format(DateTimeFormatter.ofPattern("HH:mm")));
            txtEnd.setText(todayShift.getEnd()
                    .format(DateTimeFormatter.ofPattern("HH:mm")));
        }

        //check today is checked-in
        for (CICO cico : lstCICO) {
            if (cico.getShift() == todayShift.getId()) {
                todayCICO = cico;
                break;
            }
        }

        //show today's check-in and set check-in, check-out buttons state
        if (todayCICO != null) {
            btnCheckIn.setEnabled(false);

            btnCheckOut.setEnabled(true);

            txtShift.setText(todayShift
                    .getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            txtCI.setText(todayCICO.getCiTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            if (todayCICO.getCoTime() != null) {
                btnCheckOut.setEnabled(false);
                txtCO.setText(todayCICO.getCoTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            } else {
                txtCO.setText("None");
            }
        } else {
            btnCheckIn.setEnabled(true);
            btnCheckOut.setEnabled(false);
        }
        changeCheckButtonBackground(btnCheckIn);
        changeCheckButtonBackground(btnCheckOut);

        //check-out button event
        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                checkIPForCheckIn();
                
            }
        });

        //check-out button event
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkIPForCheckOut();
                
            }
        });

        return view;
    }

    private void showSnackBar(String message, String type) {

        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        if (type.equals("error")) {
            snackbar.setTextColor(getResources().getColor(R.color.text_red));
            snackbar.setBackgroundTint(getResources().getColor(R.color.red));
        } else if (type.equals("success")) {
            snackbar.setTextColor(getResources().getColor(R.color.text_green));
            snackbar.setBackgroundTint(getResources().getColor(R.color.green));
        } else if (type.equals("warning")) {
            snackbar.setTextColor(getResources().getColor(R.color.text_yellow));
            snackbar.setBackgroundTint(getResources().getColor(R.color.yellow));
        } else {
            snackbar.setTextColor(getResources().getColor(R.color.text_blue));
            snackbar.setBackgroundTint(getResources().getColor(R.color.blue));
        }
        setSnackBarLayout(snackbar);
        snackbar.show();

    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), message, 0);
        snackbar.setBackgroundTint(getResources().getColor(R.color.grey));
        setSnackBarLayout(snackbar);
        snackbar.show();
    }

    private void setSnackBarLayout(Snackbar snackbar){
        View snackbarView = snackbar.getView();
        snackbarView.setBackground(getResources().getDrawable(R.drawable.bottom_background));
        ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT; // Set width to wrap content
        snackbarView.setLayoutParams(params);
        if (params instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) params;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
            layoutParams.bottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics()); // Set bottom margin to 20dp// Set gravity to center
            int paddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()); // Example top padding
            int paddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()); // Example bottom padding
            int paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()); // Example left padding
            int paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()); // Example right padding
            snackbarView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            snackbarView.setLayoutParams(layoutParams);
        } else {
            snackbarView.setLayoutParams(params);
        }
    }

    private void changeCheckButtonBackground(LinearLayout btn) {
        if (btn.isEnabled()) {
            btn.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            btn.setBackgroundColor(getResources().getColor(R.color.grey_1));
        }
    }

    public boolean isAutoTimeEnabled(Context context) {
        try {
            int autoTimeSetting = Settings.Global.getInt(
                    context.getContentResolver(),
                    Settings.Global.AUTO_TIME
            );
            return autoTimeSetting == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void checkIPForCheckIn() {
        String url = "https://api.ipify.org";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String publicIp = response.body().string(); // Get the public IP

                    if (publicIp.equals(companyIp)) {
                        getActivity().runOnUiThread(() -> {
                                    if (isAutoTimeEnabled(getActivity())) {
                                        //if (LocalTime.now().isBefore(todayShift.getEnd())) {
                                        if(true){
                                            CICO cico = new CICO(staff.getId(),
                                                    LocalDateTime.now(),
                                                    db.getShiftByDate(LocalDate.now()).getId());
                                            db.addCICO(cico);
                                            todayCICO = cico;
                                            txtShift.setText(db.getShiftById(cico.getShift()).getDate()
                                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                                            txtCI.setText(cico.getCiTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                                            txtCO.setText("None");
                                            btnCheckIn.setEnabled(false);
                                            btnCheckOut.setEnabled(true);
                                            showSnackBar("Check-in succeed!!!", "success");
                                        } else {
                                            showSnackBar("Too late for check-in now!!!", "error");
                                        }
                                    } else {
                                        showSnackBar("Please enable auto time!!!", "warning");
                                    }
                                    changeCheckButtonBackground(btnCheckIn);
                                    changeCheckButtonBackground(btnCheckOut);

                                }
                        );
                    } else {
                        getActivity().runOnUiThread(() ->

                                showSnackBar("No permission to execute!!!", "error")
                        );
                    }
                }
            }
        });
    }

    private void checkIPForCheckOut() {
        String url = "https://api.ipify.org";

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String publicIp = response.body().string(); // Get the public IP

                    if (publicIp.equals(companyIp)) {

                        getActivity().runOnUiThread(() -> {
                                    if (isAutoTimeEnabled(getActivity())) {
                                        if (!LocalDate.now().isAfter(todayShift.getDate())) {
                                            CICO cico = db.getCICOS(staff.getId()).get(0);
                                            cico.setCoTime(LocalDateTime.now());
                                            int rows = db.checkout(cico);
                                            if (rows > 0) {
                                                txtCO.setText(cico.getCoTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                                                showSnackBar("Check-out succeed!!!", "success");
                                                btnCheckOut.setEnabled(false);
                                            } else {
                                                showSnackBar("Check-out failure!!!", "error");
                                            }
                                        } else {
                                            showSnackBar("Too late for check-out now!!!", "error");

                                        }
                                        changeCheckButtonBackground(btnCheckIn);
                                        changeCheckButtonBackground(btnCheckOut);
                                    } else {
                                        showSnackBar("Please enable auto time!!!", "warning");
                                    }
                                    changeCheckButtonBackground(btnCheckIn);
                                    changeCheckButtonBackground(btnCheckOut);


                                }
                        );
                    } else {
                        getActivity().runOnUiThread(() ->
                                showSnackBar("No permission to execute!!!", "error")
                        );
                    }
                }
            }
        });
    }

    
}