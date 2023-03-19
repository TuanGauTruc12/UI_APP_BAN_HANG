package com.example.shopbanhang.retrofit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;
import com.example.shopbanhang.models.object.GioHang;
import com.example.shopbanhang.models.object.User;
import java.util.List;

public class Server {
    public static final String BASE_URL= "http://192.168.43.241:8080/";

    public static List<GioHang> listGioHang;

    public static User user = new User();

    public static final boolean isConnected(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if (wifi != null && wifi.isConnected() || mobile != null && mobile.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
