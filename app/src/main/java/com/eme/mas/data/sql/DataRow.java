package com.eme.mas.data.sql;


import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;
import java.util.Date;
/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class DataRow implements Serializable {
	private static final long serialVersionUID = 1L;

	private boolean isWebMode;// 默认为false;

	protected DataColumn[] columns;

	protected Object[] values;

	public DataRow(DataColumn[] types, Object[] values) {
		columns = types;
		this.values = values;
	}

	public Object get(int index) {
		if (values == null) {
			return null;
		}
		if (index < 0 || index >= columns.length) {
			throw new RuntimeException("DataRow ：" + index);
		}
		return values[index];
	}

	public Object get(String columnName) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return values[i];
			}
		}
		return null;
	}

	public String getString(int index) {
		if (values[index] != null) {
			if (!"".equals(values[index]) && this.columns[index].getColumnType() == DataColumn.DATETIME) {
				if (!(values[index] instanceof Date)) {// DataTable.set或DataRow.set时不会校验类型，所以值的类型可能不再是Date了
					return String.valueOf(values[index]);
				}
				if (StringUtil.isNotEmpty(this.columns[index].getDateFormat())) {
					return DateUtil.toString((Date) values[index], this.columns[index].getDateFormat());
				} else {
					return DateUtil.toString((Date) values[index], "yyyy-MM-dd HH:mm:ss");
				}
			}
			String t = String.valueOf(values[index]).trim();
			if (isWebMode) {
				if (t == null || t.equals("")) {
					return "&nbsp;";
				}
			}
			return t;
		} else {
			if (isWebMode) {
				return "&nbsp;";
			}
			return "";
		}
	}

	public String getString(String columnName) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return getString(i);
			}
		}
		return "";
	}

	public Date getDate(int index) {
		Object obj = get(index);
		if (obj == null) {
			return null;
		}
		if (obj instanceof Date) {
			return (Date) obj;
		} else {
			return DateUtil.parseDateTime(obj.toString());
		}
	}

	public Date getDate(String columnName) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return getDate(i);
			}
		}
		return null;
	}

	public double getDouble(int index) {
		Object obj = get(index);
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Number) {
			return ((Number) obj).doubleValue();
		} else {
			String str = obj.toString();
			if (StringUtil.isEmpty(str)) {
				return 0;
			}
			return Double.parseDouble(str);
		}
	}

	public double getDouble(String columnName) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return getDouble(i);
			}
		}
		return 0;
	}

	public long getLong(int index) {
		Object obj = get(index);
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Number) {
			return ((Number) obj).longValue();
		} else {
			String str = obj.toString();
			if (StringUtil.isEmpty(str)) {
				return 0;
			}
			return Long.parseLong(str);
		}
	}

	public long getLong(String columnName) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return getLong(i);
			}
		}
		return 0;
	}

	public int getInt(int index) {
		Object obj = get(index);
		if (obj == null) {
			return 0;
		}
		if (obj instanceof Number) {
			return ((Number) obj).intValue();
		} else {
			String str = obj.toString();
			if (StringUtil.isEmpty(str)) {
				return 0;
			}
			return Integer.parseInt(str);
		}
	}

	public int getInt(String columnName) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return getInt(i);
			}
		}
		return 0;
	}

	public boolean isNull(int index) {
		return get(index) == null;
	}

	public boolean isNull(String columnName) {
		return get(columnName) == null;
	}

	public void set(int index, Object value) {
		if (values == null) {
			return;
		}
		if (index < 0 || index >= values.length) {
			throw new RuntimeException("Index is out of range:" + index);
		}
		values[index] = value;
	}

	public void set(String columnName, Object value) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < values.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				values[i] = value;
				return;
			}
		}
		throw new RuntimeException("Column name not found：" + columnName);

	}

	public DataColumn getDataColumn(int index) {
		if (index < 0 || index >= columns.length) {
			throw new RuntimeException("Index is out of range：" + index);
		}
		return columns[index];
	}

	public DataColumn getDataColumn(String columnName) {
		if (columnName == null || columnName.equals("")) {
			throw new RuntimeException("Column name can't be empty");
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return columns[i];
			}
		}
		return null;
	}

	public int getColumnCount() {
		return columns.length;
	}

	public Object[] getDataValues() {
		return values;
	}

	public DataColumn[] getDataColumns() {
		return columns;
	}

	public void insertColumn(String columnName, Object columnValue) {
		insertColumn(new DataColumn(columnName, DataColumn.STRING), columnValue);
	}

	public void insertColumn(String columnName, Object columnValue, int index) {
		insertColumn(new DataColumn(columnName, DataColumn.STRING), columnValue, index);
	}

	public void insertColumn(DataColumn dc, Object columnValue) {
		insertColumn(dc, columnValue, values.length);
	}

	public void insertColumn(DataColumn dc, Object columnValue, int index) {
		if (index < 0 || index > columns.length) {
			throw new RuntimeException(index + " is out of range,max is " + columns.length);
		}
		columns = (DataColumn[]) ArrayUtils.add(columns, index, dc);
		values = ArrayUtils.add(values, index, columnValue);
	}

	public boolean isWebMode() {
		return isWebMode;
	}

	public void setWebMode(boolean isWebMode) {
		this.isWebMode = isWebMode;
	}

	public Mapx<String, Object> toMapx() {
		Mapx<String, Object> map = new Mapx<String, Object>();
		for (int i = 0; i < columns.length; i++) {
			Object v = values[i];
			if (columns[i].getColumnType() == DataColumn.DATETIME) {
				if (v instanceof Date) {// DataTable.set或DataRow.set时不会校验类型，所以值的类型可能不再是Date了
					if (StringUtil.isNotEmpty(columns[i].getDateFormat())) {
						v = DateUtil.toString((Date) v, columns[i].getDateFormat());
					} else {
						v = DateUtil.toString((Date) v, "yyyy-MM-dd HH:mm:ss");
					}
				}
			}
			map.put(columns[i].getColumnName(), v);
		}
		return map;
	}

	public void fill(Mapx<?, ?> map) {// Map中的键和列名可能有大小写差异
		if (map == null) {
			return;
		}
		for (Object key : map.keySet()) {
			if (key == null) {
				continue;
			}
			Object v = map.get(key);
			for (int j = 0; j < columns.length; j++) {
				if (key.toString().equalsIgnoreCase(columns[j].getColumnName())) {
					if (v != null && columns[j].ColumnType == DataColumn.DATETIME) {
						if (!Date.class.isInstance(v)) {
							Date d = DateUtil.parseDateTime(v.toString());
							if (d == null) {
								throw new RuntimeException("Invalid date string:" + v);
							}
							v = d;
						}
					}
					set(j, v);
				}
			}

		}
	}

	public void fill(Object[] values) {
		if (values == null) {
			return;
		}
		if (values.length != getColumnCount()) {
			throw new RuntimeException("Parameter's length is " + values.length + "，bit DataRow's length is " + getColumnCount());
		}
		for (int i = 0; i < values.length; i++) {
			Object v = values[i];
			if (v != null && columns[i].ColumnType == DataColumn.DATETIME) {
				if (!Date.class.isInstance(v)) {
					Date d = DateUtil.parseDateTime(v.toString());
					if (d == null) {
						throw new RuntimeException("Invalid date string:" + v);
					}
					v = d;
				}
			}
			set(i, v);
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < this.columns.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(this.columns[i].getColumnName());
			sb.append(":");
			sb.append(this.values[i]);
		}
		return sb.toString();
	}
}
