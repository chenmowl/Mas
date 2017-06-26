package com.eme.mas;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.android.volley.RequestQueue;
import com.eme.mas.connection.KVolley;
import com.eme.mas.connection.MultipartVolley;
import com.eme.mas.environment.Constant;
import com.eme.mas.model.entity.UserInfo;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * application类
 */
public class MasApplication extends Application {

    public static RequestQueue mRequestQueue;
    public static RequestQueue mMultipartQueue;
    public static SQLiteDatabase db;

    private static MasApplication instance;
    private String path;

    /**
     * 分类信息是否初始化成功
     */
    public static boolean isInitCategory = false;

    /**
     * 购物车数据是否初始化成功
     */
    public static boolean isInitCart = false;

    public static boolean isInitCategory() {
        return isInitCategory;
    }

    public static void setIsInitCategory(boolean isInitCategory) {
        MasApplication.isInitCategory = isInitCategory;
    }

    public static boolean isInitCart() {
        return isInitCart;
    }

    public static void setIsInitCart(boolean isInitCart) {
        MasApplication.isInitCart = isInitCart;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //todo 初始化数据库Realm
        Realm.init(this);
        //todo 这里不添加配置会报错：   io.realm.exceptions.RealmMigrationNeededException: RealmMigration must be provided
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);

        //AMapUtil.getInstance(this).start();  //开启定位

        // 初始化Fresco
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), Constant.IMAGE_FRESCO))
                .setBaseDirectoryName("picture")
                .setMaxCacheSize(1024 * 1024 * 1024)
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(diskCacheConfig)
                .build();

        Fresco.initialize(this, config);
        //LeakCanary.install(this);//
        mRequestQueue = KVolley.newRequestQueue(this);
        mMultipartQueue = MultipartVolley.newRequestQueue(this);

        UserInfo.getInstance().Load();

    }

    public static MasApplication getInstance() {

        return instance;

    }

    public String getPath() {
        return instance.getFilesDir().getPath();
    }
}
