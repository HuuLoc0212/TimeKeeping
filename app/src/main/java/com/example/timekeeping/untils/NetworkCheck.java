package com.example.timekeeping.untils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class NetworkCheck {

    private Context context;
    private Boolean enable=false;
//    private final String companyIpRange = "171.252.153.46";
    private final String companyIpRange = "116.106.195.231";
//    private final String companyIpRange = "115.77.231.25";

    public NetworkCheck(Context context) {
        this.context = context;
    }

    // Kiểm tra xem thiết bị có đang kết nối VPN không
    public boolean isVpnConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                    return true;  // Kết nối qua VPN
                }
            }
        }
        return false;
    }

    // Kiểm tra xem thiết bị có kết nối với mạng WiFi của công ty không
    public boolean isCompanyWifi() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            // Kiểm tra nếu SSID là của mạng công ty (Ví dụ: "Company_WiFi")
            return ssid != null && ssid.equals("\"Company_WiFi_SSID\"");  // Cần dấu "" nếu trả về chuỗi SSID có dấu ngoặc kép
        }
        return false;
    }

    // Kiểm tra xem IP public có thuộc mạng công ty không
    public boolean isCompanyPublicIP(String ipAddress) {
        // Kiểm tra dải IP public của công ty, ví dụ: 203.0.113.x//
        return ipAddress.equals(companyIpRange);
    }

    // Lấy địa chỉ IP public từ dịch vụ bên ngoài (ví dụ: ipify.org)
    public void getPublicIP(EnableCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.ipify.org");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String publicIP = reader.readLine();  // Địa chỉ IP public
                    Log.d("NetworkCheck", "Public IP: " + publicIP);  // Log để kiểm tra
                    enable= isCompanyPublicIP(publicIP) || isCompanyWifi();
                    if (callback != null) {
                        callback.onResult(enable);
                    }

                } catch (Exception e) {
                    if (callback != null) {
                        callback.onResult(false);  // Gọi callback với kết quả false nếu có lỗi
                    }
                }
            }
        }).start();
    }

    public Boolean getEnable() {
        return enable;
    }
}
