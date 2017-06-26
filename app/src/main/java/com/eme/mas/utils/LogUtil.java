package com.eme.mas.utils;

import android.text.TextUtils;
import android.util.Log;

import com.eme.mas.environment.KConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luokaiwen on 15/4/28.
 * <p/>
 * 日志工具类
 */
public class LogUtil {

    //private static final boolean LOGABLE = Environment.MODE;
    private static final boolean LOGABLE = KConfig.INSTANCE.isDebug();
    private static List<String> blockTags = new ArrayList<String>();

    static {
        blockTags.add("LruBitmapImageCache");
        blockTags.add("DiskCache");
    }

    public static void log(String tag, String msg) {

        if (LOGABLE && !blockTags.contains(tag) && !TextUtils.isEmpty(msg)) {
            if (null == msg) {
                msg = "tag msg is null";
            }

            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg) {

        if (LOGABLE && !blockTags.contains(tag) && !TextUtils.isEmpty(msg)) {
            if (null == msg) {
                msg = "tag msg is null";
            }

            Log.e(tag, msg);
        }
    }
}
