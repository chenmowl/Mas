package com.eme.mas.data.sql;

import java.io.Serializable;
/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class DataColumn implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	public static final int DATETIME = 12;

	public static final int STRING = 1;

	public static final int BLOB = 2;

	public static final int BIGDECIMAL = 3;

	public static final int DECIMAL = 4;

	public static final int FLOAT = 5;

	public static final int DOUBLE = 6;

	public static final int LONG = 7;

	public static final int INTEGER = 8;

	public static final int SMALLINT = 9;

	public static final int CLOB = 10;

	public static final int BIT = 11;

	protected String ColumnName;

	protected int ColumnType;

	protected boolean isAllowNull = true;// 暂未起用

	protected String dateFormat = null;

	public DataColumn() {

	}

	public Object clone() {
		return new DataColumn(this.ColumnName, this.ColumnType);
	}

	public DataColumn(String columnName, int columnType) {
		this.ColumnName = columnName;
		this.ColumnType = columnType;
	}

	public DataColumn(String columnName, int columnType, boolean allowNull) {
		this.ColumnName = columnName;
		this.ColumnType = columnType;
		this.isAllowNull = allowNull;
	}

	public String getColumnName() {
		return ColumnName;
	}

	public void setColumnName(String columnName) {
		ColumnName = columnName;
	}

	public int getColumnType() {
		return ColumnType;
	}

	public void setColumnType(int columnType) {
		ColumnType = columnType;
	}

	public boolean isAllowNull() {
		return isAllowNull;
	}

	public void setAllowNull(boolean isAllowNull) {
		this.isAllowNull = isAllowNull;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
