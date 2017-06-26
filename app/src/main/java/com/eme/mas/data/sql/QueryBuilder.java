package com.eme.mas.data.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eme.mas.MasApplication;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
/**
 * SQL查询构造器，用于构造参数化SQL并执行，以避免SQL注入。支持批量模式
 * 
 */
public class QueryBuilder {
	//protected SQLiteHelperSDCard helperd = SQLiteHelperSDCard.getInstance();
	SQLiteDatabase db;
	//String dbPath;
	SQLiteHelper helper ;

	private Lock lock = new ReentrantLock();

	private ArrayList<String> list = new ArrayList<String>();


	private StringBuilder sql = new StringBuilder();


	/**
	 * 根据传入的SQL字符串构造一个SQL查询，参数个数可变
	 */
	public QueryBuilder(String sql, String ... args) {
		setSQL(sql);
		if (args != null) {
			for (String obj : args) {
				add(obj);
			}
		}
	}
	
	/**
	 * 根据不同的参数判断获取打开方式
	 * @param canWrite
	 * @return
	 */
	public SQLiteDatabase getDatabase(boolean canWrite){
		if(canWrite){
			return new SQLiteHelper(MasApplication.getInstance()).getWritableDatabase();
		}else{
			return new SQLiteHelper(MasApplication.getInstance()).getReadableDatabase();
		}
	}

	/**
	 * 增加一个SQL参数
	 */
	public void add(String param) {
		list.add(param);
	}

	/**
	 * 设置指定位置的SQL参数
	 */
	public void set(int index, String param) {
		list.set(index, param);
	}

	/**
	 * 设置SQL语句
	 */
	public void setSQL(String sql) {
		this.sql = new StringBuilder(sql);
	}

	/**
	 * 追加部分SQL语句，同时追加SQL参数
	 */
	public QueryBuilder append(String sqlPart, String... params) {
		sql.append(sqlPart);
		if (params == null) {
			return this;
		}
		for (String obj : params) {
			add(obj);
		}
		return this;
	}
	
	/**
	 * 得到分页sql
	 * @param pageSize
	 * @param pageIndex
	 * @return
	 */
	public  String getPagedSQL(int pageSize, int pageIndex) {
		StringBuilder sb = new StringBuilder();
		int start = pageIndex * pageSize;
		sb.append(getSQL());
		sb.append(" limit ?,?");
		add(start+"");
		add(pageSize+"");
		
		return sb.toString();
	}

	/**
	 * 执行查询，返回DataTable
	 */
	public DataTable executeDataTable() {
		String sql = getSQL();
		String [] params = getParams();
		DataTable dt = new DataTable();
		
		try {
			db = getDatabase(false);
			Cursor cursor = db.rawQuery(sql, params);  
			dt = new DataTable(cursor);
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				try {
					db.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	    return dt;
	}

	/**
	 * 以分页方式执行查询，并返回代表指定页的DataTable
	 */
	public DataTable executePagedDataTable(int pageSize, int pageIndex) {
		String sql = getPagedSQL(pageSize, pageIndex);
		String [] params = getParams();
		DataTable dt = new DataTable();
		
		try {
			db = getDatabase(false);
			Cursor cursor = db.rawQuery(sql, params);  
			dt = new DataTable(cursor);
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				try {
					db.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
	    return dt;
	}

	/**
	 * 执行查询，并返回第一条记录的第一个字段的值，如果没有记录，则返回null
	 */
	public Object executeOneValue() {
		String sql = getSQL();
		String [] params = getParams();
		Object obj = null;
		
		try {
			db = getDatabase(false);
			Cursor cursor = db.rawQuery(sql, params);  
			
			if(cursor.moveToFirst()){
				obj = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				try {
					db.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return obj;
	}

	/**
	 * 执行查询，并返回第一条记录的第一个字段的值并转化为String，如果没有记录，则返回null
	 */
	public String executeString() {
		String sql = getSQL();
		String [] params = getParams();
		String obj = null;
		
		try {
			db = getDatabase(false);
			Cursor cursor = db.rawQuery(sql, params); 
			if(cursor.moveToFirst()){
				obj = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				try {
					db.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return obj;
	}

	/**
	 * 执行查询，并返回第一条记录的第一个字段的值并转化为int，如果没有记录，则返回null
	 */
	public int executeInt() {
		String sql = getSQL();
		String [] params = getParams();
		int obj = 0;
		
		try {
			db = getDatabase(false);
			Cursor cursor = db.rawQuery(sql, params);  
			
			if(cursor.moveToFirst()){
				obj = cursor.getInt(0);
			}
			
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				try {
					db.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return obj;
	}

	/**
	 * 执行查询，并返回第一条记录的第一个字段的值并转化为long，如果没有记录，则返回null
	 */
	public long executeLong() {
		String sql = getSQL();
		String [] params = getParams();
		long obj = 0L;

		try {
			db = getDatabase(false);
			Cursor cursor = db.rawQuery(sql, params);  
			
			if(cursor.moveToFirst()){
				obj = cursor.getLong(0);
			}
			
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				try {
					db.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		db.close();
		
		return obj;
	}

	/**
	 * 执行查询，并返回查询影响的记录数
	 */
	public int executeNoQuery() {
		String sql = getSQL();
		String [] params = getParams();
		try {
			db = getDatabase(true);
		    db.execSQL(sql, params);  	
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(db != null){
				try {
					db.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return 0;
	}
	
	public int executeNoQuery(SQLiteDatabase db) {
		String sql = getSQL();
		String [] params = getParams();
		
		db.execSQL(sql, params);  	
		return 0;
	}

	/**
	 * 获得本查询使用的参数化SQL
	 */
	public String getSQL() {
		return sql.toString();
	}

	/**
	 * 返回当前所有SQL参数
	 */
	public String [] getParams() {
		String [] arr = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			arr[i] = list.get(i);
		}
		return arr;
	}

	/**
	 * 一次性设置所有SQL参数
	 */
	public void setParams(ArrayList<String> list) {
		this.list = list;
	}

	/**
	 * 检查参数化SQL中的问号个数与SQL参数个数是否相符
	 */
	public boolean checkParams() {
		char[] arr = sql.toString().toCharArray();
		boolean StringCharFlag = false;
		int count = 0;
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			if (c == '\'') {
				if (!StringCharFlag) {
					StringCharFlag = true;
				} else {
					StringCharFlag = false;
				}
			} else if (c == '?') {
				if (!StringCharFlag) {
					count++;
				}
			}
		}
		if (count != list.size()) {
			throw new RuntimeException("SQL has " + count + " parameter，but value count is " + list.size());
		}
		return true;
	}

	/*
	 * 转成可读的SQL语句
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		sb.append("\t{");
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				sb.append(",");
			}
			Object o = list.get(i);
			if (o == null) {
				sb.append("null");
				continue;
			}
			String str = list.get(i).toString();
			if (str.length() > 40) {
				str = str.substring(0, 37);
				sb.append(str);
				sb.append("...");
			} else {
				sb.append(str);
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
