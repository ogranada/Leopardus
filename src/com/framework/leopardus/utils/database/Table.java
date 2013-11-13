package com.framework.leopardus.utils.database;

import java.util.HashMap;
import java.util.Map;

import android.util.Pair;

public class Table {
	private String name;
	private String pkName;
	private Map<String, ColumnTypes> columns;

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
			sql += field_sep
					+ String.format("%s %s%s", name,
							((ColumnTypes) columns.get(name)).toString(),
							pkName.equals(name) ? "PRIMARY KEY" : "");
			if (field_sep.equals("")) {
				field_sep = ", ";
			}
		}
		sql += ")";
		return sql;
	}

	public String getDropSQL() {
		return String.format("DROP TABLE IF EXISTS %s", name);
	}

}
