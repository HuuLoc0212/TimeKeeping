package com.example.timekeeping;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.timekeeping.Fragment.FragmentHistory;
import com.example.timekeeping.Fragment.FragmentHome;
import com.example.timekeeping.Fragment.FragmentProfile;
import com.example.timekeeping.Fragment.FragmentSetting;
import com.example.timekeeping.adapter.AdapterViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager2 pagerMain;
    BottomNavigationView bottomNav;

    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pagerMain =findViewById(R.id.pageMain);
        bottomNav =findViewById(R.id.bottomNav);

        fragmentArrayList.add(new FragmentHome());
        fragmentArrayList.add(new FragmentHistory());
        fragmentArrayList.add(new FragmentProfile());
        fragmentArrayList.add(new FragmentSetting());

        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        //set Adapter
        pagerMain.setAdapter(adapterViewPager);
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position){
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
//                Fragment selectedFragment = null;
//                switch (item.getItemId()){
//                    case R.id.bottom_home:
//                        selectedFragment = new FragmentHome();
//                        break;
//                    case R.id.bottom_history:
//                        selectedFragment = new FragmentHistory();
//                        break;
//                    case R.id.bottom_profile:
//                        selectedFragment = new FragmentProfile();
//                        break;
//                    case R.id.bottom_setting:
//                        selectedFragment = new FragmentSetting();
//                        break;
//                    default:  selectedFragment = new FragmentHome();
//                        break;
//                }
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
                }
                else if (itemId == R.id.bottom_setting) {
                    pagerMain.setCurrentItem(3);
                    return true;
                }
                return false;
            }
        });
    }
}