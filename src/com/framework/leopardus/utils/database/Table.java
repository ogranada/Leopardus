package com.framework.leopardus.utils.database;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;
import android.util.Pair;

public class Table {
	private String name;
	private String pkName;
	private Map<String, ColumnTypes> columns;
	public static Boolean DEBUG = false;

	public Table(String name) {
		this.name = name;
	}

	public void setPkName(String pkName) {
		if (columns != null && columns.keySet().contains(pkName)) {
			this.pkName = pkName;
		} else {
			throw new RuntimeException(String.format("Column %s doesn't exist",
					pkName));
		}
	}

	public void setColumns(Pair<String, ColumnTypes>... columns) {
		this.columns = new HashMap<String, ColumnTypes>();
		for (Pair<String, ColumnTypes> pair : columns) {
			this.columns.put(pair.first, pair.second);
		}
	}

	public void putColumn(Pair<String, ColumnTypes> pair) {
		this.columns.put(pair.first, pair.second);
	}

	public String getName() {
		return name;
	}

	public String getPkName() {
		return pkName;
	}

	public String getCreateSQL() {
		String sql = "";
		sql += "CREATE TABLE " + name + "(";
		String field_sep = "";
		for (String name : columns.keySet()) {
			String type = ((ColumnTypes) columns.get(name)).toString();
			if (type.contains("_")) {
				if (type.contains("__")) {
					type = type.replace("__", " DEFAULT ") + " ";
					if (type.contains(" NOW ")) {
						type = type.replace(" NOW ", " CURRENT_TIMESTAMP ");
					}
				} else {
					type = type.replace("_", " ") + " ";
				}
			}
			sql += field_sep
					+ String.format("%s %s%s", name, type,
							pkName.equals(name) ? " PRIMARY KEY" : " ");
			sql = sql.replaceAll("  ", " ");
			sql = sql.contains("AUTOINCREMENT PRIMARY KEY") ? sql.replace(
					"AUTOINCREMENT PRIMARY KEY", " PRIMARY KEY AUTOINCREMENT ")
					: sql;

			if (field_sep.equals("")) {
				field_sep = ", ";
			}
		}
		sql += ")";
		if (DEBUG) {
			Log.i("Leopardus",sql);
		}
		return sql;
	}

	public String getDropSQL() {
		return String.format("DROP TABLE IF EXISTS %s", name);
	}

}
