package com.eme.mas.data.sql;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
/**
 * auth：zulei
 * date：2016-04-25
 * email：zulei@fyfyjk.com
 */
public class DataTableUtil {
	/**
	 * 将datatable转换为json字符串
	 * 
	 * @param dt
	 * @return
	 */
	public static String datatableToJson(DataTable dt) {
		String[] columnNames;
		columnNames = new String[dt.getColumnCount()];
		for (int i = 0; i < columnNames.length; i++) {
			columnNames[i] = dt.getDataColumn(i).getColumnName();
		}
		
		JSONArray json = new JSONArray();
		try {
			for (int i = 0; i < dt.getRowCount(); i++) {
				JSONObject row = new JSONObject();
				for (int j = 0; j < dt.getColumnCount(); j++) {
					row.put(columnNames[j].toLowerCase(), dt.getString(i, j));
				}
				json.put(row);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json.toString();
	}
	
	/**
	 * 通过jsonArray转换为datatable
	 * @param json
	 * @return
	 */
	public static DataTable jsonToDataTable(JSONArray json) {
		DataTable dt = new DataTable();
		try {
			for (int i = 0; i < json.length(); i++) {
				JSONObject row = json.getJSONObject(i);
				String[] columnNames = new String[row.length()];
				String[] values = new String[row.length()];
				Iterator<String> keyIter = row.keys();
				int j = 0;
				while (keyIter.hasNext()) {
					String key = keyIter.next();
					columnNames[j] = key;
					values[j] = row.getString(key);
					j++;
				}

				if (i == 0) {
					dt.insertColumns(columnNames);
				}
				dt.insertRow(values);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return dt;
	}
	
	/**
	 * string转换成datatable
	 */
	public static DataTable string2DataTable(String string){
		DataTable dt = new DataTable();
		JSONArray tb;
		try {
			tb = new JSONArray(string);
			dt = DataTableUtil.jsonToDataTable(tb);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return dt;
	}
	
	public static void main(String[] args) {
		DataTable dt = new DataTable();
		String [] cols = {"username","password","gender"};
		dt.insertColumns(cols);
		for (int i = 0; i < 5; i++) {
			String [] values = {"u"+i,"pwd"+i,i+""};
			dt.insertRow(values);
		}
		
		String s = datatableToJson(dt);
		
		try {
			DataTable dt2 = jsonToDataTable(new JSONArray(s));
			System.out.println(dt2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
