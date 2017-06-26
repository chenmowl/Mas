package com.eme.mas.data.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.eme.mas.utils.LogUtil;


/**
 * SharedPreferences
 */
public class SPCookie extends SPBase {

    /**
     * 获取 cookie
     */
    public static String getCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        LogUtil.e(TAG, "get cookie is:" + sp.getString("cookie", ""));
        return sp.getString("cookie", "");
    }

    /**
     * 保存 cookie
     */
    public static void setCookie(Context context, String cookie) {
        SharedPreferences sp = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        sp.edit().putString("cookie", cookie == null ? "" : cookie).commit();
        LogUtil.e(TAG, "set cookie is:" + cookie);
    }

    /**
     * 获取 wwid
     */
    public static String getWWID(Context context) {
        SharedPreferences sp = context.getSharedPreferences("wwid", Context.MODE_PRIVATE);
        LogUtil.e(TAG, "get wwid is:" + sp.getString("wwid", ""));
        return sp.getString("wwid", "");
    }

    /**
     * 保存 wwid
     */
    public static void setWWID(Context context, String wwid) {
        SharedPreferences sp = context.getSharedPreferences("wwid", Context.MODE_PRIVATE);
        sp.edit().putString("wwid", wwid == null ? "" : wwid).commit();
        LogUtil.e(TAG, "set wwid is:" + wwid);
    }
}
