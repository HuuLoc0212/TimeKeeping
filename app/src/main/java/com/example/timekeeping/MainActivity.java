package com.example.timekeeping;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.timekeeping.Fragment.FragmentHistory;
import com.example.timekeeping.Fragment.FragmentHome;
import com.example.timekeeping.Fragment.FragmentProfile;
import com.example.timekeeping.Fragment.FragmentSetting;
import com.example.timekeeping.Utils.AlarmReceiver;
import com.example.timekeeping.adapter.AdapterViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button setAlarmButton;

    ViewPager2 pagerMain;
    BottomNavigationView bottomNav;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pagerMain = findViewById(R.id.pageMain);
        bottomNav = findViewById(R.id.bottomNav);

        fragmentArrayList.add(new FragmentHome());
        fragmentArrayList.add(new FragmentHistory());
        fragmentArrayList.add(new FragmentProfile());
        fragmentArrayList.add(new FragmentSetting());

        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        pagerMain.setAdapter(adapterViewPager);

        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNav.setSelectedItemId(R.id.bottom_home);
                        break;
                    case 1:
                        bottomNav.setSelectedItemId(R.id.bottom_history);
                        break;
                    case 2:
                        bottomNav.setSelectedItemId(R.id.bottom_profile);
                        break;
                    case 3:
                        bottomNav.setSelectedItemId(R.id.bottom_setting);
                        break;
                    default:
                        bottomNav.setSelectedItemId(R.id.bottom_home);
                        break;
                }
                super.onPageSelected(position);
            }
        });

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bottom_home) {
                    pagerMain.setCurrentItem(0);
                    return true;
                } else if (itemId == R.id.bottom_history) {
                    pagerMain.setCurrentItem(1);
                    return true;
                } else if (itemId == R.id.bottom_profile) {
                    pagerMain.setCurrentItem(2);
                    return true;
                } else if (itemId == R.id.bottom_setting) {
                    pagerMain.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });

        // Kiểm tra quyền lập lịch thông báo chính xác và đặt báo thức
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!canScheduleExactAlarms()) {
                requestExactAlarmPermission();
            } else {
                setAlarm();
            }
        } else {
            setAlarm();
        }
    }

    private boolean canScheduleExactAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        return alarmManager != null;
    }

    private void requestExactAlarmPermission() {
        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
        startActivity(intent);
    }

    private void setAlarm() {
        // Lấy thời gian từ TimePicker
        int hour = 21;  // Giờ cụ thể
        int minute = 59;  // Phút cụ thể

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Lập lịch thời gian thông báo
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Điều chỉnh thời gian thông báo trước 5, 10, 15, 30 phút nếu cần
        int notifyBeforeMinutes = 0; // Ví dụ chọn trước 10 phút
        calendar.add(Calendar.MINUTE, -notifyBeforeMinutes);

        // Intent để gửi thông báo
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("message", "Sắp đến giờ hẹn lúc " + hour + ":" + minute);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            // Kiểm tra xem ứng dụng có thể đặt báo thức chính xác hay không
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    requestExactAlarmPermission();
                    return; // Ngừng nếu không có quyền
                }
            }

            // Cố gắng đặt báo thức
            try {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                Log.d(TAG, "Alarm set for " + hour + ":" + minute);
            } catch (SecurityException e) {
                Log.e(TAG, "Unable to set exact alarm: " + e.getMessage());
                // Xử lý lỗi: Thông báo cho người dùng hoặc yêu cầu quyền
            }
        }
    }
}
