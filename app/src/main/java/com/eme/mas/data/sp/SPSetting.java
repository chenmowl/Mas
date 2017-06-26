package com.eme.mas.data.sp;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by luokaiwen on 15/5/31.
 * <p/>
 * 用于记录设置的配置，区别于其他配置为，此为用户选择设置配置和用户直接接触
 */
public class SPSetting extends SPBase {

    private static final String SETTING = "setting";
    private static final String PHOTO = "photo";

    /**
     * 获取 photo 开关
     */
    public static String getPhoto(Context context) {

        String photoSwitch = getContent(context, SETTING, PHOTO);

        if (TextUtils.isEmpty(photoSwitch)) {

            photoSwitch = "false";
        }

        return photoSwitch;
    }

    /**
     * 保存 photo 开关
     */
    public static void setPhoto(Context context, String content) {

        setContent(context, SETTING, PHOTO, content);
    }

}
