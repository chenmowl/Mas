package com.eme.mas.data.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eme.mas.MasApplication;

/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "mas.db";
    private final static int DB_VERSION = 1;
    private final static String DB_CREATE_TABLE_WINE_CATEGORY = "create table if not exists wine_category("
            + " id varchar(100) primary key,"
            + " name varchar(100),"
            + " parentid int)";
    private final static String DB_CREATE_TABLE_WINE_BRAND = "create table if not exists wine_brand("
            + " id varchar(100) primary key,"
            + " name varchar(100),"
            + " parentid int)";
    private final static String DB_CREATE_TABLE_WINE_FlAVOR = "create table if not exists wine_flavor("
            + " id varchar(100) primary key,"
            + " name varchar(100),"
            + " parentid int)";
    private final static String DB_CREATE_TABLE_WINE_PLACE = "create table if not exists wine_place("
            + " id varchar(100) primary key,"
            + " name varchar(100),"
            + " parentid int)";
    private final static String DB_CREATE_TABLE_WINE_PRICE = "create table if not exists wine_price("
            + " id varchar(100) primary key,"
            + " name varchar(100),"
            + " parentid int)";
    private final static String DB_CREATE_TABLE_SHOPPING_CART = "create table if not exists shopping_cart(cart_id varchar(100) primary key," +
            "product_id varchar(100),spec_id varchar(100),product_name varchar(100),product_price varchar(50),integral varchar(50),product_num varchar(50)," +
            "image_url varchar(100),product_channel varchar(50),isSelected varchar(20),goods_show varchar(50))";
    private final static String DB_CREATE_TABLE_FOOTMARK = "create table if not exists footmark(product_id varchar(100) primary key,product_name varchar(100),product_price varchar(50),product_image_url varchar(50))";


    private static SQLiteHelper instance;

    public static SQLiteHelper getInstance() {
        if (instance == null) {
            instance = new SQLiteHelper(MasApplication.getInstance());
        }
        return instance;
    }

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * 当数据库首次创建时执行该方法，一般将创建表等初始化操作放在该方法中执行.
     * 重写onCreate方法，调用execSQL方法创建表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_TABLE_WINE_CATEGORY);
        db.execSQL(DB_CREATE_TABLE_WINE_BRAND);
        db.execSQL(DB_CREATE_TABLE_WINE_FlAVOR);
        db.execSQL(DB_CREATE_TABLE_WINE_PLACE);
        db.execSQL(DB_CREATE_TABLE_WINE_PRICE);
        db.execSQL(DB_CREATE_TABLE_SHOPPING_CART);
        db.execSQL(DB_CREATE_TABLE_FOOTMARK);
    }

    //当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
