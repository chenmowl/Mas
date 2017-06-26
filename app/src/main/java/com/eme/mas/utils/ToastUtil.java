package com.eme.mas.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by luokaiwen on 15/4/28.
 * <p/>
 * 吐丝帮助类
 */
public class ToastUtil {

    public static final String TAG = ToastUtil.class.getSimpleName();

    public static void shortToast(Context context, String content) {

        if (null == context) {
            return;
        }

        if (TextUtils.isEmpty(content)) {
            LogUtil.e(TAG, "content is null");
            return;
        }

        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String content) {

        if (null == context) {
            return;
        }

        if (TextUtils.isEmpty(content)) {
            LogUtil.e(TAG, "content is null");
            return;
        }

        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context, int content) {

        if (null == context) {
            return;
        }

        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, int content) {

        if (null == context) {
            return;
        }

        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
}
