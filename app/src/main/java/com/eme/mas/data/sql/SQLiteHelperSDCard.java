package com.eme.mas.data.sql;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class SQLiteHelperSDCard {
	protected SQLiteOpenHelper helper;
	
	private static SQLiteHelperSDCard instance ;
	
	private Lock lock = new ReentrantLock();

	/**
	 * 单例
	 * @return
	 */
	public static SQLiteHelperSDCard getInstance(){
		if(null == instance){
			instance = new SQLiteHelperSDCard();
		}
		return instance;
	}

	
	/*public SQLiteHelperSDCard(SQLiteOpenHelper helper){
		this.helper = helper;
	}*/

	public SQLiteDatabase getDatabase(boolean canWrite){
		SQLiteDatabase db = null;
		lock.lock();
		try{
			if(helper != null){
				if(canWrite){
					db = helper.getWritableDatabase();
				}else{
					db = getDatabase(false);
				}

			}
 			else {
				//db = SQLiteDatabase.openDatabase("test.db",null,null);
				//String dbPath = MasApplication.getInstance().getPath();
				if(canWrite){
					//db = SQLiteDatabase.openDatabase("test.db", null,SQLiteDatabase.NO_LOCALIZED_COLLATORS);
				}else{
					//db = SQLiteDatabase.openDatabase("test.db", null, SQLiteDatabase.OPEN_READONLY);
				}
			}
		}finally{
			lock.unlock();
		}

		return db;
	}
}
