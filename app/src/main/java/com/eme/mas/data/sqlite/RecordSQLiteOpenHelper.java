package com.eme.mas.data.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RecordSQLiteOpenHelper extends SQLiteOpenHelper {

    private static String name = "pro.db";
    private static Integer version = 1;

    public RecordSQLiteOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("RecordSQLiteOpenHelper", "create Database------------->");
        db.execSQL("create table if not exists search_records(id integer primary key autoincrement,search_content varchar(200),add_time datetime)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("RecordSQLiteOpenHelper", "update Database------------->");
    }


}