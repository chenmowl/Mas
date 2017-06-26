package com.eme.mas.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.text.TextUtils;

import com.eme.mas.MasApplication;
import com.eme.mas.data.sp.SPSetting;

import java.io.File;

/**
 * Created by luokaiwen on 15/5/12.
 * <p/>
 * Uri帮助类
 */
public class UriUtil {

    public static Uri getImage(String url) {

        if (TextUtils.isEmpty(url)) {
            url = "";
        }

        if (SPSetting.getPhoto(MasApplication.getInstance()).equals("true")) {

            if (!checkNetworkConnection(MasApplication.getInstance())) {
                return Uri.parse("");
            }
        }
       // back Uri.parse(Constant.IMAGE_URL + url);
        return Uri.parse(url);
    }

    public static Uri getPicUri(File file) {

        return Uri.fromFile(file);
    }

    public static boolean checkNetworkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable())
            return true;
        else
            return false;
    }
}
