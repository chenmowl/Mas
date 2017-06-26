package com.eme.mas.data.sql;

import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class Transaction {
	/**
	 * 插入数据
	 */
	public static final int INSERT = 1;

	/**
	 * 更新数据
	 */
	public static final int UPDATE = 2;

	/**
	 * 删除数据
	 */
	public static final int DELETE = 3;

	/**
	 * 往B表备份数据
	 */
	public static final int BACKUP = 4;

	/**
	 * 删除并且备份数据
	 */
	public static final int DELETE_AND_BACKUP = 5;

	/**
	 * 先删除再插入数据
	 */
	public static final int DELETE_AND_INSERT = 6;

	/**
	 * SQL操作
	 */
	public static final int SQL = 7;

	protected boolean outerConnFlag = false;// 1为连接从外部传入，0为未传入连接

	protected ArrayList<Object> list = new ArrayList<Object>();

	protected String backupOperator;

	protected String backupMemo;

	protected String exceptionMessage;// 异常消息

	protected SQLiteDatabase db;

	public Transaction() {
	}
	

	/**
	 * 设置当前事务使用的SQLiteDatabase对象
	 */
	public void setDataAccess(SQLiteDatabase db) {
		this.db = db;
		outerConnFlag = true;
	}

	/**
	 * 增加一个SQL操作
	 */
	public void add(QueryBuilder qb) {
		list.add(new Object[] { qb, new Integer(Transaction.SQL) });
	}


	/**
	 * 提交事务到数据库
	 */
	/*public boolean commit() {
		SQLiteHelperSDCard helper = SQLiteHelperSDCard.getInstance();
		
		try {
			db = helper.getDatabase(true);
			db.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				Object[] arr = (Object[]) list.get(i);
				Object obj = arr[0];
				int type = ((Integer) arr[1]).intValue();
				if (!executeObject(obj, type)) {
					return false;
				}
			}
			db.setTransactionSuccessful();
			list.clear();
		} catch (Exception e) {
			e.printStackTrace();
			this.exceptionMessage = e.getMessage();
			return false;
		} finally {
				try {
					if(db != null){
						db.endTransaction();
						db.close();
					}					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
		}

		return true;
	}*/


	/**
	 * 提交事务到数据库
	 */
	public boolean commit() {
		SQLiteHelper helper = SQLiteHelper.getInstance();

		try {
			db = helper.getWritableDatabase();
			db.beginTransaction();
			for (int i = 0; i < list.size(); i++) {
				Object[] arr = (Object[]) list.get(i);
				Object obj = arr[0];
				int type = ((Integer) arr[1]).intValue();
				if (!executeObject(obj, type)) {
					return false;
				}
			}
			db.setTransactionSuccessful();
			list.clear();
		} catch (Exception e) {
			e.printStackTrace();
			this.exceptionMessage = e.getMessage();
			return false;
		} finally {
			try {
				if(db != null){
					db.endTransaction();
					db.close();
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return true;
	}


	protected boolean executeObject(Object obj, int type) throws SQLException {
		if (obj instanceof QueryBuilder) {
			((QueryBuilder) obj).executeNoQuery(db);
		}
		return true;
	}

	/**
	 * 清除所有的操作
	 */
	public void clear() {
		this.list.clear();
	}

	/**
	 * 获取执行过程中的SQL异常消息
	 */
	public String getExceptionMessage() {
		return exceptionMessage;
	}


	/**
	 * 返回包含所有操作的List
	 */
	public ArrayList<Object> getOperateList() {
		return list;
	}
}
