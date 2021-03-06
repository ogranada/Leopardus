package com.framework.leopardus.utils.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.framework.leopardus.models.Model;

public abstract class InternalDb extends SQLiteOpenHelper {

	public static final String DB_NAME = "INTERNALDB";
	public static final CursorFactory factory = null;
	private static int DB_VERSION = 1;

	protected static Map<String, Table> tables = new HashMap<String, Table>();

	public InternalDb(Context context) {
		super(context, DB_NAME, null, getDatabaseVersion());
		tablesSpecification();
	}

	public InternalDb(Context context, String db) {
		super(context, db, null, getDatabaseVersion());
		tablesSpecification();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (Table table : tables.values()) {
			db.execSQL(table.getCreateSQL());
		}
		System.out.println("DB created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (Table table : tables.values()) {
			db.execSQL(table.getDropSQL());
		}
		onCreate(db);
	}

	protected void addTable(Table table) {
		tables.put(table.getName(), table);
	}

	// /////////////// CRUD OPERATIONS /////////////////
	public void Add(String table, Model data) throws Exception {
		if (tables.containsKey(table)) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			for (String column : data.getKeys()) {
				values.put(column, data.getObject(column).toString());
			}
			db.insertOrThrow(table, null, values);
			db.close();
		} else {
			throw new RuntimeException(String.format("table %s doesn't exist",
					table));
		}
	}

	public List<Model> Get(String table, Model data) {
		if (tables.containsKey(table)) {
			SQLiteDatabase db = this.getReadableDatabase();
			String[] columns = data.getKeys();
			String where = "";// use ? for model given values
			String sub = "";
			ArrayList<String> gvn = new ArrayList<String>(0);
			for (String col : columns) {
				Object obj = data.getObject(col);
				if (obj != null) {
					where += String.format("%s%s=?", sub, col);
					gvn.add(String.valueOf(obj));
					if (sub.equals("")) {
						sub = " AND ";
					}
				}
			}
			String[] given = gvn.toArray(new String[0]);
			Cursor cursor = db.query(table, columns, where, given, null, null,
					null, null);
			List<Model> returnList = new ArrayList<Model>(0);
			if (cursor.moveToFirst()) {
				do {
					Model obj = new Model();
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						obj.addItem(cursor.getColumnName(i),
								cursor.getString(i));
					}
					returnList.add(obj);
				} while (cursor.moveToNext());
			}
			return returnList;
		} else {
			throw new RuntimeException(String.format("table %s doesn't exist",
					table));
		}
	}

	public List<Model> GetAll(String table) {
		if (tables.containsKey(table)) {
			List<Model> returnList = new ArrayList<Model>(0);
			SQLiteDatabase db = this.getReadableDatabase();
			String selectQuery = "SELECT  * FROM " + table;
			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {
					Model obj = new Model();
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						obj.addItem(cursor.getColumnName(i),
								cursor.getString(i));
					}
					returnList.add(obj);
				} while (cursor.moveToNext());
			}
			return returnList;
		} else {
			throw new RuntimeException(String.format("table %s doesn't exist",
					table));
		}
	}

	public List<Model> Query(String query) {
		List<Model> returnList = new ArrayList<Model>(0);
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				Model obj = new Model();
				for (int i = 0; i < cursor.getColumnCount(); i++) {
					obj.addItem(cursor.getColumnName(i), cursor.getString(i));
				}
				returnList.add(obj);
			} while (cursor.moveToNext());
		}
		return returnList;
	}

	public int getRegisterCount(String table) throws Exception {
		if (tables.containsKey(table)) {
			String countQuery = "SELECT  COUNT(*) as count FROM " + table;
			SQLiteDatabase db = this.getReadableDatabase();
			try {
				Cursor cursor = db.rawQuery(countQuery, null);
				return cursor.getInt(0);
			} catch (Exception e) {
				return 0;
			}
		} else {
			throw new RuntimeException(String.format("table %s doesn't exist",
					table));
		}
	}

	public int Update(String table, Model data, String referenceColumn)
			throws Exception {
		if (tables.containsKey(table)) {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			boolean existKk = false;
			for (String key : data.getKeys()) {
				values.put(key, String.valueOf(data.getObject(key)));
				if (key.equals(referenceColumn)) {
					existKk = true;
				}
			}
			if (existKk) {
				data.getObject(referenceColumn);
				return db.update(table, values, referenceColumn + " = ?",
						new String[] { String.valueOf(data
								.getObject(referenceColumn)) });
			} else {
				throw new Exception(String.format(
						"Value of column %s isn't specified.", referenceColumn));
			}
		} else {
			throw new RuntimeException(String.format("table %s doesn't exist",
					table));
		}
	}

	public void Delete(String table, Model data) throws Exception {
		if (tables.containsKey(table)) {
			SQLiteDatabase db = this.getWritableDatabase();
			String pk = tables.get(table).getPkName();
//			db.delete(table, pk + " = ?",
//					new String[] { String.valueOf(data.getObject(pk)) });
			String query = String.format("DELETE FROM %s WHERE %s='%s'", table,pk,String.valueOf(data.getObject(pk)));
			db.rawQuery(query, null);
			db.close();
		} else {
			throw new RuntimeException(String.format("table %s doesn't exist",
					table));
		}
	}

	// /////////////////////////////////////////////////

	public abstract void tablesSpecification();

	public static int getDatabaseVersion(){
		return DB_VERSION;
	}

	public static void setDatabaseVersion(int version){
		DB_VERSION = version;
	}
	
	
	public void exportDatabse(String databaseName, String packageName, String backupDBPath) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//"+packageName+"//databases//"+databaseName+"";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

}
