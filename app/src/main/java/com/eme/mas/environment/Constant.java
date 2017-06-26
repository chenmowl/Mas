package com.eme.mas.environment;


/**
 * Created by luokaiwen on 15/4/28.
 * <p/>
 * 常量类
 */
public class Constant {


    public static int SCREENHEITH; // 屏幕的高度
    public static int SCREENWIDTH; // 屏幕的宽度

    public static final String WONDER_WORLD_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/duoyundong/";
    public static final String IMAGE_DIR = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/duoyundong/image/";
    public static final String IMAGE_FRESCO = "/mas/image/";


    public static final String APP_SYSTEM = "android";              // Android应用标识
    public static final String API_VERSION = "2002";                // 接口版本号
    public static final String DEVICE_TYPE = "10041";               // 取4s的图片

    /**
     * 更新间隔设置
     */
    public static final long UPDATE_INTERVAL = 12 * 60 * 60 * 1000;


}
