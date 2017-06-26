package com.eme.mas.data.sql;

import android.database.Cursor;

import org.apache.commons.lang.ArrayUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class DataTable implements Serializable, Cloneable, Iterable<DataRow> {
	private static final long serialVersionUID = 1L;

	private boolean isWebMode;// 默认值为false，True表示getString的结果是null或者""时转成&nbsp;

	private DataRow[] rows;

	private DataColumn[] columns;

	public DataTable() {
		this.rows = new DataRow[0];
		this.columns = new DataColumn[0];
	}

	public DataTable(DataColumn[] types, Object[][] values) {
		if (types == null) {
			types = new DataColumn[0];
		}
		columns = types;
		rows = null;
		renameAmbiguousColumns(columns);// 将名称相同的列重命名
		if (values != null) {
			rows = new DataRow[values.length];
			for (int i = 0; i < rows.length; i++) {
				rows[i] = new DataRow(columns, values[i]);
			}
		} else {
			this.rows = new DataRow[0];
		}
	}

	// 判断是否有相同列名
	public boolean renameAmbiguousColumns(DataColumn[] types) {
		if (types == null) {
			return false;
		}
		for (int i = 0; i < types.length; i++) {
			String columnName = types[i].getColumnName();
			int count = 1;
			for (int j = i + 1; j < types.length; j++) {
				if (columnName == null) {
					throw new RuntimeException("Column name cann't be null,index is " + i);
				}
				if (columnName.equalsIgnoreCase(types[j].getColumnName())) {
					types[j].ColumnName = columnName + "_" + String.valueOf(++count);
				}
			}
		}
		return true;
	}

	public DataTable(Cursor cursor) {
		this(cursor, Integer.MAX_VALUE, 0, false);
	}

	public DataTable(Cursor rs, boolean latin1Flag) {
		this(rs, Integer.MAX_VALUE, 0, latin1Flag);
	}

	public DataTable(Cursor rs, int pageSize, int pageIndex) {
		this(rs, pageSize, pageIndex, false);
	}

	/**
	 * 当数据库为Oracle，且使用LATIN1或U7ASCII字符集时，latin1Flag为true
	 */
	public DataTable(Cursor rs, int pageSize, int pageIndex, boolean latin1Flag) {
			// 以下准备DataColumn[]
			int columnCount = rs.getColumnCount();
			DataColumn[] types = new DataColumn[columnCount];
			for (int i = 0; i < columnCount; i++) {
				String name = rs.getColumnNames()[i];
				DataColumn dc = new DataColumn();
				dc.setAllowNull(true);
				dc.setColumnName(name);
				
				dc.ColumnType = DataColumn.STRING;
				types[i] = dc;
			}

			columns = types;
			renameAmbiguousColumns(columns);

			// 以下准备ColumnValues[]
			ArrayList<DataRow> list = new ArrayList<DataRow>();
			int index = 0;
			int begin = pageIndex * pageSize;
			int end = (pageIndex + 1) * pageSize;
			while (rs.moveToNext()) {
				if (index >= end) {
					break;
				}
				if (index >= begin) {
					Object[] t = new Object[columnCount];
					for (int j = 0; j < columnCount; j++) {
						t[j] = rs.getString(j);
					}
					DataRow tmpRow = new DataRow(columns, t);
					list.add(tmpRow);
				}
				index++;
			}
			this.rows = new DataRow[list.size()];
			list.toArray(this.rows);

	}

	public void deleteColumn(int columnIndex) {
		if (columns.length == 0) {
			return;
		}
		if (columnIndex < 0 || (columns != null && columnIndex >= columns.length)) {
			throw new RuntimeException("Index is out of range：" + columnIndex);
		}
		this.columns = (DataColumn[]) ArrayUtils.remove(this.columns, columnIndex);
		for (int i = 0; i < rows.length; i++) {
			rows[i].columns = null;
			rows[i].columns = this.columns;
			rows[i].values = ArrayUtils.remove(rows[i].values, columnIndex);
		}
	}

	public void deleteColumn(String columnName) {
		if (columns.length == 0) {
			return;
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				deleteColumn(i);
				break;
			}
		}
	}

	public void insertColumn(String columnName) {
		insertColumn(new DataColumn(columnName, DataColumn.STRING), null, columns.length);
	}

	public void insertColumn(String columnName, Object columnValue) {
		Object[] cv = new Object[rows.length];
		for (int i = 0; i < cv.length; i++) {
			cv[i] = columnValue;
		}
		insertColumn(new DataColumn(columnName, DataColumn.STRING), cv, columns.length);
	}

	public void insertColumns(String[] columnNames) {
		for (int i = 0; i < columnNames.length; i++) {
			insertColumn(new DataColumn(columnNames[i], DataColumn.STRING), null, columns.length);
		}
	}

	public void insertColumn(String columnName, Object[] columnValue) {
		insertColumn(new DataColumn(columnName, DataColumn.STRING), columnValue, columns.length);
	}

	public void insertColumn(DataColumn dc) {
		insertColumn(dc, null, columns.length);
	}

	public void insertColumn(DataColumn dc, Object[] columnValue) {
		insertColumn(dc, columnValue, columns.length);
	}

	public void insertColumn(String columnName, Object[] columnValue, int index) {
		insertColumn(new DataColumn(columnName, DataColumn.STRING), columnValue, index);
	}

	public void insertColumn(DataColumn dc, Object[] columnValue, int index) {
		if (index > columns.length) {
			throw new RuntimeException("Index is out of range:" + index);
		}
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(dc.getColumnName())) {
				throw new RuntimeException("Column is exist:" + dc.getColumnName());
			}
		}
		this.columns = (DataColumn[]) ArrayUtils.add(columns, index, dc);
		if (columnValue == null) {
			columnValue = new Object[rows.length];
		}
		if (rows.length == 0) {
			rows = new DataRow[columnValue.length];
			for (int i = 0; i < rows.length; i++) {
				rows[i] = new DataRow(this.columns, new Object[] { columnValue[i] });
			}
		} else {
			for (int i = 0; i < rows.length; i++) {
				rows[i].columns = null;
				rows[i].columns = this.columns;
				rows[i].values = ArrayUtils.add(rows[i].values, index, columnValue[i]);
			}
		}
	}

	public void insertRow(DataRow dr) {
		insertRow(dr, rows.length);
	}

	public void insertRow(DataRow dr, int index) {
		if (columns.length == 0) {
			columns = dr.columns;
		}
		insertRow(dr.getDataValues(), index);
	}

	public void insertRow(Object[] rowValue) {
		insertRow(rowValue, rows.length);
	}

	public void insertRow(Object[] rowValue, int index) {
		if (index > rows.length) {
			throw new RuntimeException(index + " is out of range ,max is " + rows.length);
		}
		if (rowValue != null) {
			if (columns.length == 0) {
				columns = new DataColumn[rowValue.length];
				for (int i = 0; i < columns.length; i++) {
					columns[i] = new DataColumn("_Columns_" + i, DataColumn.STRING);
				}
			}
			if (rowValue.length != columns.length) {
				throw new RuntimeException("Parameter's length is " + rowValue.length + "，but column count is " + columns.length);
			}
			for (int i = 0; i < columns.length; i++) {
				if (columns[i].ColumnType == DataColumn.DATETIME) {
					Object v = rowValue[i];
					if (v != null) {
						if (!Date.class.isInstance(v)) {
							Date d = DateUtil.parseDateTime(v.toString());
							if (d == null) {
								throw new RuntimeException("Invalid date string:" + v);
							}
							v = d;
						}
					}
				}
			}
		} else {
			rowValue = new Object[columns.length];
		}
		DataRow[] newRows = new DataRow[rows.length + 1];
		System.arraycopy(rows, 0, newRows, 0, index);
		if (index < rows.length) {
			System.arraycopy(rows, index, newRows, index + 1, rows.length - index);
		}
		newRows[index] = new DataRow(columns, rowValue);
		rows = newRows;
	}

	public void deleteRow(int index) {
		if (index >= rows.length) {
			throw new RuntimeException(index + " is out of range,max is " + (rows.length - 1) + "!");
		}
		rows = (DataRow[]) ArrayUtils.remove(rows, index);
	}

	public void deleteRow(DataRow dr) {
		for (int i = 0; i < rows.length; i++) {
			if (dr == rows[i]) {
				deleteRow(i);
				return;
			}
		}
		throw new RuntimeException("DataRow not in DataTable");
	}

	public DataRow get(int rowIndex) {
		if (rowIndex >= rows.length || rowIndex < 0) {
			throw new RuntimeException("Index out of range:" + rowIndex);
		}
		return rows[rowIndex];
	}

	public void set(int rowIndex, int colIndex, Object value) {
		getDataRow(rowIndex).set(colIndex, value);
	}

	public void set(int rowIndex, String columnName, Object value) {
		getDataRow(rowIndex).set(columnName, value);
	}

	public Object get(int rowIndex, int colIndex) {
		return getDataRow(rowIndex).get(colIndex);
	}

	public Object get(int rowIndex, String columnName) {
		return getDataRow(rowIndex).get(columnName);
	}

	public String getString(int rowIndex, int colIndex) {
		return getDataRow(rowIndex).getString(colIndex);
	}

	public String getString(int rowIndex, String columnName) {
		return getDataRow(rowIndex).getString(columnName);
	}

	public int getInt(int rowIndex, int colIndex) {
		return getDataRow(rowIndex).getInt(colIndex);
	}

	public int getInt(int rowIndex, String columnName) {
		return getDataRow(rowIndex).getInt(columnName);
	}

	public long getLong(int rowIndex, int colIndex) {
		return getDataRow(rowIndex).getLong(colIndex);
	}

	public long getLong(int rowIndex, String columnName) {
		return getDataRow(rowIndex).getLong(columnName);
	}

	public double getDouble(int rowIndex, int colIndex) {
		return getDataRow(rowIndex).getDouble(colIndex);
	}

	public double getDouble(int rowIndex, String columnName) {
		return getDataRow(rowIndex).getDouble(columnName);
	}

	public Date getDate(int rowIndex, int colIndex) {
		return getDataRow(rowIndex).getDate(colIndex);
	}

	public Date getDate(int rowIndex, String columnName) {
		return getDataRow(rowIndex).getDate(columnName);
	}

	public DataRow getDataRow(int rowIndex) {
		if (rowIndex >= rows.length || rowIndex < 0) {
			throw new RuntimeException("Index is out of range:" + rowIndex);
		}
		return rows[rowIndex];
	}

	public DataColumn getDataColumn(int columnIndex) {
		if (columnIndex < 0 || columnIndex >= columns.length) {
			throw new RuntimeException("Index is out of range:" + columnIndex);
		}
		return columns[columnIndex];
	}

	public DataColumn getDataColumn(String columnName) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return (getDataColumn(i));
			}
		}
		return null;
	}

	public Object[] getColumnValues(int columnIndex) {
		if (columnIndex < 0 || columnIndex >= columns.length) {
			throw new RuntimeException("Index is out of range:" + columnIndex);
		}
		Object[] arr = new Object[this.getRowCount()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = this.rows[i].values[columnIndex];
		}
		return arr;
	}

	public Object[] getColumnValues(String columnName) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
				return getColumnValues(i);
			}
		}
		return null;
	}

	public void sort(Comparator<DataRow> c) {
		Arrays.sort(rows, c);
	}

	public void sort(String columnName) {
		sort(columnName, "desc", false);
	}

	public void sort(String columnName, String order) {
		sort(columnName, order, false);
	}

	public void sort(String columnName, String order, final boolean isNumber) {
		final String cn = columnName;
		final String od = order;
		sort(new Comparator<DataRow>() {
			public int compare(DataRow dr1, DataRow dr2) {
				Object v1 = dr1.get(cn);
				Object v2 = dr2.get(cn);
				if (v1 instanceof Number && v2 instanceof Number) {
					double d1 = ((Number) v1).doubleValue();
					double d2 = ((Number) v2).doubleValue();
					if (d1 == d2) {
						return 0;
					} else if (d1 > d2) {
						return "asc".equalsIgnoreCase(od) ? 1 : -1;
					} else {
						return "asc".equalsIgnoreCase(od) ? -1 : 1;
					}
				} else if (v1 instanceof Date && v2 instanceof Date) {
					Date d1 = (Date) v1;
					Date d2 = (Date) v1;
					if ("asc".equalsIgnoreCase(od)) {
						return d1.compareTo(d2);
					} else {
						return -d1.compareTo(d2);
					}
				} else if (isNumber) {
					double d1 = 0, d2 = 0;
					try {
						d1 = Double.parseDouble(String.valueOf(v1));
						d2 = Double.parseDouble(String.valueOf(v2));
					} catch (Exception e) {
					}
					if (d1 == d2) {
						return 0;
					} else if (d1 > d2) {
						return "asc".equalsIgnoreCase(od) ? -1 : 1;
					} else {
						return "asc".equalsIgnoreCase(od) ? 1 : -1;
					}
				} else {
					int c = dr1.getString(cn).compareTo(dr2.getString(cn));
					if ("asc".equalsIgnoreCase(od)) {
						return c;
					} else {
						return -c;
					}
				}
			}
		});
	}

	/**
	 * 还存在一些问题，只是浅层拷贝,有待于类型更为严格之后再来修改
	 */
	public Object clone() {
		DataColumn[] dcs = new DataColumn[columns.length];
		for (int i = 0; i < columns.length; i++) {
			dcs[i] = (DataColumn) this.columns[i].clone();
		}
		DataTable dt = new DataTable();
		dt.columns = dcs;
		dt.rows = new DataRow[this.rows.length];
		for (int i = 0; i < this.rows.length; i++) {
			dt.rows[i] = new DataRow(dcs, rows[i].values);
		}
		dt.setWebMode(this.isWebMode);
		return dt;
	}

	/**
	 * 以指定的列字段值为key,以另一指定的列值为value,填充到一个Mapx中，并返回此Mapx
	 */
	public Mapx<String, Object> toMapx(String keyColumnName, String valueColumnName) {
		if (StringUtil.isEmpty(keyColumnName)) {
			throw new RuntimeException("Key column name can't be empty");
		}
		if (StringUtil.isEmpty(valueColumnName)) {
			throw new RuntimeException("Value column name can't be empty");
		}
		int keyIndex = 0, valueIndex = 0;
		boolean keyFlag = false, valueFlag = false;
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(keyColumnName)) {
				keyIndex = i;
				keyFlag = true;
				if (valueFlag) {
					break;
				}
			}
			if (columns[i].getColumnName().equalsIgnoreCase(valueColumnName)) {
				valueIndex = i;
				valueFlag = true;
				if (keyFlag) {
					break;
				}

			}
		}
		return toMapx(keyIndex, valueIndex);
	}

	/**
	 * 以指定的列字段值为key,以另一指定的列值为value,填充到一个Mapx中，并返回此Mapx
	 */
	public Mapx<String, Object> toMapx(int keyColumnIndex, int valueColumnIndex) {
		if (keyColumnIndex < 0 || keyColumnIndex >= columns.length) {
			throw new RuntimeException("Key index is out of range:" + keyColumnIndex);
		}
		if (valueColumnIndex < 0 || valueColumnIndex >= columns.length) {
			throw new RuntimeException("Value index is out of range:" + valueColumnIndex);
		}
		Mapx<String, Object> map = new Mapx<String, Object>();
		for (int i = 0; i < rows.length; i++) {
			Object key = this.rows[i].values[keyColumnIndex];
			if (key == null) {
				map.put(null, this.rows[i].values[valueColumnIndex]);
			} else {
				map.put(key.toString(), this.rows[i].values[valueColumnIndex]);
			}
		}
		return map;
	}

	/**
	 * 以指定列的值为key，去map中寻找对应的值，并把值置到新增的列中，新增列的列名=指定列列名+"Name"
	 */
	public void decodeColumn(String colName, Map<?, ?> map) {
		for (int i = 0; i < columns.length; i++) {
			if (columns[i].getColumnName().equalsIgnoreCase(colName)) {
				decodeColumn(i, map);
				return;
			}
		}
	}

	public void decodeColumn(int colIndex, Map<?, ?> map) {
		String newName = this.columns[colIndex].ColumnName + "Name";
		this.insertColumn(newName);
		for (int i = 0; i < getRowCount(); i++) {
			String v = getString(i, colIndex);
			set(i, newName, map.get(v));
		}
	}

	/**
	 * 将两个表的内容合并
	 */
	public void union(DataTable anotherDT) {
		if (anotherDT.getRowCount() == 0) {
			return;
		}
		if (this.getRowCount() == 0) {
			this.rows = anotherDT.rows;
			return;
		}
		if (this.getColCount() != anotherDT.getColCount()) {
			throw new RuntimeException("This's column count is " + this.getColCount() + " ,but parameter's column column count is "
					+ anotherDT.getColCount());
		}
		int srcPos = rows.length;
		DataRow[] newRows = new DataRow[rows.length + anotherDT.getRowCount()];
		System.arraycopy(rows, 0, newRows, 0, srcPos);
		System.arraycopy(anotherDT.rows, 0, newRows, srcPos, anotherDT.getRowCount());
		rows = null;
		rows = newRows;
	}

	/**
	 * 返回分页后的DataTable
	 */
	public DataTable getPagedDataTable(int pageSize, int pageIndex) {
		DataTable dt = new DataTable(this.columns, null);
		for (int i = pageIndex * pageSize; i < (pageIndex + 1) * pageSize && i < rows.length; i++) {
			dt.insertRow(rows[i]);
		}
		return dt;
	}

	public int getRowCount() {
		return this.rows.length;
	}

	/**
	 * Replaced by getColumnCount()
	 */
	@Deprecated
	public int getColCount() {
		return this.columns.length;
	}

	public int getColumnCount() {
		return this.columns.length;
	}

	public DataColumn[] getDataColumns() {
		return columns;
	}

	public boolean isWebMode() {
		return isWebMode;
	}

	public void setWebMode(boolean isWebMode) {
		this.isWebMode = isWebMode;
		for (int i = 0; i < rows.length; i++) {
			this.rows[i].setWebMode(isWebMode);
		}
	}

	public boolean containsColumn(String name) {
		return getDataColumn(name) != null;
	}

	public Iterator<DataRow> iterator() {
		final DataTable dt = this;
		return new Iterator<DataRow>() {
			private int i = 0;

			public boolean hasNext() {
				return dt.getRowCount() > i;
			}

			public DataRow next() {
				return dt.getDataRow(i++);
			}

			public void remove() {
				dt.deleteRow(i);
			}
		};
	}
}
