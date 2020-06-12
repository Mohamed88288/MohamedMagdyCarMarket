package com.mohamedmagdytest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckConnection {

    public static boolean isConnected(Context context) {
        @SuppressWarnings("static-access")
        ConnectivityManager connection = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connection.getActiveNetworkInfo();
        return info != null;
    }
}